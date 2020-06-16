import java.time.LocalDateTime;
import java.util.Scanner;

interface Calendar
{

    static final Date EPOCH = Date.fromldt(LocalDateTime.of(1970,1,1,0,0));
    static final Scanner scan = new Scanner(System.in);

    Date countBack(Date from, int y, int m, int d);

    Date countFwd(Date from, int y, int m, int d);

    void promptCount();

    String serializeYear(int y);

    int daysSince(Date date);

    String serializeDate(Date date, String format);

    String isHoliday(int y, int m, int d, int w);

    Date now();

}