import java.time.LocalDateTime;

public class DecimalSolar implements Calendar
{

    static final DSCDate EPOCH = new DSCDate(4,1021,8,32);
    static final DSCDate CYCLE_EPOCH = new DSCDate(4, 1066, 1, 18);

    public DSCDate countBack(DSCDate from, int e, int y, int m, int d)
    {

        d = (int)(d+(m*36.5)+(y*365.242)+(e*563568.406));

        int era = from.era;
        int year = from.year;
        int deci = from.month;
        int day = from.day;

        do
        {

            d--;
            day--;

            if (day==0)
            {
                deci--;
                if (!isLeapYear(year))
                {
                    if (deci%2==0)
                        day=37;
                    else
                        day=36;
                }
                else
                {
                    if (deci%2==0 || deci==5)
                        day=37;
                    else
                        day=36;
                }

                if (deci==0)
                {
                    deci=10;
                    day=37;
                    year--;
                    if (year==0 && era>0)
                    {
                        era--;
                        year=1543;
                    }
                }
            }

        } while (d>0);

        return new DSCDate(era, year, deci, day);

    }

    @Override
    public Date countBack(Date from, int y, int m, int d)
    {
        return countBack(new DSCDate(4, from.year, from.month, from.day), 0, y, m, d);
    }

    public DSCDate countFwd(DSCDate from, int e, int y, int m, int d)
    {

        d = (int)(d+(m*36.5)+(y*365.242)+(e*563568.406));

        int era = from.era;
        int year = from.year;
        int deci = from.month;
        int day = from.day;

        do
        {

            d--;
            day++;

            if (!isLeapYear(year))
            {
                if (deci%2==0 && day==38)
                {
                    day=1;
                    deci++;
                }
                else if (deci%2!=0 && day==37)
                {
                    day=1;
                    deci++;
                }
            }
            else
            {
                if ((deci%2==0 || deci==5) && day==38)
                {
                    day=1;
                    deci++;
                }
                else if (deci%2!=0 && deci!=5 && day==37)
                {
                    day=1;
                    deci++;
                }
            }

            if (deci==11)
            {
                year++;
                deci=1;
                day=1;
            }

            if (year==1544)
            {
                era++;
                year=1;
            }

        } while (d>0);

        return new DSCDate(era, year, deci, day);

    }

    @Override
    public Date countFwd(Date from, int y, int m, int d)
    {
        return countFwd(new DSCDate(4, from.year, from.month, from.day), 0, y, m, d);
    }

    @Override
    public void promptCount()
    {
        DSCDate date;

        System.out.println("Would you like to calculate a future (f) or past (p) date?");
        String chirality = scan.next();

        System.out.println("Count from a specific date (d) or from now (n)?");
        String reference = scan.next();

        if (reference.equals("d"))
        {

            System.out.println("Enter the era:");
            int e = scan.nextInt();
            System.out.println("Enter the year:");
            int y = scan.nextInt();
            System.out.println("Enter the month (1-12):");
            int m = scan.nextInt();
            System.out.println("Enter the day (1-31):");
            int d = scan.nextInt();

            date = new DSCDate(e,y,m,d);

        }
        else
            date = now();

        System.out.println("Enter eras to count:");
        int e = scan.nextInt();
        System.out.println("Enter years to count:");
        int y = scan.nextInt();
        System.out.println("Enter months to count:");
        int m = scan.nextInt();
        System.out.println("Enter days to count:");
        int d = scan.nextInt();

        if (chirality.equals("f"))
            date = countFwd(date,e,y,m,d);
        else
            date = countBack(date,e,y,m,d);

        System.out.println(serializeDate(date, "w-d-m-y"));
    }

    public String serializeYear(int y)
    {
        return Integer.toString(y);
    }

    public int daysSince(DSCDate date)
    {
        Gregorian g = new Gregorian();
        return g.daysSince(toGregorian(date));
    }

    @Override
    public int daysSince(Date date)
    {
        Gregorian g = new Gregorian();
        return g.daysSince(toGregorian(new DSCDate(4, date.year, date.month, date.day)));
    }

