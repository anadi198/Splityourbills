package splityourbills;

import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.service.Firebase;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Signup {
    public static FirebaseResponse register(String email, String password ) throws FirebaseException, IOException, JacksonUtilityException
    {
        String firebase_baseUrl = "https://splityourbills.firebaseio.com/";

        // get the api-key (ie: 'tR7u9Sqt39qQauLzXmRycXag18Z2')
        String firebase_apiKey = "AIzaSyCCVHEvBSNmUF86BKBXxD3cteuXJbzuSc8";

        // create the firebase
        Firebase firebase = new Firebase( firebase_baseUrl );
        FirebaseResponse response;
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap = new LinkedHashMap<String, Object>();
        // Sign Up user for Firebase's Auth Service demo (https://firebase.google.com/docs/reference/rest/auth/)

        firebase = new Firebase("https://www.googleapis.com/identitytoolkit/v3/relyingparty", false);
        firebase.addQuery("key", firebase_apiKey);

        dataMap.clear();
        dataMap.put("email", email);
        dataMap.put("password", password);
        dataMap.put("returnSecureToken", true);

        response = firebase.post("signupNewUser", dataMap);

        return response;
    }
}
