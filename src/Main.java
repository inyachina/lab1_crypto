import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    private static final String STRING_ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    private static final char[] ALPHABET;
    private static final int LEN_ALPHABET;

    static {
        ALPHABET = STRING_ALPHABET.toCharArray();
        LEN_ALPHABET = ALPHABET.length;
    }

    private static String readConsole(String message) {
        System.out.print(message + ":\n~ ");
        return scanner.nextLine();
    }

    private static char[] toChars(byte[] bytes) {
        String s = new String(bytes);
        return s.toCharArray();
    }

    private static byte[] readFile(String message) {
        String filename = "";
        while ((filename = readConsole(message).trim()).isEmpty()) {

        }
        try {
            return Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
            System.out.println("File Not Found");
            return readFile(message);
        }
    }

    public static void main(String... s) {
        String option;
        try {
            option = readConsole("Choose option: \n 1. Encrypt \n 2. Decrypt");
            if (!option.equals("1") && !option.equals("2")) main(s);

            char[] msg = toChars(readFile("Enter filename of " + (option.equals("2") ? "encrypted" : "") + " message"));
            char[] key = readConsole("Enter key word").toCharArray();

            int msgLen = msg.length;
            char[] fullKey = new char[msgLen];
            StringBuilder result = new StringBuilder();

            System.out.println("Message:\n" + String.valueOf(msg));
            System.out.println("Key: " + String.valueOf(key));


            for(int i = 0, j = 0; i < msgLen; ++i, ++j){
                if(j == key.length)
                    j = 0;
                fullKey[i] = key[j];
            }

            int curCharInd;
            int curKeyInd;
            int keyInd = -1;
            if (option.equals("1")) {
                for (char c : msg) {
                    curCharInd = STRING_ALPHABET.indexOf(Character.toLowerCase(c));
                    if (curCharInd != -1) {
                        ++keyInd;
                        curKeyInd = STRING_ALPHABET.indexOf(Character.toLowerCase(fullKey[keyInd]));
                        result.append(ALPHABET[(curCharInd + curKeyInd) % LEN_ALPHABET]);
                    } else result.append(c);
                }

            } else if (option.equals("2")) {
                for (char c : msg) {
                    curCharInd = STRING_ALPHABET.indexOf(Character.toLowerCase(c));
                    if (curCharInd != -1) {
                        ++keyInd;
                        curKeyInd = STRING_ALPHABET.indexOf(Character.toLowerCase(fullKey[keyInd]));
                        int diff = curCharInd - curKeyInd;
                        result.append(((diff < 0) ? ALPHABET[LEN_ALPHABET + diff] : ALPHABET[diff]));
                    } else result.append(c);
                }
            }
            System.out.println("\nResult:\n" + result + "\n\n");
        } catch (Exception ignored) {
            ignored.printStackTrace();
        } finally {
            main(s);
        }
    }
}