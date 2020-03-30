package me.itsjasonn.survivalgames.utils;

import java.text.DecimalFormat;

public class DateCalculator
{
  public static float getDays(int units)
  {
    DecimalFormat dm = new DecimalFormat("#.#");
    return Float.parseFloat(dm.format(units / 86400));
  }
  
  public static float getHours(int units)
  {
    DecimalFormat dm = new DecimalFormat("#.#");
    return Float.parseFloat(dm.format(units / 3600));
  }
  
  public static float getMinutes(int units)
  {
    DecimalFormat dm = new DecimalFormat("#.#");
    return Float.parseFloat(dm.format(units / 60));
  }
  
  public static String getHMS(int units, boolean words)
  {
    int s = 0;
    int m = 0;
    int h = 0;
    for (int i = 0; i < units; i++)
    {
      s++;
      if (s == 60)
      {
        s = 0;
        m++;
      }
      if (m == 60)
      {
        m = 0;
        h++;
      }
    }
    String sS = Integer.toString(s);
    String mS = Integer.toString(m);
    String hS = Integer.toString(h);
    if (!words)
    {
      if (s < 10) {
        sS = "0" + s;
      }
      if (m < 10) {
        mS = "0" + m;
      }
      if (h < 10) {
        hS = "0" + h;
      }
    }
    if (!words) {
      return hS + ":" + mS + ":" + sS;
    }
    if (words) {
      return hS + " hour(s), " + mS + " minute(s), " + sS + " second(s)";
    }
    return "-1";
  }
  
  public static String getDHMS(int units, boolean words)
  {
    int s = 0;
    int m = 0;
    int h = 0;
    int d = 0;
    for (int i = 0; i < units; i++)
    {
      s++;
      if (s == 60)
      {
        s = 0;
        m++;
      }
      if (m == 60)
      {
        m = 0;
        h++;
      }
      if (h == 24)
      {
        h = 0;
        d++;
      }
    }
    String sS = Integer.toString(s);
    String mS = Integer.toString(m);
    String hS = Integer.toString(h);
    String dS = Integer.toString(d);
    if (!words)
    {
      if (s < 10) {
        sS = "0" + s;
      }
      if (m < 10) {
        mS = "0" + m;
      }
      if (h < 10) {
        hS = "0" + h;
      }
      if (d < 10) {
        dS = "0" + d;
      }
    }
    if (!words) {
      return dS + ":" + hS + ":" + mS + ":" + sS;
    }
    if (words) {
      return dS + " day(s), " + hS + " hour(s), " + mS + " minute(s), " + sS + " second(s)";
    }
    return "-1";
  }
  
  public static int getHMS(int units, String type)
  {
    int s = 0;
    int m = 0;
    int h = 0;
    for (int i = 0; i < units; i++)
    {
      s++;
      if (s == 60)
      {
        s = 0;
        m++;
      }
      if (m == 60)
      {
        m = 0;
        h++;
      }
    }
    if (type.equalsIgnoreCase("s")) {
      return s;
    }
    if (type.equalsIgnoreCase("m")) {
      return m;
    }
    if (type.equalsIgnoreCase("h")) {
      return h;
    }
    return -1;
  }
  
  public static String getMS(int units, boolean words)
  {
    int s = 0;
    int m = 0;
    for (int i = 0; i < units; i++)
    {
      s++;
      if (s == 60)
      {
        s = 0;
        m++;
      }
    }
    String sS = Integer.toString(s);
    String mS = Integer.toString(m);
    if (!words)
    {
      if (s < 10) {
        sS = "0" + s;
      }
      if (m < 10) {
        mS = "0" + m;
      }
    }
    if (!words) {
      return mS + ":" + sS;
    }
    if (words) {
      return mS + " minute(s), " + sS + " second(s)";
    }
    return "-1";
  }
}
