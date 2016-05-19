import java.util.LinkedList;
import java.util.Random;

/**
 * Generates a new maze using DFS
 * All the walls are inserted before the algorithm starts connecting
 * all the PATH_TILE
 */
public class MazeGenerateDfs implements MazeGenerationStrategy {

	public int[][] generateMaze(int mazeSize1, int mazeSize2, int[][] maze) {
		int midX = mazeSize1/2;
		if ((midX % 2) == 1){
			midX = midX +1;
		}
		int midY = mazeSize2/2;
		if ((midY % 2) == 1){
			midY = midY +1;
		}
		Node currNode = new Node(midX, midY);
		maze = insertWalls(mazeSize1, mazeSize2, maze);
		LinkedList<Node> visited = new LinkedList<Node>();
		LinkedList<Node> curr = new LinkedList<Node>();
		LinkedList<Node> neighbours;
		//while all of the necessary path tile has not been gone through yet, keep looping
		while(visited.size()<((mazeSize1/2+1)*(mazeSize2/2+1))){
			/*
			 * if currNode has unvisited neighbours, randomly choose
			 * 1 to visit until currNode has 0 unvisited neightbours
			 */
			while(getUnvisited(currNode, visited, mazeSize1, mazeSize2).size() > 0){
					if(!visited.contains(currNode)){
						visited.push(currNode);
					}
					curr.push(currNode);
					neighbours = getUnvisited(currNode, visited, mazeSize1, mazeSize2);
					Node neighbour = neighbours.get(new Random().nextInt(neighbours.size()));
					maze = breakWall(neighbour, currNode, maze);
					currNode = neighbour;
			}
			/*
			 * if currNode has all of it's neighbours visited,
			 * dead end reached, retrack back to a node that has
			 * unvisited nodes
			 */
			
			while (getUnvisited(currNode, visited, mazeSize1, mazeSize2).size() == 0) {
				if(!visited.contains(currNode)){
					visited.push(currNode);
				}
				//end the algo if all nodes are in the visit list
				if (visited.size() == ((mazeSize1 / 2 + 1) * (mazeSize2 / 2 + 1))) {
					break;
				}
				currNode = curr.pop();
			}
		}
		return maze;
	}
	
	/**
	 * returns a list of unvisited neighbours of a certain node
	 * @param n
	 * @param visited
	 * @return
	 */
	private LinkedList<Node> getUnvisited(Node n, LinkedList<Node> visited,
			int mazeSize1, int mazeSize2){
		LinkedList<Node> returnValue = new LinkedList<Node>();
		LinkedList<Integer> xTodo = new LinkedList<Integer>();
		LinkedList<Integer> yTodo = new LinkedList<Integer>();
		LinkedList<Integer> tmp = new LinkedList<Integer>();
		if(n.getx() != 0){
			xTodo.add(n.getx()-2);
			tmp.add(0);
		}
		if(n.getx() + 2 < mazeSize1){
			xTodo.add(n.getx()+2);
			tmp.add(0);
		}
		int i = 0;
		for(Node visit:visited){
			if(visit.gety() == n.gety()){
				while(i<xTodo.size()){
					if (visit.getx() == xTodo.get(i)){
						tmp.set(i, 1);
					}
					i++;
				}
				i=0;
			}
		}
		while(i<tmp.size()){
			if(tmp.get(i) == 0){
				returnValue.add(new Node(xTodo.get(i), n.gety()));
			}
			i++;
		}
		i = 0;
		tmp.clear();
		if(n.gety() != 0){
			yTodo.add(n.gety()-2);
			tmp.add(0);
		}
		if(n.gety() + 2 < mazeSize2) {
			yTodo.add(n.gety()+2);
			tmp.add(0);
		}
		for (Node visit:visited) {
			if (visit.getx() == n.getx()) {
				while (i < yTodo.size()) {
					if (visit.gety() == yTodo.get(i)) {
						tmp.set(i, 1);
					}
					i++;
				}
				i=0;
			}
		}
		while (i < tmp.size()) {
			if (tmp.get(i) == 0) {
				returnValue.add(new Node(n.getx(), yTodo.get(i)));
			}
			i++;
		}

		return returnValue;
	}


	/**
	 * insert walls before the algo
	 */
	private int[][] insertWalls(int mazeSize1, int mazeSize2, int[][] maze) {
		for (int i = 1; i < mazeSize1; i += 2) {
			for (int j = 0; j < mazeSize2; j++){
				maze[i][j] = Maze.WALL_TILE;
			}
		}
		for (int i = 1; i < mazeSize2; i += 2) {
			for (int j = 0; j < mazeSize1; j++){
				maze[j][i] = Maze.WALL_TILE;
			}
		}
		return maze;
	}
	
	/**
	 * break a wall into path between node 1 and node 2
	 * @param n1
	 * @param n2
	 */
	private int[][] breakWall(Node n1, Node n2, int[][] maze) {
		maze[(n1.getx()+n2.getx())/2][(n1.gety()+n2.gety())/2] = Maze.PATH_TILE;
		return maze;
	}

	

	/**
	 * ADT to store data for BFS
	 * @author Charlotte
	 *
	 */
	private class Node {
		int x;
		int y;
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int getx() {
			return this.x;
		}
		
		public int gety() {
			return this.y;
		}
		
	}
}
