package adventure.game;

public class MapProbabilities
{
	public static final double PROB_GRASS = 79.3;
	public static final double PROB_FLOWERS = 20;
	public static final double PROB_WATER = 0.4;
	public static final double PROB_TREE = 0.3;

	/**
	 * The probability that if a single tile is water, then more water
	 * exists just next to it.
	 * The "water spreading factor".
	 */
	public static final double WATER_SPREAD = 30;

	/**
	 * The probability that if a single tile is tree, then more trees
	 * exists just next to it.
	 * The "tree spreading factor".
	 */
	public static final double TREE_SPREAD = 30;
}
