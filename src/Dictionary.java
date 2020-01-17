import java.io.CharConversionException;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.*;

public class Dictionary {

	public Hashtable<String, LinkedList<String>> dictionary;
	public HashMap<Integer, ArrayList<String>> dictionary_map;
//	private File words_file;
	private Scanner scanner;
	private int maxSize = 68687;

	public static void main(String[] args) {
		long startTime = System.nanoTime();

		new Dictionary();

		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		System.out.println(duration);
		
	}

	public Dictionary() {
//		readFile("./word-list-master/common-7-letter-words.txt");
//		readFile("/Users/kenneth/eclipse-workspace/lab11-scrabble-basic/word-lists-master/common-7-letter-words.txt");

		dictionary = new Hashtable<String, LinkedList<String>>();
		dictionary_map = new HashMap<Integer, ArrayList<String>>();
		readFile("./collins-scrabble-words.txt");
//		fillMap();
		fillMapVer2();
		statistics();
//		statisticsVer2();
//		System.out.println(dictionary.size());

//		quicksortWord("Perfect");

//		String[] words = searchForWord("PerfECt");

//		for (int i = 0; i < words.length; i++)
//			System.out.println(words[i]);
	}

	private void statistics() {
		int largest = 1;
		int position = 0;
		int counter = 0;
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
			}
			counter++;
		}
		System.out.println("Largest chain: " + largest + " -> Key: " + position);
	}

	private void statisticsVer2() {
		System.out.println(dictionary_map.size());

		dictionary_map.keySet().iterator()
				.forEachRemaining(key -> System.out.println(key + "=" + dictionary_map.get(key)));

	}

	/**
	 * Method reading in the file
	 * 
	 * @param pathname String representing the path of the file
	 */
	private void readFile(String pathname) {
		try {
			scanner = new Scanner(new File(pathname), StandardCharsets.UTF_8.name());
			System.out.println("File reading successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fillMap() {
		int counter = 0;
		String regex_7_letters = "\\b[a-z]{7}\\b";

		while (scanner.hasNext()) {
			String word = scanner.next();
			word = word.toLowerCase();

			if (word.matches(regex_7_letters)) {
				String value = quicksortWord(word);
				
				// getting and remembering the value of the word in the list
				LinkedList<String> dic_words = dictionary.get(value);

				if (dic_words == null) {
					// creating a new ArrayList<String>
					LinkedList<String> words_list = new LinkedList<>();

					// adding the word into this ArrayList
					words_list.add(word);

					// putting the ArrayList into the dictionary hash table with numericValue of
					// word as key
					dictionary.put(value, words_list);
				} else {
					dic_words.add(word);
					dictionary.put(value, dic_words);
				}
				counter++;
			}
		}

		System.out.println(counter);
	}

	private void fillMapVer2() {
		int counter = 0;

		while (scanner.hasNext()) {
			String word = scanner.next();
			word = word.toLowerCase();

			if (validString(word)) {
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
					}
				}
				counter++;
			}
		}
		System.out.println(counter);
	}
	

	private int getNumericValue(String word) {
		int value = 0;

		for (int i = 0; i < word.length(); i++) {
			value += (Character.getNumericValue(word.charAt(i)));
		}

		return value;
	}

	/**
	 * K(m) = k % m 
	 * m = Anzahl der Wörter (34343 * 2) -> 68686 -> nächst größere
	 * Primzahl -> m = 68687
	 */
	private int getNumericValueVer2(String word) {
		int value = 0;
		int length = word.length();

		for (int i = 0, j = length - 1; i < length; i++, j--) {
			value = (int) (value + ((Character.getNumericValue(word.charAt(i)) - 97) * Math.pow(26, j)));
		}

		return value * 100 % 68687;
	}

	private boolean validString(String str) {
		return ((!str.equals("")) && (str != null) && (str.matches("^[a-z]*$"))) && (str.length() == 7);
	}

	private String[] searchForWord(String search_word) {
		search_word = search_word.toLowerCase();

		if (search_word.length() != 7) {
			System.out.println("You should enter a word that has exactly 7 letters.");
			return null;
		}

		int value = getNumericValue(search_word);

		String[] words;

		LinkedList<String> words_list = dictionary.get(value);

		if (words_list == null) {
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

	private String quicksortWord(String word) {
		int length = word.length();
		word = word.toLowerCase();
		int[] characters = new int[length];
		System.out.println("Word: " + word);

		for (int i = 0; i < length; i++)
			characters[i] = word.charAt(i);

		System.out.println("Non ordered");
		for (int i = 0; i < characters.length; i++)
			System.out.println(characters[i]);

		quicksort(characters, 0, word.length()-1);

		String new_word = "";
		System.out.println("Ordered");
		for (int i = 0; i < characters.length; i++) {
			System.out.println(characters[i]);
			new_word += (char) (characters[i]);
		}

		System.out.println("New Word:" + new_word);

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
