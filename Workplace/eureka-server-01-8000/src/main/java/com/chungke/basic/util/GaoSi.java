package com.chungke.basic.util;

/**
 * 高斯滤波器
 */

public class GaoSi {
    static  void generateGaussianTemplate(double[][] window, int ksize, double sigma)
    {
        double pi = 3.1415926;
        int center = ksize / 2; // 模板的中心位置，也就是坐标的原点
        double x2, y2;
        for (int i = 0; i < ksize; i++)
        {
            x2 = Math.pow(i - center, 2);
            for (int j = 0; j < ksize; j++)
            {
                y2 = Math.pow(j - center, 2);
                double g = Math.exp(-(x2 + y2) / (2 * sigma * sigma));
                g /= 2 * pi * sigma;
                window[i][j] = g;
            }
        }
        double k = 1 / window[0][0]; // 将左上角的系数归一化为1
        for (int i = 0; i < ksize; i++)
        {
            for (int j = 0; j < ksize; j++)
            {
                window[i][j] *= k;
            }
        }
    }

    public static void main(String[] args) {
        double[][] doubles = new double[5][5];
        generateGaussianTemplate(doubles,5,1);
        System.out.println(doubles);

    }
}
