package module;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Random;

public class ImageProcess {

    private static String FULL_PATH = "tmp" + File.separator + "images" + File.separator;
    // private static String FULL_PATH = File.separator +  "tmp" + File.separator + "images" + File.separator; // in windows
    private static int DEFAULT_GEN_FILENAME_LENGTH = 10;

    private static char[] charaters = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5','6','7','8','9'};

    public static String saveFromUrl(String _url, String local_path){
        //String local_path = System.getProperty("catalina.base").toString();
        String filename = ImageProcess.genFileName(DEFAULT_GEN_FILENAME_LENGTH);


        Image image = null;
        try{
            URL url = new URL(_url);
            BufferedImage buffImage = ImageIO.read(url);

            File file = new File(local_path + FULL_PATH + filename);

            ImageIO.write(buffImage, "jpg", file);

        }catch(Exception e){
            e.printStackTrace();
        }

        return FULL_PATH + filename;
    }

    private static String genFileName(int length){
        StringBuffer sb = new StringBuffer();
        Random rand = new Random();
        for(int i=0; i< length; i++){
            sb.append( charaters[rand.nextInt(charaters.length)] );
        }

        return sb.toString();

    }
}
