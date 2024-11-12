import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Properties;

public class OpenApiGeneratorRunner {

    public static void main(String[] args) {
        // Load properties from the config.properties file in the current directory (bin folder)
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            return;
        }

        // Get the base directory path (where the Java program is run from)
        Path basePath = Paths.get("").toAbsolutePath().getParent();  // Parent directory of 'bin'

        // Get folders from properties
        String[] folders = properties.getProperty("folders").trim().split(",");

        // Get paths for the generator JAR (from the current directory)
        Path jarPath = Paths.get("openapi-generator-cli-7.8.0.jar");

        for (String folder : folders) {
            folder = folder.trim();
            if (folder.isEmpty()) continue;

            // Construct paths for config and swagger files using properties
            String configFileKey = folder + ".config.file";
            String swaggerFileKey = folder + ".swagger.file";
            String generatedtoKey = folder + ".generatedto";

            String configFileName = properties.getProperty(configFileKey).trim();
            String swaggerFileName = properties.getProperty(swaggerFileKey).trim();
            boolean generateDTO = Boolean.parseBoolean(properties.getProperty(generatedtoKey, "false"));

            if (!generateDTO) {
                System.out.println("DTO generation is disabled for folder: " + folder);
                continue;
            }

            if (configFileName.isEmpty() || swaggerFileName.isEmpty()) {
                System.err.println("Missing config or swagger file for folder: " + folder);
                continue;
            }

            // Construct paths relative to the parent directory of 'bin'
            Path folderPath = basePath.resolve(folder);
            Path configFilePath = folderPath.resolve(configFileName);
            Path swaggerFilePath = folderPath.resolve(swaggerFileName);
            Path outputPath = folderPath.resolve("output");

            // Clear the output directory if it exists
            clearDirectory(outputPath.toFile());

            // Print paths for debugging
            System.out.println("========================================");
            System.out.println("Generating DTOs for directory: " + folder);
            System.out.println("Config File: " + configFilePath.toAbsolutePath());
            System.out.println("Swagger File: " + swaggerFilePath.toAbsolutePath());
            System.out.println("Output Directory: " + outputPath.toAbsolutePath());
            System.out.println("========================================");

            // Run the OpenAPI Generator
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(
                    "java", "-jar", jarPath.toString(),
                    "generate",
                    "-g", "java",
                    "-i", swaggerFilePath.toString(),
                    "-c", configFilePath.toString(),
                    "-o", outputPath.toString()
                );
                processBuilder.inheritIO().start().waitFor();
                System.out.println("DTO generation for " + folder + " completed!");
            } catch (IOException | InterruptedException e) {
                System.err.println("Error running OpenAPI Generator: " + e.getMessage());
            }
        }

        System.out.println("All DTOs processed successfully!");
    }

    private static void clearDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        clearDirectory(file);  // Recursively delete subdirectories
                    }
                    file.delete();
                }
            }
        }
    }
}
