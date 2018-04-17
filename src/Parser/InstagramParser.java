package Parser;

import module.ImageProcess;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstagramParser {
    public InstagramParser() {
    }

    public JSONObject doParsing(String data, Document _doc, String local_path){
        JSONObject returnJson = new JSONObject();

        String pat_script_incl_json = "window._sharedData\\s?=\\s?(.*?);?<\\/script>";

        long timestamp = -1;
        String username = null;

        JSONArray dataJsonArray = null;

        // 루틴 시작
        Document doc;
        doc = _doc;

        // img url and description extract
        try{
            if(doc == null){
                doc = Jsoup.connect(data).get();
            }

            Elements script_elems = doc.getElementsByTag("script");
            for(Element script : script_elems){
                String allJSONData = regexSingleMatch(script.toString(), pat_script_incl_json);
                if(allJSONData == null || allJSONData.compareTo("") ==0){
                    continue;
                }

                JSONParser parser = new JSONParser();
                //Parser.reset();
                JSONObject allJSON = null;
                if(_doc == null){

                    allJSON = (JSONObject)parser.parse(allJSONData);
                }else{
                    allJSON = (JSONObject)parser.parse(unescape(allJSONData));
                }
                JSONArray postPageArray = (JSONArray)((JSONObject)allJSON.get("entry_data")).get("PostPage"); // reallocate
                allJSON = (JSONObject)((JSONObject)((JSONObject)postPageArray.get(0)).get("graphql")).get("shortcode_media");
                allJSON.remove("edge_media_to_comment");
                allJSON.remove("edge_media_preview_like");


                // 비디오 지원가능
                /*
                boolean isVideo = (boolean)allJSON.get("is_video");
                if(isVideo){
                    returnJson.put("msg", "정지 화상이 아닙니다");
                    returnJson.put("code", "-101");

                    return returnJson;
                }
                */

                JSONObject edgeSidecarToChildren = (JSONObject)allJSON.get("edge_sidecar_to_children");
                if(edgeSidecarToChildren != null){ // 여러사진 일 때
                    dataJsonArray = parseJsonMultipleImage(edgeSidecarToChildren, local_path);

                }else{ // 낱장 일 떄
                    dataJsonArray = parseJsonSingleImage(allJSON, local_path);

                }

                JSONObject owner = (JSONObject)allJSON.get("owner");
                username = (String)owner.get("username");

                timestamp = (long)allJSON.get("taken_at_timestamp");

                returnJson.put("data", dataJsonArray);
                returnJson.put("title", timestamp);
                returnJson.put("user", username);

                //찾았으니 break;
                break;
            }

        }catch(Exception e){
            e.printStackTrace();
        }

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

    private static String unescape(String input) {
        StringBuilder builder = new StringBuilder();

        int i = 0;
        while (i < input.length()) {
            // input의 각 문자를 delimiter로 봄
            char delimiter = input.charAt(i); i++; // consume letter or backslash

            // 구분자가 \\이고 현재 순서가 input의 마지막이 아닐 경우
            if(delimiter == '\\' && i < input.length()) {

                // consume first after backslash
                // delimiter 혹은 escape 문자를 consume하고 그 다음의 진짜 문자를 파악
                char ch = input.charAt(i); i++;

                // 만약 백슬래시나 슬래시, 싱글 쿼트, 더블 쿼트를 escape한거면 그 문자를 그대로 builder에 추가한다.
                if(ch == '\\' || ch == '/' || ch == '"' || ch == '\'') {
                    builder.append(ch);
                }
                // 만약 문자 제어 (개행문자등)을 만났다면 변환해서 넣어준다
                else if(ch == 'n') builder.append('\n');
                else if(ch == 'r') builder.append('\r');
                else if(ch == 't') builder.append('\t');
                else if(ch == 'b') builder.append('\b');
                else if(ch == 'f') builder.append('\f');
                // 만약 유니코드를 표현한 \uFFFF 같은 문자라면 4개씩 끊어서 추가한다.
                else if(ch == 'u') {

                    StringBuilder hex = new StringBuilder();

                    // expect 4 digits
                    if (i+4 > input.length()) {
                        throw new RuntimeException("Not enough unicode digits! ");
                    }
                    for (char x : input.substring(i, i + 4).toCharArray()) {
                        if(!Character.isLetterOrDigit(x)) {
                            throw new RuntimeException("Bad character in unicode escape.");
                        }
                        hex.append(Character.toLowerCase(x));
                    }
                    i+=4; // consume those four digits.

                    int code = Integer.parseInt(hex.toString(), 16);
                    builder.append((char) code);
                } else {
                    throw new RuntimeException("Illegal escape sequence: \\"+ch);
                }
            } else { // it's not a backslash, or it's the last character.
                builder.append(delimiter);
            }
        }

        return builder.toString();
    }

    private JSONObject getFitImage(JSONArray _json, String local_path){
        JSONObject eachDataJson = new JSONObject();

        String image_url = null;
        String thumbnail_image_url = null;

        // image size정보는 아직 사용하지 않으나 추후 desc에 추가 가능
        long maxW = 0, minW = Long.MAX_VALUE;
        long maxH = 0, minH = Long.MAX_VALUE;

        int displayListSize = _json.size();
        for(int i=0; i<displayListSize ; i++){
            JSONObject eachResource = (JSONObject)_json.get(i);
            long w= (long)eachResource.get("config_width");
            long h = (long)eachResource.get("config_height");

            if(maxW < w){
                maxW = w;
                maxH = h;
                image_url = (String)eachResource.get("src");
            }
            if(minW > w){
                minW = w;
                minH = h;
                thumbnail_image_url = (String)eachResource.get("src");
            }
        }
        String thumbnail_file_path = ImageProcess.saveFromUrl(thumbnail_image_url, local_path);

        eachDataJson.put("thumb", thumbnail_file_path);
        eachDataJson.put("url", image_url);

        return eachDataJson;

    }

    private JSONArray parseJsonSingleImage(JSONObject eachNode, String local_path){
        JSONArray dataJsonArray = new JSONArray();

        JSONArray displayList = (JSONArray)eachNode.get("display_resources");


        JSONObject parsedObject = getFitImage(displayList, local_path);

        boolean isVideo = (boolean)eachNode.get("is_video");
        if(isVideo) {
            parsedObject.replace("url", eachNode.get("video_url"));
        }

        dataJsonArray.add(parsedObject);


        return dataJsonArray;
    }

    private JSONArray parseJsonMultipleImage(JSONObject _json, String local_path){
        JSONArray dataJsonArray = new JSONArray();

        JSONArray allEdges = (JSONArray)_json.get("edges");
        int allEdgesSize = allEdges.size();
        for(int i=0; i< allEdgesSize; i++){

            JSONObject eachNode = (JSONObject)((JSONObject)allEdges.get(i)).get("node");
            JSONArray displayList = (JSONArray)eachNode.get("display_resources");

            JSONObject parsedObject = getFitImage(displayList, local_path);

            boolean isVideo = (boolean)eachNode.get("is_video");
            if(isVideo) {
                parsedObject.replace("url", eachNode.get("video_url"));
            }
            dataJsonArray.add(parsedObject);
        }

        return dataJsonArray;
    }

}
