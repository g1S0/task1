package org.tbank.hw5.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringToNumberUtilsTest {

    @Test
    void testValidNumberWithDot() {
        String input = "123.45";
        double result = StringToNumberUtils.getNumberFromString(input);
        assertEquals(123.45, result, 0.001);
    }

    @Test
    void testValidNumberWithComma() {
        String input = "123,45";
        double result = StringToNumberUtils.getNumberFromString(input);
        assertEquals(123.45, result, 0.001);
    }

    @Test
    void testInvalidNumber() {
        String input = "abc123.45";
        double result = StringToNumberUtils.getNumberFromString(input);
        assertEquals(123.45, result, 0.001);
    }

    @Test
    void testNumberInMiddle() {
        String input = "abc123.45xyz";
        double result = StringToNumberUtils.getNumberFromString(input);
        assertEquals(123.45, result, 0.001);
    }

    @Test
    void testEmptyString() {
        String input = "";
        double result = StringToNumberUtils.getNumberFromString(input);
        assertEquals(500.0, result);
    }

    @Test
    void testNullString() {
        String input = null;
        double result = StringToNumberUtils.getNumberFromString(input);
        assertEquals(500.0, result);
    }

    @Test
    void testNoNumbersInString() {
        String input = "abcxyz";
        double result = StringToNumberUtils.getNumberFromString(input);
        assertEquals(500.0, result);
    }

    @Test
    void testNumberWithLeadingSymbols() {
        String input = "$123.45";
        double result = StringToNumberUtils.getNumberFromString(input);
        assertEquals(123.45, result, 0.001);
    }

    @Test
    void testNumberWithTrailingSymbols() {
        String input = "123.45abc";
        double result = StringToNumberUtils.getNumberFromString(input);
        assertEquals(123.45, result, 0.001);
    }
}