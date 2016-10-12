package com.github.malkomich.nanodegree.Util;

import java.math.BigDecimal;

/**
 * Utils class for mathematical operations.
 */
public class MathUtils {

    /**
     * Round a double number and limits the precision.
     *
     * @param number
     *               Number to convert
     * @param precision
     *                  Number of decimal characters
     * @return double
     */
    public static double roundDouble(double number, int precision) {
        return (Double.isNaN(number)) ? number : new BigDecimal(number)
            .setScale(precision, BigDecimal.ROUND_HALF_EVEN)
            .doubleValue();
    }
}
