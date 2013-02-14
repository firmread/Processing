
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PVector;
import shapes3d.Box;
import shapes3d.Shape3D;

public class Board {
	private PApplet app;

	private HashMap<Box, Cell> map = new HashMap<Box, Cell>();
	private LinkedList<Position> undoList = new LinkedList<Position>();
	private ArrayList<Pair> pairs =  new ArrayList<Pair>();
	
	private Cell[][][] cell;
	private TileBag bag;

	private Box table;
	private int nbrAcross, nbrDown, nbrHigh;
	private float nearestApproach;

	public Board (PApplet theApplet, int nbrAcross,  int nbrDown, int nbrHigh, TileBag tb){
		app = theApplet;
		bag = tb;
		this.nbrAcross = nbrAcross;
		this.nbrDown = nbrDown;
		this.nbrHigh = nbrHigh;
		undoList = new LinkedList<Position>();

		cell = new Cell[2*nbrAcross][2*nbrDown][nbrHigh];
		for(int a = 0; a < 2*nbrAcross; a++)
			for(int d = 0; d < 2*nbrDown; d++)
				for(int h = 0; h < nbrHigh; h++)
					cell[a][d][h] = new Cell(a, d, h);
	}

	public void selectTile(Shape3D tile){
		if(tile != null){
			tile.drawMode(Shape3D.TEXTURE | Shape3D.WIRE);
			tile.drawMode(Shape3D.TEXTURE | Shape3D.SOLID | Shape3D.WIRE, Box.TOP);
		}
	}

	public void deselectTile(Shape3D tile){
		if(tile != null){
			tile.drawMode(Shape3D.TEXTURE);
		}
	}
	
	public int getNbrOfTiles(){
		return bag.tiles.length;
	}

	public void setTableColor(int c){
		table.fill(c);
	}

	public float getPercentFree(){
		int nbrFree = 0;
		for(int i = 0; i < bag.tiles.length; i++)
			if(isFree(bag.tiles[i]))
				nbrFree++;
		
		return (nbrFree * 100.0f)/bag.tiles.length;
	}
	
	public int createLevel(int level, String[] lines){
		String line;
		int count = 0;
		char c;
		for(int down = 0; down < nbrDown; down++){
			line = lines[down];
			for(int across = 0; across < nbrAcross; across++){
				c = line.charAt(across);
				switch(c){
				case 'O':
				case 'o':
					cell[2*across][2*down][level].tileReqd = true;
					count++;
					break;
				case '>':
					cell[2*across+1][2*down][level].tileReqd = true;
					count++;
					break;
				case 'v':
					cell[2*across][2*down+1][level].tileReqd = true;
					count++;
					break;
				case 'x':
					cell[2*across+1][2*down+1][level].tileReqd = true;
					count++;
					break;
				case '+':
					break;
				default:
					PApplet.println("Illegal character '" + c +"' in line " + down + " on level " + level);
				}
			}
		}
		return count;
	}

	public void calcCellPositions(){
		float deltaA = bag.tileSizeX + bag.tileGap;
		float deltaD = bag.tileSizeZ + bag.tileGap;
		float deltaH = 1.01f * bag.tileSizeY;
		float minA = -(nbrAcross -1) * 0.5f * deltaA;
		float minD = -(nbrDown - 1) * 0.5f * deltaD;
		float minH = -0.5f * deltaH;

		for(int a = 0; a < 2*nbrAcross; a++)
			for(int d = 0; d < 2*nbrDown; d++)
				for(int h = 0; h < nbrHigh; h++){
					cell[a][d][h].x = minA + a * 0.5f * deltaA;
					cell[a][d][h].z = minD + d * 0.5f * deltaD;
					cell[a][d][h].y = minH - h * deltaH;		
				}
		// Calculate table size
		float tSize = 1.08f * PApplet.max(nbrAcross * deltaA, nbrDown * deltaD);
		float tHeight = 1.0f;
		table = new Box(app, tSize, tHeight, tSize);
		table.pickable(false);
		table.moveBy(0, 0.51f * tHeight, 0);
		// Now get camera nearest approach distance to avoid 
		// P3D clipping problems
		nearestApproach = tSize / 1.1f; 
	}

	public float getCamMinDist(){
		return nearestApproach;
	}

	public void draw(){
		table.draw();
		for(int i = 0; i < bag.tiles.length; i++)
			bag.tiles[i].draw();
	}

	public void placeTiles(){
		map.clear();
		undoList.clear();
		bag.reset();
		Cell aCell;
		Box aTile;
		for(int a = 0; a < 2*nbrAcross; a++)
			for(int d = 0; d < 2*nbrDown; d++)
				for(int h = 0; h < nbrHigh; h++){
					aCell = cell[a][d][h];
					if(aCell.tileReqd){
						aTile = bag.nextTile();
						aTile.visible(true);
						aTile.pickable(true);
						aTile.moveTo(aCell.x, aCell.y, aCell.z);
						aCell.currTile = aTile;
						aCell.undoTile = null;
						map.put(aTile, aCell);
					}
				}	
	}

