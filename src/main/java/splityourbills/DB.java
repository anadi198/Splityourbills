package splityourbills;

import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.service.Firebase;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


public class DB {
    // get the base-url (ie: 'http://gamma.firebase.com/username')
    public static String firebase_baseUrl = "https://splityourbills.firebaseio.com";

    // get the api-key (ie: 'tR7u9Sqt39qQauLzXmRycXag18Z2')
    public static String firebase_apiKey = "AIzaSyAyLMUYMdIjiy5oJDcYqpoV-oeoJTtnF-8";
    public static String getUsername(String localId)  throws FirebaseException, JsonParseException, JsonMappingException, IOException, JacksonUtilityException
    {
        Firebase firebase = new Firebase( firebase_baseUrl );
        localId = localId.replace("\"","%22");
        FirebaseResponse response = firebase.get(localId);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getRawBody());
        JsonNode nickNode = rootNode.path("Nickname");
        String nick = nickNode.toString().replace("\""," ").trim();
        return nick;
    }
    public static void storeUser(String localId, String nickname, String email) throws FirebaseException, JsonParseException, JsonMappingException, IOException, JacksonUtilityException
    {
        Firebase firebase = new Firebase( firebase_baseUrl );
        FirebaseResponse response;
        localId = localId.replace("\"","%22");
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("Nickname", nickname);
        dataMap.put("Email",email);
        response = firebase.put(localId, dataMap);
    }
    public static void main(String[] args) throws FirebaseException, JsonParseException, JsonMappingException, IOException, JacksonUtilityException
    {
        // create the firebase
        Firebase firebase = new Firebase( firebase_baseUrl );
        // "DELETE" (the fb4jDemo-root)
        FirebaseResponse response = firebase.delete();


        // "PUT" (test-map into the fb4jDemo-root)
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("Hisaab", "Group created as test");
        response = firebase.put(dataMap);


        // "PUT" (test-map into a sub-node off of the fb4jDemo-root)
        dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("Key_1", "{'anmol':['500','14/07/2019','7 pm', 'bread'}");
        dataMap.put("Key_2", "{'anadi':['200','15/07/2019','7 am', 'milk'}");
        response = firebase.put("Hisaab", dataMap);


        response = firebase.get("hisaab");


        // "POST" (test-map into a sub-node off of the fb4jDemo-root)
        response = firebase.post("hisaab", dataMap);
        System.out.println("\n\nResult of POST (for the test-POST):\n" + response);
        System.out.println("\n");

        response = firebase.delete("test-DELETE");

    }
}
