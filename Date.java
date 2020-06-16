import java.time.LocalDateTime;

public class Date
{

    public int year;
    public int month;
    public int day;
    public int weekday;

    public Date(int y, int m, int d)
    {
        this.year = y;
        this.month = m;
        this.day = d;
        try
        {
            this.weekday = this.toldt().getDayOfWeek().getValue();
        }
        catch (Exception e)
        {
            this.weekday = 0;
        }
    }

    public Date(int y, int m, int d, int w)
    {
        this.year = y;
        this.month = m;
        this.day = d;
        this.weekday = w;
    }

    public LocalDateTime toldt()
    {
        return LocalDateTime.of(this.year, this.month, this.day, 0, 0);
    }

    public static Date fromldt(LocalDateTime ldt)
    {
        return new Date(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth());
    }

    public boolean isBefore(Date ref)
    {
        return this.year < ref.year || (this.year == ref.year && this.month < ref.month) ||
            (this.year == ref.year && this.month == ref.month && this.day < ref.day);
    }

    public boolean isAfter(Date ref)
    {
        return this.year > ref.year || (this.year == ref.year && this.month > ref.month) ||
                (this.year == ref.year && this.month == ref.month && this.day > ref.day);
    }

    public boolean equals(Date ref)
    {
        return this.year == ref.year && this.month == ref.month && this.day == ref.day;
    }

}
