package org.tbank.hw5.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import lombok.Data;

import java.time.LocalDate;

import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

@Data
public class EventsRequestDto {
    private BigDecimal budget;
    private String currency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate dateFrom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate dateTo;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public EventsRequestDto(BigDecimal budget, String currency, String dateFrom, String dateTo) {
        this.budget = budget;
        this.currency = currency;
        this.dateFrom = (dateFrom != null && !dateFrom.isEmpty()) ? LocalDate.parse(dateFrom, formatter) : getStartOfCurrentWeek();
        this.dateTo = (dateTo != null && !dateTo.isEmpty()) ? LocalDate.parse(dateTo, formatter) : getEndOfCurrentWeek();
    }

    public long getDateFromAsUnixTimestamp() {
        return convertToUnixTimestamp(dateFrom);
    }

    public long getDateToAsUnixTimestamp() {
        return convertToUnixTimestamp(dateTo);
    }

    private long convertToUnixTimestamp(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    }

    private LocalDate getStartOfCurrentWeek() {
        return LocalDate.now().with(previousOrSame(java.time.DayOfWeek.MONDAY));
    }

    private LocalDate getEndOfCurrentWeek() {
        return LocalDate.now().with(nextOrSame(java.time.DayOfWeek.SUNDAY));
    }
}


