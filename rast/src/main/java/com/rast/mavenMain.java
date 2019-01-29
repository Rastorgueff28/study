package com.rast;

import org.apache.poi.sl.extractor.SlideShowExtractor;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.*;

import javax.naming.ldap.HasControls;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class mavenMain {
    public static void main(String[] args) {

        String fileName = "D:\\workspace\\study\\rast\\src\\main\\resources\\TestPP.pptx";
        String changedFileName = "D:\\workspace\\study\\rast\\src\\main\\resources\\TestPP - copy.pptx";
        try {

                XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(fileName));
                SlideShowExtractor pptExtractor = new SlideShowExtractor(ppt);
                List<XSLFSlide> pptSlides = ppt.getSlides();
                List<XSLFPictureData> pptPictures = ppt.getPictureData();
                String pptMD5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(new FileInputStream(fileName));
                List<Long> ppthashes = getSlidesHashes(ppt);

                XMLSlideShow changedPpt = new XMLSlideShow(new FileInputStream(changedFileName));
                SlideShowExtractor changedPptExtractor = new SlideShowExtractor(changedPpt);
                List<XSLFSlide> changedPptSlides = changedPpt.getSlides();
                List<XSLFPictureData> changedPptPictures = changedPpt.getPictureData();
                String changedPptMD5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(new FileInputStream(changedFileName));
                List<Long> changedPpthashes = getSlidesHashes(changedPpt);


            System.out.println(ppthashes);
            System.out.println(changedPpthashes);
            System.out.println(getPresentationsDiff(ppthashes, changedPpthashes));

            System.out.println(pptMD5.equals(changedPptMD5));


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
    protected static List<Long> getSlidesHashes(final XMLSlideShow presentation){

        double zoom = 2; // magnify it by 2
        AffineTransform at = new AffineTransform();
        at.setToScale(zoom, zoom);

        Dimension pgsize = presentation.getPageSize();

        List<XSLFSlide> slides = presentation.getSlides();
        List<Long> hashes = new ArrayList<>();

        for (XSLFSlide slide : slides) {
            BufferedImage img = new BufferedImage((int) Math.ceil(pgsize.width * zoom), (int) Math.ceil(pgsize.height * zoom), BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D graphics = img.createGraphics();
            graphics.setTransform(at);

            graphics.setPaint(Color.white);
            graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
            slide.draw(graphics);
            hashes.add( IOUtils.calculateChecksum(((DataBufferByte) img.getRaster().getDataBuffer()).getData()));
        }
       return hashes;
    }
    protected static List<Long> getPresentationsDiff (final List<Long> source, final List<Long> target){

        if(source.removeAll(target)){
            return source;
        }
        return null;
    }


}






