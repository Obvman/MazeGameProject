import java.util.LinkedList;
import java.util.Random;

/**
 * Implementation of MazeGenerator interface that generates a maze using DFS methods
 */
public class MazeGeneratorDFS implements MazeGenerator {

	@Override
	public int[][] generateMaze(int height, int width) {
		// initialize empty maze
		int[][] maze = new int[height][width];
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				maze[row][col] = Maze.PATH_TILE;
			}
		}

		// start algorithm from center tile
		int midX = height/2;
		if ((midX % 2) == 1){
			midX = midX +1;
		}
		int midY = width/2;
		if ((midY % 2) == 1){
			midY = midY +1;
		}
		Node currNode = new Node(midX, midY);
		
		// insert preliminary walls into the maze
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

		LinkedList<Node> visited = new LinkedList<Node>();
		LinkedList<Node> curr = new LinkedList<Node>();
		LinkedList<Node> neighbours;

		// loop until a maze has been fully generated
		while(visited.size() < ((height/2 + 1) * (width/2 + 1))){

			// visit the unvisited neighbors of the current Node
			while(getUnvisited(currNode, visited, height, width).size() > 0){
				if(!visited.contains(currNode)){
					visited.push(currNode);
				}
				curr.push(currNode);
				neighbours = getUnvisited(currNode, visited, height, width);
				Node neighbour = neighbours.get(new Random().nextInt(neighbours.size()));
				
				// convert the wall tile between neighbour and current node into a path tile
				maze[(neighbour.getX()+currNode.getX()) / 2][(neighbour.getY()+currNode.getY()) / 2] = Maze.PATH_TILE;
				
				currNode = neighbour;
			}

			// if current Node's neighbors have all been visited and a dead end is reached,
			// backtrack to a Node that has unvisited nodes
			while (getUnvisited(currNode, visited, height, width).size() == 0) {
				if(!visited.contains(currNode)){
					visited.push(currNode);
				}

				// end the algorithm if all nodes are in the visited list
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
	 * Returns a LinkedList of Nodes representing the unvisited neighbors of a given Node
	 * @param n the Node whose unvisited neighbors will be returned
	 * @param visited LinkedList containing all of the visited Nodes in the maze
	 * @param height the height of the maze in number of cells
	 * @param width the width of the maze in number of cells
	 * @return LinkedList of Nodes representing the unvisited neighbors of the given Node
	 */
	private LinkedList<Node> getUnvisited(Node n, LinkedList<Node> visited, int height, int width){
		LinkedList<Node> returnValue = new LinkedList<Node>();
		LinkedList<Integer> xTodo = new LinkedList<Integer>();
		LinkedList<Integer> yTodo = new LinkedList<Integer>();
		LinkedList<Integer> tmp = new LinkedList<Integer>();

		if(n.getX() != 0){
			xTodo.add(n.getX()-2);
			tmp.add(0);
		}

		if(n.getX() + 2 < height){
			xTodo.add(n.getX()+2);
			tmp.add(0);
		}

		int i = 0;
		for(Node visit:visited){
			if(visit.getY() == n.getY()){
				while(i<xTodo.size()){
					if (visit.getX() == xTodo.get(i)){
						tmp.set(i, 1);
					}
					i++;
				}
				i=0;
			}
		}
		
		while(i<tmp.size()){
			if(tmp.get(i) == 0){
				returnValue.add(new Node(xTodo.get(i), n.getY()));
			}
			i++;
		}
		
		i = 0;
		tmp.clear();
		
		if(n.getY() != 0){
			yTodo.add(n.getY()-2);
			tmp.add(0);
		}
		
		if(n.getY() + 2 < width) {
			yTodo.add(n.getY()+2);
			tmp.add(0);
		}
		
		for (Node visit:visited) {
			if (visit.getX() == n.getX()) {
				while (i < yTodo.size()) {
					if (visit.getY() == yTodo.get(i)) {
						tmp.set(i, 1);
					}
					i++;
				}
				i=0;
			}
		}
		
		while (i < tmp.size()) {
			if (tmp.get(i) == 0) {
				returnValue.add(new Node(n.getX(), yTodo.get(i)));
			}
			i++;
		}

		return returnValue;
	}

	/**
	 * Private class to represent a pair of coordinates in the maze
	 */
	private class Node {
		int x;
		int y;

		/**
		 * Constructs a Node with a given x and y coordinate
		 * @param x the x coordinate of the Node
		 * @param y the y coordinate of the Node
		 */
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * @return the x coordinate of the Node
		 */
		public int getX() {
			return this.x;
		}

		/**
		 * @return the y coordinate of the Node
		 */
		public int getY() {
			return this.y;
		}
	}
}