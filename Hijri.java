import java.time.LocalDateTime;

public class Hijri implements Calendar
{

    static final Date EPOCH = new Date(1389, 10, 3);

    @Override
    public Date countBack(Date from, int y, int m, int d)
    {

        d = (int)(d+(m*29.53059)+(y*354.5));

        int year = from.year;
        int month = from.month;
        int day = from.day;

        do
        {

            d--;
            day--;

            if (day==0)
            {
                month--;
                if (!isLeapYear(year))
                {
                    if (month%2==0)
                        day=29;
                    else
                        day=30;
                }
                else
                {
                    if (month%2!=0 || month==12)
                        day=30;
                    else
                        day=29;
                }

                if (month==0)
                {
                    month=12;
                    if (isLeapYear(year))
                        day = 30;
                    else
                        day = 29;
                    year--;
                }
            }

        } while (d>0);

        return new Date(year, month, day);

    }

    @Override
    public Date countFwd(Date from, int y, int m, int d)
    {
        d = (int)(d+(m*29.53059)+(y*354.5));

        int year = from.year;
        int month = from.month;
        int day = from.day;

        do
        {

            d--;
            day++;

            if (!isLeapYear(year))
            {
                if (month%2==0 && day==30)
                {
                    day=1;
                    month++;
                }
                else if (month%2!=0 && day==31)
                {
                    day=1;
                    month++;
                }
            }
            else
            {
                if (month%2==0 && day==30)
                {
                    day=1;
                    month++;
                }
                else if ((month%2!=0 || month==12) && day==31)
                {
                    day=1;
                    month++;
                }
            }

            if (month==13)
            {
                year++;
                month=1;
                day=1;
            }

        } while (d>0);

        return new Date(year, month, day);
    }

    @Override
    public void promptCount()
    {

        Date date;

        System.out.println("Would you like to calculate a future (f) or past (p) date?");
        String chirality = scan.next();

        System.out.println("Count from a specific date (d) or from now (n)?");
        String reference = scan.next();

        if (reference.equals("d"))
        {

            System.out.println("Enter the year:");
            int y = scan.nextInt();
            System.out.println("Enter the month (1-13):");
            int m = scan.nextInt();
            System.out.println("Enter the day (1-30):");
            int d = scan.nextInt();

            date = new Date(y,m,d);

        }
        else
            date = now();

        System.out.println("Enter years to count:");
        int y = scan.nextInt();
        System.out.println("Enter months to count:");
        int m = scan.nextInt();
        System.out.println("Enter days to count:");
        int d = scan.nextInt();

        if (chirality.equals("f"))
            date = countFwd(date,y,m,d);
        else
            date = countBack(date,y,m,d);

        Date gregEq = toGregorian(date);

        System.out.println(serializeDate(
                new Date(date.year, date.month, date.day, gregEq.toldt().getDayOfWeek().getValue()), "w-d-m-y"));

    }

    @Override
    public String serializeYear(int y)
    {
        if (y<0)
            return Integer.toString(y*-1) + " BH";
        else if (y>0 && y<=1000)
            return Integer.toString(y)+" AH";
        else
            return Integer.toString(y);
    }

    @Override
    public int daysSince(Date date)
    {
        Gregorian g = new Gregorian();
        return g.daysSince(toGregorian(date));
    }

    @Override
    public String serializeDate(Date date, String format)
    {

        Date gregEq = toGregorian(date);

        if (format.equals("w-d-m-y"))
            return gregEq.toldt().getDayOfWeek() + ", " + date.day + " " +
                    hijMonth(date.month) + " " + serializeYear(date.year);
        else if (format.equals("w-m-d-y"))
            return gregEq.toldt().getDayOfWeek() + ", " +
                    hijMonth(date.month) + " " + date.day + ", " +
                    serializeYear(date.year);
        else if (format.equals("m-d-y"))
            return hijMonth(date.month) + " " + date.day +
                    ", " + serializeYear(date.year);
        else if (format.equals("d-m-y"))
            return date.day + " " + hijMonth(date.month) + " " +
                    serializeYear(date.year);
        else if (format.equals("d-m"))
            return date.day + " " + hijMonth(date.month);
        else if (format.equals("w-d-m"))
            return gregEq.toldt().getDayOfWeek() + ", " + date.day + " " +
                    hijMonth(date.month);
        else if (format.equals("m-d"))
            return hijMonth(date.month) + " " + date.day;
        else if (format.equals("w-m-d"))
            return gregEq.toldt().getDayOfWeek() + ", " +
                    hijMonth(date.month) + " " + date.day;
        else
            return "Invalid date format specified";

    }

