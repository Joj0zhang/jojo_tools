package pers.jojo.tools.utils;

import java.io.*;

/**
 * @ClassName FormatTransformUtil
 * @Description
 * @Author 张淳
 * @Date 2019/12/19 13:52
 * @Version 1.0.0
 **/
public class FormatTransformUtil {

    /**
     * File转byte
     *
     * @param tradeFile
     * @return
     */
    public static byte[] file2Byte(File tradeFile) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * byte转is
     *
     * @param buffer
     * @return
     */
    public static InputStream byte2InputStream(byte[] buffer) {
        return new ByteArrayInputStream(buffer);
    }

    /**
     * file转is
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream file2InputStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    /**
     * is转file
     *
     * @param is
     * @param file
     * @throws IOException
     */
    public static void inputStream2File(InputStream is, File file) throws IOException {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int len = 0;
            byte[] buffer = new byte[8192];

            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } finally {
            os.close();
            is.close();
        }
    }
}