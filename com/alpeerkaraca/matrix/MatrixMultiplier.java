package com.alpeerkaraca.matrix;

import com.alpeerkaraca.util.FileUtil;

public class MatrixMultiplier implements Runnable {
    private int[][] matrix1;
    private int[][] matrix2;
    private int[][] result;
    private int startRow;
    private int endRow;

    public MatrixMultiplier(int[][] matrix1, int[][] matrix2, int[][] result, int startRow, int endRow) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.result = result;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public void run() {
        long threadStartTime = System.nanoTime();
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                for (int k = 0; k < matrix2.length; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        long threadEndTime = System.nanoTime();
        FileUtil.writeThreadProcessingTime(threadStartTime, threadEndTime, Thread.currentThread().getName());
        FileUtil.printThreadProcessingTime(threadStartTime, threadEndTime, Thread.currentThread().getName());
    }
}
