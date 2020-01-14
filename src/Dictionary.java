import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class Dictionary {

	public Hashtable<Integer, ArrayList<String>> dictionary;
	private File words_file;
	
	public static void main(String[] args) 
	{
		new Dictionary();
	}
	
	public Dictionary() 
	{
//		readFile("./word-list-master/common-7-letter-words.txt");
//		readFile("/Users/kenneth/eclipse-workspace/lab11-scrabble-basic/word-lists-master/common-7-letter-words.txt");
		readFile("./collins-scrabble-words.txt");
	}

	/**
	 * Method reading in the file
	 * @param pathname String representing the path of the file
	 */
	private void readFile(String pathname) 
	{
		words_file = new File(pathname);
		int counter = 0;
		try (Scanner scanner = new Scanner(words_file, StandardCharsets.UTF_8.name())) 
		{
			String regex_7_letters = "\\b[a-z]{7}\\b";
			
				while (scanner.hasNext()) 
				{
					String word = scanner.next();
					word = word.toLowerCase();
					
					if(word.matches(regex_7_letters))
					{
						int value = getNumericValue(word);
						ArrayList<String> dic_words = dictionary.get(value);
						
						if (dic_words == null) 
						{
							ArrayList<String> words_list = new ArrayList<>();
							words_list.add(word);
						}
						else 
						{
							dictionary.get(value);
						}
						counter++;
					}
				}
			
			System.out.println(counter);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int getNumericValue(String word) 
	{
		int value = 0;
		
		for (int i = 0; i < word.length(); i++) 
		{
			value += Character.getNumericValue(word.charAt(i));
		}
		
		return value;
	}
}
