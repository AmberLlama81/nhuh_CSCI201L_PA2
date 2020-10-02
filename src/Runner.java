import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import com.google.gson.Gson;

public class Runner {
	private Dictionary<String> codesDic = null;
	String path;
	public Runner(String input) throws FileNotFoundException {
		path = input;
		codesDic = new Dictionary<String>();
		Gson gson = new Gson();
		BufferedReader br = new BufferedReader(new FileReader(path));
		Codes[] tmpCodes = null;
		tmpCodes = gson.fromJson(br, Codes[].class);
		for (Codes c: tmpCodes) {
			List<String> stringList = c.getCodes();
			for (String s: stringList) {
				codesDic.add(s);
			}
		}
	}

	public int freqCode(String code) {
		return codesDic.frequency(code);
	}
	
	public boolean checkCode(String code) {
		return codesDic.contains(code);
	}
	
	public void removeCode(String code) {
		while (codesDic.contains(code)) {
			codesDic.remove(code);
		}
	}
	
	public static Runner getRunner() throws IOException {
		System.out.println("Enter an input file");
		
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		
		Runner tmpRunner = new Runner(input);
		
		return tmpRunner;
	}
	
	public static void main(String[] args) {
		boolean invalid = true;
		boolean quit = false;
		Runner userRunner = null;
		
		while (invalid) {
			try {
				userRunner = getRunner();
				invalid = false;
			} catch (IOException e) {
				System.out.println("File not found, enter a new input file");
			} catch (com.google.gson.JsonSyntaxException e) {
				System.out.println("Data in file unreadable");
			}
		}
		
		while (!quit) {
			System.out.println("What would you like to do with your database of codes?\n"
					+ "	1) Get frequency of a code\n"
					+ "	2) Check if a code was guessed\n"
					+ "	3) Remove a code\n"
					+ "	4) quit");
			
			Scanner scanner = new Scanner(System.in);
			int menuInput = scanner.nextInt();
			
			if (menuInput == 1) {
				System.out.println("Enter a code to check its frequency");
				Scanner scanner1 = new Scanner(System.in);
				String userCode1 = scanner1.nextLine();
				int freq = 0;
				freq = userRunner.freqCode(userCode1);
				System.out.println(userCode1 + " was guessed by " + freq + " teammates");
			} else if (menuInput == 2) {
				System.out.println("Enter a code to check if it was guessed by a teammate");
				Scanner scanner2 = new Scanner(System.in);
				String userCode2 = scanner2.nextLine();
				Boolean guessed = false;
				guessed = userRunner.checkCode(userCode2);
				if (guessed) {
					System.out.println(userCode2 + " was guessed by a teammate");
				} else {
					System.out.println(userCode2 + " was not guessed by a teammate");
				}
			} else if (menuInput == 3) {
				System.out.println("Enter a code to remove");
				Scanner scanner3 = new Scanner(System.in);
				String userCode3 = scanner3.nextLine();
				userRunner.removeCode(userCode3);
				System.out.println(userCode3 + " was removed from your database");
			} else if (menuInput == 4) {
				System.out.println("Quitting, have a nice day!");
				quit = true;
			}
		}	
	}
}
