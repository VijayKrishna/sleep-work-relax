
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Clusters Comments by each hour of the day 0 to 23h (military time).
 * 
 * @author Christian M. Adriano
 *
 */
public class ClusterCommentsPerDayTime {

	HashMap<String,ArrayList<String>> DayTime_FileNameList_Map = new	HashMap<String,ArrayList<String>>(); 

	HashMap<String,ArrayList<String>> timePostMap = new	HashMap<String,ArrayList<String>>();
	HashMap<String,ArrayList<String>> weekDayPostMap = new	HashMap<String,ArrayList<String>>();
	HashMap<String, ArrayList<String>> timeCommentsMap = new	HashMap<String,ArrayList<String>>();
	HashMap<String, ArrayList<String>> weekDayCommentsMap = new	HashMap<String,ArrayList<String>>();

	ParseComments parseComments;
	String path;
	String hourDestPath = "C://Users//adrianoc//Desktop//Reddit Hackathon//hourDestPath//"; 
	String dayDestPath = "C://Users//adrianoc//Desktop//Reddit Hackathon//dayDestPath//"; 

	public ClusterCommentsPerDayTime(){
		this.parseComments = new ParseComments();
		this.path = parseComments.path;
	}

	public void buildFileNameMap(){

		File file = new File("C://Users//adrianoc//Desktop//Reddit Hackathon//timebaseData.csv");
		/*  FileInputStream fis = null;
	    BufferedInputStream bis = null;
	    DataInputStream dis = null;

	    try {
	      fis = new FileInputStream(file);

	      // Here BufferedInputStream is added for fast reading.
	      bis = new BufferedInputStream(fis);
	      dis = new DataInputStream(bis);

	      // dis.available() returns 0 if the file does not have more lines.
	      while (dis.available() != 0) {

	      // this statement reads the line from the file and print it to
	        // the console.
	        System.out.println(dis.readreadLine());
	      }

	      // dispose all the resources after using them.
	      fis.close();
	      bis.close();
	      dis.close();

	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }			
		 */

	}

	public List<String> readCSVToList(){

		Path path = FileSystems.getDefault().getPath("C://Users//adrianoc//Desktop//Reddit Hackathon//", "timebasedData.csv");
		try{
			Files.newInputStream(path, StandardOpenOption.READ);
			return Files.readAllLines(path, Charset.defaultCharset());
		}
		catch(Exception e){
			System.out.println(e.toString());
			return null;
		}

	}

	public void process(String line){
		StringTokenizer tokenizer = new StringTokenizer(line,",");
		while(tokenizer.hasMoreElements()){
			String index = tokenizer.nextToken(); //index
			String redditID = tokenizer.nextToken(); //index
			String localTime = tokenizer.nextToken(); //time
			String dayWeek = tokenizer.nextToken(); //day week
			String hourDay = tokenizer.nextToken(); //hourDay
			String score = tokenizer.nextToken(); //score

			ArrayList<String> postList1 = timePostMap.get(hourDay);
			if(postList1==null)
				postList1 = new ArrayList<String>();
			postList1.add(redditID);
			timePostMap.put(hourDay, postList1);

			ArrayList<String> postList2 = this.weekDayPostMap.get(dayWeek);
			if(postList2==null)
				postList2 = new ArrayList<String>();
			postList2.add(redditID);
			weekDayPostMap.put(dayWeek, postList2);
		}
	}


	public void aggregateCommentsHourDayFiles(){
		Iterator<String> iter = this.timePostMap.keySet().iterator();

		while(iter.hasNext()){ //For each hour day
			String hourDay = iter.next();
			//System.out.print("Time:"+hourDay);
			ArrayList<String> redditList = this.timePostMap.get(hourDay);
			ArrayList<String> hourCommentList = this.timeCommentsMap.get(hourDay);
			if(hourCommentList == null) hourCommentList = new ArrayList<String> (); 
			for(String reddit: redditList){ //for each reddit post

				File file = new File(this.path+reddit+".json");
				if(file.exists()){
					ArrayList<String> list = this.parseComments.commentReader(reddit+".json");
					hourCommentList.addAll(list);
				}
			}
			this.timeCommentsMap.put(hourDay, hourCommentList);
			//System.out.println(", number of Comments:"+hourCommentList.size());
		}
	}


	public void printHourDayFiles(){
		Iterator<String> iter = this.timePostMap.keySet().iterator();

		while(iter.hasNext()){ //For each hour day
			String hourDay = iter.next();
			System.out.print("Time:"+hourDay);
			ArrayList<String> hourCommentList = this.timeCommentsMap.get(hourDay);
			this.parseComments.writeStringToFile(hourCommentList, this.hourDestPath+hourDay+".txt");
			System.out.println("Printed: "+  this.hourDestPath+hourDay+".txt");
		}
	}


	public void aggregateCommentsWeekDayFiles(){
		Iterator<String> iter = this.weekDayPostMap.keySet().iterator();

		while(iter.hasNext()){ //For each hour day
			String weekDay = iter.next();
			System.out.print("Day:"+weekDay);
			ArrayList<String> redditList = this.weekDayPostMap.get(weekDay);
			ArrayList<String> dayCommentList = this.weekDayCommentsMap.get(weekDay);
			if(dayCommentList == null) dayCommentList = new ArrayList<String> (); 
			for(String reddit: redditList){ //for each reddit post

				File file = new File(this.path+reddit+".json");
				if(file.exists()){
					ArrayList<String> list = this.parseComments.commentReader(reddit+".json");
					dayCommentList.addAll(list);
				}
			}
			this.weekDayPostMap.put(weekDay, dayCommentList);
			System.out.println(", number of Comments:"+dayCommentList.size());
		}
	}

	public void printWeekDayFiles(){
		Iterator<String> iter = this.weekDayPostMap.keySet().iterator();

		while(iter.hasNext()){ //For each hour day
			String weekDay = iter.next();
			
			ArrayList<String> dayCommentList = this.weekDayPostMap.get(weekDay);
			this.parseComments.writeStringToFile(dayCommentList, this.dayDestPath+weekDay+".txt");
			System.out.println("Printed: "+  this.dayDestPath+weekDay+".txt");
		}
	}
	
	
	public static void main(String args[]){
		ClusterCommentsPerDayTime cluster= new ClusterCommentsPerDayTime();
		List<String> list = cluster.readCSVToList();

		for(String line : list){
			cluster.process(line);
		}

		//cluster.aggregateCommentsHourDayFiles();
		//cluster.printHourDayFiles();
		cluster.aggregateCommentsWeekDayFiles();
		cluster.printWeekDayFiles();
	}
}
