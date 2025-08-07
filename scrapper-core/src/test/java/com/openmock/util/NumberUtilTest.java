package com.openmock.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NumberUtilTest {
    @Test
    void isNumeric() {
        assertTrue(NumberUtil.isNumeric("123"));
        assertTrue(NumberUtil.isNumeric("-50869"));
        assertTrue(NumberUtil.isNumeric("296.7"));
        assertTrue(NumberUtil.isNumeric("-9356.6"));
        assertFalse(NumberUtil.isNumeric("-9356a.6"));
    }

    @Test
    void isPositiveInt() {
        assertTrue(NumberUtil.isPositiveInt("123"));
        assertTrue(NumberUtil.isPositiveInt("934989854"));

        assertFalse(NumberUtil.isPositiveInt("-50869"));
        assertFalse(NumberUtil.isPositiveInt("296.7"));
        assertFalse(NumberUtil.isPositiveInt("-9356.6"));
        assertFalse(NumberUtil.isPositiveInt("-9356a.6"));
    }
}
