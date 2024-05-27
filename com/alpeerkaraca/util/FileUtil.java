package com.alpeerkaraca.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Scanner;

public class FileUtil {

    public static void writeMatrixToFile(int[][] resultMatrix, String fileName) {
        try {
            File file = new File(fileName);
            File parentDir = file.getParentFile();

            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            if (file.createNewFile()) {
                System.out.println("Dosya Oluşturuldu: " + file.getName());
            }
        } catch (IOException e) {
            System.err.println("Dosya oluşturma hatası: " + e.getMessage());
        }

        try (FileWriter fileWriter = new FileWriter(fileName, true)) {
            fileWriter.write(
                    new SimpleDateFormat("\r\n" + "dd-MM-YYYY HH:mm:ss").format(new Date(System.currentTimeMillis()))
                            + "\n");
            for (int i = 0; i < resultMatrix.length; i++) {
                for (int j = 0; j < resultMatrix[0].length; j++) {
                    fileWriter.write(resultMatrix[i][j] + " ");
                }
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            System.err.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    public static int[][] readMatrixFromFile(String fileName, int checkRows, int checkCols) {
        int[][] matrix = null;
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            int rows = 0;
            int cols = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] elements = line.split(" ");
                if (rows == 0) {
                    cols = elements.length;
                }
                rows++;
            }
            if (rows != checkRows || cols != checkCols) {
                System.err.println("Matris boyutları uyumsuz! (Dosya İçeriği: " + rows + "x" + cols + ") Girilen Değer: " + checkRows + "x" + checkCols);
                System.exit(1);
            }
            matrix = new int[rows][cols];
            scanner = new Scanner(file);
            int i = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] elements = line.split(" ");
                for (int j = 0; j < elements.length; j++) {
                    matrix[i][j] = Integer.parseInt(elements[j]);
                }
                i++;
            }
            scanner.close();
        } catch (Exception e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }
        System.out.println("Okunan Matris: ");
        printMatrix(matrix);
        return matrix;
    }

    public static boolean checkFilesExists(String[] fileNames) {
        if (fileNames == null || fileNames.length == 0) {
            return false;
        }
        for (String fileName : fileNames) {
            File file = new File(fileName);
            if (!file.exists()) {
                System.err.println("Dosya bulunamadı: " + fileName);
                System.exit(1);
            }
        }
        return true;
    }

    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static int[][] generateRandomMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        Random rand = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rand.nextInt(10);
            }
        }
        return matrix;
    }

    public static void printThreadProcessingTime(long threadStartTime, long threadEndTime, String threadName) {
        System.out.println(threadName + ": Thread Çalışma Süresi: "
                + (threadEndTime - threadStartTime) + " ns");
    }

    public static void writeThreadProcessingTime(long startTime, long endTime, String threadName) {
        try (FileWriter fileWriter = new FileWriter("threadProcessingTime.txt", true)) {
            fileWriter.write(threadName + " -> Thread Çalışma Süresi: " + (endTime - startTime) + " ns ~~ "
                    + ((endTime - startTime) / 1000000) + "ms\n");
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    public static void writeThreadHeader() {
        try {
            FileWriter fileWriter = new FileWriter("threadProcessingTime.txt", true);
            fileWriter.write(
                    new SimpleDateFormat("\r\n" + "dd-MM-YYYY HH:mm:ss").format(new Date(System.currentTimeMillis()))
                            + "\n");
            fileWriter.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
