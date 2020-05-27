package io.github.up2jakarta.dte.web.config;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.groovy.io.StringBuilderWriter;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.messages.Message;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import io.github.up2jakarta.dte.dsl.BusinessException;
import io.github.up2jakarta.dte.exe.loader.LoadingException;
import io.github.up2jakarta.dte.exe.script.CompilationException;
import io.github.up2jakarta.dte.exe.script.ExecutionException;
import io.github.up2jakarta.dte.web.exceptions.EnumProcessingException;
import io.github.up2jakarta.dte.web.exceptions.HttpException;
import io.github.up2jakarta.dte.web.models.ErrorModel;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path;
import java.io.EOFException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ConnectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;
import static io.github.up2jakarta.dte.web.models.ErrorModel.Type.*;

/**
 * Controller Advice, responsible for translate uncaught exceptions to API errors.
 *
 * @author A.ABBESSI
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    private static final String ERROR_MSG = "{} error while processing the HTTP request {{}]: {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);
    private static final String KEY = "%d:%d:%d:%d";

    private static Path.Node last(final Path path) {
        Path.Node node = null;
        for (final Path.Node element : path) {
            if (element.getKind() != ElementKind.CONTAINER_ELEMENT) {
                node = element;
            }
        }
        return node;
    }

    /**
     * Maps any unexpected exception to 500.
     *
     * @param req the HTTP request
     * @param ex  the origin exception
     * @return the HTTP response error
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorModel> unexpectedException(final HttpServletRequest req, final Throwable ex) {
        LOGGER.error(ERROR_MSG, "Unexpected", req.getServletPath(), ex.getMessage(), ex);
        return status(INTERNAL_SERVER_ERROR).body(new ErrorModel(SRV, ex));
    }

    /**
     * Maps {@link AccessDeniedException} to 403.
     *
     * @param r the HTTP request
     * @param e the origin exception
     * @return the HTTP response error
     */
    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ErrorModel> authException(final HttpServletRequest r, final AccessDeniedException e) {
        LOGGER.warn(ERROR_MSG, "Security", r.getServletPath(), e.getMessage());
        return status(FORBIDDEN).body(new ErrorModel(API, "security", e.getMessage()));
    }

    /**
     * Maps {@link ClientAbortException} to 499.
     *
     * @param r the HTTP request
     * @param e the origin exception
     * @return the HTTP response error
     */
    @ExceptionHandler(ClientAbortException.class)
    public ResponseEntity clientAbortException(final HttpServletRequest r, final ClientAbortException e) {
        LOGGER.warn(ERROR_MSG, "Client", r.getServletPath(), r.getRemoteHost());
        return status(499).body(new ErrorModel(API, e));
    }

    /**
     * Maps {@link HttpRequestMethodNotSupportedException} to 405.
     *
     * @param r the HTTP request
     * @param e the origin exception
     * @return the HTTP response error
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity supportException(final HttpServletRequest r, final HttpRequestMethodNotSupportedException e) {
        LOGGER.warn(ERROR_MSG, "Support", r.getServletPath(), e.getMessage());
        return status(METHOD_NOT_ALLOWED).body(new ErrorModel(API, "method", e.getMessage()));
    }

    /**
     * Maps {@link UnsatisfiedServletRequestParameterException} to 422.
     *
     * @param r the HTTP request
     * @param e the origin exception
     * @return the HTTP response error
     */
    @ExceptionHandler({UnsatisfiedServletRequestParameterException.class})
    public ResponseEntity supportException(final HttpServletRequest r, final ServletRequestBindingException e) {
        LOGGER.warn(ERROR_MSG, "Parameter", r.getServletPath(), e.getMessage());
        return status(UNPROCESSABLE_ENTITY).body(new ErrorModel(API, "parameters", e.getMessage()));
    }

    /**
     * Maps {@link ConnectException} {@link EOFException} to 503.
     *
     * @param req the HTTP request
     * @param ex  the origin exception
     * @return the HTTP response error
     */
    @ExceptionHandler({ConnectException.class, EOFException.class})
    public ResponseEntity<ErrorModel> connectionErrorHandler(final HttpServletRequest req, final ConnectException ex) {
        LOGGER.error(ERROR_MSG, "Connection", req.getServletPath(), ex.getMessage());
        return status(503).body(new ErrorModel(NET, ex));
    }

    /**
     * Maps any {@link DataAccessResourceFailureException} to 503.
     *
     * @param req the HTTP request
     * @param ex  the origin exception
     * @return the HTTP response error
     */
    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity failureException(final HttpServletRequest req, final DataAccessResourceFailureException ex) {
        LOGGER.warn(ERROR_MSG, "Access", req.getServletPath(), ex.getCause().getMessage());
        return status(503).body(new ErrorModel(DBC, ex.getCause()));
    }

    /**
     * Maps {@link SQLException} to 456.
     *
     * @param req the HTTP request
     * @param ex  the origin exception
     * @return the HTTP response error
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorModel> sqlException(final HttpServletRequest req, final SQLException ex) {
        LOGGER.warn(ERROR_MSG, "SQL", req.getServletPath(), ex.getMessage());
        return status(456).body(new ErrorModel(DBC, ex.getSQLState(), ex.getMessage()));
    }

    /**
     * Maps {@link DataAccessException} to 409.
     *
     * @param req the HTTP request
     * @param ex  the origin exception
     * @return the HTTP response error
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorModel> dataException(final HttpServletRequest req, final DataAccessException ex) {
        final Throwable cause = ex.getMostSpecificCause();
        if (cause instanceof SQLException) {
            return sqlException(req, (SQLException) cause);
        }
        if (cause instanceof PersistenceException) {
            return persistenceException(req, (PersistenceException) cause);
        }
        LOGGER.warn(ERROR_MSG, "Data", req.getServletPath(), cause.getMessage());
        return status(CONFLICT).body(new ErrorModel(DBC, cause));
    }

    /**
     * Maps {@link PersistenceException} to 409.
     *
     * @param r the HTTP request
     * @param e the origin exception
     * @return the HTTP response error
     */
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorModel> persistenceException(final HttpServletRequest r, final PersistenceException e) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        if (cause instanceof SQLException) {
            return sqlException(r, (SQLException) cause);
        }
        LOGGER.warn(ERROR_MSG, "Persistence", r.getServletPath(), cause.getMessage());
        return status(CONFLICT).body(new ErrorModel(DBC, cause));
    }

    /**
     * Maps {@link HttpMessageNotReadableException} to 400.
     *
     * @param req the HTTP request
     * @param ex  the origin exception
     * @return the HTTP response error
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity requestException(final HttpServletRequest req, final HttpMessageNotReadableException ex) {
        LOGGER.warn(ERROR_MSG, "Request", req.getServletPath(), ex.getMessage());
        final Throwable cause = ex.getMostSpecificCause();
        if (cause instanceof EnumProcessingException) {
            final EnumProcessingException error = (EnumProcessingException) cause;
            return badRequest().body(singletonList(new ErrorModel(API, error.getOrigin(), error.getMessage())));
        }
        return badRequest().body(singletonList(new ErrorModel(API, "Request", cause.getMessage())));
    }

    /**
     * Maps {@link BindException} to 400.
     *
     * @param req the HTTP request
     * @param ex  the origin exception
     * @return the HTTP response error
     */
    @ExceptionHandler({BindException.class})
    public ResponseEntity bindException(final HttpServletRequest req, final BindException ex) {
        LOGGER.warn(ERROR_MSG, "Binding", req.getServletPath(), ex.getMessage());
        if (ex.hasErrors()) {
            final List<ErrorModel> errors = ex.getBindingResult().getFieldErrors().stream()
                    .map(f -> new ErrorModel(API, f.getField(), f.getDefaultMessage()))
                    .collect(Collectors.toList());
            return badRequest().body(errors);
        }
        final String origin = ex.getBindingResult().getObjectName();
        return badRequest().body(singletonList(new ErrorModel(API, origin, ex.getMessage())));
    }

    /**
     * Maps {@link MethodArgumentNotValidException} to 400.
     *
     * @param r the HTTP request
     * @param e the origin exception
     * @return the HTTP response error
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity argException(final HttpServletRequest r, final MethodArgumentNotValidException e) {
        LOGGER.warn(ERROR_MSG, "Argument", r.getServletPath(), e.getMessage());
        if (e.getBindingResult().getFieldErrorCount() != 0) {
            final List<ErrorModel> errors = e.getBindingResult().getFieldErrors().stream()
                    .map(f -> new ErrorModel(API, f.getField(), f.getDefaultMessage()))
                    .collect(Collectors.toList());
            return badRequest().body(errors);
        }
        return badRequest()
                .body(singletonList(new ErrorModel(API, e.getParameter().getParameterName(), e.getMessage())));
    }

    /**
     * Maps {@link MissingServletRequestParameterException} to 400.
     *
     * @param r the HTTP request
     * @param e the origin exception
     * @return the HTTP response error
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity paramException(final HttpServletRequest r, final MissingServletRequestParameterException e) {
        LOGGER.warn(ERROR_MSG, "Parameter", r.getServletPath(), e.getMessage());
        return badRequest().body(singletonList(new ErrorModel(API, e.getParameterName(), e.getMessage())));
    }

    /**
     * Maps {@link MethodArgumentTypeMismatchException} to 400.
     *
     * @param r the HTTP request
     * @param e the origin exception
     * @return the HTTP response error
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity typeException(final HttpServletRequest r, final MethodArgumentTypeMismatchException e) {
        LOGGER.warn(ERROR_MSG, "Type", r.getServletPath(), e.getMessage());
        final String msg = "Cannot convert [" + e.getValue() + "] to required type [" + e.getRequiredType() + "]";
        return badRequest().body(singletonList(new ErrorModel(API, e.getName(), msg)));
    }

    /**
     * Maps {@link HttpException} to the specified status {@link HttpException#getStatus()}.
     *
     * @param rq the HTTP request
     * @param ex the API exception
     * @return the HTTP response error
     */
    @ExceptionHandler({HttpException.class})
    public ResponseEntity apiException(final HttpServletRequest rq, final HttpException ex) {
        if (ex.getStatus() == BAD_REQUEST) {
            return badRequest().body(singletonList(new ErrorModel(API, ex.getOrigin(), ex.getMessage())));
        }
        if (ex.getStatus() == NOT_FOUND) {
            return notFound().header(HttpHeaders.LOCATION, rq.getServletPath())
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, rq.getMethod()).build();
        }
        return status(ex.getStatus()).body(new ErrorModel(API, ex));
    }

    /**
     * Maps {@link ConstraintViolationException} to 400.
     *
     * @param ex the validation exception
     * @return the HTTP response error
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity validationException(final ConstraintViolationException ex) {
        final List<ErrorModel> errors = ex.getConstraintViolations().stream()
                .map(c -> new ErrorModel(API, last(c.getPropertyPath()).toString(), c.getMessage()))
                .collect(Collectors.toList());
        return badRequest().body(errors);
    }

    /**
     * Maps engine {@link ExecutionException} to unknown error (520).
     *
     * @param ex the DTE exception
     * @return the HTTP response error
     */
    @ExceptionHandler({ExecutionException.class})
    public ResponseEntity executionException(final ExecutionException ex) {
        return status(520).body(new ErrorModel(API, ex.getScript(), ex.getMessage()));
    }

    /**
     * Maps engine {@link BusinessException} to misdirected  error (421).
     *
     * @param ex the DTE exception
     * @return the HTTP response error
     */
    @ExceptionHandler({BusinessException.class})
    public ResponseEntity businessException(final BusinessException ex) {
        return status(421).body(new ErrorModel(API, ex.getScript(), ex.getMessage()));
    }

    /**
     * Maps {@link LoadingException} to 400.
     *
     * @param r the HTTP request
     * @param e the origin exception
     * @return the HTTP response error
     */
    @ExceptionHandler(LoadingException.class)
    public ResponseEntity<List<ErrorModel>> loadingException(final HttpServletRequest r, final LoadingException e) {
        return badRequest().body(singletonList(new ErrorModel(DBC, e.getOrigin(), e.getMessage())));
    }

    /**
     * Maps engine {@link CompilationException} to unknown error (400).
     *
     * @param ex the DTE exception
     * @return the HTTP response error
     */
    @ExceptionHandler({CompilationException.class})
    public ResponseEntity syntaxException(final CompilationException ex) {
        final MultipleCompilationErrorsException cause = (MultipleCompilationErrorsException) ex.getCause();
        final List<ErrorModel> errors = new ArrayList<>(cause.getErrorCollector().getErrorCount());
        for (int i = 0; i < cause.getErrorCollector().getErrorCount(); i++) {
            final Message msg = cause.getErrorCollector().getError(i);
            if (msg instanceof SyntaxErrorMessage) {
                final SyntaxException e = ((SyntaxErrorMessage) msg).getCause();
                String key = String.format(KEY, e.getStartLine(), e.getStartColumn(), e.getEndLine(), e.getEndColumn());
                final String error = e.getOriginalMessage().replace("[Static type checking] - ", "");
                errors.add(new ErrorModel(API, key, error));
            } else {
                final Writer message = new StringBuilderWriter();
                msg.write(new PrintWriter(message));
                errors.add(new ErrorModel(API, "script", message.toString()));
            }
        }
        return badRequest().body(errors);
    }

}
