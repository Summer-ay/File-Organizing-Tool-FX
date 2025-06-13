package org.example.fileorganizingtoolfx;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileOrganizingLogic {
    public static void main(String[] args) {
        String folderpath = "C:\\Users\\HP\\OneDrive\\Desktop\\Java Organizing Folder";

        //Subfolders based on extensions.
        String PDF = folderpath + "\\PDF";
        String Images = folderpath + "\\Images";
        String WordDocuments = folderpath + "\\Word Documents";
        String Text = folderpath + "\\Text Files";

        //Create required Folders
        createFolder(PDF);
        createFolder(Images);
        createFolder(WordDocuments);
        createFolder(Text);

        // List and organize folders
        Lister(folderpath, PDF, Images, WordDocuments, Text);

    }

    public static void Lister(String folderpath, String PDF, String Images, String WordDocuments, String Text){
        try {
            File folder = new File(folderpath);

            if (!folder.exists() || !folder.isDirectory()){
                throw new IllegalArgumentException("The specified path is not a valid directory");
            }

            File[] files = folder.listFiles();

            if(files == null || files.length == 0){
                System.out.println("Folder is empty or the files could not be retrieved");
                return;
            }

            //Listing the folder's names and their extensions
            for (File file : files){
                if (file.isFile()) {
                    String fileName = file.getName();
                    String extension = "";

                    int dot = fileName.lastIndexOf('.');
                    if (dot > 0 && dot < fileName.length() - 1) {
                        extension = fileName.substring(dot + 1).toLowerCase();
                    }

                    System.out.println("File: " + fileName + " || Extension: " + extension);

                    // Choosing the folders in which different files will move in
                    String destinationFolder = null;
                    switch (extension){
                        case "pdf":
                            destinationFolder = PDF;
                            break;
                        case "jpg":
                        case "jpeg":
                        case "png":
                        case "gif":
                            destinationFolder = Images;
                            break;
                        case "docx":
                        case "doc":
                            destinationFolder = WordDocuments;
                            break;
                        case "txt":
                            destinationFolder = Text;
                            break;
                        default:
                            continue; //Skip moving unknown file types
                    }

                    moveFiles(file.getAbsolutePath(), destinationFolder);
                }
            }

        } catch (IllegalArgumentException e){
            System.out.println("Error: " + e.getMessage());
        } catch (SecurityException e){
            System.out.println("Permission to the directory denied");
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void createFolder(String folderpath){
        try {
            Path path = Paths.get(folderpath);
            Files.createDirectories(path);
            System.out.println("Folder created: " + path.toAbsolutePath()); //path.toAbsolutePath() gives the absolute path of the file
        } catch (Exception e){
            System.out.println("Failed to create folder: " + e.getMessage());
        }
    }

    public static void moveFiles(String sourceFilePath, String destinationFolderPath){
        Path sourcePath = Paths.get(sourceFilePath);
        Path destinationPath = Paths.get(destinationFolderPath, sourcePath.getFileName().toString());  //We will get the destination folder path and the file's name using getFileName().

        try{
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING); //StandardCopyOption.REPLACE_EXISTING means if the file already exists, overwrite it
            System.out.println("Moved: " + sourcePath.getFileName() + " -> " + destinationFolderPath);
        } catch (Exception e){
            System.out.println("Failed to move the file: " + sourcePath.getFileName() + " | " + e.getMessage());
        }
    }
}
