import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
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
		
		dictionary = new Hashtable<Integer, ArrayList<String>>();
		readFile("./collins-scrabble-words.txt");
//		statistics();
		
//		System.out.println(dictionary.size());
		
		String[] words = searchForWord("PerfECt");
		
		for (int i = 0; i < words.length; i++)
			System.out.println(words[i]);
	}
	
	private void statistics() 
	{
		Enumeration<ArrayList<String>> enu = dictionary.elements();
		
		while (enu.hasMoreElements()) 
		{
			ArrayList<String> list = enu.nextElement();
			System.out.println("\nElements in this list:");
			System.out.println("Chain of: " + list.size() + " \n");
			for(int i = 0; i < list.size(); i++)
				System.out.println(list.get(i));
			
			System.out.println("\n--------------------------");
		}
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
						
						// getting and remembering the value of the word in the list
						ArrayList<String> dic_words = dictionary.get(value);
						
						if (dic_words == null) 
						{
							// creating a new ArrayList<String>
							ArrayList<String> words_list = new ArrayList<>();
							
							// adding the word into this ArrayList
							words_list.add(word);
							
							// putting the ArrayList into the dictionary hash table with numericValue of word as key 
							dictionary.put(value, words_list);
						}
						else 
						{
							dic_words.add(word);
							
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
	
	private String[] searchForWord(String search_word) 
	{
		search_word = search_word.toLowerCase();
		
		if (search_word.length() != 7) 
		{
			System.out.println("You should enter a word that has exactly 7 letters.");
			return null;
		}
		
		int value = getNumericValue(search_word);
		
		String[] words;
		
		ArrayList<String> words_list = dictionary.get(value);
		
		if (words_list == null) 
		{
			System.out.println("There is no word for you.");
			return null;
		}
		else 
		{
			int size = words_list.size();
			words = new String[size];
			
			for(int i = 0; i < size; i++)
				words[i] = words_list.get(i);
		}
		
		return words;
	}
}