    @Override
    public String isHoliday(int y, int m, int d, int w)
    {
        if (m==1&&d==1)
            return "New Year (Ra's as-Sanah)";
        else if (m==1&&d==10)
            return "Ashura";
        else if (m==2&&d==20||d==21)
            return "Arba'een";
        else if (m==3&&d==9)
            return "'Eid e-Shuja'";
        else if (m==3&&d==12)
            return "Mawlid an-Nabi";
        else if (m==7&&d==13)
            return "Mawlid 'Ali";
        else if (m==7&&d==27)
            return "Laylat al-Mi'raj";
        else if (m==8&&d==15)
            return "Mid-Sha'ban (Laylat al-Bara'at)";
        else if (m==9&&d==1)
            return "Yawm Ahad Ramadan";
        else if (m==9&&d==27)
            return "Laylat al-Qadr";
        else if (m==10&&d==1)
            return "'Eid al-Fitr";
        else if (m==12&&d>7&&d<14&&d!=10)
            return "Hajj";
        else if (m==12&&d==10)
            return "'Eid al-Adha";
        else if (m==18&&d==18)
            return "'Eid al-Ghadeer";
        else
            return "Not a holiday";
    }

    @Override
    public Date now()
    {
        Gregorian g = new Gregorian();
        int daycount = g.daysSince(g.EPOCH);
        return countFwd(EPOCH, 0, 0, daycount);
    }

    private boolean isLeapYear(int y)
    {
        if (y%30==2||y%30==5||y%30==7||y%30==10||y%30==13||y%30==16||y%30==18||y%30==21||y%30==24||y%30==26||y%30==29)
            return true;
        else
            return false;
    }

    public Date toGregorian(Date date)
    {

        Gregorian g = new Gregorian();

        if (date.isBefore(EPOCH))
        {
            Date hold = EPOCH;
            int count = 0;
            do
            {
                hold = countBack(hold, 0, 0, 1);
                count++;
            } while (!hold.equals(date));
            return g.countBack(g.EPOCH, 0, 0, count);
        }
        else if (date.equals(EPOCH))
            return g.EPOCH;
        else
        {
            Date hold = EPOCH;
            int count = 0;
            do
            {
                hold = countFwd(hold, 0, 0, 1);
                count++;
            } while (!hold.equals(date));
            return g.countFwd(g.EPOCH, 0, 0, count);
        }

    }

    public Date fromGregorian(Date date)
    {

        Gregorian g = new Gregorian();

        if (date.isBefore(g.EPOCH))
        {
            Date hold = g.EPOCH;
            int count = 0;
            do
            {
                hold = g.countBack(hold, 0, 0, 1);
                count++;
            } while (!hold.equals(date));
            return countBack(EPOCH, 0, 0, count);
        }
        else if (date.equals(g.EPOCH))
            return EPOCH;
        else
        {
            Date hold = g.EPOCH;
            int count = 0;
            do
            {
                hold = g.countFwd(hold, 0, 0, 1);
                count++;
            } while (!hold.equals(date));
            return countFwd(EPOCH, 0, 0, count);
        }

    }

    private String hijMonth(int m)
    {
        switch(m)
        {
            case 1:
                return "Muharram";
            case 2:
                return "Safar";
            case 3:
                return "Rabi 1";
            case 4:
                return "Rabi 2";
            case 5:
                return "Jumada 1";
            case 6:
                return "Jumada 2";
            case 7:
                return "Rajab";
            case 8:
                return "Sha'ban";
            case 9:
                return "Ramadan";
            case 10:
                return "Shawwal";
            case 11:
                return "Dhu al-Qa'dah";
            case 12:
                return "Dhu al-Hijjah";
            default:
                return "ERROR";
        }
    }

}
