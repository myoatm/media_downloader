package Parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FacebookParser {

    public FacebookParser(){

    }

    public JSONObject doParsing(String data, Document _doc, String local_path){
        String pat_split_whole = "videoData:(.*)";
        String pat_live_check = "is_live_stream:false";
        String pat_video_url_hd = "hd_src:\\\\?\\\"(null|.*?)\\\\?\\\"";
        String pat_video_url_sd = "sd_src:\\\\?\\\"(null|.*?)\\\\?\\\"";
        String pat_actorname = "actorname:\\\\?\\\"(.*?)\\\\?\\\"";

        String video_url = null;
        String thumbnail_image_url = null;

        JSONObject returnJson = new JSONObject();
        JSONArray dataJsonArray = new JSONArray();


        Document doc = _doc;
        try{
            JSONObject tmpJson = new JSONObject();

            if(_doc == null){
                doc = Jsoup.connect(data).get();
            }
            Elements scriptElems = doc.getElementsByTag("script");
            Elements titleElems = doc.getElementsByTag("title");
            Elements metaElems = doc.getElementsByTag("meta");

            for(Element elem : metaElems){
                String findProperty_og_img = elem.attr("property");
                if(findProperty_og_img.compareTo("og:image") == 0){
                    thumbnail_image_url = elem.attr("content");
                    tmpJson.put("thumb", thumbnail_image_url);
                }
            }

            Element title = titleElems.get(0);
            returnJson.put("title", title.childNode(0).toString());

            for(Element script : scriptElems){
                String findString = regexSingleMatch(script.toString(), pat_split_whole);

                String actorname = regexSingleMatch(script.toString(), pat_actorname);
                if(actorname != null && actorname.compareTo("") != 0) {
                    returnJson.put("user", actorname);
                }

                if(findString == null || findString.compareTo("") == 0){
                    // 못찾았으면 스킵
                    continue;
                }

                String isLive = regexSingleMatch(script.toString(), pat_live_check);
                if(isLive == null || isLive.compareTo("") == 0) {
                    // 라이브 방송중이면 return null해버리자.
                    return null;
                }

                video_url = regexSingleMatch(script.toString(), pat_video_url_hd);
                if(video_url == null || video_url.compareTo("null") == 0 || video_url.compareTo("") == 0) {
                    video_url = regexSingleMatch(script.toString(), pat_video_url_sd);

                }


                tmpJson.put("url", video_url);
            }
            dataJsonArray.add(tmpJson);
            returnJson.put("data", dataJsonArray);

        }catch(Exception e){
            e.printStackTrace();
        }
        return returnJson;
    }


    private String regexSingleMatch(String data, String pattern){

        Pattern p = Pattern.compile(pattern);
        Matcher match =  p.matcher(data);
        boolean isFound = match.find();

        /*
        System.out.println("-----------------------");
        System.out.println(pattern);
        System.out.println(isFound);
        */

        if(!isFound){
            return null;
        }

        String findString = null;
        try{
            findString = match.group(1);
        }catch(Exception e){
            findString = match.group(0); // just map whole string
        }
        return findString;

    }

}
