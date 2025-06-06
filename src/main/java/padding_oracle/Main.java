package padding_oracle;

import java.util.Arrays;

import static padding_oracle.PaddingOracle.xor;

public class Main {
    public static void main(String[] args) {
        byte[] key = new byte[] { 0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70, 0x08 };

        byte[] m2 = new byte[] { 'S', 'E', 'C', 'R', 'E', 'T', 0x02, 0x02 }; // padded message
        byte[] x1 = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };

        byte[] x2 = xor(x1, xor(m2, key)); // simulate CBC encryption: x2 = EK(m2) ⊕ x1

        PaddingOracle oracle = new PaddingOracle(key);
        PaddingOracleAttack attacker = new PaddingOracleAttack(oracle);

        byte[] recovered = attacker.recoverPlaintext(x1, x2);
        System.out.println("Recovered plaintext: " + Arrays.toString(recovered));
        System.out.println("As string: " + new String(recovered));
    }
}
