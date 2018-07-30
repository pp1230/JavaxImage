

import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.LinkedList;
import java.util.List;

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
     * 图片解析测试，不包含图片的读取时间
     * JPG,30K,990*300pixel,三通道,1000 loop,12.1ms/张
     * JPG,30K,990*300pixel,三通道,10000 loop,11.9ms/张
     * JPG,30K,990*300pixel,三通道,100000 loop,12.1ms/张
     *
     * PNG,27K,1294*295pixel,四通道,1000 loop,16.8ms/张
     * PNG,27K,1294*295pixel,四通道,10000 loop,16.6ms/张
     * PNG,27K,1294*295pixel,四通道,100000 loop,16.6ms/张
     *
     * BMP,30K,109*96pixel,三通道,1000 loop,1.1ms/张
     * BMP,30K,109*96pixel,三通道,10000 loop,0.8ms/张
     * BMP,30K,109*96pixel,三通道,100000 loop,0.9ms/张
     */
    @Test
    public void getImageRGB() {
        File file = bmp;
        String format = "BMP";
        int loopNum = 100000;

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
                    int imageType = bufferedImage.getType();
                    System.out.println("通道格式：" + imageType);
                    if (imageType == BufferedImage.TYPE_3BYTE_BGR) {
                        bufferImageUtil.getTYPE_3BYTE_BGR(bufferedImage);
                    } else System.out.println("其他通道格式，暂时无法解析！");
                }
            } else {
                try {
                    BufferedImage bufferedImage = bufferImageUtil.getBufferImage(file);
                    int type = bufferedImage.getType();
                    if (type == BufferedImage.TYPE_3BYTE_BGR) {
                        bufferImageUtil.getTYPE_3BYTE_BGR(bufferedImage);
                    }
                    else if(type == BufferedImage.TYPE_4BYTE_ABGR){
                        bufferImageUtil.getTYPE_4BYTE_ABGR(bufferedImage);
                    }
                    else System.out.println("通道格式"+type+"，暂时无法解析！");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
