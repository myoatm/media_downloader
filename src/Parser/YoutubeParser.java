package Parser;

import org.json.simple.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeParser {

    public YoutubeParser() {
    }

    public JSONObject doParsing(String url){
        JSONObject returnJson = new JSONObject();


        return returnJson;
    }

    private String regexSingleMatch(String data, String pattern){

        Pattern p = Pattern.compile(pattern);
        Matcher match =  p.matcher(data);
        boolean isFound = match.find();

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
