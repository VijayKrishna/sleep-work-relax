package edu.uci.ics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import javax.print.attribute.standard.DateTimeAtCompleted;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class DateAggregator {
  private String rowId;
  private int timeStamp;
  private Date localTime;
  private String redditId;
  private int score;
  private String title;
  
  private static final String[] DAYS = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
  
  DateAggregator(String id, String redditId, int time, int score, String title) {
    rowId = id;
    timeStamp = time;
    this.redditId = redditId; 
    this.localTime= new Date((long)timeStamp*1000);
    this.score = score;
    this.title = title;
  }
  
  public String toString() {
//    String rowId = this.rowId.substring(1, this.rowId.length() - 1);
//    String redditId = this.redditId.substring(1, this.redditId.length() - 1);
    String day = DAYS[localTime.getDay()];
    int scoreBucket = scoreBucket(this.score);
    return rowId + "," 
        + redditId + "," 
    + localTime.toString() + "," 
        + day + "," 
    + localTime.getHours() + "," 
        + this.score + "," 
    + scoreBucket + "," 
    + title;
  }
  
  public static void main(String[] args) throws IOException {
    
    File file = new File("/home/vijay/hackdata/localtimes2.csv");
    Scanner scanner = null;
    try {
      scanner = new Scanner(file);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    
    int[] timeSlots = new int[24];
    int[] daySlots = new int[7];
    int[][] dayHourSlots = new int[7][24];
    System.out.println("row_id,reddit_id,localdate,day,hour,score,score_bucket,title");
    Reader in = new FileReader("/home/vijay/hackdata/localtimes2.csv");
    Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);

//    for (CSVRecord record : records) {
//      String reddit_id = record.get(0);
////      System.out.println(reddit_id);
////      String firstName = record.get("First Name");
//    }
    
//    while(scanner.hasNextLine()) {
    for (CSVRecord record : records) {
//      String line = scanner.nextLine();
//      String[] lineSplit = line.split(",");
      try {
        String title =  record.get(4).replace(',', ' ');
        DateAggregator aggr = new DateAggregator(record.get(0), 
            record.get(1), 
                           Integer.parseInt(record.get(2)),
                           Integer.parseInt(record.get(3)),
                           title);
        int timeSlot = assignTimeSlot(aggr.localTime);
        timeSlots[timeSlot] += 1;
        int daySlot = assignDaySlot(aggr.localTime);
        daySlots[daySlot] += 1;
//        int[] dayHour = assignDayHourSlot(time);
        dayHourSlots[daySlot][timeSlot] += 1;
        System.out.println(aggr);
      } catch(NumberFormatException|ArrayIndexOutOfBoundsException numex) {
//        System.err.println(line);
      }
    }
    
//    printSlots(timeSlots, "Hour");
//    printSlots(daySlots, "Day");
//    printSlots(dayHourSlots,  "", "");
    
  }

  public static void printSlots(int[] slots, String label) {
    for(int i = 0; i < slots.length; i += 1) {
      System.out.println(label + ": " + i + "::" + slots[i]);
    }
    
    System.out.println(Arrays.toString(slots));
  }
  
  public static void printSlots(int[][] slots, String label1, String label2) {
    for(int hour = 0; hour < 24; hour += 1) {
      for(int day = 0; day < 7; day += 1) {
        System.out.println(slots[hour][day]);
      }
    }
    
    System.out.println(Arrays.toString(slots));
  }
  
    private static int assignTimeSlot(Date time) {
      int hour = time.getHours();
      return hour;
    }
    
    private static int assignDaySlot(Date time) {
      int day = time.getDay();
      return day;
    }
    
    private static int[] assignDayHourSlot(Date time) {
      int day = time.getDay();
      int hour = time.getHours();
      return new int[] {day, hour};
    }
  
    private static int scoreBucket(int score) {
      final int offset = -264;
      final int max = 20570 + offset;
      
      int newScore = score + offset;
      double percentage = newScore / (max * 1.0);
      
      if(percentage >= 0.0 && percentage < 2.0) {
        return 0;
      } else if(percentage >= 2.0 && percentage < 4.0) {
        return 1;
      } else if(percentage >= 4.0 && percentage < 6.0) {
        return 2;
      } else if(percentage >= 6.0 && percentage < 8.0) {
        return 3;
      } else {
        return 4;
      }
    }
    
    
}
