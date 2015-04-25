package com.adamcrawford.ejuicer;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;

/**
 * Author:  Adam Crawford
 * Project: EJuicer
 * Package: com.adamcrawford.ejuicer
 * File:    JuiceItem
 * Purpose: TODO Minimum 2 sentence description
 */
@ParseClassName("Juice")
public class JuiceItem extends ParseObject implements Serializable {

    public static String FLAVOR = "Flavor";
    public static String RATING = "Rating";
    public static String DELETING = "Deleting";

    public static ParseQuery<JuiceItem> getQuery() throws ParseException {
        Log.i("JI", "getting query");
        return ParseQuery.getQuery(JuiceItem.class);
    }

    public String getFlavor() {
        return getString(FLAVOR);
    }

    public void setFlavor(String flavor) {
        put(FLAVOR, flavor);
    }

    public Number getRating() {
        return getNumber(RATING);
    }

    public void setRating(Number rating) {
        put(RATING, rating);
    }

    public boolean getDeleting() {
        return getBoolean(DELETING);
    }

    public void setDeleting(boolean deleting) {
        put(DELETING, deleting);
    }
}
