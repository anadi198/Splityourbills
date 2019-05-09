package splityourbills;

import java.util.ArrayList;

public class GroupDetails {
    public String time, description, creator;
    public Double amount;
    public ArrayList<String> users = new ArrayList<>();
    public String Display()
    {
        String s = "New expense added by - "+creator+"\nDescription - "+description+"\nFor a total of INR - "+amount+"\nExpense shared with: "+users;
        return s;
    }
}
