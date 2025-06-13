package org.example.fileorganizingtoolfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.*;


public class OrganizerController {

    @FXML private Button selectFolderButton;
    @FXML private TextArea logArea;

    @FXML
    private void initialize() {
        selectFolderButton.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            File selectedDirectory = chooser.showDialog(new Stage());

            if (selectedDirectory != null) {
                organizeFiles(selectedDirectory.getAbsolutePath());
            } else {
                log("No folder selected.");
            }
        });
    }

    private void organizeFiles(String folderPath) {
        log("Organizing files in: " + folderPath);

        String pdf = folderPath + "\\PDF";
        String img = folderPath + "\\Images";
        String doc = folderPath + "\\Word Documents";
        String txt = folderPath + "\\Text Files";

        createFolder(pdf);
        createFolder(img);
        createFolder(doc);
        createFolder(txt);

        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            log("No files found.");
            return;
        }

        for (File file : files) {
            if (file.isFile()) {
                String name = file.getName();
                String ext = "";
                int dot = name.lastIndexOf('.');
                if (dot > 0 && dot < name.length() - 1)
                    ext = name.substring(dot + 1).toLowerCase();

                String dest = switch (ext) {
                    case "pdf" -> pdf;
                    case "jpg", "jpeg", "png", "gif" -> img;
                    case "doc", "docx" -> doc;
                    case "txt" -> txt;
                    default -> null;
                };

                if (dest != null)
                    moveFile(file.getAbsolutePath(), dest);
            }
        }

        log("Done.");
    }

    private void createFolder(String path) {
        try {
            Files.createDirectories(Paths.get(path));
            log("Created folder: " + path);
        } catch (Exception e) {
            log("Failed to create folder: " + e.getMessage());
        }
    }

    private void moveFile(String source, String destFolder) {
        Path src = Paths.get(source);
        Path dest = Paths.get(destFolder, src.getFileName().toString());
        try {
            Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);
            log("Moved: " + src.getFileName());
        } catch (Exception e) {
            log("Failed to move " + src.getFileName() + ": " + e.getMessage());
        }
    }

    private void log(String message) {
        logArea.appendText(message + "\n");
    }

}
