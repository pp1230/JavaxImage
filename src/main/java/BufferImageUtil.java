import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class BufferImageUtil {

    public List<BufferedImage> getBufferImages(File file, String format) throws IOException {
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

    public BufferedImage getBufferImage(File file) throws IOException{
        return ImageIO.read(file);
    }

    /**
     * BGR类型
     * 注意：类型名BGR和储存顺序相反，也就是说从低位开始存储
     * @param bufferedImage
     * @return 按照RGB顺序返回
     */
    public int[] getTYPE_3BYTE_BGR(BufferedImage bufferedImage){
        int[] bgr = new int[3];
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int minx = bufferedImage.getMinX();
        int miny = bufferedImage.getMinY();
        //System.out.println("width=" + width + ",height=" + height + ".");
        //System.out.println("minx=" + minx + ",miniy=" + miny + ".");
        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int pixel = bufferedImage.getRGB(i, j);
                /**
                 * 得到的pixel使用十六进制存储
                 * 0x为十六进制标识符
                 * 按位与运算并位移后得到原始颜色值的二进制表示
                 */
                bgr[0] = (pixel & 0xff0000) >> 16;
                bgr[1] = (pixel & 0xff00) >> 8;
                bgr[2] = (pixel & 0xff);
//                if(i<200)
//                    System.out.println("i=" + i + ",j=" + j + ":(" + bgr[0] + ","
//                            + bgr[1] + "," + bgr[2] + ")");
            }
        }
        return bgr;
    }

    /**
     * ARGB类型
     * 注意：类型名BGR和储存顺序相反，也就是说从低位开始存储
     * 但是Alpha在高位
     * @param bufferedImage
     * @return 按照RGBA顺序返回
     */
    public int[] getTYPE_4BYTE_ABGR(BufferedImage bufferedImage){
        int[] argb = new int[4];
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int minx = bufferedImage.getMinX();
        int miny = bufferedImage.getMinY();
        //System.out.println("width=" + width + ",height=" + height + ".");
        //System.out.println("minx=" + minx + ",miniy=" + miny + ".");
        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int pixel = bufferedImage.getRGB(i, j);

                argb[0] = (pixel & 0xff000000) >> 24;
                argb[1] = (pixel & 0xff0000) >> 16;
                argb[2] = (pixel & 0xff00) >> 8;
                argb[3] = (pixel & 0xff) ;

//                    System.out.println("i=" + i + ",j=" + j + ":(" + argb[1] + ","
//                            + argb[2] + "," + argb[3] + ")"+" alpha:"+argb[0]);
            }
        }
        return argb;
    }
}
