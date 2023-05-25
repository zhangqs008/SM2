package src.framework.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class WatermarkUtils {
    // 水印透明度
    private static float alpha = 0.5f;
    // 水印文字大小
    public static final int FONT_SIZE = 22;
    // 水印文字字体
    private static Font font = new Font("宋体", Font.PLAIN, FONT_SIZE);
    // 水印文字颜色
    private static Color color = Color.red;
    // 水印之间的间隔
    private static final int XMOVE = 80;
    // 水印之间的间隔
    private static final int YMOVE = 80;

    /**
     * 给图片添加水印文字
     *
     * @param logoText   水印文字
     * @param srcImgPath 源图片路径
     * @param targerPath 目标图片路径
     */
    public static void ImageByText(String logoText, String srcImgPath, String targerPath) {
        ImageByText(logoText, srcImgPath, targerPath, null);
    }

    /**
     * 获取文本长度。汉字为1:1，英文和数字为2:1
     */
    private static int getTextLength(String text) {
        int length = text.length();
        for (int i = 0; i < text.length(); i++) {
            String s = String.valueOf(text.charAt(i));
            if (s.getBytes().length > 1) {
                length++;
            }
        }
        length = length % 2 == 0 ? length / 2 : length / 2 + 1;
        return length;
    }

    /**
     * 给图片添加水印文字、可设置水印文字的旋转角度
     *
     * @param logoText
     * @param srcImgPath
     * @param targerPath
     * @param degree
     */
    public static void ImageByText(String logoText, String srcImgPath, String targerPath, Integer degree) {

        InputStream is = null;
        OutputStream os = null;
        try {
            // 源图片
            Image srcImg = ImageIO.read(new File(srcImgPath));
            int width = srcImg.getWidth(null);// 原图宽度
            int height = srcImg.getHeight(null);// 原图高度
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null),
                    BufferedImage.TYPE_INT_RGB);
            // 得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0,
                    0, null);
            // 设置水印旋转
            if (null != degree) {
                g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
            }
            // 设置水印文字颜色
            g.setColor(color);
            // 设置水印文字Font
            g.setFont(font);
            // 设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

            int x = -width / 2;
            int y = -height / 2;
            int markWidth = FONT_SIZE * getTextLength(logoText);// 字体长度
            int markHeight = FONT_SIZE;// 字体高度

            // 循环添加水印
            while (x < width * 1.5) {
                y = -height / 2;
                while (y < height * 1.5) {
                    g.drawString(logoText, x, y); 
                    y += markHeight + YMOVE;
                }
                x += markWidth + XMOVE;
            }
            // 释放资源
            g.dispose();
            // 生成图片
            os = new FileOutputStream(targerPath);
            ImageIO.write(buffImg, "JPG", os);
            System.out.println("添加水印文字成功!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
 
}