	public boolean isFree(Shape3D tile){
		Cell aCell = map.get(tile);
		boolean lockedLeft, lockedRight;
		lockedLeft = lockedRight = false;
		int minA, maxA, minD, maxD;

		// Check above
		// Limit search to valid array locations
		minA = Math.max(aCell.a - 1 , 0);
		maxA = Math.min(aCell.a +  1, 2 * nbrAcross - 1);
		minD = Math.max(aCell.d - 1 , 0);
		maxD = Math.min(aCell.d +  1, 2 * nbrDown - 1);
		int above = aCell.h + 1;
		if(above < nbrHigh){
			for(int a = minA; a <= maxA; a++){
				for(int d = minD; d <= maxD; d++){
					if(cell[a][d][above].currTile != null)
						return false;
				}
			}
		}

		// Check left
		int level = aCell.h;
		minA = Math.max(aCell.a - 2 , 0);
		maxA = Math.min(aCell.a +  2, 2 * nbrAcross - 2);
		if(minA < aCell.a){
			for(int d = minD; d <= maxD; d++){
				if(cell[minA][d][level].currTile != null)
					lockedLeft = true;
			}
		}

		// Check right
		if(maxA > aCell.a){
			for(int d = minD; d <= maxD; d++){
				if(cell[maxA][d][level].currTile != null)
					lockedRight = true;
			}
		}
		boolean free = !(lockedLeft & lockedRight);
		return free;
	}
	
	public boolean showPair(int pn){
		Shape3D b1, b2;
		Cell c1, c2;
		Pair p;
		if(pn > 0 && pn <= pairs.size()){
			p = pairs.get(pn - 1);
			c1 = cell[p.p1.a][p.p1.d][p.p1.h];
			c2 = cell[p.p2.a][p.p2.d][p.p2.h];
			b1 = c1.currTile;
			b2 = c2.currTile;
			deselectTile(b1);
			deselectTile(b2);	
		}
		if(pn >= pairs.size()) return false;

		p = pairs.get(pn);

		c1 = cell[p.p1.a][p.p1.d][p.p1.h];
		c2 = cell[p.p2.a][p.p2.d][p.p2.h];
		b1 = c1.currTile;
		b2 = c2.currTile;
		selectTile(b1);
		selectTile(b2);	
		return true;

	}
	
	public int findPairs(){
		ArrayList<Position> places = new ArrayList<Position>(bag.tiles.length);
		for(int a = 0; a < 2*nbrAcross; a++)
			for(int d = 0; d < 2*nbrDown; d++)
				for(int h = 0; h < nbrHigh; h++){
					if(cell[a][d][h].currTile != null && isFree(cell[a][d][h].currTile)){
						places.add(new Position(a,d,h));
					}
				}
		Position p1, p2;
		pairs.clear();
		if(places.size() >= 2){
			for(int i = 0; i < places.size() - 1; i++)
				for(int j = i+1; j < places.size(); j++){
					p1 = places.get(i);
					p2 = places.get(j);
					if(cell[p1.a][p1.d][p1.h].currTile.tagNo == cell[p2.a][p2.d][p2.h].currTile.tagNo){
						pairs.add(new Pair(p1,p2));
					}
				}					
		}	
		return pairs.size();
	}
	
	public PVector getTilePosition(Shape3D selected){
		Cell loc = map.get(selected);
		return new PVector(loc.a, loc.d, loc.h);
	}

	public void removePair(Shape3D[] pair){
		Cell loc;
		for(int i = 0; i < 2; i++){
			loc = map.get(pair[i]);
			undoList.addLast(new Position(loc.a, loc.d, loc.h));
			cell[loc.a][loc.d][loc.h].makeRemoved();
		}
	}

	public boolean undo(){
		Position pos;
		if(!undoList.isEmpty()){
			for(int i = 0; i < 2; i++){
				pos = undoList.removeLast();
				cell[pos.a][pos.d][pos.h].makeUndo();
			}
			return true;
		}
		return false;
	}
	
	public void removeFromPlay(){
		for(int i = 0; i < bag.tiles.length; i++)
			bag.tiles[i].pickable(false);
	}
	
	private class Position {
		public int a, d, h;

		/**
		 * @param a
		 * @param d
		 * @param h
		 */
		public Position(int a, int d, int h) {
			this.a = a;
			this.d = d;
			this.h = h;
		}
	} // End of Position class
	
	private class Pair {
		public Position p1;
		public Position p2;
		
		public Pair(Position p1, Position p2){
			this.p1 = p1;
			this.p2 = p2;
		}	
	}  // End of Pair class

	private class Cell{
		public float x,y,z;
		public int a, d, h;
		public boolean tileReqd = false;
		public Box currTile = null;
		public Box undoTile = null;

		public Cell(int a, int d, int h) {
			this.a = a;
			this.d = d;
			this.h = h;
		}

		public void makeRemoved(){
			currTile.visible(false);
			undoTile = currTile;
			currTile = null;
		}

		public void makeUndo(){
			currTile = undoTile;
			currTile.visible(true);
			undoTile = null;
		}
	}  // end of Cell class
	
}
