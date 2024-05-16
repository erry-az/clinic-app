package az.erry.clinicrest.entities;

public class PatientIdGenerator {
    private char prefix;
    private int number;
    private static final char PREFIX_START = 'A';
    private static final char PREFIX_END = 'Z';
    private static final int MAX_NUMBER = 9999;

    public PatientIdGenerator(String lastId) {
        if (lastId == null || !validateId(lastId)) {
            prefix = PREFIX_START;
            number = 0;
            return;
        }
        prefix = lastId.charAt(0);
        number = Integer.parseInt(lastId.substring(1));
    }

    public PatientIdGenerator() {
        this.prefix = PREFIX_START;
        this.number = 0;
    }

    private boolean validateId(String id) {
        if (id.length() != 5 || !Character.isAlphabetic(id.charAt(0))) {
            return false;
        }
        try {
            int num = Integer.parseInt(id.substring(1));
            return num > 0 && num <= MAX_NUMBER;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public synchronized String generateId() {
        if (prefix == PREFIX_END && number == MAX_NUMBER) {
            throw new IllegalStateException("Maximum number of patient IDs reached");
        }

        number++;
        if (number > MAX_NUMBER) {
            prefix = (char) (prefix + 1);
            number = 1;
        }

        return prefix + String.format("%04d", number);
    }
}
