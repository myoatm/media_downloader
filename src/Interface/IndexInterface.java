package Interface;


import Parser.FacebookParser;
import Parser.InstagramParser;
import Parser.YoutubeParser;
import org.json.simple.JSONObject;
import org.jsoup.*;
import org.jsoup.nodes.Document;

public class IndexInterface {

    FacebookParser facebookParser;
    InstagramParser instagramParser;
    YoutubeParser youtubeParser;

    public IndexInterface() {
        facebookParser = new FacebookParser();
        instagramParser = new InstagramParser();
    }

    public JSONObject parsingFromFacebookPublic(String data, String local_path){
        JSONObject dataJson = facebookParser.doParsing(data, null, local_path);

        return dataJson;
    }

    public JSONObject parsingFromFacebookPrivate(String data, String local_path){
        Document doc = null;
        JSONObject dataJson = null;
        try {
            //Document doc = null;
            /*
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(data));
            doc = db.parse(is);
            */
            doc = Jsoup.parse(data);
            dataJson = facebookParser.doParsing(null, doc, local_path);

        }catch(Exception e){
            e.printStackTrace();
        }


        return dataJson;
    }

    public JSONObject parsingFromInstagramPrivate(String data, String local_path){
        JSONObject dataJson = null;
        Document doc = null;
        try{
            doc = Jsoup.parse(data);
            dataJson = instagramParser.doParsing(data, doc, local_path);
        }catch(Exception e){
            e.printStackTrace();
        }
        return dataJson;
    }

    public JSONObject parsingFromInstagramPublic(String data, String local_path){
        JSONObject dataJson = instagramParser.doParsing(data, null, local_path);

        return dataJson;
    }

    public JSONObject parsingFromYoutube(String data, String local_path){
        JSONObject dataJson = youtubeParser.doParsing(data, local_path, false);

        return dataJson;
    }

    public JSONObject parsingFromYoutubePlaylist(String data, String local_path){
        JSONObject dataJson = youtubeParser.doParsing(data, local_path, false);

        return dataJson;
    }

    public JSONObject integratedParse(String media, String data, String local_path){

        if(media.compareTo("facebook_private") ==0){
            return parsingFromFacebookPrivate(data, local_path);
        }else if(media.compareTo("facebook_public") ==0){
            return parsingFromFacebookPublic(data, local_path);
        }else if(media.compareTo("youtube") == 0){
            return parsingFromYoutube(data, local_path);
        }else if(media.compareTo("youtube_playlist") == 0){
            return parsingFromYoutubePlaylist(data, local_path);
        }else if(media.compareTo("instagram_private") == 0){
            return parsingFromInstagramPrivate(data, local_path);
        }else if(media.compareTo("instagram_public") ==0){
            return parsingFromInstagramPublic(data, local_path);
        }


        return null;
    }




}
