package dev.ashraf;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Scanner;



public class FileSplit {

    public String inputFolderPath;
    public String outputFolderPath;
    public int splitSize;
    public String splitSizeUnit; // For now, set to MB
    public long splitSizeByte;

    public FileSplit(String inputFolderPath, String outputFolderPath, int splitSize, String splitSizeUnit) {
        this.inputFolderPath = inputFolderPath;
        this.outputFolderPath = outputFolderPath;
        this.splitSize = splitSize;
        this.splitSizeUnit = splitSizeUnit;

        // Default = MB
        if (this.splitSizeUnit.equals("MB")){
            this.splitSizeByte = this.splitSize * FileUtils.ONE_MB;
        }
    }
    public String getCurrentOutputFolder(File targetFile, String inputFolderPath, String outputFolderPath){
        String relative = targetFile.getParent().replace(inputFolderPath, "");
        String currentFolder = outputFolderPath + relative;
        return currentFolder;
    }

    public void readWriteBuffer(byte[] buffer, FileInputStream fis, File targetFile, File inputFolder, String outputFolderPath, int currentIndex){
        try {
            int bytesRead = fis.read(buffer);
            String currentFolder = getCurrentOutputFolder(targetFile, inputFolder.getAbsolutePath(), outputFolderPath);
            File output = new File(currentFolder + "\\" +  targetFile.getName() + "_" + currentIndex);
            FileUtils.writeByteArrayToFile(output,buffer);
            System.out.println("--> [Splitted] Output file : "+output.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run(){
        File inputFolder = new File(this.inputFolderPath);
        String outputFolder = this.outputFolderPath;
        FileUtils.listFiles(inputFolder, null,true).forEach(f -> {
            System.out.println("Current file : "+f.getAbsolutePath());
            long fileSize = FileUtils.sizeOf(f);
            // Split file if it is larger than split size
            if (fileSize > this.splitSizeByte){
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(f);
                    int portion = (int) (fileSize / this.splitSizeByte);
                    int remainder = (int) (fileSize % this.splitSizeByte);
                    byte[] buffer = new byte[(int) this.splitSizeByte];
                    // Read normal-size buffer
                    for (int i = 1; i <= portion; i++){
                        readWriteBuffer(buffer, fis, f, inputFolder, outputFolderPath, i);
                    }
                    // Read remaining buffer
                    buffer = new byte[(int) remainder];
                    readWriteBuffer(buffer, fis, f, inputFolder, outputFolderPath, (portion + 1));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            // Otherwise, simply copy the file to respective folder
            } else {
                String currentFolder = getCurrentOutputFolder(f, inputFolder.getAbsolutePath(), outputFolder);
                File output = new File(currentFolder + "\\" +  f.getName());
                try {
                    FileUtils.copyFile(f, output);
                    System.out.println("--> [Normal] Output file : "+output.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void main(String[] args){
        String inputFolder = null;
        String outputFolder = null;
        Integer splitSize = null;
        System.out.println("=== FileSplit Tool ===");
        System.out.println("Simple Java-based tool for splitting large files and preserving the containing folder structure.");
        if (args.length == 0){
            System.out.println("** Manual Mode **");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter full input folder path : ");
            inputFolder = scanner.nextLine();
            System.out.println("Enter full output folder path : ");
            outputFolder = scanner.nextLine();
            System.out.println("Enter split size (in MB) : ");
            splitSize = Integer.valueOf(scanner.nextLine());
        } else {
            System.out.println("** Auto Mode **");
            inputFolder = args[0];
            outputFolder = args[1];
            splitSize = Integer.valueOf(args[2]);
        }
        // Run the splitter
        FileSplit fileSplit = new FileSplit(inputFolder, outputFolder, splitSize, "MB");
        fileSplit.run();
    }

}