    public String serializeDate(DSCDate date, String format)
    {

        if (format.equals("w-d-m-y"))
            return date.dayOfWeek(date.day) + ", " + date.day + " " + deciyear(date.month) + " " +
                    date.era + "E " + serializeYear(date.year);
        else if (format.equals("w-m-d-y"))
            return date.dayOfWeek(date.day) + ", " + deciyear(date.month) + " " + date.day + ", " + date.era + "E " +
                    serializeYear(date.year);
        else if (format.equals("m-d-y"))
            return deciyear(date.month) + " " + date.day + ", " + date.era + "E " + serializeYear(date.year);
        else if (format.equals("d-m-y"))
            return date.day + " " + deciyear(date.month) + " " + date.era + "E " + serializeYear(date.year);
        else if (format.equals("d-m"))
            return date.day + " " + deciyear(date.month);
        else if (format.equals("w-d-m"))
            return date.dayOfWeek(date.day) + ", " + date.day + " " + deciyear(date.month);
        else if (format.equals("m-d"))
            return deciyear(date.month) + " " + date.day;
        else if (format.equals("w-m-d"))
            return date.dayOfWeek(date.day) + ", " + deciyear(date.month) + " " + date.day;
        else
            return "Invalid date format specified";

    }

    @Override
    public String serializeDate(Date date, String format)
    {
        return serializeDate(new DSCDate(4, date.year, date.month, date.day), format);
    }

    @Override
    public String isHoliday(int y, int m, int d, int w)
    {
        if (m==1 && d==1)
        {
            return "New Year's Day";
        }
        else if (m==1 && d==36)
        {
            return "Earthfast";
        }
        else if (m==2 && d==36)
        {
            return "Greenfast";
        }
        else if (m==2 && d==37)
        {
            return "Greenfeast";
        }
        else if (m==3 && d==20)
        {
            return "Summer Solstice";
        }
        else if (m==3 && d==36)
        {
            return "Firefast";
        }
        else if (m==4 && d==36)
        {
            return "Fruitfast";
        }
        else if (m==4 && d==37)
        {
            return "Fruitfeast";
        }
        else if (m==5 && (d==36 || d==37))
        {
            return "Midyear's Festival";
        }
        else if (m==6 && d==3)
        {
            return "Autumn Equinox";
        }
        else if (m==6 && d==36)
        {
            return "Cornfast";
        }
        else if (m==6 && d==37)
        {
            return "Cornfeast";
        }
        else if (m==7 && d==36)
        {
            return "Windfast";
        }
        else if (m==8 && d==36)
        {
            return "Meatfast";
        }
        else if (m==8 && d==37)
        {
            return "Meatfeast";
        }
        else if (m==9 && d==36)
        {
            return "Waterfast";
        }
        else if (m==10 && (d==36 || d==37))
        {
            return "Yearsend Festival";
        }
        else if (daysSince(CYCLE_EPOCH)%1000==0)
        {
            return "Cycle Day";
        }
        else
        {
            return "Not a holiday";
        }
    }

    @Override
    public DSCDate now()
    {
        Gregorian g = new Gregorian();
        int daycount = g.daysSince(g.EPOCH);
        return countFwd(EPOCH, 0, 0, 0, daycount);
    }

    private boolean isLeapYear(int y)
    {
        return y%4==0 && (y%100!=0 || y%400==0);
    }

    public Date toGregorian(DSCDate date)
    {

        Gregorian g = new Gregorian();

        if (date.isBefore(EPOCH))
        {
            DSCDate hold = EPOCH;
            int count = 0;
            do
            {
                hold = countBack(hold, 0, 0, 0, 1);
                count++;
            } while (!hold.equals(date));
            return g.countBack(g.EPOCH, 0, 0, count);
        }
        else if (date.equals(EPOCH))
            return g.EPOCH;
        else
        {
            DSCDate hold = EPOCH;
            int count = 0;
            do
            {
                hold = countFwd(hold, 0, 0, 0, 1);
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

    private String deciyear(int m)
    {
        switch (m)
        {
            case 1:
                return "Spring Equinox";
            case 2:
                return "Verdant";
            case 3:
                return "Summer Solstice";
            case 4:
                return "Solshine";
            case 5:
                return "Midyear";
            case 6:
                return "Autumn Equinox";
            case 7:
                return "Amberleaf";
            case 8:
                return "Winter Solstice";
            case 9:
                return "Mornfrost";
            case 10:
                return "Yearsend";
            default:
                return "error";
        }
    }

}
