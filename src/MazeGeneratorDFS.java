import java.util.LinkedList;
import java.util.Random;

public class MazeGeneratorDFS implements MazeGenerator {
	
	/**
	 * Returns a maze with the given height and width.
	 * @param height
	 * @param width
	 * @return
	 */
	public int[][] generateMaze(int height, int width) {
		int[][] maze = new int[height][width];
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				maze[row][col] = Maze.PATH_TILE;
			}
		}
		
		int midX = height/2;
		if ((midX % 2) == 1){
			midX = midX +1;
		}
		int midY = width/2;
		if ((midY % 2) == 1){
			midY = midY +1;
		}
		Node currNode = new Node(midX, midY);
		maze = insertWalls(height, width, maze);
		LinkedList<Node> visited = new LinkedList<Node>();
		LinkedList<Node> curr = new LinkedList<Node>();
		LinkedList<Node> neighbours;
		//while all of the necessary path tile has not been gone through yet, keep looping
		while(visited.size()<((height/2+1)*(width/2+1))){
			/*
			 * if currNode has unvisited neighbours, randomly choose
			 * 1 to visit until currNode has 0 unvisited neightbours
			 */
			while(getUnvisited(currNode, visited, height, width).size() > 0){
					if(!visited.contains(currNode)){
						visited.push(currNode);
					}
					curr.push(currNode);
					neighbours = getUnvisited(currNode, visited, height, width);
					Node neighbour = neighbours.get(new Random().nextInt(neighbours.size()));
					maze = breakWall(neighbour, currNode, maze);
					currNode = neighbour;
			}
			/*
			 * if currNode has all of it's neighbours visited,
			 * dead end reached, retrack back to a node that has
			 * unvisited nodes
			 */
			
			while (getUnvisited(currNode, visited, height, width).size() == 0) {
				if(!visited.contains(currNode)){
					visited.push(currNode);
				}
				//end the algo if all nodes are in the visit list
				if (visited.size() == ((height / 2 + 1) * (width / 2 + 1))) {
					break;
				}
				currNode = curr.pop();
			}
		}
		
		maze[0][0] = Maze.START_TILE;
		maze[height - 1][width - 1] = Maze.END_TILE;
		return maze;
	}
	
	/**
	 * Returns a list of unvisited neighbors of a certain node
	 * @param n
	 * @param visited
	 * @return
	 */
	private LinkedList<Node> getUnvisited(Node n, LinkedList<Node> visited,
			int height, int width){
		LinkedList<Node> returnValue = new LinkedList<Node>();
		LinkedList<Integer> xTodo = new LinkedList<Integer>();
		LinkedList<Integer> yTodo = new LinkedList<Integer>();
		LinkedList<Integer> tmp = new LinkedList<Integer>();
		if(n.getx() != 0){
			xTodo.add(n.getx()-2);
			tmp.add(0);
		}
		if(n.getx() + 2 < height){
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
		if(n.gety() + 2 < width) {
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
	 * Insert walls before the algorithm.
	 * @param height
	 * @param width
	 * @param maze
	 * @return
	 */
	private int[][] insertWalls(int height, int width, int[][] maze) {
		for (int i = 1; i < height; i += 2) {
			for (int j = 0; j < width; j++){
				maze[i][j] = Maze.WALL_TILE;
			}
		}
		for (int i = 1; i < width; i += 2) {
			for (int j = 0; j < height; j++){
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
	 * ADT to store data for DFS
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
