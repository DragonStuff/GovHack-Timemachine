package com.timemachine.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.text.util.Linkify;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jeri on 1/04/14.
 */
public class MyUtils {
    public static void linkifyMe(String str, TextView view){
        linkifyMe(str, view, true);
    }

    public static void linkifyMe(String str, TextView view, boolean singleLine)
    {
        String out = str;
        if(singleLine)
            out = str.replaceAll("[\\t\\n\\r]", " ").replace("  ", " ");

        view.setText(out);
        Pattern tagMatcher = Pattern.compile("[#@]+[A-Za-z0-9-_]+\\b");
        //String newActivityURL = "content://com.latershot.hashtags/";
        Linkify.addLinks(view, tagMatcher, Constants.LINKIFY_URL);
    }

    public static String getFriendlyDate(long date){
        String friendlyDate = "recently";

        Calendar current = Calendar.getInstance();
        long diff = current.getTimeInMillis() - date;
        diff = diff / 1000;

        if(diff > 0) {
            long min = diff / (60) % 60;
            long hours = (diff / (60 * 60)) % 24;
            long days = (diff / (60 * 60 * 24));

            if(days > 0)
            {
                friendlyDate = days + "d ago";
            }
            else
            if(hours > 0)
            {
                friendlyDate = hours + "h ago";
            }
            else
            if(min > 0)
            {
                friendlyDate = min + "m ago";
            }
        }

        return friendlyDate;
    }

    public static String getFriendlyDate(long date, String ago){
        String friendlyDate = "recently";

        Calendar current = Calendar.getInstance();
        long diff = current.getTimeInMillis() - date;
        diff = diff / 1000;

        if(diff > 0) {
            long min = diff / (60) % 60;
            long hours = (diff / (60 * 60)) % 24;
            long days = (diff / (60 * 60 * 24));

            if(days > 0)
            {
                friendlyDate = days + "d " + ago;
            }
            else
            if(hours > 0)
            {
                friendlyDate = hours + "h " + ago;
            }
            else
            if(min > 0)
            {
                friendlyDate = min + "m " + ago;
            }
        }

        return friendlyDate.trim();
    }

    public static String getFriendlyDistance(float num) {
        int dist = Math.round(num);
        String distance = "";
        if(dist > 500000)
            distance = "> 50km";
        else
            if(dist > 1000)
            distance = dist / 1000 + "km";
        else
            distance = dist + "m";

        return distance;
    }

    public static String getFriendlyNumber(int num)
    {
        StringBuffer sb = new StringBuffer();

        if(num > 1000){

        }
        else{
            sb.append(num);
        }

        return sb.toString();
    }

    public static String getFriendlyTimer(long date){
        return getFriendlyTimer(date, "unlocked", true, false, true);
    }

    public static String getFriendlyTimer(long date, String expired, boolean abbrev, boolean doTick, boolean single){
        String friendlyDate = expired;

        StringBuffer sb = new StringBuffer();

        Calendar current = Calendar.getInstance();
        long diff = date - current.getTimeInMillis();
        diff = diff / 1000;

        if(diff > 0) {
            long sec = diff % 60;
            long min = diff / (60) % 60;
            long hours = (diff / (60 * 60)) % 24;
            long days = (diff / (60 * 60 * 24));

            // always display everything
            if(doTick){
                if (days > 0)
                    sb.append(days).append((abbrev) ? "d" : " days");
                if (hours > 0)
                    sb.append(" ").append(hours).append((abbrev) ? "h" : " hours");
                if (min > 0)
                    sb.append(" ").append(min).append((abbrev) ? "m" : " minutes");
                    sb.append(" ").append(sec).append((abbrev) ? "s" : " seconds");
            }
            else {

                if (days > 0) {
                    sb.append(days).append((abbrev) ? "d" : " days");
                    if (hours > 0 && !single)
                        sb.append(" ").append(hours).append((abbrev) ? "h" : " hours");
                } else {
                    if (hours > 0) {
                        sb.append(hours).append((abbrev) ? "h" : " hours");
                        if (min > 0 && !single)
                            sb.append(" ").append(min).append((abbrev) ? "m" : " minutes");
                    } else {
                        if (min > 0) {
                            sb.append(min).append((abbrev) ? "m" : "  minutes");
                            if (sec > 0 && min < 3)
                                sb.append(" ").append(sec).append((abbrev) ? "s" : " seconds");
                        } else {
                            sb.append(sec).append((abbrev) ? "s" : " seconds");
                        }
                    }
                }
            }

            return sb.toString();
        }

        return friendlyDate;
    }

    public static LatLng midPoint(double lat1,double lon1,double lat2,double lon2){

        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        //print out in degrees
        //System.out.println(Math.toDegrees(lat3) + " " + Math.toDegrees(lon3));
        LatLng mid = new LatLng(Math.toDegrees(lat3), Math.toDegrees(lon3));
        return mid;
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    public static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > Constants.MIN_TIME;
        boolean isSignificantlyOlder = timeDelta < -Constants.MIN_TIME;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    public static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public static int getZoomLevel(float distance){
        if(distance < 200)
            return 17;

        if(distance < 500)
            return 16;

        if(distance < 1000)
            return 15;

        if(distance < 2000)
            return 14;

        if(distance < 3000)
            return 13;

        if(distance < 5000)
            return 12;

        if(distance < 7000)
            return 11;

        if(distance < 25000)
            return 10;

        return 9;
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static boolean copyToClipboard(Context context, String text) {
        try {
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                        .getSystemService(context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
                        .getSystemService(context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData
                        .newPlainText("message", text);
                clipboard.setPrimaryClip(clip);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validate(String value, String regex)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
