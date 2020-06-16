import java.time.LocalDateTime;

public class DSCTime
{

    int deci;
    int centi;
    int milli;
    int dimi;
    int legi;

    public DSCTime()
    {
        this.deci = 0;
        this.centi = 0;
        this.milli = 0;
        this.dimi = 0;
        this.legi = 0;
    }

    public DSCTime(int deci, int centi, int milli, int dimi, int legi)
    {
        this.deci = deci;
        this.centi = centi;
        this.milli = milli;
        this.dimi = dimi;
        this.legi = legi;
    }

    public DSCTime(int hour, int minute, int second)
    {
        this.deci = hour;
        this.centi = minute/10;
        this.milli = minute%10;
        this.dimi = second/10;
        this.legi = second%10;
    }

    public int secondsSinceDawn(LocalDateTime ldt)
    {

        int count = 0;
        do
        {
            ldt = ldt.minusSeconds(1);
            count++;
        } while (!(ldt.getHour()==6 && ldt.getMinute()==0 && ldt.getSecond()==0));

        return count;

    }

    public DSCTime fromStd(LocalDateTime ldt)
    {
        int seconds = secondsSinceDawn(ldt);

        int deci = (seconds/8640)+1;
        seconds-=8640*(deci-1);
        int centi = seconds/864;
        seconds-=864*centi;
        int milli = (int)(seconds/86.4);
        seconds-=86.4*milli;
        int dimi = (int)(seconds/8.64);
        seconds-=8.64*dimi;
        int legi = (int)(seconds/0.864);

        return new DSCTime(deci, centi, milli, dimi, legi);
    }

    public String serialize(String format)
    {

        if (format.equals("std"))
        {
            return deci + ":" + (centi*10+milli) + ":" + (dimi*10+legi);
        }
        else if (format.equals("precise"))
        {
            return deci + ":" + centi + ":" + milli + ":" + dimi + ":" + legi;
        }
        else
        {
            return "unrecognized format";
        }

    }

    public DSCTime now()
    {
        return fromStd(LocalDateTime.now());
    }

}
