import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;

public class Dictionary {

	private Hashtable<String, LinkedList<String>> dictionary;
//	private File words_file;
	private Scanner scanner;
	private String regex_7_letters = "\\b[a-z]{7}\\b";

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		new Dictionary();
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		System.out.println("\nDuration: " + duration + " nano-seconds");
		
	}

	public Dictionary() {
		dictionary = new Hashtable<String, LinkedList<String>>();
		readFile("./collins-scrabble-words.txt");
		fillMap();	
//		search("LACKERS");
		search("eatings");
	}

	private void statistics(int c, int d, int col) {
		int largest = 1;
		int position = 0;
		int counter = 0;
		int words = c;
		int duplicates = d;
		int collisions = col;
		String element = "";
		Enumeration<LinkedList<String>> enu = dictionary.elements();

		while (enu.hasMoreElements()) {
			LinkedList<String> list = enu.nextElement();
			/*System.out.println("\nElements in this list:");
			System.out.println("Chain of: " + list.size());
			for (int i = 0; i < list.size(); i++)
				System.out.println(list.get(i));

			System.out.println("\n--------------------------");*/
			if(Integer.compare(largest, list.size())<0) {
				largest = list.size();	
				position = counter;
				element = list.getFirst();
			}
			counter++;
		}
		System.out.println("Words in the dictionary: " + words);
		System.out.println("\nDuplicates found: " + duplicates);
		System.out.println("\nSize of the Hashtable: " + counter);
		System.out.println("\nCollisions appeared: " + collisions);
		System.out.println("\nLargest chain: " + largest + " -> Key: " + position + " / " + element + "\n");
		System.out.println("<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>\n");
	}

	/**
	 * Method reading in the file
	 * 
	 * @param pathname String representing the path of the file
	 */
	private void readFile(String pathname) {
		try {
			scanner = new Scanner(new File(pathname), StandardCharsets.UTF_8.name());
			System.out.println("------------------------------");
			System.out.println("| File reading successfully. |");
			System.out.println("------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fillMap() {
		int counter = 0;
		int duplicates = 0;
		int collisions = 0;

		while (scanner.hasNext()) {
			String word = scanner.next();
			word = word.toLowerCase();

			if (word.matches(regex_7_letters)) {
				String key = quicksortWord(word);	

				// getting and remembering the value of the word in the list
				LinkedList<String> dic_words = dictionary.get(key);

				if (dic_words == null) {
					// creating a new ArrayList<String>
					LinkedList<String> words_list = new LinkedList<>();

					// adding the word into this ArrayList
					words_list.addFirst(word);

					// putting the ArrayList into the dictionary hash table with numericValue of
					// word as key
					dictionary.put(key, words_list);
				} else {
					if (!dic_words.contains(word)) {
						dic_words.addFirst(word);
						dictionary.put(key, dic_words);
						collisions++;
					}else
						duplicates++;
				}
				counter++;
			}
		}
		statistics(counter, duplicates, collisions);
	}


	private void search(String search_word) 
	{
		String[] words = searchForWord(search_word);
		
		if (words != null)
			printWords(words);
	}
	
	private String[] searchForWord(String search_word) {
		search_word = search_word.toLowerCase();

		if (!search_word.matches(regex_7_letters)) {
			System.out.println("You should enter a word that has exactly 7 letters and does not contain a number, any special characters or a blank.");
			return null;
		}
		
		System.out.println("Searching Word: " + search_word + "\n");
		String new_word = quicksortWord(search_word);
		
		String[] words;
		LinkedList<String> words_list = dictionary.get(new_word);
		
		if (words_list == null) 
		{
			System.out.println("There is no word for you.");
			return null;
		} else {
			int size = words_list.size();
			words = new String[size];
			
			for (int i = 0; i < size; i++) 
				words[i] = words_list.get(i);
		}
		return words;
	}
	
	private void printWords(String[] words) 
	{
		System.out.println("Found Words: \n");
		for (int i = 0; i < words.length; i++)
			System.out.println(words[i]);
	}

	private String quicksortWord(String word) {
		int length = word.length();
		word = word.toLowerCase();
		int[] characters = new int[length];
//		System.out.println("Word: " + word);

		for (int i = 0; i < length; i++)
			characters[i] = word.charAt(i);

//		System.out.println("Non ordered");
//		for (int i = 0; i < characters.length; i++)
//			System.out.println(characters[i]);

		quicksort(characters, 0, word.length()-1);

		String new_word = "";
//		System.out.println("Ordered");
		for (int i = 0; i < characters.length; i++) {
//			System.out.println(characters[i]);
			new_word += (char) (characters[i]);
		}

//		System.out.println("New Word:" + new_word);

		return new_word;
	}

	/**
	 * @credits WW
	 * @credits https://moodle.htw-berlin.de/pluginfile.php/791655/mod_resource/content/1/QuickSorter.java
	 */
	private void exchange(int[] a, int i, int j) {
		int t = a[i];
		a[i] = a[j];
		a[j] = t;
	}

	/**
	 * @credits WW
	 * @credits https://moodle.htw-berlin.de/pluginfile.php/791655/mod_resource/content/1/QuickSorter.java
	 */
	private void quicksort(int[] a, int lo, int hi) {
		// partition
		int i = lo, j = hi;
		int x = a[(lo + hi) / 2];

		while (i <= j) {
			while (a[i] < x)
				i++;
			while (a[j] > x)
				j--;
			if (i <= j) {
				exchange(a, i, j);
				i++;
				j--;
			}
		}

		// recurr
		if (lo < j)
			quicksort(a, lo, j);
		if (i < hi)
			quicksort(a, i, hi);
	}
}
