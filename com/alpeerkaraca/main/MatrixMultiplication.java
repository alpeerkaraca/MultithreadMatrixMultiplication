package com.alpeerkaraca.main;

import java.io.Console;
import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.alpeerkaraca.matrix.MatrixMultiplier;
import com.alpeerkaraca.util.FileUtil;

public class MatrixMultiplication {

    public static void main(String[] args) {
        if (args.length < 3 || args[0].equals("-h") || args.length > 6) {
            System.out.print('\u000C');
            System.out.println(
                    "Kullanım: java com.alpeerkaraca.main.MatrixMultiplication <satır> <sütun> <thread sayısı> (opsiyonel: <sonuç dosya adı> <matris 1 dosya adı> <matris 2 dosya adı> )\n\nsatır: İlk matrisin satır sayısı\nsütun: İlk matrisin sütun sayısı ve ikinci matrisin satır sayısı\nthread sayısı: Kullanılacak thread sayısı\nsonuç dosya adı: Sonuç matrisinin yazılacağı dosya adı\nmatris 1 dosya adı: İlk matrisin okunacağı dosya adı\nmatris 2 dosya adı: İkinci matrisin okunacağı dosya adı\n\nÖrnek kullanım: java com.alpeerkaraca.main.MatrixMultiplication 100 100 4 result.txt matrix1.txt matrix2.txt\n\nNot: Girdi dosyası belirtilmezse rastgele matrisler oluşturulur.\nNot: Sonuç dosyası belirtilmezse sonuç sadece konsola yazdırılır.");
            return;
        }

        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[1]);
        int threadCount = Integer.parseInt(args[2]);
        String matrix1FileName = null;
        String matrix2FileName = null;
        String resultFileName = null;

        if (args.length == 4) {
            resultFileName = args[3];
        }

        if (args.length == 6) {
            resultFileName = args[3];
            matrix1FileName = args[4];
            matrix2FileName = args[5];
        }

        if (rows <= 0 || cols <= 0 || threadCount <= 0) {
            System.out.println("Satır, sütun ve thread sayısı pozitif olmalıdır.");
            return;
        }

        int[][] matrix1 = new int[rows][cols];
        int[][] matrix2 = new int[cols][rows];

        if (matrix1FileName != null && matrix2FileName != null &&
                FileUtil.checkFilesExists(
                        new String[] { matrix1FileName, matrix2FileName
                        })) {
            matrix1 = FileUtil.readMatrixFromFile(matrix1FileName, rows, cols);
            matrix2 = FileUtil.readMatrixFromFile(matrix2FileName, cols, rows);
        } else {
            matrix1 = FileUtil.generateRandomMatrix(rows, cols);
            matrix2 = FileUtil.generateRandomMatrix(cols, rows);
        }

        FileUtil.writeThreadHeader();
        int[][] result = new int[cols][rows];
        Thread[] threads = new Thread[threadCount];
        int chunkSize = rows / threadCount;
        int startRow = 0;
        int endRow = chunkSize;

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new MatrixMultiplier(matrix1, matrix2, result, startRow, endRow));
            threads[i].start();
            startRow = endRow;
            endRow = (i == threadCount - 2) ? rows : endRow + chunkSize;
        }
        for (int i = 0; i < threadCount; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();

        if (resultFileName == null) {
            System.out.println("Result: ");
            FileUtil.printMatrix(result);
            System.out.println("Total process time: " + (endTime - startTime) + " ms");
        } else {
            FileUtil.writeMatrixToFile(result, resultFileName);
            System.out.println("Result: ");
            FileUtil.printMatrix(result);
            System.out.println("Total process time: " + (endTime - startTime) + " ms");
        }

    }
}
