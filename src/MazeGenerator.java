import java.util.LinkedList;
import java.util.Random;

import javax.swing.ImageIcon;

public class MazeGenerator implements MazeConstants {
	private int[][] maze;
	private int mazeSize1;
	private int mazeSize2;

	/**
	 * This method takes an integer mazeSize as input and 
	 * returns a generated maze.
	 * The input must be a power of 2-1(2^n -1) to do recursive 
	 * The input must be 2*n - 1 for the new generate method
	 * generate.
	 * @param mazeSize
	 * @return
	 */
	public int[][] generateMaze(int mazeSize1, int mazeSize2){
		this.mazeSize1 = mazeSize1;
		this.mazeSize2 = mazeSize2;
		if (this.maze == null) {
			this.maze = new int[mazeSize1][mazeSize2];

			for (int row = 0; row < mazeSize1; row++) {
				for (int col = 0; col < mazeSize2; col++) {
					this.maze[row][col] = PATH_TILE;
				}
			}
			//this is the old method that generate short catwalks
			//generateMazeRecursive(0, mazeSize1,0 , mazeSize2);
			newGenerateMaze();
		}

		// set start, end, and key tile
		maze[0][0] = START_TILE;
		maze[mazeSize1 - 1][mazeSize2 - 1] = END_TILE;
		
		return this.maze;
	}
	/**
	 * Generates a new maze using DFS
	 * All the walls are inserted before the algorithm starts connecting
	 * all the PATH_TILE
	 */
	private void newGenerateMaze(){
		Node currNode = new Node(0, 0);
		insertWalls();
		LinkedList<Node> visited = new LinkedList<Node>();
		LinkedList<Node> curr = new LinkedList<Node>();
		LinkedList<Node> neighbours;
		//while all of the necessary path tile has not been gone through yet, keep looping
		while(visited.size()<((mazeSize1/2+1)*(mazeSize2/2+1))){
			/*
			 * if currNode has unvisited neighbours, randomly choose
			 * 1 to visit until currNode has 0 unvisited neightbours
			 */
			while(getUnvisited(currNode, visited).size() > 0){
					if(!visited.contains(currNode)){
						visited.push(currNode);
					}
					curr.push(currNode);
					neighbours = getUnvisited(currNode, visited);
					Node neighbour = neighbours.get(new Random().nextInt(neighbours.size()));
					breakWall(neighbour, currNode);
					currNode = neighbour;
			}
			/*
			 * if currNode has all of it's neighbours visited,
			 * dead end reached, retrack back to a node that has
			 * unvisited nodes
			 */
			
			while (getUnvisited(currNode, visited).size() == 0){
				if(!visited.contains(currNode)){
					visited.push(currNode);
				}
				//end the algo if all nodes are in the visit list
				if(visited.size() == ((mazeSize1/2+1)*(mazeSize2/2+1))){
					break;
				}
				currNode = curr.pop();
			}
		}
	}
	
	/**
	 * returns a list of unvisited neighbours of a certain node
	 * @param n
	 * @param visited
	 * @return
	 */
	private LinkedList<Node> getUnvisited(Node n, LinkedList<Node> visited){
		LinkedList<Node> returnValue = new LinkedList<Node>();
		LinkedList<Integer> xTodo = new LinkedList<Integer>();
		LinkedList<Integer> yTodo = new LinkedList<Integer>();
		LinkedList<Integer> tmp = new LinkedList<Integer>();
		if(n.getx() != 0){
			xTodo.add(n.getx()-2);
			tmp.add(0);
		}
		if(n.getx()+2<this.mazeSize1){
			xTodo.add(n.getx()+2);
			tmp.add(0);
		}
		int i =0;
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
		i=0;
		tmp.clear();
		if(n.gety() != 0){
			yTodo.add(n.gety()-2);
			tmp.add(0);
		}
		if(n.gety()+2<this.mazeSize2){
			yTodo.add(n.gety()+2);
			tmp.add(0);
		}
		for(Node visit:visited){
			if(visit.getx() == n.getx()){
				while(i<yTodo.size()){
					if (visit.gety() == yTodo.get(i)){
						tmp.set(i, 1);
					}
					i++;
				}
				i=0;
			}
		}
		while(i<tmp.size()){
			if(tmp.get(i) == 0){
				returnValue.add(new Node(n.getx(),yTodo.get(i)));
			}
			i++;
		}

		return returnValue;
	}
	
