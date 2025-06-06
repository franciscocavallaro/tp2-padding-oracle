import org.junit.jupiter.api.Test;
import padding_oracle.PaddingOracle;
import padding_oracle.PaddingOracleAttack;

import static org.junit.jupiter.api.Assertions.*;
import static padding_oracle.PaddingOracle.xor;

public class PaddingOracleTest {


    // Basic test: valid padding 01
    @Test
    public void testValidPadding01() {
        byte[] key = new byte[8];
        byte[] x1 = new byte[8];
        byte[] m2 = new byte[] {1, 1, 1, 1, 1, 1, 1, 0x01};
        byte[] x2 = xor(x1, xor(m2, key));

        PaddingOracle oracle = new PaddingOracle(key);
        assertTrue(oracle.isPaddingValid(x1, x2));
    }

    // Basic test: valid padding 02 02
    @Test
    public void testValidPadding02() {
        byte[] key = new byte[8];
        byte[] x1 = new byte[8];
        byte[] m2 = new byte[] {1, 1, 1, 1, 1, 1, 0x02, 0x02};
        byte[] x2 = xor(x1, xor(m2, key));

        PaddingOracle oracle = new PaddingOracle(key);
        assertTrue(oracle.isPaddingValid(x1, x2));
    }

    // Basic test: valid padding 05 05 05 05 05
    @Test
    public void testValidPadding05() {
        byte[] key = new byte[8];
        byte[] x1 = new byte[8];
        byte[] m2 = new byte[] {9, 9, 9, 0x05, 0x05, 0x05, 0x05, 0x05};
        byte[] x2 = xor(x1, xor(m2, key));

        PaddingOracle oracle = new PaddingOracle(key);
        assertTrue(oracle.isPaddingValid(x1, x2));
    }

    // Intermediate: invalid padding (no padding at all)
    @Test
    public void testInvalidPadding() {
        byte[] key = new byte[8];
        byte[] x1 = new byte[8];
        byte[] m2 = new byte[] {10, 20, 30, 40, 50, 60, 70, 80}; // no valid padding
        byte[] x2 = xor(x1, xor(m2, key));

        PaddingOracle oracle = new PaddingOracle(key);
        assertFalse(oracle.isPaddingValid(x1, x2));
    }

    // Advanced: full attack simulation
    @Test
    public void testFullPaddingOracleAttack() {
        byte[] key = new byte[] { 0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70, 0x08 };
        byte[] m2 = new byte[] { 'S', 'E', 'C', 'R', 'E', 'T', 0x02, 0x02 }; // padded plaintext
        byte[] x1 = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
        byte[] x2 = xor(x1, xor(m2, key));

        PaddingOracle oracle = new PaddingOracle(key);
        PaddingOracleAttack attacker = new PaddingOracleAttack(oracle);
        byte[] recovered = attacker.recoverPlaintext(x1, x2);

        assertArrayEquals(m2, recovered);
    }
}
