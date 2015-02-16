package model.universe;

public abstract class UComp {
	
	Tile tile;
	protected boolean destroyed = false;

	public UComp(Tile tile) {
		this.tile = tile;
		tile.registerOnCreation(this);
	}
	public abstract void update();
	
	protected void destroy() {
		destroyed = true;
		tile.unregisterOnDestroy(this);
	}
	public boolean isDestroyed() {
		return destroyed;
	}
}
