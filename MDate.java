public class MDate extends Date
{

    public int baktun;
    public int katun;
    // tun=year
    // winal=month
    // kin=day
    public int tzolkinSeq;
    public int tzolkinCount;
    public int haabSeq;
    public int haabCount;

    public MDate(int b, int k, int y, int m, int d, int tc, int ts, int hc, int hs)
    {
        super(y, m, d);
        this.baktun = b;
        this.katun = k;
        this.tzolkinCount = tc;
        this.tzolkinSeq = ts;
        this.haabCount = hc;
        this.haabSeq = hs;
    }

    public boolean isBefore(MDate ref)
    {
        return this.baktun < ref.baktun || (this.baktun == ref.baktun && this.katun < ref.katun) ||
                (this.baktun == ref.baktun && this.katun == ref.katun && this.year < ref.year) ||
                (this.baktun == ref.baktun && this.katun == ref.katun &&
                         this.year == ref.year && this.month < ref.month) ||
                (this.baktun == ref.baktun && this.katun == ref.katun &&
                        this.year == ref.year && this.month == ref.month && this.day < ref.day);
    }

    public boolean isAfter(MDate ref)
    {
        return this.baktun > ref.baktun || (this.baktun == ref.baktun && this.katun > ref.katun) ||
                (this.baktun == ref.baktun && this.katun == ref.katun && this.year > ref.year) ||
                (this.baktun == ref.baktun && this.katun == ref.katun &&
                        this.year == ref.year && this.month > ref.month) ||
                (this.baktun == ref.baktun && this.katun == ref.katun &&
                        this.year == ref.year && this.month == ref.month && this.day > ref.day);
    }

    public boolean equals(MDate ref)
    {
        return this.baktun == ref.baktun && this.katun == ref.katun &&
                this.year == ref.year && this.month == ref.month && this.day == ref.day;
    }

}
