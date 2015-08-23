package com.zinc.zoopy.waste;

/**
 * Created by Zoopy86 on 15-08-15.
 */
public enum Periods {
    ALL_TIME(Config.context.getString(R.string.period_all)),
    TODAY(Config.context.getString(R.string.period_today)),
    THIS_WEEK(Config.context.getString(R.string.period_this_week)),
    THIS_MONTH(Config.context.getString(R.string.period_this_month)),
    LAST_MONTH(Config.context.getString(R.string.period_last_month)),
    THIS_YEAR(Config.context.getString(R.string.period_this_year)),
    LAST_YEAR(Config.context.getString(R.string.period_last_year));

    String mLabel;

    Periods(String label){
        mLabel = label;
    }
}
