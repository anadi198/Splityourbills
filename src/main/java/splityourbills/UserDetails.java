package splityourbills;

import static java.lang.Math.abs;

public class UserDetails {
    public String nickname;
    public double balance;
    public String finalBalance()
    {
        if(balance<0)
        {
            return "You owe "+nickname+": INR "+ abs(balance);
        }
        else if(this.balance>0)
        {
            return nickname+" owes you: INR "+ balance;
        }
        else
            return "All settled with "+nickname;
    }
}
