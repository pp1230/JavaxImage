# 图像解析测试
## 测试环境
CPU: I7-8850H
RAM: 8GB
IDE: IntelliJ IDEA 2018
SDK: Java 1.8
## 使用javax.imageio.ImageIO解析图片步骤
### 第一步：
使用getImageReadersBySuffix方法可读取文件中包含的全部图片；
若为动态GIF可读出所有图片，若为静态，则读出一张。
### 第二步：
读出图片为BufferedImage类，其中包含了14种通道格式；
使用BufferedImage.getType()获得。
### 第三步：
根据通道格式，使用不同的方法解析，得到最终的RGB以及Alpha值。
## 测试结果
### 单张循环测试
/**
     * 一张图片循环解析测试，不包含图片的读取时间
     * JPG,30K,990*300pixel,三通道,1000 loop,12.1ms/张
     * JPG,30K,990*300pixel,三通道,10000 loop,11.9ms/张
     * JPG,30K,990*300pixel,三通道,100000 loop,12.1ms/张
     *
     * PNG,27K,1294*295pixel,四通道,1000 loop,16.8ms/张
     * PNG,27K,1294*295pixel,四通道,10000 loop,16.6ms/张
     * PNG,27K,1294*295pixel,四通道,100000 loop,16.6ms/张
     *
     * BMP,30K,109*96pixel,三通道,1000 loop,1.0ms/张
     * BMP,30K,109*96pixel,三通道,10000 loop,0.8ms/张
     * BMP,30K,109*96pixel,三通道,100000 loop,0.9ms/张
     */
### 真实数据集测试
/**
     * 17111张jpg图片单线程解析测试
     * 总时间：3m53s509ms
     * 单张解析时间：13.64ms
     */

/**
     * 17111张jpg图片多线程解析测试, 1.78GB
     * 2线程：2m12s359ms
     * 单张解析时间: 7.7ms
     * 4线程：1m19s302ms
     * 单张解析时间: 4.6ms
     * 8线程：47s846ms
     * 单张解析时间: 2.7ms
     * 12线程：40s467ms
     * 单张解析时间: 2.3ms
     */
## 结论
通过ImageIO，可以解析常用的图片格式，包括动态GIF图。但也有少量格式如webp、tiff等不能解析。
通过多线程处理图片，在真实数据集上解析最快速度可以达到44.5M/s,单张图片解析速度2.3ms。
