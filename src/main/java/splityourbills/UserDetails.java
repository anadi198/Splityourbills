package splityourbills;

import java.text.DecimalFormat;

import static java.lang.Math.abs;

public class UserDetails {
    public String nickname;
    public double balance;

    public String finalBalance()
    {
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        if(balance<0)
        {
            return "You owe "+nickname+": INR "+ numberFormat.format(abs(balance));
        }
        else if(this.balance>0)
        {
            return nickname+" owes you: INR "+ numberFormat.format(balance);
        }
        else
            return "All settled with "+nickname;
    }
}
