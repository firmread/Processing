import shapes3d.Box;

/**
 * 
 * @author Peter Lager
 *
 */
public class TileBag {

	public final Box[] tiles;
	public int selTint;
	private int nbrTilesInBag;

	public final float tileSizeX;
	public final float tileSizeY;
	public final float tileSizeZ;
	public final float tileGap;

	/**
	 * @param app
	 * @param tiles
	 * @param selectors 
	 * @param gap 
	 */
	public TileBag(Box[] tiles, int selTint,  float gap) {
		this.tiles = tiles;
		this.selTint = selTint;
		nbrTilesInBag = tiles.length;
		tileSizeX = this.tiles[0].getWidth();
		tileSizeY = this.tiles[0].getHeight();
		tileSizeZ = this.tiles[0].getDepth();
		tileGap = gap;
	}

	public Box nextTile(){
		int tn;
		nbrTilesInBag--;
		// Swap a tile at random with the last available 
		// in the bag;
		tn = (int) (Math.random() * nbrTilesInBag);
		Box temp = tiles[tn];
		tiles[tn] = tiles[nbrTilesInBag];
		tiles[nbrTilesInBag] = temp;
		tiles[nbrTilesInBag].visible(true);
		tiles[nbrTilesInBag].pickable(true);
		return  tiles[nbrTilesInBag];
	}
	
	public boolean isEmpty(){
		return (nbrTilesInBag == 0);
	}

	public void reset(){
		nbrTilesInBag = tiles.length;
	}
}
