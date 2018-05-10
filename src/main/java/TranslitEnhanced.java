import java.util.*;

public class TranslitEnhanced {

    private static String text = "BALANC";

    private static Map<String, String> singleVariantMap = new HashMap<>();
    private static Map<String, String> multiVariantMap = new HashMap<>();

    private static Map<Integer, String> singleVariantChars = new LinkedHashMap<>();

    private static Map<Integer, String> multiVariantChars = new LinkedHashMap<>(); // symbol position + symbol
    private static Map<Integer, String> unhandledChars = new LinkedHashMap<>();

    static {

        // ENG first, RUS follows
        multiVariantMap.put("a", "aа");
        multiVariantMap.put("c", "cс");
        multiVariantMap.put("e", "eе");
        multiVariantMap.put("k", "kк");
        multiVariantMap.put("o", "oо");
        multiVariantMap.put("p", "pр");
        multiVariantMap.put("x", "xх");
        multiVariantMap.put("y", "yу");

        multiVariantMap.put("A", "AА");
        multiVariantMap.put("B", "BВ");
        multiVariantMap.put("C", "CС");
        multiVariantMap.put("E", "EЕ");
        multiVariantMap.put("H", "HН");
        multiVariantMap.put("K", "KК");
        multiVariantMap.put("M", "MМ");
        multiVariantMap.put("O", "OО");
        multiVariantMap.put("P", "PР");
        multiVariantMap.put("T", "TТ");
        multiVariantMap.put("X", "XХ");
        multiVariantMap.put("Y", "YУ");

        singleVariantMap.put("b", "b");
        singleVariantMap.put("d", "d");
        singleVariantMap.put("f", "f");
        singleVariantMap.put("g", "g");
        singleVariantMap.put("h", "h");
        singleVariantMap.put("i", "i");
        singleVariantMap.put("j", "j");
        singleVariantMap.put("l", "l");
        singleVariantMap.put("m", "m");
        singleVariantMap.put("n", "n");
        singleVariantMap.put("q", "q");
        singleVariantMap.put("r", "r");
        singleVariantMap.put("s", "s");
        singleVariantMap.put("t", "t");
        singleVariantMap.put("u", "u");
        singleVariantMap.put("v", "v");
        singleVariantMap.put("w", "w");
        singleVariantMap.put("z", "z");

        singleVariantMap.put("D", "D");
        singleVariantMap.put("F", "F");
        singleVariantMap.put("G", "G");
        singleVariantMap.put("I", "I");
        singleVariantMap.put("J", "J");
        singleVariantMap.put("L", "L");
        singleVariantMap.put("N", "N");
        singleVariantMap.put("Q", "Q");
        singleVariantMap.put("R", "R");
        singleVariantMap.put("S", "S");
        singleVariantMap.put("U", "U");
        singleVariantMap.put("V", "V");
        singleVariantMap.put("W", "W");
        singleVariantMap.put("Z", "Z");
    }

    public static void main(String[] args) {

        distributeCharsByInterimMaps();
        List<Map<Integer, String>> interimResult = new ArrayList<>();
        int multiVariantCharsSize = multiVariantChars.size();
        System.out.println("Count of variants = " + Math.pow(multiVariantCharsSize, 2));

        for (int i = 0; i < Math.pow(multiVariantCharsSize, 2); i++) {
            Map<Integer, String> interimResultEntry = new LinkedHashMap<>();

            if (i == 0) { // fill 1st variant
                for (Map.Entry<Integer, String> entry : multiVariantChars.entrySet()) {
                    interimResultEntry.put(entry.getKey(),
                            Character.toString(multiVariantMap.get(entry.getValue()).charAt(0))); // 1st variant always consist of ENG chars
                }
                handleSingleVariantChars(interimResultEntry);
                interimResult.add(interimResultEntry);
                continue;
            }

            // fill another variants
            List<Integer> shiftMatrix = getShiftMatrix(i);
            handleMultiVariantChars(interimResultEntry, shiftMatrix);
            handleSingleVariantChars(interimResultEntry);
            handleUnhandledChars();
            interimResult.add(interimResultEntry);
        }
        composeResult(interimResult);
    }


    private static void distributeCharsByInterimMaps() {

        for (int i = 0; i < text.length(); i++) {
            String oneChar = text.substring(i, i + 1);
            if (multiVariantMap.containsKey(oneChar)) {
                multiVariantChars.put(i, oneChar);
            } else if (singleVariantMap.containsKey(oneChar)) {
                singleVariantChars.put(i, oneChar);
            } else {
                unhandledChars.put(i, oneChar);
            }
        }
    }


    private static void handleMultiVariantChars(Map<Integer, String> interimResultEntry, List<Integer> shiftMatrix) {

        // apply shift matrix
        int j = 0;
        for (Map.Entry<Integer, String> entry : multiVariantChars.entrySet()) {
            if (shiftMatrix.contains(j)) {
                interimResultEntry.put(entry.getKey(),
                        Character.toString(multiVariantMap.get(entry.getValue()).charAt(1)));
            } else {
                interimResultEntry.put(entry.getKey(),
                        Character.toString(multiVariantMap.get(entry.getValue()).charAt(0)));
            }
            j++;
        }
    }


    private static void handleSingleVariantChars(Map<Integer, String> interimResultEntry) {

        for (Map.Entry<Integer, String> entry : singleVariantChars.entrySet()) {
            interimResultEntry.put(entry.getKey(), entry.getValue());
        }
    }


    private static void handleUnhandledChars() {

        for (int k = 0; k < 0; k++) {
            // TODO add handling a unhandledChars map
        }
    }


    private static void composeResult(List<Map<Integer, String>> interimResult) {

        for (int i = 0; i < interimResult.size(); i++) {
            String variant = "";
            TreeMap<Integer, String> sortedResult = new TreeMap<>(interimResult.get(i));
            for (Map.Entry<Integer, String> entry : sortedResult.entrySet()) {
                variant += entry.getValue();
            }
            System.out.println("variant " + (i + 1) + " = " + variant);
        }
    }


    private static List<Integer> getShiftMatrix(int iteration) {

        List<Integer> shiftMatrix = new ArrayList<>();
        int index = 0;
        while (iteration != 0) {
            if ((iteration & 1) == 1)
                shiftMatrix.add(index);
            iteration = iteration >> 1;
            index = index + 1;
        }
        return shiftMatrix;
    }
}