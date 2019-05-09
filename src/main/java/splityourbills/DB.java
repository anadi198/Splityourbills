package splityourbills;

import javafx.collections.ObservableList;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import net.thegreshams.firebase4j.service.Firebase;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.*;

public class DB {
    // get the base-url (ie: 'http://gamma.firebase.com/username')
    public static String firebase_baseUrl = "https://splityourbills.firebaseio.com";

    // get the api-key (ie: 'tR7u9Sqt39qQauLzXmRycXag18Z2')
    public static String firebase_apiKey = "AIzaSyAyLMUYMdIjiy5oJDcYqpoV-oeoJTtnF-8";
    public static void addUser(String time, ArrayList<String> arrStr)throws FirebaseException, IOException, JacksonUtilityException
    {
        String nick = arrStr.get(0);
        Firebase firebase = new Firebase( firebase_baseUrl+"/Groups/"+time+"/Members/" );
        FirebaseResponse response = firebase.get();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        JsonNode rootNode = objectMapper.readTree(response.getRawBody());
        if (rootNode.isArray()) {
            for (final JsonNode objNode : rootNode) {
                if(objNode.toString().replace("\"","").equals(arrStr.get(0).replace("\"","")))
                    continue;
                arrStr.add(objNode.toString().replace("\"",""));
            }
        }
        Map<String, Object> dataMap = new LinkedHashMap<>();
        Firebase firebase2 = new Firebase(firebase_baseUrl+"/Groups/"+time);
        dataMap.put("Members",arrStr);
        firebase2.patch(dataMap);
        response = firebase2.get("Group");
        String group = response.getRawBody();
        Firebase firebase3 = new Firebase(firebase_baseUrl+"/"+nick.replace("\"",""));
        Map<String, Object> dataMap1 = new LinkedHashMap<>();
        dataMap1.put("Group", group);
        dataMap1.put("Owner", "\"somebody\"");
        firebase3.patch(time, dataMap1);
    }
    public static GroupDetails[] getGroupDetails(String time) throws FirebaseException, IOException, JacksonUtilityException
    {
        Firebase firebase = new Firebase(firebase_baseUrl+"/Groups/Data/"+time);
        FirebaseResponse response = firebase.get();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        JsonNode rootNode = objectMapper.readTree(response.getRawBody());
        System.out.println(rootNode);
        List<JsonNode> listOfNodes = rootNode.findParents("Amount");
        int size = listOfNodes.size();
        GroupDetails[] gd = new GroupDetails[size];
        Iterator<String> it = rootNode.getFieldNames();
        int i = 0;
        while (it.hasNext())
        {
            String key = it.next();
            JsonNode messageNode = rootNode.path(key);
            gd[i] = new GroupDetails();
            gd[i].amount = messageNode.path("Amount").getDoubleValue();
            gd[i].description = messageNode.path("Description").toString();
            gd[i].time = key;
            gd[i].creator = messageNode.path("Creator").toString();
            JsonNode usersNode = messageNode.get("Shared with");
            TypeReference<List<String>> typeRef = new TypeReference<List<String>>(){};
            List<String> list = objectMapper.readValue(usersNode.traverse(), typeRef);
            for (String s : list) {
                gd[i].users.add(s);
            }
            i++;
        }

        return gd;
    }
    public static UserDetails[] getDetails(UserCred uc, String time) throws FirebaseException, IOException, JacksonUtilityException
    {
        String Url = "https://splityourbills.firebaseio.com/"+uc.username;
        Firebase firebase = new Firebase(Url);
        FirebaseResponse response = firebase.get();
        ArrayList<String> users = new ArrayList<>();
        users = findUsers(time);
        System.out.println("USERS ARE: "+users);
        int size = users.size();
        UserDetails[] ud = new UserDetails[size];
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getRawBody());
        System.out.println("ROOTNODE"+rootNode);
        int j = 0;
        for(int i =0; i<size; i++)
        {
            if(users.get(i).replace("\"","").equals(uc.username)==false)
            {
                JsonNode userNode = rootNode.path(users.get(i).replace("\"",""));
                System.out.println("USERNODE: "+userNode+"for user:"+users.get(i));
                if(userNode.toString().contains("Balance"))
                {
                    ud[j] = new UserDetails();
                    ud[j].nickname = users.get(i).replace("\"","");
                    JsonNode debtNode = userNode.path("Balance");
                    ud[j].balance = Double.parseDouble(debtNode.toString());
                    j++;
                }
                else
                {
                    System.out.println("NULLLOL");
                }
            }
        }
        return ud;
    }
    public static void updateGroup(String time, UserCred uc, String description, Double amount, ObservableList selectedUsers) throws FirebaseException, JsonParseException, JsonMappingException, IOException, JacksonUtilityException
    {
        Firebase firebase = new Firebase( firebase_baseUrl+"/Groups/Data/"+time );
        FirebaseResponse response;
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("Creator", uc.username);
        dataMap.put("Description", description);
        dataMap.put("Amount", amount);
        dataMap.put("Shared with", selectedUsers);
        Date date = new Date();
        long time_curr = date.getTime();
        String current_time = Long.toString(time_curr);
        response = firebase.put(current_time, dataMap);
    }
    public static void oweUser(UserCred uc, String nickname, Double amount) throws FirebaseException, JsonParseException, JsonMappingException, IOException, JacksonUtilityException
    {
        nickname.replace("\"","");
        Firebase firebase = new Firebase( firebase_baseUrl+"/"+nickname );
        FirebaseResponse response;
        response = firebase.get(uc.username.replace("\"",""));
        Double balance;
        if(response.getRawBody().contains("Balance"))
        {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getRawBody());
            JsonNode memberNode = rootNode.path("Balance");
            balance = Double.parseDouble(memberNode.toString()); //if it's negative then nickname owes uc.nickname
            balance -= amount;
        }
        else
        {
            balance = 0.0;
            balance -= amount;
        }

        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("Balance", balance);
        response = firebase.patch(uc.username.replace("\"",""), dataMap);
    }
    public static void oweThem(UserCred uc, String nickname, Double amount) throws FirebaseException, JacksonUtilityException, IOException
    {
        String ucnick = uc.username.replace("\"","");
        Firebase firebase = new Firebase( firebase_baseUrl+"/"+ucnick );
        FirebaseResponse response;
        response = firebase.get(nickname.replace("\"",""));
        Double balance;
        if(response.getRawBody().contains("Balance"))
        {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getRawBody());
            JsonNode memberNode = rootNode.path("Balance");
            balance = Double.parseDouble(memberNode.toString()); //if it's negative then nickname owes uc.nickname
            balance += amount;
        }
        else
        {
            balance = 0.0;
            balance += amount;
        }

        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("Balance", balance);
        response = firebase.patch(nickname.replace("\"",""), dataMap);
    }
    public static Group[] findGroups(String nick) throws FirebaseException, JsonParseException, JsonMappingException, IOException, JacksonUtilityException
    {
        String Url = "https://splityourbills.firebaseio.com/"+nick;
        Firebase firebase = new Firebase(Url);
        FirebaseResponse response = firebase.get();
        Map<String, Object> dataMap = new LinkedHashMap();
        System.out.println("RESPIONSE: "+response.getBody());
        dataMap.putAll(response.getBody());
        dataMap.remove("Email");
        ArrayList<String> s = new ArrayList<>();
        dataMap.forEach((k,v)->{
            if(v.toString().contains("Balance"))
            {
                s.add(k);
            }
        });
        for(int n = 0; n<s.size(); n++)
        {
            dataMap.remove(s.get(n));
        }
        System.out.println(dataMap); //map of groups with key as their timestamps
        int size = dataMap.size();//how many groups
        System.out.println("SIZE"+size);
        Group[] g = new Group[size];
        int i = 0;
        //Traversing map
        for(Map.Entry<String, Object> entry:dataMap.entrySet())
        {
            String time=entry.getKey();
            Object o = entry.getValue();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            JsonNode rootNode = objectMapper.readTree(o.toString().replace("=",":")); //convert Object to String and read it as a json
            String group, owner;
            group = rootNode.path("Group").toString().trim();
            owner = rootNode.path("Owner").toString().trim();
            g[i] = new Group(time, group, owner);
            i++;
        }
        return g;
    }
    public static ArrayList<String> findUsers(String time) throws FirebaseException, JsonParseException, JsonMappingException, IOException, JacksonUtilityException
    {
        ArrayList<String> users = new ArrayList<>();
        String Url = "https://splityourbills.firebaseio.com/Groups/"+time;
        Firebase firebase = new Firebase(Url);
        FirebaseResponse response = firebase.get();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getRawBody());
        JsonNode memberNode = rootNode.path("Members");
        Iterator<JsonNode> iterator = memberNode.getElements();
        while (iterator.hasNext()) {
            users.add(iterator.next().toString());
        }
        return users;
    }
    public static void storeGroup(String group_name, UserCred uc, long time, ArrayList<String> arrStr) throws FirebaseException, IOException, JacksonUtilityException
    {
        String Time = Long.toString(time);
        Firebase firebase = new Firebase( firebase_baseUrl );
        String localId = uc.localId.replace("\"","");
        FirebaseResponse response;
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("Group", group_name);
        dataMap.put("Created by", localId);
        dataMap.put("Members", arrStr);
        Map<String, Object> dataMap2 = new LinkedHashMap<String, Object>();
        dataMap2.put(Time, dataMap);
        response = firebase.patch("Groups", dataMap2);
        for(int i = 0; i<arrStr.size(); i++) //now just traverse through nicknames to find all groups
        {
            String t = Long.toString(time);
            String nick = arrStr.get(i);
            Map<String, Object> dataMap3 = new LinkedHashMap<>();
            dataMap3.put("Group","\""+group_name+"\"");
            dataMap3.put("Owner","\""+localId+"\"");
            Firebase firebase1 = new Firebase( firebase_baseUrl+"/"+nick );
            firebase1.patch(t, dataMap3);
        }
    }
    public static String getUsername(String localId) throws FirebaseException, JsonParseException, JsonMappingException, IOException, JacksonUtilityException
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
        response = firebase.get("/");
        Map<String, Object> dataMap1 = new LinkedHashMap<String, Object>();
        dataMap1.put("Email", email);
        response = firebase.put(nickname,dataMap1);
    }
    public static boolean checkUser(String nickname) throws FirebaseException, JsonParseException, JsonMappingException, IOException, JacksonUtilityException
    {
        Firebase firebase = new Firebase (firebase_baseUrl);
        FirebaseResponse response;
        response = firebase.get(nickname);
        if(response.getRawBody().trim().contains("Email"))
        {
            return true;
        }
        else
        {
            return false;
        }

    }
    public static void main(String[] args) throws FirebaseException, JsonParseException, JsonMappingException, IOException, JacksonUtilityException
    {
        // create the firebase
        Firebase firebase = new Firebase( firebase_baseUrl );
        // "DELETE" (the fb4jDemo-root)
        FirebaseResponse response;


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
