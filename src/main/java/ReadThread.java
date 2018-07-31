import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

public class ReadThread implements Callable<int[][]>{
    private File[] files;
    private BufferImageUtil bufferImageUtil;
    public ReadThread(File[] files, BufferImageUtil bufferImageUtil){
        this.files = files;
        this.bufferImageUtil = bufferImageUtil;
    }
    public int[][] call() {
        return bufferImageUtil.getARGBs(files);
    }
}
