import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
	
	static Scanner reader;
	static ArrayList<String> txtLines;
	static Graph g;

	public static void main(String[] args) {
		init();
		g.TopologicalSort();
		
		reader = new Scanner(System.in);
		String input;
		do {
			System.out.println("\nSelect: (a) add tasks (b) add dependencies (c) quit");
			input = reader.next();
			switch(input.charAt(0)) {
			case 'a':
				addTasks();
				break;
			case 'b':
				addDependencies();
				break;
			}
			g.TopologicalSort();
		}while( input.charAt(0) != 'c' );
		reader.close();
		System.out.println("Bye");
	}
	
	public static void init() {
		g = new Graph();
		// createExample();
		readInputFile("src/taskData.txt");
		formatInputFile();
		System.out.println("Welcome to my Task Scheduling Project:\n");
	}
	
	public static void createExample() {
		g.addNode('a');
		g.addNode('b');
		g.addNode('c');
		g.addNode('d');
		g.addNode('e');
	      
		g.addDep('a', 'b');
		g.addDep('a', 'c');
		g.addDep('b', 'd');
		g.addDep('d', 'e');
		g.addDep('e', 'c');
		//g.addDep('c', 'a'); // uncomment for cycle error
	      
		System.out.println( g.outputSummary() );
		g.TopologicalSort(); 
	}
	
	public static void readInputFile(String fileName){
		txtLines = new ArrayList<String>();
		try{
			Scanner sc = new Scanner(new File(fileName));
			while(sc.hasNextLine()){
				String line = sc.nextLine();
				txtLines.add(line);
			}
			sc.close();
		}
		catch(IOException ioe){
			System.out.println("An I/O exception error has occurred: "+ ioe);
		}
	}

	public static void formatInputFile() {
		if( txtLines.size() > 0 ) {
			readTasks(txtLines.get(0), false);
		}
		if( txtLines.size() >= 1 ) {	
			readDependencies(txtLines.get(1));
		}
	}
	
	public static void addTasks() {
		Scanner reader = new Scanner(System.in);
		System.out.println("What tasks do you want to add?");
		String str = reader.nextLine();
		readTasks(str, true);
	}
	
	public static void addDependencies() {
		Scanner reader = new Scanner(System.in);
		System.out.println("What dependencies do you want to add?");
		String str = reader.nextLine();
		readDependencies(str);
	}
	
	public static void readTasks(String str, boolean askDeps) {
		String[] tasks = str.split(" ");
		for(String t : tasks) {
			char new_task = t.charAt(0);
			g.addNode(new_task);
			if( askDeps ) {
				System.out.println("What dependencies does "+ new_task +" have?");
				Scanner reader = new Scanner(System.in);
				String[] fathers = reader.nextLine().split(" ");
				for(String fa : fathers) {
					//String[] sides = d.split(",");
					g.addDep(fa.charAt(0), new_task);
				}
				System.out.println("What depends on " + new_task + "?");
				reader = new Scanner(System.in);
				String[] childs = reader.nextLine().split(" ");
				for(String ch : childs) {
					//String[] sides = d.split(",");
					g.addDep(new_task, ch.charAt(0));
				}
			}
		}
		if(askDeps) {
			System.out.println("Do you want to add additional dependencies? [Y/N]");
			reader = new Scanner(System.in);
			String input = reader.next();
			switch(input.charAt(0)) {
			case 'y':
			case 'Y':
				addDependencies();
				break;
			case 'n':
			case 'N':
				break;
			}
		}
	}
	
	public static void readDependencies(String str) {
		String[] deps = str.split(" ");
		for(String d : deps) {
			String[] sides = d.split(",");
			g.addDep(sides[0].charAt(1), sides[1].charAt(0));
		}
	}
}
