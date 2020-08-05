package other;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Settings {

    public static final String SETTINGS_PATH_LINUX = "/opt/tomcat/latest/conf";
    public static final String SETTINGS_PATH_WINDOWS = "Z:\\code\\java\\FunnotesJServer\\settings";
    public static final String SETTINGS_FILE_NAME = "settings.json";
    public static final String COMMAND_FILE_NAME = "SQLCommands.json";

    public static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/";

    public static final String USER = "server";

    private static final String COMMAND_FILE_NOT_FOUND_MESSAGE = "Command file not found";

    public enum OperatingSystem {
        LINUX,
        WINDOWS
    }

    public static final OperatingSystem OPERATING_SYSTEM = OperatingSystem.LINUX;

    public static JSONObject loadCommandsFromFile() throws FileNotFoundException {
        String commandsString;
        StringBuilder commandsStringBuilder = new StringBuilder();
        File commandsFile;
        if (OPERATING_SYSTEM == OperatingSystem.WINDOWS) {
            commandsFile = new File(SETTINGS_PATH_WINDOWS, COMMAND_FILE_NAME);
        } else if (OPERATING_SYSTEM == OperatingSystem.LINUX) {
            commandsFile = new File(SETTINGS_PATH_LINUX, COMMAND_FILE_NAME);
            if (commandsFile.exists()) {
                System.out.println();
            } else {
                System.out.println(COMMAND_FILE_NOT_FOUND_MESSAGE);
            }
        } else return null;
        Scanner scanner;
        if (commandsFile.exists() && commandsFile.canRead()) {
            scanner = new Scanner(commandsFile);
            while (scanner.hasNextLine()) {
                commandsStringBuilder.append(scanner.nextLine());
            }
            commandsString = commandsStringBuilder.toString();
            return new JSONObject(commandsString);
        } else throw new FileNotFoundException(commandsFile.getAbsolutePath());
    }

    public static void main(String[] args) {

    }
}
