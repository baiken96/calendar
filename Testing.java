import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class Testing
{

    // X Gregorian
    // X Hebrew
    // x Decimal Solar
    // x Tabular Lunar Hijri
    // x Solar Hijri
    // x Roman Republican
    // x Mayan

    public static void main(String[] args)
    {

        printToday();

    }

    static void printToday()
    {
        Gregorian g = new Gregorian();
        Hebrew h = new Hebrew();
        DecimalSolar d = new DecimalSolar();
        DSCTime dt = new DSCTime().now();
        Hijri i = new Hijri();
        Persian p = new Persian();
        Roman r = new Roman();
        Mayan m = new Mayan();

        if (!g.isHoliday(g.now().year, g.now().month, g.now().day, g.now().weekday).equals("Not a holiday"))
            System.out.println(g.isHoliday(g.now().year, g.now().month, g.now().day, g.now().weekday));
        System.out.println(g.serializeDate(g.now(), "w-d-m-y"));
        if (!h.isHoliday(h.now().year, h.now().month, h.now().day, h.now().weekday).equals("Not a holiday"))
            System.out.println(h.isHoliday(h.now().year, h.now().month, h.now().day, h.now().weekday));
        System.out.println(h.serializeDate(h.now(), "w-d-m-y"));
        if (!d.isHoliday(d.now().year, d.now().month, d.now().day, d.now().weekday).equals("Not a holiday"))
            System.out.println(d.isHoliday(d.now().year, d.now().month, d.now().day, d.now().weekday));
        System.out.println(d.serializeDate(d.now(), "w-d-m-y") + " " + dt.serialize("std"));
        if (!i.isHoliday(i.now().year, i.now().month, i.now().day, i.now().weekday).equals("Not a holiday"))
            System.out.println(i.isHoliday(i.now().year, i.now().month, i.now().day, i.now().weekday));
        System.out.println(i.serializeDate(i.now(), "w-d-m-y"));
        if (!p.isHoliday(p.now().year, p.now().month, p.now().day, p.now().weekday).equals("Not a holiday"))
            System.out.println(p.isHoliday(p.now().year, p.now().month, p.now().day, p.now().weekday));
        System.out.println(p.serializeDate(p.now(), "w-d-m-y"));
        if (!r.isHoliday(r.now().year, r.now().month, r.now().day, r.now().weekday).equals("Not a holiday"))
            System.out.println(r.isHoliday(r.now().year, r.now().month, r.now().day, r.now().weekday));
        System.out.println(r.serializeDate(r.now(), "w-d-m-y"));
        System.out.println(m.serializeDate(m.now(), ""));

    }

}
