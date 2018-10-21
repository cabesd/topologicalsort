import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Graph {
	
   private ArrayList<Node> TaskList;
   private int min_levels;
   private boolean unique_sort;
   private boolean is_cycle;
   
   class Node {
      char letter;
      int indegree;
      int outdegree;
      int level;
      public ArrayList<Node> fathers;
      public ArrayList<Node> childrens;
      
      public Node(char c){
         this.letter = c;
         this.indegree = 0;
         this.outdegree = 0;
         this.level = 0;
         this.fathers = new ArrayList<Node>();
         this.childrens = new ArrayList<Node>();
      }
      
      public void reinit() {
    	  this.indegree = this.fathers.size();
      }
      
      public void addFather(Node n){
         if( !this.fathers.contains(n) ){
            this.fathers.add(n);
            this.indegree++;
         }
      }
      
      public void addChildren(Node n){
         if( !this.childrens.contains(n) ){
            this.childrens.add(n);
            this.outdegree++;
         }
      }
      
      public char getLetter(){
         return this.letter;
      }
   }

   public Graph(){
	  this.min_levels = 0;
	  this.unique_sort = true; // will identify this while processing
	  this.is_cycle = false;
      this.TaskList = new ArrayList<Node>();
   }
   
   private Node findNode(char input){
      for (Node item : this.TaskList) {
          if (item.getLetter() == (input)) {
              return item;
          }
      }
      return null;
   }

   public void addNode(char n){
      if( this.findNode(n) != null ) return; // avoids duplicates
      Node new_node = new Node(n);
      this.TaskList.add(new_node);
   }
   
   public void addDep(char father, char children){
      Node nFather = this.findNode(father);
      Node nChildren = this.findNode(children);
      nFather.addChildren(nChildren);
      nChildren.addFather(nFather);
   }
   
   public void TopologicalSort(){
	  for(Node n : this.TaskList) {
		  n.reinit();
	  }
	   
      Queue<Node> q = new LinkedList<>();
      // First look for one that has 0 indegrees
      for( Node n : this.TaskList ){
         if( n.indegree == 0 ){
            q.add(n);
         }
      }
      
      if( q.size() == 0 ) {
    	  // There is a cyclic
    	  this.is_cycle = true;
      }
      else if( q.size() > 1 ) {
    	  this.unique_sort = false;
      }
      
      int counter = 0;
      ArrayList<Node> SortedArray = new ArrayList<Node>();
      while( q.size() > 0 ){ // while its not empty
         Node graph_head = q.remove();
         SortedArray.add( graph_head );
         counter++;
         // Get childrens for the q.removed
         boolean unique_flag = false;
         for( Node ch : graph_head.childrens ){
            ch.indegree--;
            if( ch.indegree == 0 ){
            	if( !unique_flag ) { unique_flag = true; } else { this.unique_sort = false; }
            	ch.level = counter;
            	q.add(ch);
            }
         }
      }
      
      this.ShowResults(SortedArray);
   }
   
   private void ShowResults(ArrayList<Node> SortedArray) {
	   if( this.is_cycle ) {
		   this.checkCycle();
		   return;
	   }
	   
	   String sortedOutput = "";
	   for( Node n : SortedArray ){
		   sortedOutput += n.getLetter() + " ";
	   }
	   System.out.println(this.outputSummary());
	   System.out.println("A valid ordering of tasks is as follows:");
	   System.out.println( sortedOutput );
	   System.out.println("This is "+ (this.unique_sort ? "the ONLY" : "NOT the only") +" valid ordering of tasks.");
   }
   
   // For debugging
   public String outputSummary(){
      String out = "";
      for (Node n1 : this.TaskList) {
         out += "'" + n1.getLetter() + "' depends on "+ n1.indegree +" element: ";
         for(Node n2 : n1.fathers){
            out += n2.getLetter() + " ";
         }
         out += " - Level "+ n1.level +"\n";
      }
      return out;
   }
   
   private void checkCycle() {
	   ArrayList<Node[]> NodesWithCycles = new ArrayList<Node[]>();
	   for(Node task : this.TaskList) {
		   for(Node n : task.childrens) {
			   Node[] temp = {task, n};
			   if( !NodesWithCycles.contains(temp) && n.childrens.contains(task) ) {
				   NodesWithCycles.add(temp);
			   }
		   }
	   }
	   if( NodesWithCycles.size() > 0 ) {
		   System.out.println("There is NO valid ordering of tasks.");
		   System.out.println("There is a cycle: ");
		   for(Node[] n : NodesWithCycles) {
			   System.out.println(n[0].getLetter() +" ==> "+ n[1].getLetter());
		   }
	   }
   }

}