	/**
	 * insert walls before the algo
	 */
	private void insertWalls(){
		for (int i = 1; i< this.mazeSize1; i = i+2){
			for (int j = 0; j<this.mazeSize2; j++){
				maze[i][j] = WALL_TILE;
			}
		}
		for (int i = 1; i< this.mazeSize2; i = i+2){
			for (int j = 0; j<this.mazeSize1; j++){
				maze[j][i] = WALL_TILE;
			}
		}
	}
	
	/**
	 * break a wall into path between node 1 and node 2
	 * @param n1
	 * @param n2
	 */
	private void breakWall(Node n1, Node n2){
		maze[(n1.getx()+n2.getx())/2][(n1.gety()+n2.gety())/2] = PATH_TILE;
	}

	/**
	 * A recursive maze generator
	 * @param xStart
	 * @param xEnd
	 * @param yStart
	 * @param yEnd
	 * @param mazeSize
	 */
	private void generateMazeRecursive(int xStart, int xEnd, int yStart, int yEnd){
		if(xEnd - xStart > 2) {
			//This will insert 4 walls in the middle of the matrix
			if(yEnd - yStart>2){
				for(int i = 0; i<yEnd-yStart; i++){
					this.maze[xStart + (xEnd-xStart)/2][i+yStart]= WALL_TILE;
				}
			}
			for(int i = 0; i<xEnd-xStart; i++){
				this.maze[i+xStart][yStart + (yEnd-yStart)/2]= WALL_TILE;
			}
			/* This will dig 3 holes on the 3 of the 4 walls we generated.
			 * 0 -> the top wall; 1 -> the left wall; 2 -> the bottom wall;
			 * 3 -> the right wall
			 */
			int ignore = new Random().nextInt(4);
			int newHole;
			for(int i = 0; i<4 ; i++){
				if (ignore == i){
					continue;
				}
				switch (i){
					case 0:
						//this if statement makes sure we won't dig a hole in where a wall might generate
						//since all walls will be generated in an even index (0, 2, 4...)
						if(yEnd - yStart > 3){
							newHole = new Random().nextInt((yEnd-yStart)/4)*2;
						}else{
							newHole = 0;
						}
						this.maze[xStart + (xEnd-xStart)/2][yStart + newHole] = PATH_TILE;
						break;
					case 1:
						if(xEnd - xStart > 3){
							newHole = new Random().nextInt((xEnd-xStart)/4)*2;
						}else{
							newHole = 0;
						}
						this.maze[xStart + newHole][yStart + (yEnd-yStart)/2] = PATH_TILE;
						break;
					case 2:
						if(yEnd - yStart > 3){
							newHole = new Random().nextInt((yEnd-yStart)/4)*2;
						}else{
							newHole = 0;
						}
						this.maze[xStart + (xEnd-xStart)/2][yStart + newHole+(yEnd-yStart)/2 +1] = PATH_TILE;
						break;
					case 3:
						if(xEnd - xStart > 3){
							newHole = new Random().nextInt((xEnd-xStart)/4)*2;
						}else{
							newHole = 0;
						}
						this.maze[newHole + (xEnd-xStart)/2 +1 + xStart][yStart + (yEnd-yStart)/2] = PATH_TILE;
						break;
				}
			}
			if((xEnd-xStart > 3) && (yEnd-yStart > 3)){
				generateMazeRecursive(xStart, xStart + (xEnd-xStart)/2, yStart, yStart + (yEnd-yStart)/2);
				generateMazeRecursive(xStart + (xEnd-xStart)/2+1, xEnd, yStart, yStart + (yEnd-yStart)/2);
				generateMazeRecursive(xStart + (xEnd-xStart)/2+1, xEnd, yStart + (yEnd-yStart)/2+1, yEnd);
				generateMazeRecursive(xStart, xStart + (xEnd-xStart)/2, yStart + (yEnd-yStart)/2+1, yEnd);
			}
		}
	}
	
	/**
	 * ADT to store data for BFS
	 * @author Charlotte
	 *
	 */
	private class Node{
		int x;
		int y;
		public Node(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		public int getx(){
			return this.x;
		}
		
		public int gety(){
			return this.y;
		}
		
	}
}
