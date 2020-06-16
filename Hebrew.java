import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Hebrew implements Calendar
{

    static final Date EPOCH = new Date(5730, 4, 23);
    static final LocalDateTime EPOCH_MOLAD = LocalDateTime.of(1969, 9, 11, 19, 19, 53);
    static final int LUNAR_YEAR = 30622920;//30621720; +1200 to compensate for 20 min delay
    static final int LUNAR_LEAP_YEAR = 33168463;//33167563; +900 "

    @Override
    public Date countBack(Date from, int y, int m, int d)
    {

        d = (int)(d+(m*29.53059)+(y*365.242));

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
                if (isLeapYear(year))
                {
                    if (month==1 || month==5 || month==6 || month==8 || month==10 || month==12)
                        day = 30;
                    else if (month==4 || month==7 || month==9 || month==11 || month==13)
                        day = 29;
                    else
                    {
                        String yearType = getYearType(year);
                        if (yearType.equals("chaserah"))
                            day = 29;
                        else if (yearType.equals("shlemah"))
                            day = 30;
                        else
                        {
                            if (month==2)
                                day = 29;
                            else
                                day = 30;
                        }
                    }
                }
                else
                {
                    if (month==1 || month==5 || month==7 || month==9 || month==11)
                        day = 30;
                    else if (month==4 || month==6 || month==8 || month==10 || month==12)
                        day = 29;
                    else
                    {
                        String yearType = getYearType(year);
                        if (yearType.equals("chaserah"))
                            day = 29;
                        else if (yearType.equals("shlemah"))
                            day = 30;
                        else
                        {
                            if (month==2)
                                day = 29;
                            else
                                day = 30;
                        }
                    }
                }
            }

            if (month==0)
            {
                year--;
                if (isLeapYear(year-1))
                    month=13;
                else
                    month=12;
            }

        } while (d > 0);

        return new Date(year, month, day);

    }

    @Override
    public Date countFwd(Date from, int y, int m, int d)
    {

        d = (int)(d+(m*29.53059)+(y*365.242));

        int year = from.year;
        int month = from.month;
        int day = from.day;

        do
        {

            d--;
            day++;

            if (day==30)
            {
                if (isLeapYear(year))
                {
                    if (month==4 || month==7 || month==9 || month==11)
                    {
                        month++;
                        day = 1;
                    }
                    else if (month==2 || month==3)
                    {
                        String yearType = getYearType(year);
                        if (yearType.equals("chaserah"))
                        {
                            month++;
                            day = 1;
                        }
                        else if (yearType.equals("kesidran") && month==2)
                        {
                            month++;
                            day = 1;
                        }
                    }
                    else if (month==13)
                    {
                        day=1;
                        month=1;
                        year++;
                    }
                }
                else
                {
                    if (month==4 || month==6 || month==8 || month==10)
                    {
                        month++;
                        day = 1;
                    }
                    else if (month==2 || month==3)
                    {
                        String yearType = getYearType(year);
                        if (yearType.equals("chaserah"))
                        {
                            month++;
                            day = 1;
                        }
                        else if (yearType.equals("kesidran") && month==2)
                        {
                            month++;
                            day = 1;
                        }
                    }
                    else if (month==12)
                    {
                        day=1;
                        month=1;
                        year++;
                    }
                }
            }
            else if (day==31)
            {
                if (isLeapYear(year))
                {
                    if (month==1 || month==5 || month==6 || month==8 || month==10 || month==12)
                    {
                        month++;
                        day = 1;
                    }
                    else if (month==2 || month==3)
                    {
                        String yearType = getYearType(year);
                        if (yearType.equals("shlemah"))
                        {
                            month++;
                            day = 1;
                        }
                        else if (yearType.equals("kesidran") && month==3)
                        {
                            month++;
                            day = 1;
                        }
                    }
                }
                else
                {
                    if (month==1 || month==5 || month==7 || month==9 || month==11)
                    {
                        month++;
                        day = 1;
                    }
                    else if (month==2 || month==3)
                    {
                        String yearType = getYearType(year);
                        if (yearType.equals("shlemah"))
                        {
                            month++;
                            day = 1;
                        }
                        else if (yearType.equals("kesidran") && month==3)
                        {
                            month++;
                            day = 1;
                        }
                    }
                }
            }

        } while (d > 0);

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
        if (y<=1000)
            return Integer.toString(y)+" AM";
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
                    hebMonth(date.month, date.year) + " " + serializeYear(date.year);
        else if (format.equals("w-m-d-y"))
            return gregEq.toldt().getDayOfWeek() + ", " +
                    hebMonth(date.month, date.year) + " " + date.day + ", " +
                    serializeYear(date.year);
        else if (format.equals("m-d-y"))
            return hebMonth(date.month, date.year) + " " + date.day +
                    ", " + serializeYear(date.year);
        else if (format.equals("d-m-y"))
            return date.day + " " + hebMonth(date.month, date.year) + " " +
                    serializeYear(date.year);
        else if (format.equals("d-m"))
            return date.day + " " + hebMonth(date.month, date.year);
        else if (format.equals("w-d-m"))
            return gregEq.toldt().getDayOfWeek() + ", " + date.day + " " +
                    hebMonth(date.month, date.year);
        else if (format.equals("m-d"))
            return hebMonth(date.month, date.year) + " " + date.day;
        else if (format.equals("w-m-d"))
            return gregEq.toldt().getDayOfWeek() + ", " +
                    hebMonth(date.month, date.year) + " " + date.day;
        else
            return "Invalid date format specified";

    }

    private String hebMonth(int m, int y)
    {

        if (!isLeapYear(y))
        {
            switch(m)
            {
                case 1:
                    return "Tishrei";
                case 2:
                    return "Cheshvan";
                case 3:
                    return "Kislev";
                case 4:
                    return "Tevet";
                case 5:
                    return "Shevat";
                case 6:
                    return "Adar";
                case 7:
                    return "Nisan";
                case 8:
                    return "Iyar";
                case 9:
                    return "Sivan";
                case 10:
                    return "Tammuz";
                case 11:
                    return "Av";
                case 12:
                    return "Elul";
                default:
                    return "ERROR";
            }
        }
        else
        {
            switch(m)
            {
                case 1:
                    return "Tishrei";
                case 2:
                    return "Cheshvan";
                case 3:
                    return "Kislev";
                case 4:
                    return "Tevet";
                case 5:
                    return "Shevat";
                case 6:
                    return "Adar I";
                case 7:
                    return "Adar II";
                case 8:
                    return "Nisan";
                case 9:
                    return "Iyar";
                case 10:
                    return "Sivan";
                case 11:
                    return "Tammuz";
                case 12:
                    return "Av";
                case 13:
                    return "Elul";
                default:
                    return "ERROR";
            }
        }

    }

    @Override
    public String isHoliday(int y, int m, int d, int w)
    {
        if (m==1 && (d==1 || d==2))
            return "Rosh Hashanah";
        else if (((isLeapYear(y) && m==13) || (!isLeapYear(y) && m==12)) && d==29)
            return "Erev Rosh Hashanah";
        else if (m==1 && d==10)
            return "Yom Kippur";
        else if (m==1 && (d>14 && d<23))
            return "Sukkot";
        else if ((m==3 && d>24) || (m==4 && d<4))
            return "Hanukkah";
        else if ((isLeapYear(y) && m==7 && d==14) || (!isLeapYear(y) && m==6 && d==14))
            return "Purim";
        else if ((isLeapYear(y) && m==8 && d>14 && d<23) || (!isLeapYear(y) && m==7 && d>14 && d<23))
            return "Passover";
        else if ((isLeapYear(y) && m==10 && d>5 && d<8) || (!isLeapYear(y) && m==9 && d>5 && d<8))
            return "Shavuot";
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

        int m = y % 19;

        switch (m)
        {
            case 3:
            case 6:
            case 8:
            case 11:
            case 14:
            case 17:
            case 0:
                return true;
            default:
                return false;
        }

    }

    private LocalDateTime molad(int y)
    {

        LocalDateTime hold = EPOCH_MOLAD;
        if (y < EPOCH.year)
        {
            for (int i=EPOCH.year;i>y;i--)
            {
                if (isLeapYear(i))
                    hold = hold.minusSeconds(LUNAR_LEAP_YEAR);
                else
                    hold = hold.minusSeconds(LUNAR_YEAR);
            }
            return hold;
            //return EPOCH_MOLAD.minusSeconds((EPOCH.year-y)*LUNAR_YEAR);
        }
        else if (y==EPOCH.year)
            return EPOCH_MOLAD;
        else
        {
            for (int i=EPOCH.year;i<y;i++)
            {
                if (isLeapYear(i))
                    hold = hold.plusSeconds(LUNAR_LEAP_YEAR);
                else
                    hold = hold.plusSeconds(LUNAR_YEAR);
            }
            return hold;
            //return EPOCH_MOLAD.plusSeconds((y-EPOCH.year)*LUNAR_YEAR);
        }

    }

    private int getMoladRow(int y)
    {

        LocalDateTime molad = molad(y);
        LocalTime moladtime = molad.toLocalTime();

        if (molad.getDayOfWeek().getValue()==6)
        {
            if (molad.getHour()>=12)
                return 1;
            else
                return 13;
        }
        else if (molad.getDayOfWeek().getValue()==7)
        {
            if (moladtime.isBefore(LocalTime.of(3,11,19)))
                return 1;
            else if (moladtime.isAfter(LocalTime.of(3,11,18)) &&
                    moladtime.isBefore(LocalTime.of(14,27,25)))
                return 2;
            else
                return 3;
        }
        else if (molad.getDayOfWeek().getValue()==1)
        {
            if (moladtime.isBefore(LocalTime.of(9,32,41)))
                return 3;
            else if (moladtime.isAfter(LocalTime.of(9,32,40)) &&
                    moladtime.isBefore(LocalTime.of(12, 0, 0)))
                return 4;
            else
                return 5;
        }
        else if (molad.getDayOfWeek().getValue()==2)
        {
            if (moladtime.isBefore(LocalTime.of(3, 11, 19)))
                return 5;
            else if (moladtime.isAfter(LocalTime.of(3, 11, 18)) &&
                    moladtime.isBefore(LocalTime.of(12,0,0)))
                return 6;
            else
                return 7;
        }
        else if (molad.getDayOfWeek().getValue()==3)
        {
            if (moladtime.isBefore(LocalTime.of(5,38,34)))
                return 7;
            else
                return 8;
        }
        else if (molad.getDayOfWeek().getValue()==5)
        {
            if (moladtime.isBefore(LocalTime.of(3,11,19)))
                return 11;
            else if (moladtime.isAfter(LocalTime.of(3,11,18)) &&
                    moladtime.isBefore(LocalTime.of(14,27,25)))
                return 12;
            else
                return 13;
        }
        else
        {
            if (moladtime.isBefore(LocalTime.of(3,11,19)))
                return 8;
            else if (moladtime.isAfter(LocalTime.of(3,11,18)) &&
                    moladtime.isBefore(LocalTime.of(12,0,0)))
                return 9;
            else if (moladtime.isAfter(LocalTime.of(11,59,59)) &&
                    moladtime.isBefore(LocalTime.of(18,22,38)))
                return 10;
            else
                return 11;
        }

    }

    private String getYearType(int y)
    {

        int row = getMoladRow(y);
        int m = y%19;

        if (m==1 || m==4 || m==9 || m==12 || m==15)
        {
            if (row==1 || row==10)
                return "chaserah";
            else if (row==2 || row==3 || row==9 || row==11 || row==12 || row==13)
                return "shlemah";
            else
                return "kesidran";
        }
        else if (m==7 || m==18)
        {
            if (row==1 || row==10 || row==11)
                return "chaserah";
            else if (row==2 || row==3 || row==9 || row==12 || row==13)
                return "shlemah";
            else
                return "kesidran";
        }
        else if (m==2 || m==5 || m==10 || m==13 || m==16)
        {
            if (row==1 || row==10 || row==11)
                return "chaserah";
            else if (row==2 || row==3 || row==4 || row==9 || row==12 || row==13)
                return "shlemah";
            else
                return "kesidran";
        }
        else
        {
            if (row==1 || row==2 || row==7 || row==10 || row==11 || row==12)
                return "chaserah";
            else if (row==3 || row==4 || row==8 || row==9 || row==13)
                return "shlemah";
            else
                return "kesidran";
        }

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

}