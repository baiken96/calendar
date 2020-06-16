import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Gregorian implements Calendar
{

    public Date countBack(Date from, int y, int m, int d)
    {
        LocalDateTime f = from.toldt();
        return Date.fromldt(f.minusYears(y).minusMonths(m).minusDays(d));
    }

    public Date countFwd(Date from, int y, int m, int d)
    {
        LocalDateTime f = from.toldt();
        return Date.fromldt(f.plusYears(y).plusMonths(m).plusDays(d));
    }

    public void promptCount()
    {
        LocalDateTime date;

        System.out.println("Would you like to calculate a future (f) or past (p) date?");
        String chirality = scan.next();

        System.out.println("Count from a specific date (d) or from now (n)?");
        String reference = scan.next();

        if (reference.equals("d"))
        {

            System.out.println("Enter the year:");
            int y = scan.nextInt();
            System.out.println("Enter the month (1-12):");
            int m = scan.nextInt();
            System.out.println("Enter the day (1-31):");
            int d = scan.nextInt();

            date = LocalDateTime.of(y,m,d,0,0);

        }
        else
            date = LocalDateTime.now();

        System.out.println("Enter years to count:");
        int y = scan.nextInt();
        System.out.println("Enter months to count:");
        int m = scan.nextInt();
        System.out.println("Enter days to count:");
        int d = scan.nextInt();

        if (chirality.equals("f"))
            date = countFwd(Date.fromldt(date),y,m,d).toldt();
        else
            date = countBack(Date.fromldt(date),y,m,d).toldt();

        System.out.println(date.getDayOfWeek() + ", " + date.getDayOfMonth() + " " + date.getMonth() + " " + serializeYear(date.getYear()));
    }

    public String serializeYear(int y)
    {
        String year = String.valueOf(y);

        if (y<0)
        {
            year = String.valueOf(y*-1) + " BC";
        }
        else if (y<1000)
        {
            year = "AD " + String.valueOf(y);
        }
        return year;
    }

    public int daysSince(Date date)
    {
        LocalDateTime d = date.toldt();
        LocalDateTime now = LocalDateTime.now();
        int count = 0;
        if (now.isEqual(d))
            return 0;
        else if (now.isAfter(d))
        {
            while (!(Date.fromldt(now).equals(Date.fromldt(d))))
            {
                now = countBack(Date.fromldt(now), 0,0,1).toldt();
                count++;
            }
            return count;
        }
        else if (now.isBefore(d))
        {
            while (!(Date.fromldt(now).equals(Date.fromldt(d))))
            {
                now = countFwd(Date.fromldt(now), 0,0,1).toldt();
                count++;
            }
            return count;
        }
        else
        {
            System.out.println("Error in daysSince()");
            return -1;
        }
    }

    public String serializeDate(Date date, String format)
    {
        LocalDateTime d = date.toldt();
        if (format.equals("w-d-m-y"))
            return d.getDayOfWeek() + ", " + d.getDayOfMonth() + " " + d.getMonth() + " " + serializeYear(d.getYear());
        else if (format.equals("w-m-d-y"))
            return d.getDayOfWeek() + ", " + d.getMonth() + " " + d.getDayOfMonth() + ", " + serializeYear(d.getYear());
        else if (format.equals("m-d-y"))
            return d.getMonth() + " " + d.getDayOfMonth() + ", " + serializeYear(d.getYear());
        else if (format.equals("d-m-y"))
            return d.getDayOfMonth() + " " + d.getMonth() + " " + serializeYear(d.getYear());
        else if (format.equals("d-m"))
            return d.getDayOfMonth() + " " + d.getMonth();
        else if (format.equals("w-d-m"))
            return d.getDayOfWeek() + ", " + d.getDayOfMonth() + " " + d.getMonth();
        else if (format.equals("m-d"))
            return d.getMonth() + " " + d.getDayOfMonth();
        else if (format.equals("w-m-d"))
            return d.getDayOfWeek() + ", " + d.getMonth() + " " + d.getDayOfMonth();
        else
            return "Invalid date format specified";
    }

    public String isHoliday(int y, int m, int d, int w)
    {
        
        LocalDateTime easter = anonComputus(y);
        if (m==1 && d==1)
            return "New Year's Day";
        else if (m==12 && d==31)
            return "New Year's Eve";
        else if (m==12 && d==25)
            return "Christmas";
        else if (m==12 && d==24)
            return "Christmas Eve";
        else if (m==11 && (d>21 && d<29) && w==4)
            return "Thanksgiving";
        else if (m==11 && (d>21 && d<29) && w==5)
            return "Black Friday";
        else if (m==5 && (d>7 && d<15) && w==7)
            return "Mothers' Day";
        else if (m==easter.getMonthValue() && d==easter.getDayOfMonth())
            return "Easter Sunday";
        else if (m==easter.getMonthValue() && d==easter.getDayOfMonth()-2)
            return "Good Friday";
        else if (m==7 && d==4)
            return "Independence Day";
        else if (m==6 && (d>14 && d<22) && w==7)
            return "Fathers' Day";
        else if (m==10 && d==31)
            return "Halloween";
        else if (m==2 && d==14)
            return "Valentine's Day";
        else if (m==3 && d==17)
            return "St. Patrick's Day";
        else if (m==5 && d>24 && w==1)
            return "Memorial Day";
        else if (m==9 && d>23 && w==1)
            return "Labor Day";
        else if (m==2 && d==2)
            return "Groundhog Day";
        else if (m==4 && d==1)
            return "April Fools Day";
        else if (m==4 && d==22)
            return "Earth Day";
        else if (m==5 && d==5)
            return "Cinco de Mayo";
        else
            return "Not a holiday";

    }

    @Override
    public Date now()
    {
        return Date.fromldt(LocalDateTime.now());
    }

    private LocalDateTime anonComputus(int y)
    {

        int a = y % 19;
        int b = y / 100;
        int c = y % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b+8)/25;
        int g = (b-f+1)/3;
        int h = (19*a+b-d-g+15)%30;
        int i = c / 4;
        int k = c % 4;
        int l = (32+2*e+2*i-h-k)%7;
        int m = (a+11*h+22*l)/451;
        int month = (h+l-7*m+114)/31;
        int day = ((h+l-7*m+114)%31)+1;
        return LocalDateTime.of(y, month, day, 0, 0);

    }

}
