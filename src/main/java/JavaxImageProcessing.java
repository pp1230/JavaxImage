

import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 皮怀雨 2018-07-28
 * 使用Java原生库ImageIO解析图片
 */
public class JavaxImageProcessing {

    File gif = new File("src/main/resources/snow.gif");
    File jpg = new File("src/main/resources/jpgTest.jpg");
    File png = new File("src/main/resources/pngTest.png");

    /**
     * 经测试jpg和gif都能使用这种方法读取
     * 其中gif可以读取出所有图片
     * @throws IOException
     */
    @Test
    public void gifReadTest() throws IOException{
        ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
        ImageInputStream in = ImageIO.createImageInputStream(gif);
        reader.setInput(in);
        for (int i = 0, count = reader.getNumImages(true); i < count; i++) {
            BufferedImage image = reader.read(i);
            ImageIO.write(image, "PNG", new File(  "src/main/resources/output/snow" +i + ".png"));
        }
    }

    public List<BufferedImage> getBufferImages(File file, String format) throws IOException{
        ImageReader reader = ImageIO.getImageReadersBySuffix(format).next();
        ImageInputStream in = ImageIO.createImageInputStream(file);
        reader.setInput(in);
        List<BufferedImage> bufferedImages = new LinkedList<BufferedImage>();
        for (int i = 0, count = reader.getNumImages(true); i < count; i++) {
            BufferedImage image = reader.read(i);
            bufferedImages.add(image);
        }
        return bufferedImages;
    }

    /**
     * 获得通道的RGB值，使用0-255表示
     */
    @Test
    public void getImageRGB(){

        List<BufferedImage> bufferedImages = null;
        File file = jpg;
        String format = "JPG";
        try {
         bufferedImages = getBufferImages(file, format);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int n = 0; n<bufferedImages.size();n++) {
            System.out.println("解析第"+(n+1)+"张"+format+"图片");
            BufferedImage bufferedImage = bufferedImages.get(n);
            /**
             * 全部通道格式可以在BufferImage类中查找,共14种
             * 每种格式RGB三原色的顺序可能不同，还可能包含alpha通道
             * 这里以TYPE_3BYTE_BGR 为例
             */
            int imageType = bufferedImage.getType();
            System.out.println("通道格式："+ imageType);
            if(imageType == BufferedImage.TYPE_3BYTE_BGR) {
                int[] rgb = new int[3];
                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();
                int minx = bufferedImage.getMinX();
                int miny = bufferedImage.getMinY();
                System.out.println("width=" + width + ",height=" + height + ".");
                //System.out.println("minx=" + minx + ",miniy=" + miny + ".");
                for (int i = minx; i < width; i++) {
                    for (int j = miny; j < height; j++) {
                        int pixel = bufferedImage.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
                        rgb[0] = (pixel & 0xff0000) >> 16;
                        rgb[1] = (pixel & 0xff00) >> 8;
                        rgb[2] = (pixel & 0xff);
//                    System.out.println("i=" + i + ",j=" + j + ":(" + rgb[0] + ","
//                            + rgb[1] + "," + rgb[2] + ")");
                    }
                }
            }
            else System.out.println("其他通道格式，暂时无法解析！");
        }
    }


}
