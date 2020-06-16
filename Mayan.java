public class Mayan implements Calendar
{

    static final MDate EPOCH = new MDate(12,17,16,7,5, 13, 5,3, 14);

    @Override
    public Date countBack(Date from, int y, int m, int d)
    {

        d = (int)(d+(m*20)+(y*360));

        from = (MDate)from;

        int baktun = ((MDate) from).baktun;
        int katun = ((MDate) from).katun;
        int tun = from.year;
        int winal = from.month;
        int kin = from.day;
        int tzolkinCount = ((MDate) from).tzolkinCount;
        int tzolkinSeq = ((MDate) from).tzolkinSeq;
        int haabCount = ((MDate) from).haabCount;
        int haabSeq = ((MDate) from).haabSeq;

        do
        {

            d--;
            kin--;
            tzolkinCount--;
            tzolkinSeq--;
            haabCount--;

            if (kin==-1)
            {
                kin=19;
                winal--;
                if (winal==-1)
                {
                    winal=17;
                    tun--;
                    if (tun==-1)
                    {
                        tun=19;
                        katun--;
                        if (katun==-1)
                        {
                            katun=19;
                            baktun--;
                        }
                    }
                }
            }

            if (tzolkinCount==0)
                tzolkinCount=13;
            if (tzolkinSeq==0)
                tzolkinSeq=20;

            if (haabCount==-1 && haabSeq==1)
            {
                haabCount=5;
                haabSeq=19;
            }
            else
            {
                haabCount=19;
                haabSeq--;
            }

            if (haabSeq==0)
                haabSeq=19;

        } while (d>0);

        return new MDate(baktun, katun, tun, winal, kin, tzolkinCount, tzolkinSeq, haabCount, haabSeq);

    }

    @Override
    public Date countFwd(Date from, int y, int m, int d)
    {

        d = (int)(d+(m*20)+(y*360));

        from = (MDate)from;

        int baktun = ((MDate) from).baktun;
        int katun = ((MDate) from).katun;
        int tun = from.year;
        int winal = from.month;
        int kin = from.day;
        int tzolkinCount = ((MDate) from).tzolkinCount;
        int tzolkinSeq = ((MDate) from).tzolkinSeq;
        int haabCount = ((MDate) from).haabCount;
        int haabSeq = ((MDate) from).haabSeq;

        do
        {

            d--;
            kin++;
            tzolkinCount++;
            tzolkinSeq++;
            haabCount++;

            if (kin==20)
            {
                kin=0;
                winal++;
                if (winal==18)
                {
                    winal=0;
                    tun++;
                    if (tun==20)
                    {
                        tun=0;
                        katun++;
                        if (katun==20)
                        {
                            katun=0;
                            baktun++;
                        }
                    }
                }
            }

            if (tzolkinCount==14)
                tzolkinCount=1;
            if (tzolkinSeq==21)
                tzolkinSeq=1;
            if ((haabCount==5 && haabSeq==19) || haabCount==20)
            {
                haabCount=0;
                haabSeq++;
            }
            if (haabSeq==20)
                haabSeq=1;

        } while (d>0);

        return new MDate(baktun, katun, tun, winal, kin, tzolkinCount, tzolkinSeq, haabCount, haabSeq);

    }

    @Override
    public void promptCount()
    {

        MDate date;

        System.out.println("Would you like to calculate a future (f) or past (p) date?");
        String chirality = scan.next();

        System.out.println("Count from a specific date (d) or from now (n)?");
        String reference = scan.next();

        if (reference.equals("d"))
        {

            System.out.println("Enter the b'ak'tun:");
            int b = scan.nextInt();
            System.out.println("Enter the k'atun:");
            int k = scan.nextInt();
            System.out.println("Enter the tun:");
            int y = scan.nextInt();
            System.out.println("Enter the winal:");
            int m = scan.nextInt();
            System.out.println("Enter the k'in:");
            int d = scan.nextInt();
            System.out.println("Enter the tzolk'in count:");
            int tc = scan.nextInt();
            System.out.println("Enter the tzolk'in sequence (1-20):");
            int ts = scan.nextInt();
            System.out.println("Enter the haab' count:");
            int hc = scan.nextInt();
            System.out.println("Enter the haab' sequence:");
            int hs = scan.nextInt();

            date = new MDate(b,k,y,m,d,tc,ts,hc,hs);

        }
        else
            date = (MDate) now();

        System.out.println("Enter days to count:");
        int d = scan.nextInt();

        if (chirality.equals("f"))
            date = (MDate) countFwd(date,0,0,d);
        else
            date = (MDate) countBack(date,0,0,d);

        System.out.println(serializeDate(date, ""));

    }

    @Override
    public String serializeYear(int y)
    {
        return null;
    }

    @Override
    public int daysSince(Date date)
    {
        Gregorian g = new Gregorian();
        return g.daysSince(toGregorian(new DSCDate(4, date.year, date.month, date.day)));
    }

    @Override
    public String serializeDate(Date date, String format)
    {

        date = (MDate)date;

        return ((MDate) date).baktun + "." + ((MDate) date).katun + "." + date.year + "." + date.month + "." +
                date.day + ", " + ((MDate) date).tzolkinCount + " " + tzolkin(((MDate) date).tzolkinSeq) + " " +
                ((MDate) date).haabCount + " " + haab(((MDate) date).haabSeq);

    }

    @Override
    public String isHoliday(int y, int m, int d, int w)
    {
        return null;
    }

    @Override
    public Date now()
    {
        Gregorian g = new Gregorian();
        int daycount = g.daysSince(g.EPOCH);
        return countFwd(EPOCH, 0, 0, daycount);
    }

    private String tzolkin(int seq)
    {
        switch(seq)
        {
            case 1:
                return "Imix";
            case 2:
                return "Ik'";
            case 3:
                return "Ak'b'al";
            case 4:
                return "K'an";
            case 5:
                return "Chikchan";
            case 6:
                return "Kimi";
            case 7:
                return "Manik'";
            case 8:
                return "Lamat";
            case 9:
                return "Muluk";
            case 10:
                return "Ok";
            case 11:
                return "Chuwen";
            case 12:
                return "Eb'";
            case 13:
                return "B'en";
            case 14:
                return "Ix";
            case 15:
                return "Men";
            case 16:
                return "K'ib";
            case 17:
                return "Kab'an";
            case 18:
                return "Etz'nab'";
            case 19:
                return "Kawak";
            case 20:
                return "Ajaw";
            default:
                return "ERROR";
        }
    }

    private String haab(int seq)
    {
        switch(seq)
        {
            case 1:
                return "Pop";
            case 2:
                return "Wo'";
            case 3:
                return "Sip";
            case 4:
                return "Sotz'";
            case 5:
                return "Sek";
            case 6:
                return "Xul";
            case 7:
                return "Yaxk'in'";
            case 8:
                return "Mol";
            case 9:
                return "Ch'en";
            case 10:
                return "Yax";
            case 11:
                return "Sak";
            case 12:
                return "Keh";
            case 13:
                return "Mak";
            case 14:
                return "K'ank'in";
            case 15:
                return "Muwan";
            case 16:
                return "Pax";
            case 17:
                return "K'ayab'";
            case 18:
                return "Kumk'u";
            case 19:
                return "Wayeb";
            default:
                return "ERROR";
        }
    }

    public Date toGregorian(DSCDate date)
    {

        Gregorian g = new Gregorian();

        if (date.isBefore(EPOCH))
        {
            MDate hold = EPOCH;
            int count = 0;
            do
            {
                hold = (MDate) countBack(hold, 0, 0, 1);
                count++;
            } while (!hold.equals(date));
            return g.countBack(g.EPOCH, 0, 0, count);
        }
        else if (date.equals(EPOCH))
            return g.EPOCH;
        else
        {
            MDate hold = EPOCH;
            int count = 0;
            do
            {
                hold = (MDate) countFwd(hold, 0, 0, 1);
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

























