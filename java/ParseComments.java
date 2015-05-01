import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Just read comments from th CSV to a datastructures which will be used to filter and cluter data.
 * 
 * @author Christian M. Adriano
 *
 */
public class ParseComments {


	File[] listOfFiles;
	String path = "C://Users//adrianoc//Desktop//Reddit Hackathon//json_data//";
	String destPath = "C://Users//adrianoc//Desktop//Reddit Hackathon//dest//";

	HashMap<String, ArrayList<String>> postCommentList = new HashMap<String, ArrayList<String>>();
	HashMap<String,String> fileNameMap = new HashMap<String,String>();
	
	public void readAllFileNames(){
		File folder = new File(path);
		listOfFiles = folder.listFiles();	
	}

	public void fileGenerators(){

		String fileName;

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				fileName = listOfFiles[i].getName();
				String destName = fileName.substring(0,fileName.indexOf(".")) + ".txt";
				System.out.println("orig: "+fileName+", dest: "+destName);
				fileNameMap.put(fileName, destName);
				ArrayList<String> commentList = commentReader(fileName);
				//System.out.println("Comment = "+comment);
				postCommentList.put(fileName, commentList);
			}
		}
	}

	public  String readFileToString(String filePath)  {
		try{

			//System.out.print("Reading file '" + filePath + "'");
			StringBuilder fileData = new StringBuilder(100000);
			BufferedReader reader = new BufferedReader(new FileReader(filePath));

			char[] buf = new char[10];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				//				System.out.println(numRead);
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
			reader.close();
			return  fileData.toString();
		}
		catch(IOException exception){
			exception.printStackTrace();
			return null;
		}
	}

	public void writeStringToFile(ArrayList<String> contentList, String filePath){
		try{
			System.out.println("  ----  Writing file '" + filePath + "'");
			FileWriter writer = new FileWriter(new File(filePath));
			for(String content: contentList){
				writer.write(content);
				writer.write(System.getProperty("line.separator"));
			}
			writer.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}

	public ArrayList<String> commentReader(String sourceFileName){

		ArrayList<String> commentList = new ArrayList<String>();

	
		
		String content = this.readFileToString(path+sourceFileName);
		
	//	System.out.println("CommentReader: "+path+sourceFileName);
		
		int start = content.indexOf("\"text\": \"");  

		while (start>0){
			int end = content.indexOf("\"", start+9); 

			String singleComment= content.substring(start+9, end);
			commentList.add(singleComment);

			//next comment
			start = content.indexOf("\"text\": \"", start+9);  
			
		}
	return commentList;

		//extract the comment
		//write in a new file

	}

	
	public static void main(String args[]){
		ParseComments parseComments = new ParseComments();

		parseComments.readAllFileNames();
		parseComments.fileGenerators();
		
	
		//this.writeStringToFile(commentList,destPath+ destName);

		//	ArrayList<String> commentList = parseComments.commentReader("6ttui.json","6ttui.txt");

	}
}
