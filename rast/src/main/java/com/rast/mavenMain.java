package com.rast;

import org.apache.poi.sl.extractor.SlideShowExtractor;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.*;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;

public class mavenMain {
    public static void main(String[] args) {

        String fileName = "C:\\Users\\arastorguev\\Desktop\\TestPP.pptx";
        String changedFileName = "C:\\Users\\arastorguev\\Desktop\\TestPP - Copy.pptx";
        try {

            XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(fileName));
            SlideShowExtractor pptExtractor = new SlideShowExtractor(ppt);
            List<XSLFSlide> pptSlides = ppt.getSlides();
            List<XSLFPictureData> pptPictures = ppt.getPictureData();
            String pptMD5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(new FileInputStream(fileName));


            XMLSlideShow changedPpt = new XMLSlideShow(new FileInputStream(changedFileName));
            SlideShowExtractor changedPptExtractor = new SlideShowExtractor(changedPpt);
            List<XSLFSlide> changedPptSlides = changedPpt.getSlides();
            List<XSLFPictureData> changedPptPictures = changedPpt.getPictureData();
            String changedPptMD5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(new FileInputStream(changedFileName));

            System.out.println(slideHasPicture(pptSlides.get(3)));




        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    public static Boolean slideHasPicture(XSLFSlide slide){
        return slide.getShapes().stream().filter(obj -> obj instanceof XSLFPictureShape).count() > 0;
    }

}






