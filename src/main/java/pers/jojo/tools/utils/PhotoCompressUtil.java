package pers.jojo.tools.utils;

import org.springframework.util.Base64Utils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @ClassName PhotoCompressUtil
 * @Description
 * @Author 张淳
 * @Date 2019/12/18 17:27
 * @Version 1.0.0
 **/
public class PhotoCompressUtil {

    /**
     * @param base64Image base64格式的图片，可根据情况调整格式
     * @param targetSize  图片文件目标大小，单位KB，取值范围[minSize, maxSize]
     * @param minSize     图片文件最小大小，单位KB
     * @param maxSize     图片文件最大大小，单位KB
     * @return 压缩后的图片, base64格式，可根据情况调整格式
     */
    public static String compressPhoto(String base64Image, double targetSize, double minSize, double maxSize) {
        if (targetSize < minSize || targetSize > maxSize) {
            return null;
        }
        try {
            //base64转IS
            byte[] bytes = Base64Utils.decodeFromString(base64Image);
            InputStream input = new ByteArrayInputStream(bytes);
            OutputStream output;
            //图片宽度，初值为300
            int width = 300;
            //压缩次数
            int count = 0;
            int countMax = 5;
            //参数t，初值为0
            double t = 0;
            double imageSize;
            //经估算，ImageUtils.resize方法生成的图片大小imageSize与设置宽度width的大约满足参数方程
            //width=2^t * width[0]
            //imageSize=1.5^t * imageSize[0]
            do {
                output = new ByteArrayOutputStream();
                width = (int) (width * Math.pow(1.5, t));
                resize(input, output, width, -1, -1, -1);
                input = new ByteArrayInputStream(((ByteArrayOutputStream) output).toByteArray());
                imageSize = (double) input.available() / 1024;
                t = Math.log(targetSize / imageSize) / Math.log(2);
                count++;
            } while ((imageSize < minSize || imageSize > maxSize) && count < countMax);
            if (count >= countMax) {
                return null;
            }
            return Base64Utils.encodeToString(((ByteArrayOutputStream) output).toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void resize(InputStream input, OutputStream output, int width, int height, int maxWidth, int maxHeight) throws Exception {
        if (width < 1 && height < 1 && maxWidth < 1 && maxHeight < 1) {
            try {
                IOUtils.copy(input, output);
            } catch (IOException e) {
                throw new Exception("resize error: ", e);
            }
        }
        try {
            BufferedImage img = ImageIO.read(input);
            boolean hasNotAlpha = !img.getColorModel().hasAlpha();
            double w = img.getWidth(null);
            double h = img.getHeight(null);
            int toWidth;
            int toHeight;
            double rate = w / h;
            if (width > 0 && height > 0) {
                rate = ((double) width) / ((double) height);
                toWidth = width;
                toHeight = height;
            } else if (width > 0) {
                toWidth = width;
                toHeight = (int) (toWidth / rate);
            } else if (height > 0) {
                toHeight = height;
                toWidth = (int) (toHeight * rate);
            } else {
                toWidth = ((Number) w).intValue();
                toHeight = ((Number) h).intValue();
            }
            if (maxWidth > 0 && toWidth > maxWidth) {
                toWidth = maxWidth;
                toHeight = (int) (toWidth / rate);
            }
            if (maxHeight > 0 && toHeight > maxHeight) {
                toHeight = maxHeight;
                toWidth = (int) (toHeight * rate);
            }
            BufferedImage tag = new BufferedImage(toWidth, toHeight, hasNotAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
            // Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
            Graphics2D graphics2d = (Graphics2D) tag.getGraphics();
            graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            graphics2d.drawImage(img, 0, 0, toWidth, toHeight, null);
            ImageIO.write(tag, hasNotAlpha ? "jpg" : "png", output);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            input.close();
            output.close();
        }
    }
}