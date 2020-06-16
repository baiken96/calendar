import java.time.LocalDateTime;

public class Roman implements Calendar
{

    static final Date EPOCH = new Date(2786, 7, 15);

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
                if (getYearType(y).equals("leap 2"))
                {
                    if (month==4 || month==6 || month==8 || month==11)
                        day = 31;
                    else if (month==2)
                        day = 28;
                    else if (month==3)
                        day = 27;
                    else
                        day = 29;
                }
                else if (getYearType(y).equals("leap 4"))
                {
                    if (month==4 || month==6 || month==8 || month==11)
                        day = 31;
                    else if (month==2)
                        day = 29;
                    else if (month==3)
                        day = 27;
                    else
                        day = 29;
                }
                else
                {
                    if (month==3 || month==5 || month==7 || month==10)
                        day = 31;
                    else if (month==2)
                        day = 28;
                    else
                        day = 29;
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

            if (day==28 && isLeapYear(year) && month==3)
            {
                month++;
                day=1;
            }
            else if (day==29 && !getYearType(year).equals("leap 4") && month==2)
            {
                month++;
                day=1;
            }
            else if (day==30)
            {
                if (getYearType(year).equals("leap 2"))
                {
                    if (month!=2 && month!=3 && month!=4 && month!=6 && month!=8 && month!=11)
                    {
                        month++;
                        day=1;
                    }
                }
                else if (getYearType(year).equals("leap 4"))
                {
                    if (month!=3 && month!=4 && month!=6 && month!=8 && month!=11)
                    {
                        month++;
                        day=1;
                    }
                }
                else
                {
                    if (month!=2 && month!=3 && month!=5 && month!=7 && month!=10)
                    {
                        month++;
                        day=1;
                    }
                }
            }
            else if (day==32)
            {
                if (isLeapYear(year))
                {
                    if (month==4 || month==6 || month==8 || month==11)
                    {
                        month++;
                        day=1;
                    }
                }
                else
                {
                    if (month==3 || month==5 || month==7 || month==10)
                    {
                        month++;
                        day=1;
                    }
                }
            }

            if (isLeapYear(year) && month==14)
            {
                year++;
                month=1;
                day=1;
            }
            else if (month==13)
            {
                year++;
                month=1;
                day=1;
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
        return y+" AVC";
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
            return gregEq.toldt().getDayOfWeek() + ", " + serializeDay(date.year, date.month, date.day) + " " +
                    romMonth(date.month, date.year) + " " + serializeYear(date.year);
        else if (format.equals("w-m-d-y"))
            return gregEq.toldt().getDayOfWeek() + ", " + romMonth(date.month, date.year) + " " +
                    serializeDay(date.year, date.month, date.day) + ", " + serializeYear(date.year);
        else if (format.equals("m-d-y"))
            return romMonth(date.month, date.year) + " " + serializeDay(date.year, date.month, date.day) + ", " +
                    serializeYear(date.year);
        else if (format.equals("d-m-y"))
            return serializeDay(date.year, date.month, date.day) + " " + romMonth(date.month, date.year) + " " +
                    serializeYear(date.year);
        else if (format.equals("d-m"))
            return serializeDay(date.year, date.month, date.day) + " " + romMonth(date.month, date.year);
        else if (format.equals("w-d-m"))
            return gregEq.toldt().getDayOfWeek() + ", " + serializeDay(date.year, date.month, date.day) + " " +
                    romMonth(date.month, date.year);
        else if (format.equals("m-d"))
            return romMonth(date.month, date.year) + " " + serializeDay(date.year, date.month, date.day);
        else if (format.equals("w-m-d"))
            return gregEq.toldt().getDayOfWeek() + ", " + romMonth(date.month, date.year) + " " +
                    serializeDay(date.year, date.month, date.day);
        else
            return "Invalid date format specified";
    }

    @Override
    public String isHoliday(int y, int m, int d, int w)
    {
        if (m==1 && d==1)
            return "Calendar New Year";
        else if (m==1 && d==9)
            return "Agonalia of Janus";
        else if (m==2 && d>12 && d<22)
            return "Parentalia";
        else if (m==2 && d==22)
            return "Parentalia Caristia";
        else if (m==2 && d==23)
            return "Terminalia";

        if (!isLeapYear(y))
        {
            if (m==3 && d==1)
                return "Traditional New Year";
            else if (m==3 && d==17)
                return "Agonalia of Mars";
            else if (m==3 && d==15)
                return "Canna Intrat";
            else if (m==3 && d==19)
                return "Incipit Quinquatria";
            else if (m==3 && d==22)
                return "Arbor Intrat";
            else if (m==3 && d==24)
                return "Sanguis";
            else if (m==3 && d==27)
                return "Lavatio";
            else if (m==4 && d>3 && d<11)
                return "Ludi Megalenses";
            else if (m==4 && d==15)
                return "Fordicidia";
            else if (m==4 && d==21)
                return "Parilia";
            else if (m==4 && d==23)
                return "Vinalia";
            else if (m==4 && d>27)
                return "Floralia";
            else if (m==5 && d<4)
                return "Floralia";
            else if (m==5 && d==9 || d==11 || d==13)
                return "Lemuria";
            else if (m==5 && d==21)
                return "Agonalia of Vediovis";
            else if (m==6 && d>6 && d<16)
                return "Vestalia";
            else if (m==6 && d==24)
                return "Fors Fortuna";
            else if (m==7 && d>5 && d<14)
                return "Ludi Apollonares";
            else if (m==7 && d==23)
                return "Neptunalia";
            else if (m==9 && d>4 && d<20)
                return "Ludi Magna";
            else if (m==10 && d==19)
                return "Armulistrium";
            else if (m==12 && d==11)
                return "Agonalia Indiges";
            else if (m==12 && d>16 && d<24)
                return "Saturnalia";
        }
        else
        {
            if (m==4 && d==1)
                return "Traditional New Year";
            else if (m==4 && d==17)
                return "Agonalia of Mars";
            else if (m==4 && d==15)
                return "Canna Intrat";
            else if (m==4 && d==19)
                return "Incipit Quinquatria";
            else if (m==4 && d==22)
                return "Arbor Intrat";
            else if (m==4 && d==24)
                return "Sanguis";
            else if (m==4 && d==27)
                return "Lavatio";
            else if (m==5 && d>3 && d<11)
                return "Ludi Megalenses";
            else if (m==5 && d==15)
                return "Fordicidia";
            else if (m==5 && d==21)
                return "Parilia";
            else if (m==5 && d==23)
                return "Vinalia";
            else if (m==5 && d>27)
                return "Floralia";
            else if (m==6 && d<4)
                return "Floralia";
            else if (m==6 && d==9 || d==11 || d==13)
                return "Lemuria";
            else if (m==6 && d==21)
                return "Agonalia of Vediovis";
            else if (m==7 && d>6 && d<16)
                return "Vestalia";
            else if (m==7 && d==24)
                return "Fors Fortuna";
            else if (m==8 && d>5 && d<14)
                return "Ludi Apollonares";
            else if (m==8 && d==23)
                return "Neptunalia";
            else if (m==10 && d>4 && d<20)
                return "Ludi Magna";
            else if (m==11 && d==19)
                return "Armulistrium";
            else if (m==13 && d==11)
                return "Agonalia Indiges";
            else if (m==13 && d>16 && d<24)
                return "Saturnalia";
        }

        return "Not a holiday";

    }

    @Override
    public Date now()
    {
        Gregorian g = new Gregorian();
        int daycount = g.daysSince(g.EPOCH);
        return countFwd(EPOCH, 0, 0, daycount);
    }

    public String getYearType(int y)
    {
        if (y%2==0 && y%4!=0)
            return "leap 2";
        else if (y%4==0)
            return "leap 4";
        else
            return "common";
    }

    private boolean isLeapYear(int y)
    {
        return !getYearType(y).equals("common");
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

    private String romMonth(int m, int y)
    {

        if (!isLeapYear(y))
        {
            switch(m)
            {
                case 1:
                    return "IANVARII";
                case 2:
                    return "FEBRVARII";
                case 3:
                    return "MARTII";
                case 4:
                    return "APRILIS";
                case 5:
                    return "MAII";
                case 6:
                    return "IVNII";
                case 7:
                    return "QVINTILIS";
                case 8:
                    return "SEXTILIS";
                case 9:
                    return "SEPTEMBRIS";
                case 10:
                    return "OCTOBRIS";
                case 11:
                    return "NOVEMBRIS";
                case 12:
                    return "DECEMBRIS";
                default:
                    return "ERROR";
            }
        }
        else
        {
            switch(m)
            {
                case 1:
                    return "IANVARII";
                case 2:
                    return "FEBRVARII";
                case 3:
                    return "MERCEDONII";
                case 4:
                    return "MARTII";
                case 5:
                    return "APRILIS";
                case 6:
                    return "MAII";
                case 7:
                    return "IVNII";
                case 8:
                    return "QVINTILIS";
                case 9:
                    return "SEXTILIS";
                case 10:
                    return "SEPTEMBRIS";
                case 11:
                    return "OCTOBRIS";
                case 12:
                    return "NOVEMBRIS";
                case 13:
                    return "DECEMBRIS";
                default:
                    return "ERROR";
            }
        }

    }

    private String serializeDay(int y, int m, int d) {

        if (getYearType(y).equals("leap 2")) {
            if (m == 2) {
                switch (d) {
                    case 1:
                        return "K";
                    case 2:
                        return "III N";
                    case 3:
                        return "II N";
                    case 4:
                        return "I N";
                    case 5:
                        return "N";
                    case 6:
                        return "VII I";
                    case 7:
                        return "VI I";
                    case 8:
                        return "V I";
                    case 9:
                        return "IV I";
                    case 10:
                        return "III I";
                    case 11:
                        return "II I";
                    case 12:
                        return "I I";
                    case 13:
                        return "I";
                    case 14:
                        return "XV K";
                    case 15:
                        return "XIV K";
                    case 16:
                        return "XIII K";
                    case 17:
                        return "XII K";
                    case 18:
                        return "XI K";
                    case 19:
                        return "X K";
                    case 20:
                        return "IX K";
                    case 21:
                        return "VIII K";
                    case 22:
                        return "VII K";
                    case 23:
                        return "VI K";
                    case 24:
                        return "V K";
                    case 25:
                        return "IV K";
                    case 26:
                        return "III K";
                    case 27:
                        return "II K";
                    case 28:
                        return "I K";
                    default:
                        return "ERROR";
                }
            } else if (m == 3) {
                switch (d) {
                    case 1:
                        return "K";
                    case 2:
                        return "III N";
                    case 3:
                        return "II N";
                    case 4:
                        return "I N";
                    case 5:
                        return "N";
                    case 6:
                        return "VII I";
                    case 7:
                        return "VI I";
                    case 8:
                        return "V I";
                    case 9:
                        return "IV I";
                    case 10:
                        return "III I";
                    case 11:
                        return "II I";
                    case 12:
                        return "I I";
                    case 13:
                        return "I";
                    case 14:
                        return "XIV K";
                    case 15:
                        return "XIII K";
                    case 16:
                        return "XII K";
                    case 17:
                        return "XI K";
                    case 18:
                        return "X K";
                    case 19:
                        return "IX K";
                    case 20:
                        return "VIII K";
                    case 21:
                        return "VII K";
                    case 22:
                        return "VI K";
                    case 23:
                        return "V K";
                    case 24:
                        return "IV K";
                    case 25:
                        return "III K";
                    case 26:
                        return "II K";
                    case 27:
                        return "I K";
                    default:
                        return "ERROR";
                }
            } else if (m == 1 || m == 5 || m == 7 || m == 9 || m == 10 || m == 12 || m == 13) {
                switch (d) {
                    case 1:
                        return "K";
                    case 2:
                        return "III N";
                    case 3:
                        return "II N";
                    case 4:
                        return "I N";
                    case 5:
                        return "N";
                    case 6:
                        return "VII I";
                    case 7:
                        return "VI I";
                    case 8:
                        return "V I";
                    case 9:
                        return "IV I";
                    case 10:
                        return "III I";
                    case 11:
                        return "II I";
                    case 12:
                        return "I I";
                    case 13:
                        return "I";
                    case 14:
                        return "XVI K";
                    case 15:
                        return "XV K";
                    case 16:
                        return "XIV K";
                    case 17:
                        return "XIII K";
                    case 18:
                        return "XII K";
                    case 19:
                        return "XI K";
                    case 20:
                        return "X K";
                    case 21:
                        return "IX K";
                    case 22:
                        return "VIII K";
                    case 23:
                        return "VII K";
                    case 24:
                        return "VI K";
                    case 25:
                        return "V K";
                    case 26:
                        return "IV K";
                    case 27:
                        return "III K";
                    case 28:
                        return "II K";
                    case 29:
                        return "I K";
                    default:
                        return "ERROR";
                }
            } else {
                switch (d) {
                    case 1:
                        return "K";
                    case 2:
                        return "V N";
                    case 3:
                        return "IV N";
                    case 4:
                        return "III N";
                    case 5:
                        return "II N";
                    case 6:
                        return "I N";
                    case 7:
                        return "N";
                    case 8:
                        return "VII I";
                    case 9:
                        return "VI I";
                    case 10:
                        return "V I";
                    case 11:
                        return "IV I";
                    case 12:
                        return "III I";
                    case 13:
                        return "II I";
                    case 14:
                        return "I I";
                    case 15:
                        return "I";
                    case 16:
                        return "XVI K";
                    case 17:
                        return "XV K";
                    case 18:
                        return "XIV K";
                    case 19:
                        return "XIII K";
                    case 20:
                        return "XII K";
                    case 21:
                        return "XI K";
                    case 22:
                        return "X K";
                    case 23:
                        return "IX K";
                    case 24:
                        return "VIII K";
                    case 25:
                        return "VII K";
                    case 26:
                        return "VI K";
                    case 27:
                        return "V K";
                    case 28:
                        return "IV K";
                    case 29:
                        return "III K";
                    case 30:
                        return "II K";
                    case 31:
                        return "I K";
                    default:
                        return "ERROR";
                }
            }
        }

        if (getYearType(y).equals("leap 2")) {
            if (m == 2) {
                switch (d) {
                    case 1:
                        return "K";
                    case 2:
                        return "III N";
                    case 3:
                        return "II N";
                    case 4:
                        return "I N";
                    case 5:
                        return "N";
                    case 6:
                        return "VII I";
                    case 7:
                        return "VI I";
                    case 8:
                        return "V I";
                    case 9:
                        return "IV I";
                    case 10:
                        return "III I";
                    case 11:
                        return "II I";
                    case 12:
                        return "I I";
                    case 13:
                        return "I";
                    case 14:
                        return "XV K";
                    case 15:
                        return "XIV K";
                    case 16:
                        return "XIII K";
                    case 17:
                        return "XII K";
                    case 18:
                        return "XI K";
                    case 19:
                        return "X K";
                    case 20:
                        return "IX K";
                    case 21:
                        return "VIII K";
                    case 22:
                        return "VII K";
                    case 23:
                        return "VI K";
                    case 24:
                        return "V K";
                    case 25:
                        return "IV K";
                    case 26:
                        return "III K";
                    case 27:
                        return "II K";
                    case 28:
                        return "I K";
                    default:
                        return "ERROR";
                }
            } else if (m == 3) {
                switch (d) {
                    case 1:
                        return "K";
                    case 2:
                        return "III N";
                    case 3:
                        return "II N";
                    case 4:
                        return "I N";
                    case 5:
                        return "N";
                    case 6:
                        return "VII I";
                    case 7:
                        return "VI I";
                    case 8:
                        return "V I";
                    case 9:
                        return "IV I";
                    case 10:
                        return "III I";
                    case 11:
                        return "II I";
                    case 12:
                        return "I I";
                    case 13:
                        return "I";
                    case 14:
                        return "XIV K";
                    case 15:
                        return "XIII K";
                    case 16:
                        return "XII K";
                    case 17:
                        return "XI K";
                    case 18:
                        return "X K";
                    case 19:
                        return "IX K";
                    case 20:
                        return "VIII K";
                    case 21:
                        return "VII K";
                    case 22:
                        return "VI K";
                    case 23:
                        return "V K";
                    case 24:
                        return "IV K";
                    case 25:
                        return "III K";
                    case 26:
                        return "II K";
                    case 27:
                        return "I K";
                    default:
                        return "ERROR";
                }
            } else if (m == 1 || m == 5 || m == 7 || m == 9 || m == 10 || m == 12 || m == 13) {
                switch (d) {
                    case 1:
                        return "K";
                    case 2:
                        return "III N";
                    case 3:
                        return "II N";
                    case 4:
                        return "I N";
                    case 5:
                        return "N";
                    case 6:
                        return "VII I";
                    case 7:
                        return "VI I";
                    case 8:
                        return "V I";
                    case 9:
                        return "IV I";
                    case 10:
                        return "III I";
                    case 11:
                        return "II I";
                    case 12:
                        return "I I";
                    case 13:
                        return "I";
                    case 14:
                        return "XVI K";
                    case 15:
                        return "XV K";
                    case 16:
                        return "XIV K";
                    case 17:
                        return "XIII K";
                    case 18:
                        return "XII K";
                    case 19:
                        return "XI K";
                    case 20:
                        return "X K";
                    case 21:
                        return "IX K";
                    case 22:
                        return "VIII K";
                    case 23:
                        return "VII K";
                    case 24:
                        return "VI K";
                    case 25:
                        return "V K";
                    case 26:
                        return "IV K";
                    case 27:
                        return "III K";
                    case 28:
                        return "II K";
                    case 29:
                        return "I K";
                    default:
                        return "ERROR";
                }
            } else {
                switch (d) {
                    case 1:
                        return "K";
                    case 2:
                        return "V N";
                    case 3:
                        return "IV N";
                    case 4:
                        return "III N";
                    case 5:
                        return "II N";
                    case 6:
                        return "I N";
                    case 7:
                        return "N";
                    case 8:
                        return "VII I";
                    case 9:
                        return "VI I";
                    case 10:
                        return "V I";
                    case 11:
                        return "IV I";
                    case 12:
                        return "III I";
                    case 13:
                        return "II I";
                    case 14:
                        return "I I";
                    case 15:
                        return "I";
                    case 16:
                        return "XVI K";
                    case 17:
                        return "XV K";
                    case 18:
                        return "XIV K";
                    case 19:
                        return "XIII K";
                    case 20:
                        return "XII K";
                    case 21:
                        return "XI K";
                    case 22:
                        return "X K";
                    case 23:
                        return "IX K";
                    case 24:
                        return "VIII K";
                    case 25:
                        return "VII K";
                    case 26:
                        return "VI K";
                    case 27:
                        return "V K";
                    case 28:
                        return "IV K";
                    case 29:
                        return "III K";
                    case 30:
                        return "II K";
                    case 31:
                        return "I K";
                    default:
                        return "ERROR";
                }
            }
        }

        else if (getYearType(y).equals("leap 4"))
        {
            if (m==2)
            {
                switch(d)
                {
                    case 1:
                        return "K";
                    case 2:
                        return "III N";
                    case 3:
                        return "II N";
                    case 4:
                        return "I N";
                    case 5:
                        return "N";
                    case 6:
                        return "VII I";
                    case 7:
                        return "VI I";
                    case 8:
                        return "V I";
                    case 9:
                        return "IV I";
                    case 10:
                        return "III I";
                    case 11:
                        return "II I";
                    case 12:
                        return "I I";
                    case 13:
                        return "I";
                    case 14:
                        return "XVI K";
                    case 15:
                        return "XV K";
                    case 16:
                        return "XIV K";
                    case 17:
                        return "XIII K";
                    case 18:
                        return "XII K";
                    case 19:
                        return "XI K";
                    case 20:
                        return "X K";
                    case 21:
                        return "IX K";
                    case 22:
                        return "VIII K";
                    case 23:
                        return "VII K";
                    case 24:
                        return "VI K";
                    case 25:
                        return "V K";
                    case 26:
                        return "IV K";
                    case 27:
                        return "III K";
                    case 28:
                        return "II K";
                    case 29:
                        return "I K";
                    default:
                        return "ERROR";
                }
            }
            else if (m==3)
            {
                switch(d)
                {
                    case 1:
                        return "K";
                    case 2:
                        return "III N";
                    case 3:
                        return "II N";
                    case 4:
                        return "I N";
                    case 5:
                        return "N";
                    case 6:
                        return "VII I";
                    case 7:
                        return "VI I";
                    case 8:
                        return "V I";
                    case 9:
                        return "IV I";
                    case 10:
                        return "III I";
                    case 11:
                        return "II I";
                    case 12:
                        return "I I";
                    case 13:
                        return "I";
                    case 14:
                        return "XIV K";
                    case 15:
                        return "XIII K";
                    case 16:
                        return "XII K";
                    case 17:
                        return "XI K";
                    case 18:
                        return "X K";
                    case 19:
                        return "IX K";
                    case 20:
                        return "VIII K";
                    case 21:
                        return "VII K";
                    case 22:
                        return "VI K";
                    case 23:
                        return "V K";
                    case 24:
                        return "IV K";
                    case 25:
                        return "III K";
                    case 26:
                        return "II K";
                    case 27:
                        return "I K";
                    default:
                        return "ERROR";
                }
            }
            else if (m==1 || m==5 || m==7 || m==9 || m==10 || m==12 || m==13)
            {
                switch(d)
                {
                    case 1:
                        return "K";
                    case 2:
                        return "III N";
                    case 3:
                        return "II N";
                    case 4:
                        return "I N";
                    case 5:
                        return "N";
                    case 6:
                        return "VII I";
                    case 7:
                        return "VI I";
                    case 8:
                        return "V I";
                    case 9:
                        return "IV I";
                    case 10:
                        return "III I";
                    case 11:
                        return "II I";
                    case 12:
                        return "I I";
                    case 13:
                        return "I";
                    case 14:
                        return "XVI K";
                    case 15:
                        return "XV K";
                    case 16:
                        return "XIV K";
                    case 17:
                        return "XIII K";
                    case 18:
                        return "XII K";
                    case 19:
                        return "XI K";
                    case 20:
                        return "X K";
                    case 21:
                        return "IX K";
                    case 22:
                        return "VIII K";
                    case 23:
                        return "VII K";
                    case 24:
                        return "VI K";
                    case 25:
                        return "V K";
                    case 26:
                        return "IV K";
                    case 27:
                        return "III K";
                    case 28:
                        return "II K";
                    case 29:
                        return "I K";
                    default:
                        return "ERROR";
                }
            }
            else
            {
                switch(d)
                {
                    case 1:
                        return "K";
                    case 2:
                        return "V N";
                    case 3:
                        return "IV N";
                    case 4:
                        return "III N";
                    case 5:
                        return "II N";
                    case 6:
                        return "I N";
                    case 7:
                        return "N";
                    case 8:
                        return "VII I";
                    case 9:
                        return "VI I";
                    case 10:
                        return "V I";
                    case 11:
                        return "IV I";
                    case 12:
                        return "III I";
                    case 13:
                        return "II I";
                    case 14:
                        return "I I";
                    case 15:
                        return "I";
                    case 16:
                        return "XVI K";
                    case 17:
                        return "XV K";
                    case 18:
                        return "XIV K";
                    case 19:
                        return "XIII K";
                    case 20:
                        return "XII K";
                    case 21:
                        return "XI K";
                    case 22:
                        return "X K";
                    case 23:
                        return "IX K";
                    case 24:
                        return "VIII K";
                    case 25:
                        return "VII K";
                    case 26:
                        return "VI K";
                    case 27:
                        return "V K";
                    case 28:
                        return "IV K";
                    case 29:
                        return "III K";
                    case 30:
                        return "II K";
                    case 31:
                        return "I K";
                    default:
                        return "ERROR";
                }
            }
        }

        else
        {
            if (m==2)
            {
                switch(d)
                {
                    case 1:
                        return "K";
                    case 2:
                        return "III N";
                    case 3:
                        return "II N";
                    case 4:
                        return "I N";
                    case 5:
                        return "N";
                    case 6:
                        return "VII I";
                    case 7:
                        return "VI I";
                    case 8:
                        return "V I";
                    case 9:
                        return "IV I";
                    case 10:
                        return "III I";
                    case 11:
                        return "II I";
                    case 12:
                        return "I I";
                    case 13:
                        return "I";
                    case 14:
                        return "XV K";
                    case 15:
                        return "XIV K";
                    case 16:
                        return "XIII K";
                    case 17:
                        return "XII K";
                    case 18:
                        return "XI K";
                    case 19:
                        return "X K";
                    case 20:
                        return "IX K";
                    case 21:
                        return "VIII K";
                    case 22:
                        return "VII K";
                    case 23:
                        return "VI K";
                    case 24:
                        return "V K";
                    case 25:
                        return "IV K";
                    case 26:
                        return "III K";
                    case 27:
                        return "II K";
                    case 28:
                        return "I K";
                    default:
                        return "ERROR";
                }
            }
            else if (m==1 || m==4 || m==6 || m==8 || m==9 || m==11 || m==12)
            {
                switch(d)
                {
                    case 1:
                        return "K";
                    case 2:
                        return "III N";
                    case 3:
                        return "II N";
                    case 4:
                        return "I N";
                    case 5:
                        return "N";
                    case 6:
                        return "VII I";
                    case 7:
                        return "VI I";
                    case 8:
                        return "V I";
                    case 9:
                        return "IV I";
                    case 10:
                        return "III I";
                    case 11:
                        return "II I";
                    case 12:
                        return "I I";
                    case 13:
                        return "I";
                    case 14:
                        return "XVI K";
                    case 15:
                        return "XV K";
                    case 16:
                        return "XIV K";
                    case 17:
                        return "XIII K";
                    case 18:
                        return "XII K";
                    case 19:
                        return "XI K";
                    case 20:
                        return "X K";
                    case 21:
                        return "IX K";
                    case 22:
                        return "VIII K";
                    case 23:
                        return "VII K";
                    case 24:
                        return "VI K";
                    case 25:
                        return "V K";
                    case 26:
                        return "IV K";
                    case 27:
                        return "III K";
                    case 28:
                        return "II K";
                    case 29:
                        return "I K";
                    default:
                        return "ERROR";
                }
            }
            else
            {
                switch(d)
                {
                    case 1:
                        return "K";
                    case 2:
                        return "V N";
                    case 3:
                        return "IV N";
                    case 4:
                        return "III N";
                    case 5:
                        return "II N";
                    case 6:
                        return "I N";
                    case 7:
                        return "N";
                    case 8:
                        return "VII I";
                    case 9:
                        return "VI I";
                    case 10:
                        return "V I";
                    case 11:
                        return "IV I";
                    case 12:
                        return "III I";
                    case 13:
                        return "II I";
                    case 14:
                        return "I I";
                    case 15:
                        return "I";
                    case 16:
                        return "XVI K";
                    case 17:
                        return "XV K";
                    case 18:
                        return "XIV K";
                    case 19:
                        return "XIII K";
                    case 20:
                        return "XII K";
                    case 21:
                        return "XI K";
                    case 22:
                        return "X K";
                    case 23:
                        return "IX K";
                    case 24:
                        return "VIII K";
                    case 25:
                        return "VII K";
                    case 26:
                        return "VI K";
                    case 27:
                        return "V K";
                    case 28:
                        return "IV K";
                    case 29:
                        return "III K";
                    case 30:
                        return "II K";
                    case 31:
                        return "I K";
                    default:
                        return "ERROR";
                }
            }
        }

    }
}
