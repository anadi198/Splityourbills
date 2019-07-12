package splityourbills;

import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.service.Firebase;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Signin
{
    public static String signIn(String email, String password) throws FirebaseException, IOException, JacksonUtilityException
    {
        String firebase_baseUrl = "https://splityourbills.firebaseio.com/";

        // get the api-key (ie: 'tR7u9Sqt39qQauLzXmRycXag18Z2')
        String firebase_apiKey = "AIzaSyCuxWT5UWe26fjGJ53yVwaceGhfsuK9KKE";

        // create the firebase
        Firebase firebase = new Firebase(firebase_baseUrl);
        FirebaseResponse response;
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap = new LinkedHashMap<String, Object>();
        // Sign Up user for Firebase's Auth Service demo (https://firebase.google.com/docs/reference/rest/auth/)
        if (firebase_apiKey != null) {

            firebase = new Firebase("https://www.googleapis.com/identitytoolkit/v3/relyingparty", false);
            firebase.addQuery("key", firebase_apiKey);

            dataMap.clear();
            dataMap.put("email", email);
            dataMap.put("password", password);
            dataMap.put("returnSecureToken", true);

            response = firebase.post("verifyPassword", dataMap);
            //create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getRawBody());
            if(rootNode.path("registered").toString()!="true")
            {
                //please sign up first
            }
            else
            {
                if(response.getSuccess()!=true)
                {
                    //please enter a valid password
                }
                else
                {

                    JsonNode idNode = rootNode.path("localId");
                    return idNode.toString();
                    //return "User";
                }
                return "Error";
            }
            return "Error";

        } else {
            System.out.println("\n\nResult of Signing Up:\n failed, because no API Key was provided.");
            System.out.println("\n");
            return "Error, no API key";
        }
    }
}