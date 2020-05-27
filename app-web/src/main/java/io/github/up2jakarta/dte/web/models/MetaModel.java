package io.github.up2jakarta.dte.web.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.Version;
import io.github.up2jakarta.dte.exe.engine.Statistics;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * API meta data model.
 */
@SuppressWarnings("unused")
@JsonPropertyOrder({"startDate", "startTime", "dataSource", "project"})
public final class MetaModel {

    private static final LocalDate START_DATE = LocalDate.now();
    private static final LocalTime START_TIME = LocalTime.now();

    private final String dataSource;

    /**
     * Public constructor for MetaModel.
     *
     * @param dataSource the data source version
     */
    public MetaModel(final String dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @return the server start date
     */
    public LocalDate getStartDate() {
        return START_DATE;
    }

    /**
     * @return the server start time
     */
    public LocalTime getStartTime() {
        return START_TIME;
    }

    /**
     * @return the data source version
     */
    public String getDataSource() {
        return dataSource;
    }

    /**
     * @return the project version.
     */
    public Version getProject() {
        return StaticEngine.version();
    }

    /**
     * @return the engine statistics.
     */
    public Statistics getEngine() {
        return StaticEngine.statistics();
    }
}
