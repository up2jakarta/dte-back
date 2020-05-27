package io.github.up2jakarta.dte.web.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * API User Model.
 */
@JsonPropertyOrder({"username", "roles"})
@SuppressWarnings("unused")
public class UserModel {

    private final Authentication delegate;

    /**
     * Public constructor for UserModel.
     *
     * @param user the logged user
     */
    public UserModel(final Authentication user) {
        this.delegate = user;
    }

    /**
     * @return the granted roles.
     */
    public String[] getRoles() {
        return delegate.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
    }

    /**
     * @return the user name.
     */
    public String getUsername() {
        return delegate.getName();
    }
}
