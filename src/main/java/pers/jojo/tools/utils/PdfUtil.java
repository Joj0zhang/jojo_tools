package pers.jojo.tools.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @ClassName PdfUtil
 * @Description
 * @Author 张淳
 * @Date 2019/12/19 10:27
 * @Version 1.0.0
 **/
public class PdfUtil {
    /**
     * 将PDF按页数每页转换成一个jpg图片
     *
     * @param pdfBytes
     * @return
     */
    public static List<BufferedImage> pdfToImagePath(byte[] pdfBytes) {
        List<BufferedImage> list = new ArrayList<>();
        try {
            PDDocument doc = PDDocument.load(pdfBytes);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                //第二个参数越大生成图片分辨率越高，转换时间也就越长
                BufferedImage image = renderer.renderImage(i, 3f);
                list.add(image);
            }
            //关闭文件,不然该pdf文件会一直被占用。
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> bufferedImageToBase64(List<BufferedImage> bufferedImagesList) {
        List<String> list = new ArrayList<>();
        bufferedImagesList.forEach(bufferedImage -> {
            try {
                //io流
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //写入流中
                ImageIO.write(bufferedImage, "png", baos);
                //转换成字节
                byte[] bytes = baos.toByteArray();
                //转换成base64串
                String base64Image = Base64.getEncoder().encodeToString(bytes).trim();
                //删除 \r\n
                base64Image = base64Image.replaceAll("\n", "").replaceAll("\r", "");
                base64Image = "data:image/png;base64, " + base64Image;
                list.add(base64Image);
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return list;
    }
}