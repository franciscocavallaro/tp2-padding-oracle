package padding_oracle;

import java.util.Arrays;

import static padding_oracle.PaddingOracle.xor;

public class App {
    public static void main(String[] args) {
        // Showcase a full attack with known IV.
        byte[] key = new byte[] { 0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70, 0x08 };
        byte[] iv  = new byte[] { 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x00, 0x01 };

        byte[] m1 = new byte[] { 'P', 'A', 'S', 'S', 'W', 'O', 'R', 'D' };
        byte[] m2 = new byte[] { 'S', 'E', 'C', 'R', 'E', 'T', 0x02, 0x02 }; // padded

        byte[] x1 = xor(iv, xor(m1, key)); // simulate CBC: x1 = E_k(m1 ^ IV)
        byte[] x2 = xor(x1, xor(m2, key)); // x2 = E_k(m2 ^ x1)

        PaddingOracle oracle = new PaddingOracle(key);
        PaddingOracleAttack attacker = new PaddingOracleAttack(oracle);

        // Recover first block m2
        byte[] recoveredM2 = attacker.recoverPlaintext(x1, x2);

        // Since we know IV, we can retrieve the first block as well
        byte[] recoveredM1 = attacker.recoverPlaintext(iv, x1);

        byte[] fullPlaintext = concat(recoveredM1, recoveredM2);
        System.out.println("Recovered plaintext: " + Arrays.toString(fullPlaintext));
        System.out.println("As string: " + new String(fullPlaintext)); // Should print 'PASSWORDSECRET'
    }

    public static byte[] concat(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
