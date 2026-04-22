package cs350.poker.model;

/**
 * Represents a single immutable playing card in a standard 52-card deck.
 *
 * <p>
 * Each card is identified by a suit (1=Clubs, 2=Diamonds, 3=Hearts, 4=Spades)
 * and a face value (1=Ace, 2=2, ..., 11=Jack, 12=Queen, 13=King). Cards are
 * immutable once constructed; no mutator methods are provided.
 * </p>
 *
 * <p>
 * The rank of a card is computed so that Aces are highest and Deuces lowest.
 * Rank formula: if Ace, rank = 12*4 + suit; otherwise rank = (faceValue - 2)*4
 * + suit. This yields a unique rank for every card in the deck (1..52).
 * </p>
 *
 * @author Poker Demo
 * @since 03/02/26
 * @version 1.0
 */
public class Card implements Comparable<Card>, Cardi {

	// ----- Constants -----

	public static final String SPADES = "Spades";
	public static final String HEARTS = "Hearts";
	public static final String DIAMONDS = "Diamonds";
	public static final String CLUBS = "Clubs";

	// (1=Clubs, 2=Diamonds, 3=Hearts, 4=Spades)
	// clubs
	public static final int MIN_SUIT = 1;
	// spades
	public static final int MAX_SUIT = 4;

	// (1=Ace, 2=2, ..., 11=Jack, 12=Queen, 13=King)
	// ace
	public static final int MIN_FACE = 1;
	// king
	public static final int MAX_FACE = 13;

	private static final String[] SUIT_NAMES = { "", CLUBS, DIAMONDS, HEARTS, SPADES };
	private static final String[] FACE_NAMES = { "", "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack",
			"Queen", "King" };

	// ----- Fields ----
	protected int suit; // 1=Clubs, 2=Diamonds, 3=Hearts, 4=Spades
	protected int face; // 1=Ace, 2-10, 11=Jack, 12=Queen, 13=King

	// ----- Constructors ------

	/**
	 * Constructs a Card with the specified suit and face value.
	 *
	 * @param suit the suit integer (1-4)
	 * @param face the face value integer (1-13)
	 * @throws IllegalArgumentException if suit or face is out of valid range
	 */
	public Card(int suitVal, int faceVal) {
		this.suit = suitVal;
		this.face = faceVal;
		if (suitVal < MIN_SUIT || suitVal > MAX_SUIT) {
			throw new IllegalArgumentException("Invalid suit: " + suitVal);
		}
		if (faceVal < MIN_FACE || faceVal > MAX_FACE) {
			throw new IllegalArgumentException("Invalid face value: " + faceVal);
		}
	}

	// ------ Getters ----

	/**
	 * Returns the suit as an integer (1=Clubs, 2=Diamonds, 3=Hearts, 4=Spades).
	 *
	 * @return the suit integer
	 */
	
	public int getSuitNum() {
		return suit;
	}

	/**
	 * Returns the face value as an integer (1=Ace, 2-10, 11=Jack, 12=Queen,
	 * 13=King).
	 *
	 * @return the face integer
	 */
	
	public int getFaceNum() {
		return face;
	}

	/**
	 * Returns the suit as readable string ("Hearts").
	 *
	 * @return the suit name
	 */
	
	public String getSuitString() {
		return SUIT_NAMES[suit];
	}

	/**
	 * Returns the face value as readable string ("Queen", "7").
	 *
	 * @return the face name
	 */
	
	public String getFaceString() {
		return FACE_NAMES[face];
	}

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
	
	public int getRank() {
		if (face == 1) { // Ace is highest
			return 12 * 4 + suit;
		}
		return (face - 2) * 4 + suit;
	}

	// ---- Comparison Methods ----

	/**
	 * Compares this card to another card by rank.
	 *
	 * @param other the card to compare to
	 * @return negative if this card has lower rank, 0 if equal, positive if higher
	 */

	
	public int compareTo(Card other) {
		return Integer.compare(this.getRank(), other.getRank());
	}

	/**
	 * Returns true if this card has the same face value as the given card.
	 *
	 * @param c the card to compare to
	 * @return true if face values match
	 */
	public boolean faceEquals(Card c) {
		if (c == null) {
			return false;
		}
		return this.face == c.face;
	}

	// object equals (added after seeing in class)
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Card card = (Card) obj;
		return suit == card.suit && face == card.face;
	}
	


	/**
	 * Returns a string
	 *
	 * @return the card as a string
	 */

	public String toString() {
		return getFaceString() + " of " + getSuitString();
	}
}
