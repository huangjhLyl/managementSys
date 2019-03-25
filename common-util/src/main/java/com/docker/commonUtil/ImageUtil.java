package com.docker.commonUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author huangjh
 * @date 2019/3/13 11:57
 */
public class ImageUtil {
    /**
     * 设置背景颜色
     * @param image
     * @param out
     * @throws IOException
     */
    public static  void setBackgroundColor(BufferedImage image, OutputStream out) throws IOException {

        int windowWidth = 1920; //获得窗口宽

        int windowHeight = 1080; //获得窗口高

        BufferedImage outImage = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB);


        Graphics2D graphics2D = (Graphics2D) outImage.getGraphics();
        // 设置背景颜色
        graphics2D.setBackground(Color.WHITE);
        graphics2D.clearRect(0, 0, windowWidth, windowHeight);
        int oldheight = image.getHeight();
        int oldwidth = image.getWidth();
        // 设置图片居中显示
        graphics2D.drawImage(image, (windowWidth - oldwidth) / 2, (windowHeight - oldheight) / 2, null);
        //输出图片
        ImageIO.write(outImage, "png", out);
    }
}
