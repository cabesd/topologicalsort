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
		System.out.println("Welcome to my Task Scheduling Project:\n");
		g.TopologicalSort();
		
		reader = new Scanner(System.in);
		char input;
		do {
			System.out.println("\nSelect: (a) add tasks (b) add dependencies (c) quit");
			input = reader.next().charAt(0);
			switch(input) {
			case 'a':
				addTasks();
				break;
			case 'b':
				addDependencies();
				break;
			}
			g.TopologicalSort();
		}while( input != 'c' );
	}
	
	public static void init() {
		g = new Graph();
		// createExample();
		readInputFile("src/taskData.txt");
		formatInputFile();
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
			readTasks(txtLines.get(0));
		}
		if( txtLines.size() >= 1 ) {	
			readDependencies(txtLines.get(1));
		}
	}
	
	public static void addTasks() {
		
	}
	
	public static void addDependencies() {
		System.out.println("What dependencies do you want to add?");
		String str = reader.next();
		readDependencies(str);
	}
	
	public static void readTasks(String str) {
		String[] tasks = str.split(" ");
		for(String t : tasks) {
			g.addNode(t.charAt(0));
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
