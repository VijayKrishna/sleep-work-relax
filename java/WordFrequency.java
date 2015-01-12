import java.io.File;
import java.io.FileWriter;
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
 * Computes the frequency of words found in topic posts.
 * Also computes the average comments and the score.
 * 
 * @author Christian M. Adriano
 * 
 */
public class WordFrequency {

	public class ScoreComments{

		public ScoreComments(int score, int comments, int postOcurrences){
			this.score=score;
			this.comments=comments;
			this.postOccurrences = postOcurrences;
		}

		int score;
		int comments;
		int postOccurrences;
	}

	String path = "C://Users//adrianoc//Desktop//Reddit Hackathon//";

	HashMap<String,ScoreComments> wordMap = new HashMap<String,ScoreComments>();

	public List<String> readCSVToList(){

		Path path = FileSystems.getDefault().getPath("C://Users//adrianoc//Desktop//Reddit Hackathon//", "OutputTokenized.txt");
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
		int wordCount = tokenizer.countTokens();
		//System.out.println(wordCount+":"+line);
		ArrayList<String> wordList = new ArrayList<String>();
		if(wordCount>0){
			for(int i=1; i<wordCount-1;i++){
				String word = tokenizer.nextToken().trim();
				//System.out.println(word);
				if(word.length()>2){
					word = word.substring(1,word.length()-1);	
					wordList.add(word);
					ScoreComments point = this.wordMap.get(word);
					if(point==null)
						point = new ScoreComments(0,0,0);
					this.wordMap.put(word, point);
				}
			}

			String scoreStr = tokenizer.nextToken();
			Integer score = new Integer(scoreStr.trim());
			String commentsStr = tokenizer.nextToken();
			Integer comments = new Integer(commentsStr.trim());

			for(String word: wordList){
				ScoreComments point = this.wordMap.get(word);
				point.comments = point.comments+comments;
				point.score = point.score + score;
				point.postOccurrences = point.postOccurrences + 1;
				this.wordMap.put(word, point);
			}
		}

	}

	

	

	public void printDatPoints(){
		ArrayList<String> wordDataList =  new ArrayList<String>();
		Iterator<String> iter = this.wordMap.keySet().iterator();
		String wordData = "word,average score, average comments, frequency";
		wordDataList.add(wordData);
		//System.out.println("word,average score, average comments, frequency");
		
		while(iter.hasNext()){
			String word = iter.next();
			ScoreComments point = this.wordMap.get(word);

			int averageScore = point.score/point.postOccurrences;
			int averageComments = point.comments/point.postOccurrences;
			wordData = word+","+averageScore+","+averageComments+","+ point.postOccurrences;
			wordDataList.add(wordData);
			
			//System.out.println(word+","+averageScore+","+averageComments+","+ point.postOccurrences);
			//System.out.println("Word: "+word+"score: "+point.score+", comments: "+point.comments+", post occurrences:"+ point.postOccurrences);
		}
		this.writeStringToFile(wordDataList, path+"wordFrequency.txt");

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
	
	public static void main(String args[]){
		WordFrequency frequency = new WordFrequency();
		List<String> list = frequency.readCSVToList();
		for(String line: list){
			frequency.process(line);
		}
		frequency.printDatPoints();
	}

	
}
