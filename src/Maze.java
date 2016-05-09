/**
 * Main GUI class that handles all panels/screens
 * 
 * COMP2911 Project - 16s1
 * @author Anna Azzam
 * @author Charlotte Han
 * @author Connor Coyne
 * @author Craig Feeney
 * @author Leon Nguyen
 * 
 */

import java.util.Random;

public class Maze {
	private boolean[][] maze;
	public Maze(){
		maze = null;
	}
	/**
	 * This method takes an integer mazeSize as input and 
	 * returns a generated maze.
	 * The input must be a power of 2-1(2^n -1) to do recursive 
	 * generate.
	 * @param mazeSize
	 * @return
	 */
	public boolean[][] getMaze(int mazeSize){
		if (this.maze == null){
			this.maze = new boolean[mazeSize][mazeSize];
			for (int row = 0; row < mazeSize; row++){
				for (int col = 0; col < mazeSize; col++){
					this.maze[row][col] = false;
				}
		}
			mazeGenerate(0, mazeSize,0 , mazeSize, mazeSize);
		}
		return this.maze;
	}
	/**
	 * A recursive maze generator
	 * @param xstart
	 * @param xend
	 * @param ystart
	 * @param yend
	 * @param mazeSize
	 */
	private void mazeGenerate(int xstart, int xend, int ystart, int yend, int mazeSize){
		if(xend - xstart >2){
			//This will insert 4 walls in the middle of the matrix
			if(yend - ystart>2){
				for(int i = 0; i<yend-ystart; i++){
					this.maze[ystart + (yend-ystart)/2][i+xstart]=true;
				}
			}
			for(int i = 0; i<xend-xstart; i++){
				this.maze[i+ystart][xstart + (xend-xstart)/2]=true;
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
						if(xend - xstart > 3){
							newHole = new Random().nextInt((xend-xstart)/4)*2;
						}else{
							newHole = 0;
						}
						this.maze[ystart + newHole][xstart + (xend-xstart)/2] = false;
						break;
					case 1:
						if(yend - ystart > 3){
							newHole = new Random().nextInt((yend-ystart)/4)*2;
						}else{
							newHole = 0;
						}
						this.maze[ystart + (yend-ystart)/2][xstart + newHole] = false;
						break;
					case 2:
						if(xend - xstart > 3){
							newHole = new Random().nextInt((xend-xstart)/4)*2;
						}else{
							newHole = 0;
						}
						this.maze[ystart + newHole+(xend-xstart)/2 +1][xstart + (xend-xstart)/2] = false;
						break;
					case 3:
						if(yend - ystart > 3){
							newHole = new Random().nextInt((yend-ystart)/4)*2;
						}else{
							newHole = 0;
						}
						this.maze[ystart + (yend-ystart)/2][newHole + (yend-ystart)/2 +1 + xstart] = false;
						break;
				}
			}
			if((xend-xstart > 3) && (yend-ystart > 3)){
			mazeGenerate(xstart, xstart + (xend-xstart)/2, ystart, ystart + (yend-ystart)/2, mazeSize);
			mazeGenerate(xstart + (xend-xstart)/2+1, xend, ystart, ystart + (yend-ystart)/2, mazeSize);
			mazeGenerate(xstart + (xend-xstart)/2+1, xend, ystart + (yend-ystart)/2+1, yend, mazeSize);
			mazeGenerate(xstart, xstart + (xend-xstart)/2, ystart + (yend-ystart)/2+1, yend, mazeSize);
			}
		}
	}

}
