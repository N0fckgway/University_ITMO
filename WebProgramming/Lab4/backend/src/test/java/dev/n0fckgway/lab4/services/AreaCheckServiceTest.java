package dev.n0fckgway.lab4.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AreaCheckServiceTest {

    private AreaCheckService areaCheckService;

    @BeforeEach
    void setUp() {
        areaCheckService = new AreaCheckService();
    }

    @Test
    void shouldReturnTrueForPointInsideRectangle() {
        assertTrue(areaCheckService.isHit(1.0, 1.0, 2.0));
    }

    @Test
    void shouldReturnTrueForPointInsideTriangle() {
        assertTrue(areaCheckService.isHit(1.0, -0.2, 2.0));
    }

    @Test
    void shouldReturnTrueForPointInsideQuarterCircle() {
        assertTrue(areaCheckService.isHit(-1.0, -1.0, 2.0));
    }

    @Test
    void shouldIncludeAreaBoundaries() {
        assertTrue(areaCheckService.isHit(2.0, 2.0, 2.0));
        assertTrue(areaCheckService.isHit(2.0, 0.5, 2.0));
        assertTrue(areaCheckService.isHit(-2.0, 0.0, 2.0));
    }

    @Test
    void shouldReturnFalseForPointOutsideArea() {
        assertFalse(areaCheckService.isHit(-1.5, -1.5, 2.0));
        assertFalse(areaCheckService.isHit(2.1, 1.0, 2.0));
        assertFalse(areaCheckService.isHit(1.0, -1.0, 2.0));
    }

    @Test
    void shouldReturnFalseForNonPositiveRadius() {
        assertFalse(areaCheckService.isHit(0.0, 0.0, 0.0));
        assertFalse(areaCheckService.isHit(0.0, 0.0, -1.0));
    }
}
