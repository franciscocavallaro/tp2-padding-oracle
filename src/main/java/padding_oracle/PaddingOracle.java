package padding_oracle;

public class PaddingOracle {
    private final byte[] key;

    public PaddingOracle(byte[] key) {
        this.key = key;
    }

    // Simulates simple decryption with XOR and checks for valid padding
    public boolean isPaddingValid(byte[] x1, byte[] x2) {
        byte[] decrypted = xor(x2, key); // DK(x2) = x2 XOR key
        byte[] plaintext = xor(decrypted, x1); // m2 = DK(x2) XOR x1

        int pad = plaintext[7] & 0xFF; // Get padding byte as unsigned
        if (pad < 1 || pad > 8) return false;

        for (int i = 8 - pad; i < 8; i++) {
            if ((plaintext[i] & 0xFF) != pad) return false;
        }
        return true;
    }

    public static byte[] xor(byte[] a, byte[] b) {
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = (byte)(a[i] ^ b[i]);
        }
        return result;
    }
}
