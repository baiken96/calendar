public class DSCDate extends Date
{

    public int era;

    public DSCDate(int e, int y, int m, int d)
    {
        super(y, m, d);
        this.era = e;
    }

    // Add eras

    public boolean isBefore(DSCDate ref)
    {
        return this.era < ref.era || (this.era == ref.era && this.year < ref.year) ||
                (this.era == ref.era && this.year == ref.year && this.month < ref.month) ||
                (this.era == ref.era && this.year == ref.year && this.month == ref.month && this.day < ref.day);
    }

    public boolean isAfter(DSCDate ref)
    {
        return this.era > ref.era || (this.era == ref.era && this.year > ref.year)  ||
                (this.era == ref.era && this.year == ref.year && this.month > ref.month) ||
                (this.era == ref.era && this.year == ref.year && this.month == ref.month && this.day > ref.day);
    }

    public boolean equals(DSCDate ref)
    {
        return this.era == ref.era && this.year == ref.year && this.month == ref.month && this.day == ref.day;
    }

    public String dayOfWeek(int d)
    {
        if (d==1||d==8||d==15||d==22||d==29)
            return "Sunday";
        else if (d==2||d==9||d==16||d==23||d==30)
            return "Moonday";
        else if (d==3||d==10||d==17||d==24||d==31)
            return "Earthday";
        else if (d==4||d==11||d==18||d==25||d==32)
            return "Midweek";
        else if (d==5||d==12||d==19||d==26||d==33)
            return "Fireday";
        else if (d==6||d==13||d==20||d==27||d==34)
            return "Windsday";
        else if (d==7||d==14||d==21||d==28||d==35)
            return "Waterday";
        else
            return "";
    }

}
