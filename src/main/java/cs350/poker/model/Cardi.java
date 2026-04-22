package cs350.poker.model;

public interface Cardi {

	/**
	 * Returns the suit as an integer (1=Clubs, 2=Diamonds, 3=Hearts, 4=Spades).
	 *
	 * @return the suit integer
	 */
	int getSuitNum();

	/**
	 * Returns the face value as an integer (1=Ace, 2-10, 11=Jack, 12=Queen,
	 * 13=King).
	 *
	 * @return the face integer
	 */
	int getFaceNum();

	/**
	 * Returns the suit as readable string ("Hearts").
	 *
	 * @return the suit name
	 */
	String getSuitString();

	/**
	 * Returns the face value as readable string ("Queen", "7").
	 *
	 * @return the face name
	 */
	String getFaceString();

	/**
	 * Returns the rank of this card.
	 *
	 * <p>
	 * Aces are ranked highest (rank 49-52 depending on suit). Deuces are ranked
	 * lowest (rank 1-4 depending on suit). Formula: Ace -> 12*4 + suit; otherwise
	 * -> (face - 2)*4 + suit.
	 * </p>
	 *
	 * @return the rank (1..52)
	 */
	int getRank();

}