package org.coq.qingdaobeer.tools;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Common tools
 *
 * @author Quanyec
 */
public class C_ {

    /**
     * Remove background then return the BufferedImage instance
     *
     * @param stream
     * @return bufferedImage
     */
    public static BufferedImage rmBackground(InputStream stream) {
        int threshold = 300;
        try {
            BufferedImage img = ImageIO.read(stream);
            stream.close();
            int width = img.getWidth();
            int height = img.getHeight();
            for (int i = 1; i < width; i++) {
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        Color color = new Color(img.getRGB(x, y));
                        int num = color.getRed() + color.getGreen() + color.getBlue();
                        if (num >= threshold) {
                            img.setRGB(x, y, Color.WHITE.getRGB());
                        }
                    }
                }
            }
            for (int i = 1; i < width; i++) {
                Color color1 = new Color(img.getRGB(i, 1));
                int num1 = color1.getRed() + color1.getGreen() + color1.getBlue();
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        Color color = new Color(img.getRGB(x, y));

                        int num = color.getRed() + color.getGreen() + color.getBlue();
                        if (num == num1) {
                            img.setRGB(x, y, Color.BLACK.getRGB());
                        } else {
                            img.setRGB(x, y, Color.WHITE.getRGB());
                        }
                    }
                }
            }
            return img;
        } catch (Exception e) {
        }
        return null;
    }


    /**
     * Get image-content
     *
     * @param stream
     * @return
     */
    public static String getImgContent(InputStream stream) {
        BufferedImage buff = C_.rmBackground(stream);
        if (buff == null) {
            return null;
        }
        ITesseract instance = new Tesseract();
        instance.setDatapath("tessdata");
        instance.setLanguage("eng");
        try {
            String code = instance.doOCR(buff);
            return code;
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return null;
    }

}
