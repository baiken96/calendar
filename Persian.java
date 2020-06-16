public class Persian implements Calendar
{

    static final Date EPOCH = new Date(1348, 10, 11);

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
                if (!isLeapYear(year))
                {
                    if (month<7)
                        day=31;
                    else if (month>=7 && month<12)
                        day=30;
                    else
                        day=29;
                }
                else
                {
                    if (month<7)
                        day=31;
                    else
                        day=30;
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
        d = (int)(d+(m*29.53059)+(y*365.242));

        int year = from.year;
        int month = from.month;
        int day = from.day;

        do
        {

            d--;
            day++;

            if (!isLeapYear(year))
            {
                if (month<7 && day==32)
                {
                    day=1;
                    month++;
                }
                else if (month>=7 && month<12 && day==31)
                {
                    day=1;
                    month++;
                }
                else if (month==12 && day==30)
                {
                    day=1;
                    month++;
                }
            }
            else
            {
                if (month<7 && day==32)
                {
                    day=1;
                    month++;
                }
                else if (month>=7 && month<=12 && day==31)
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
                    prsMonth(date.month) + " " + serializeYear(date.year);
        else if (format.equals("w-m-d-y"))
            return gregEq.toldt().getDayOfWeek() + ", " +
                    prsMonth(date.month) + " " + date.day + ", " +
                    serializeYear(date.year);
        else if (format.equals("m-d-y"))
            return prsMonth(date.month) + " " + date.day +
                    ", " + serializeYear(date.year);
        else if (format.equals("d-m-y"))
            return date.day + " " + prsMonth(date.month) + " " +
                    serializeYear(date.year);
        else if (format.equals("d-m"))
            return date.day + " " + prsMonth(date.month);
        else if (format.equals("w-d-m"))
            return gregEq.toldt().getDayOfWeek() + ", " + date.day + " " +
                    prsMonth(date.month);
        else if (format.equals("m-d"))
            return prsMonth(date.month) + " " + date.day;
        else if (format.equals("w-m-d"))
            return gregEq.toldt().getDayOfWeek() + ", " +
                    prsMonth(date.month) + " " + date.day;
        else
            return "Invalid date format specified";

    }

    @Override
    public String isHoliday(int y, int m, int d, int w)
    {
        if (m==1&&d<5)
            return "Nowruz";
        else if (m==1&&d==13)
            return "Sizdah Bedar";
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

    // 5 | 9 13 17 21 25 29 | 34 | 38 42 46 50 54 58 62 | 67 | 71 75 79 83 87 91 95 | 100 | 104 108 112 116 120 124 128
    private boolean isLeapYear(int y)
    {
        int c = y % 128;

        switch(c)
        {
            case 5:
            case 9:
            case 13:
            case 17:
            case 21:
            case 25:
            case 29:
            case 34:
            case 38:
            case 42:
            case 46:
            case 50:
            case 54:
            case 58:
            case 62:
            case 67:
            case 71:
            case 75:
            case 79:
            case 83:
            case 87:
            case 91:
            case 95:
            case 100:
            case 104:
            case 108:
            case 112:
            case 116:
            case 120:
            case 124:
            case 0:
                return true;
            default:
                return false;
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

    private String prsMonth(int m)
    {
        switch(m)
        {
            case 1:
                return "Favardin";
            case 2:
                return "Ordibehesht";
            case 3:
                return "Khordad";
            case 4:
                return "Tir";
            case 5:
                return "Mordad";
            case 6:
                return "Shahrivar";
            case 7:
                return "Mehr";
            case 8:
                return "Aban";
            case 9:
                return "Azar";
            case 10:
                return "Dey";
            case 11:
                return "Bahman";
            case 12:
                return "Esfand";
            default:
                return "ERROR";
        }
    }

}
