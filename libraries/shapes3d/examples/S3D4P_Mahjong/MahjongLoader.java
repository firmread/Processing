import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import shapes3d.Box;
import shapes3d.Shape3D;
import shapes3d.utils.Textures;

public class MahjongLoader {

	private static PApplet app;
	private static int nbrTilesReqd;
	
	public static Board createGame(PApplet theApplet, String configFolder){
		app = theApplet;
		TileBag tbag = createTileBag(configFolder);
		Board b = createBoard(configFolder, tbag);
		if(nbrTilesReqd != tbag.tiles.length){
			PApplet.println("UNABLE TO CREATE BOARD");
			PApplet.println("Board requires " + nbrTilesReqd + " tiles");
			PApplet.println("The bag has " + tbag.tiles.length + " tiles");
			return null;
		}
		return b;
	}
	
	private static Board createBoard(String configFolder, TileBag bag){
		String[] rawdata = app.loadStrings(configFolder + "/board_config.txt");
		ArrayList<String> temp = new ArrayList<String>();

		for(int i = 0; i < rawdata.length; i++){
			rawdata[i] = rawdata[i].trim();
			if(rawdata[i].length() > 0 && rawdata[i].charAt(0) != '#'){
				temp.add(rawdata[i]);
			}
		}
		rawdata = (String[]) temp.toArray(new String[temp.size()]);
		int[] bs = getIntArray(rawdata[0]);
		int across = bs[0];
		int down = bs[1];
		int high = bs[2];

		Board board = new Board(app, across, down, high, bag);
		String[] levelLines = new String[down];
		int fileLine = 1;
		nbrTilesReqd = 0;
		for(int h = 0; h < high; h++){
			for(int d = 0; d < down; d++)
				levelLines[d] = rawdata[fileLine++];
			nbrTilesReqd += board.createLevel(h, levelLines);
		}
		board.calcCellPositions();
		// Set the table color for the board
		int[] rgb = getIntArray(rawdata[fileLine++]);
		board.setTableColor(app.color(rgb[0],rgb[1],rgb[2]));
		return board;
	}

	private static TileBag createTileBag(String configFolder){
		String[] rawdata = app.loadStrings(configFolder + "/tile_config.txt");
		ArrayList<String> temp = new ArrayList<String>();

		for(int i = 0; i < rawdata.length; i++){
			rawdata[i] = rawdata[i].trim();
			if(rawdata[i].length() > 0 && rawdata[i].charAt(0) != '#'){
				temp.add(rawdata[i]);
			}
		}
		rawdata = (String[]) temp.toArray(new String[temp.size()]);
		// Get images
		PImage faces = 	Textures.loadImage(app, configFolder + "/" + rawdata[0]);
		PImage side = 	Textures.loadImage(app, configFolder + "/" + rawdata[1]);
		PImage back = 	Textures.loadImage(app, configFolder + "/" + rawdata[2]);

		// Get image tile data
		int[] tempArray = getIntArray(rawdata[3]);
		int cols = tempArray[0];
		int rows = tempArray[1];

		// Get tile set data
		int[] sets = getIntArray(rawdata[4]);
		int[] dupes = getIntArray(rawdata[5]);
		String[] allSame = PApplet.splitTokens(rawdata[6], " ");
		float[] tSize = getFloatArray(rawdata[7]);

		// Get the selection color for the selector
		int[] rgb = getIntArray(rawdata[8]);
		int selColor = app.color(rgb[0], rgb[1], rgb[2]);
		int white = app.color(255);
		
		ArrayList<Box> tileList = new ArrayList<Box>();

		PImage[][] timgs = Textures.makeTiles(app, faces, cols, rows);
		int tileType = 1;
		Box b;
		int c = 0;
		for(int s=0 ; s < sets.length; s++){
			for(int n=0 ; n < sets[s]; n++){
				for(int d = 0; d < dupes[s]; d++){
					b = new Box(app, tSize[0], tSize[2], tSize[1]);
					b.setTexture(timgs[n][s], Box.TOP);
					b.setTexture(side, Box.LEFT | Box.FRONT | Box.RIGHT | Box.BACK);
					b.setTexture(back, Box.BOTTOM);
					b.fill(white);
					b.fill(selColor, Box.TOP);
					b.stroke(selColor);
					b.strokeWeight(3.0f);
					b.strokeWeight(5.0f, Box.TOP);
					b.drawMode(Shape3D.TEXTURE);
					b.tagNo = tileType;
					b.tag = "Box " + c++;
					tileList.add(b);
				}
				if(allSame[s].charAt(0) == 'N')
					tileType++;
			}
			if(allSame[s].charAt(0) == 'Y')
			tileType++;
		}
		Box[] tiles = (Box[]) tileList.toArray(new Box[tileList.size()]);

		return new TileBag(tiles, selColor, tSize[3]);
	}

	private static int[] getIntArray(String s){
		String[] stringValue = PApplet.splitTokens(s, " ");
		int[] numValue = new int[stringValue.length];
		for(int i = 0; i < stringValue.length; i++){
			numValue[i] = Integer.parseInt(stringValue[i]);
		}
		return numValue;
	}

	private static float[] getFloatArray(String s){
		String[] stringValue = PApplet.splitTokens(s, " ");
		float[] numValue = new float[stringValue.length];
		for(int i = 0; i < stringValue.length; i++){
			numValue[i] = Float.parseFloat(stringValue[i]);
		}
		return numValue;
	}
	
	private MahjongLoader(){}
}
