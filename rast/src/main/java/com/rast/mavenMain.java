package com.rast;

import org.apache.poi.sl.extractor.SlideShowExtractor;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class mavenMain {
    public static void main(String[] args) {

        String fileName = "C:\\Users\\arastorguev\\Desktop\\TestPP.pptx";
        String changedFileName = "C:\\Users\\arastorguev\\Desktop\\TestPP - Copy.pptx";
        try {

            XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(fileName));


            double zoom = 2; // magnify it by 2
            AffineTransform at = new AffineTransform();
            at.setToScale(zoom, zoom);

            Dimension pgsize = ppt.getPageSize();

            List<XSLFSlide> slide = ppt.getSlides();
            for (int i = 0; i < slide.size(); i++) {
                BufferedImage img = new BufferedImage((int) Math.ceil(pgsize.width * zoom), (int) Math.ceil(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                graphics.setTransform(at);

                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                slide.get(i).draw(graphics);
                FileOutputStream out = new FileOutputStream("slide-" + (i + 1) + ".png");
                javax.imageio.ImageIO.write(img, "png", out);
                out.close();

                SlideShowExtractor pptExtractor = new SlideShowExtractor(ppt);
                List<XSLFSlide> pptSlides = ppt.getSlides();
                List<XSLFPictureData> pptPictures = ppt.getPictureData();
                String pptMD5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(new FileInputStream(fileName));


                XMLSlideShow changedPpt = new XMLSlideShow(new FileInputStream(changedFileName));
                SlideShowExtractor changedPptExtractor = new SlideShowExtractor(changedPpt);
                List<XSLFSlide> changedPptSlides = changedPpt.getSlides();
                List<XSLFPictureData> changedPptPictures = changedPpt.getPictureData();
                String changedPptMD5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(new FileInputStream(changedFileName));


            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }
        protected static Boolean slideHasPicture(XSLFSlide slide){
        return slide.getShapes().stream().filter(obj -> obj instanceof XSLFPictureShape).count() > 0;
    }

    protected static List<XSLFShape> getShapeWithPictures(XSLFSlide slide){
       return slide.getShapes().stream().filter(obj -> obj instanceof XSLFPictureShape).collect(Collectors.toList());
    }
    protected static Boolean compareSlidesByPictures(XSLFSlide source, XSLFSlide target){

        List<XSLFShape> sourceShapes = getShapeWithPictures(source);

        List<XSLFShape> targetShapes = getShapeWithPictures(target);

        return  false;

    }



}






