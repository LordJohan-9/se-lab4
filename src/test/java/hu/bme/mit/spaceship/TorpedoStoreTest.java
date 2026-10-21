package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TorpedoStoreTest {

    // ---------- fire(): successful firing ----------

    @Test
    void fire_Success() {
        // Arrange
        TorpedoStore store = new TorpedoStore(1);

        // Act
        boolean result = store.fire(1);

        // Assert
        assertEquals(true, result);
    }

    @Test
    void fire_OneTorpedo_ReturnsTrueAndDecreasesCountByOne() {
        // Arrange (failure rate 0.0: firing never fails)
        TorpedoStore store = new TorpedoStore(5, 0.0);

        // Act
        boolean result = store.fire(1);

        // Assert
        assertTrue(result);
        assertEquals(4, store.getTorpedoCount());
    }

    @Test
    void fire_SeveralTorpedoesAtOnce_DecreasesCountByThatNumber() {
        // Arrange
        TorpedoStore store = new TorpedoStore(5, 0.0);

        // Act
        boolean result = store.fire(3);

        // Assert
        assertTrue(result);
        assertEquals(2, store.getTorpedoCount());
    }

    @Test
    void fire_ExactlyAllTorpedoes_Succeeds() {
        // Arrange: boundary, numberOfTorpedos == torpedoCount
        TorpedoStore store = new TorpedoStore(5, 0.0);

        // Act
        boolean result = store.fire(5);

        // Assert
        assertTrue(result);
        assertEquals(0, store.getTorpedoCount());
        assertTrue(store.isEmpty());
    }

    @Test
    void fire_LastTorpedo_StoreBecomesEmpty() {
        // Arrange
        TorpedoStore store = new TorpedoStore(1, 0.0);

        // Act
        store.fire(1);

        // Assert
        assertTrue(store.isEmpty());
        assertEquals(0, store.getTorpedoCount());
    }

    // ---------- fire(): simulated failure ----------

    @Test
    void fire_FailureRateOne_ReturnsFalse() {
        // Arrange (failure rate 1.0: firing always fails)
        TorpedoStore store = new TorpedoStore(5, 1.0);

        // Act
        boolean result = store.fire(1);

        // Assert
        assertFalse(result);
    }

    @Test
    void fire_FailureRateOne_DoesNotConsumeTorpedoes() {
        // Arrange
        TorpedoStore store = new TorpedoStore(5, 1.0);

        // Act: repeat to make sure it is not a lucky random outcome
        for (int i = 0; i < 100; i++) {
            assertFalse(store.fire(1));
        }

        // Assert
        assertEquals(5, store.getTorpedoCount());
        assertFalse(store.isEmpty());
    }

    @Test
    void fire_FailureRateZero_AlwaysSucceeds() {
        // Arrange
        TorpedoStore store = new TorpedoStore(100, 0.0);

        // Act + Assert: repeat to make sure it is not a lucky random outcome
        for (int i = 0; i < 100; i++) {
            assertTrue(store.fire(1));
        }
        assertEquals(0, store.getTorpedoCount());
    }

    // ---------- fire(): invalid arguments ----------

    @Test
    void fire_ZeroTorpedoes_ThrowsException() {
        // Arrange
        TorpedoStore store = new TorpedoStore(5, 0.0);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> store.fire(0));
        assertEquals(5, store.getTorpedoCount());
    }

    @Test
    void fire_NegativeNumberOfTorpedoes_ThrowsException() {
        // Arrange
        TorpedoStore store = new TorpedoStore(5, 0.0);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> store.fire(-1));
        assertEquals(5, store.getTorpedoCount());
    }

    @Test
    void fire_MoreThanAvailable_ThrowsException() {
        // Arrange: boundary, numberOfTorpedos == torpedoCount + 1
        TorpedoStore store = new TorpedoStore(5, 0.0);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> store.fire(6));
        assertEquals(5, store.getTorpedoCount());
    }

    @Test
    void fire_FromEmptyStore_ThrowsException() {
        // Arrange
        TorpedoStore store = new TorpedoStore(0, 0.0);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> store.fire(1));
    }

    @Test
    void fire_AfterStoreWasEmptied_ThrowsException() {
        // Arrange
        TorpedoStore store = new TorpedoStore(1, 0.0);
        store.fire(1);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> store.fire(1));
    }

    // ---------- isEmpty() and getTorpedoCount() ----------

    @Test
    void isEmpty_StoreWithTorpedoes_ReturnsFalse() {
        TorpedoStore store = new TorpedoStore(1, 0.0);

        assertFalse(store.isEmpty());
    }

    @Test
    void isEmpty_StoreWithZeroTorpedoes_ReturnsTrue() {
        TorpedoStore store = new TorpedoStore(0, 0.0);

        assertTrue(store.isEmpty());
    }

    @Test
    void getTorpedoCount_ReturnsInitialCount() {
        TorpedoStore store = new TorpedoStore(7, 0.0);

        assertEquals(7, store.getTorpedoCount());
    }
}