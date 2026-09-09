package ru.itmo.ode;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class InputValidationTest {
    private Map<String, String> form(String x0, String xn, String y0, String h, String eps) {
        return Map.of("equation", "linear", "methods", "euler,rk4,adams",
                "x0", x0, "xn", xn, "y0", y0, "h", h, "epsilon", eps);
    }

    @Test
    void hundredsOfDecimalPlacesAreAcceptedWithoutArtificialRounding() {
        String number = "0.123456789012345678901234567890" + "1234567890".repeat(30);
        double expected = Double.parseDouble(number);
        assertEquals(expected, Input.number(number, "y₀"));
        assertEquals(expected, Input.number(number.replace('.', ','), "y₀"));
        assertNotEquals(0.123457, Input.number(number, "y₀"));
        Input p = Input.from(form("0", "1", number, "0.1", "0.001"));
        assertEquals(expected, p.y0());
        assertTrue(Json.encode(Map.of("y0", p.y0())).contains(Double.toString(expected)));
    }

    @Test
    void commasAreAcceptedInEveryField() {
        Input a = Input.from(form("-0,25", "1,75", "0,12345678901234567", "0,05", "1,5e-4"));
        Input b = Input.from(form("-0.25", "1.75", "0.12345678901234567", "0.05", "1.5e-4"));
        assertEquals(b, a);
    }

    @Test
    void invalidNumbersExplainTheProblemAndNameTheField() {
        for (String value : new String[]{"", " ", "abc", "1,2.3", "NaN", "Infinity", "1e999", "1e-999"}) {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> Input.from(form("0", "1", value, "0.1", "0.001")));
            assertTrue(e.getMessage().contains("y₀"), value + ": " + e.getMessage());
            assertTrue(e.getMessage().matches(".*[А-Яа-яЁё].*"));
        }
    }

    @Test
    void invalidConditionsHaveActionableMessages() {
        assertTrue(assertThrows(IllegalArgumentException.class,
                () -> Input.from(form("1", "0", "0.5", "0.1", "0.001")))
                .getMessage().contains("больше"));
        assertTrue(assertThrows(IllegalArgumentException.class,
                () -> Input.from(form("0", "1", "0.5", "0", "0.001")))
                .getMessage().contains("положительным"));
        assertTrue(assertThrows(IllegalArgumentException.class,
                () -> Input.from(form("0", "1", "0.5", "0.1", "-1")))
                .getMessage().contains("положительной"));
    }
}
