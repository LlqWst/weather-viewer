package dev.lqwd.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;


@UtilityClass
@Slf4j
public final class Converter {

    public static BigDecimal convertToCelsius(BigDecimal fahrenheit) {
        if (fahrenheit == null) {
            return new BigDecimal(0);
        }
        return fahrenheit.subtract(new BigDecimal("32"))
                .multiply(new BigDecimal("5"))
                .divide(new BigDecimal("9"), 0, RoundingMode.HALF_UP);
    }

}
