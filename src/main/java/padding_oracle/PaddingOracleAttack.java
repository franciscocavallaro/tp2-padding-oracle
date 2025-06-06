package padding_oracle;

public class PaddingOracleAttack {
    private final PaddingOracle oracle;

    public PaddingOracleAttack(PaddingOracle oracle) {
        this.oracle = oracle;
    }

    public byte[] recoverPlaintext(byte[] x1, byte[] x2) {
        int blockSize = 8;
        byte[] recovered = new byte[blockSize];
        byte[] intermediate = new byte[blockSize];
        byte[] modifiedX1 = new byte[blockSize];

        for (int pad = 1; pad <= blockSize; pad++) {
            for (int i = 0; i < blockSize - pad; i++) {
                modifiedX1[i] = 0;
            }

            for (int i = 1; i < pad; i++) {
                modifiedX1[blockSize - i] = (byte)(intermediate[blockSize - i] ^ pad);
            }

            boolean found = false;
            for (int guess = 0; guess < 256; guess++) {
                modifiedX1[blockSize - pad] = (byte) guess;
                if (oracle.isPaddingValid(modifiedX1, x2)) {
                    intermediate[blockSize - pad] = (byte)(guess ^ pad);
                    recovered[blockSize - pad] = (byte)(intermediate[blockSize - pad] ^ x1[blockSize - pad]);
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new RuntimeException("Valid padding not found for byte " + (blockSize - pad));
            }
        }

        return recovered;
    }
}
