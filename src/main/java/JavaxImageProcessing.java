

import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 皮怀雨 2018-07-28
 * 使用Java原生库ImageIO解析图片
 */
public class JavaxImageProcessing {

    BufferImageUtil bufferImageUtil = new BufferImageUtil();
    File gifs = new File("src/main/resources/snow.gif");
    File gif = new File("src/main/resources/gif30k.gif");
    File jpg = new File("src/main/resources/jpg30k.jpg");
    File png = new File("src/main/resources/png27k.png");
    File bmp = new File("src/main/resources/bmp30k.bmp");
    //不支持格式
    //File tiff = new File("src/main/resources/tiff171k.tiff");
    //File webp = new File("src/main/resources/webp30k.webp");

    File vocDir = new File("C:\\Users\\P52\\Pictures\\VOCdevkit\\VOC2012\\JPEGImages");

    /**
     * 经测试jpg和gif都能使用这种方法读取
     * 其中gif可以读取出所有图片
     * @throws IOException
     */
    @Test
    public void gifReadTest() throws IOException{
        ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
        ImageInputStream in = ImageIO.createImageInputStream(gifs);
        reader.setInput(in);
        for (int i = 0, count = reader.getNumImages(true); i < count; i++) {
            BufferedImage image = reader.read(i);
            ImageIO.write(image, "PNG", new File(  "src/main/resources/output/snow" +i + ".png"));
        }
    }

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
    @Test
    public void getImageRGB() {
        File file = jpg;
        String format = "JPG";
        int loopNum = 1;

        while (loopNum > 0) {
            loopNum--;
            if (format.equals("GIF")) {
                List<BufferedImage> bufferedImages = null;
                try {
                    bufferedImages = bufferImageUtil.getBufferImages(file, format);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int n = 0; n < bufferedImages.size(); n++) {
                    System.out.println("解析第" + (n + 1) + "张" + format + "图片");
                    BufferedImage bufferedImage = bufferedImages.get(n);
                    /**
                     * 全部通道格式可以在BufferImage类中查找,共14种
                     * 每种格式RGB三原色的顺序可能不同，还可能包含alpha通道
                     * 这里以TYPE_3BYTE_BGR 为例
                     */
                    bufferImageUtil.getARGB(bufferedImage);
                }
            } else {
                try {
                    BufferedImage bufferedImage = bufferImageUtil.getBufferImage(file);
                    bufferImageUtil.getARGB(bufferedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 17111张jpg图片单线程解析测试
     * 总时间：3m53s509ms
     * 单张解析时间：13.64ms
     */
    @Test
    public void SingleRead(){
        File[] files = vocDir.listFiles();
        bufferImageUtil.getARGBs(files);
    }

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
    @Test
    public void MultiThreadRead() throws InterruptedException, ExecutionException{
        int threadNum = 12;
        ExecutorService e = Executors.newFixedThreadPool(threadNum);
        File[] files = vocDir.listFiles();
        List<Future<int[][]>> fileList = new ArrayList<Future<int[][]>>();
        int n = files.length;

        for(int i=0;i<n-1;i+=n/threadNum){
            fileList.add(e.submit(new ReadThread(Arrays.copyOfRange(files, i, i+n/threadNum-1), bufferImageUtil)));
        }
        int sum = 0;
        for(Future<int[][]> feature:fileList){
            sum += feature.get().length;
            System.out.println("第"+sum+"张");
        }

    }

}
