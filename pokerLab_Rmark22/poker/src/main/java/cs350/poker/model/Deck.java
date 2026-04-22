package cs350.poker.model;

import java.util.Random;

/**
 * Represents a standard 52-card deck of playing cards.
 *
 * <p>
 * A deck is initialized with all 52 cards (4 suits × 13 face values). The deck
 * supports shuffling via the repeated-swap technique, dealing the next card
 * from the top, and returning cards back to the deck.
 * </p>
 *
 * @author Poker Demo
 * @version 1.0
 */
public class Deck {

	/** Total number of cards in a standard deck. */
	public static final int DECK_SIZE = 52;

	private final Card[] cards; // fixed-size array holding all 52 slots
	private int topIndex; // index of the next card to deal
	private final Random random; // random number generator for shuffling

	/**
	 * Constructs a new deck containing all 52 standard playing cards.
	 *
	 * <p>
	 * Cards are arranged in order: Clubs (Ace through King), Diamonds, Hearts, then
	 * Spades. The deck is not shuffled upon creation.
	 * </p>
	 */
	public Deck() {
		this(new Random());
	}

	/**
	 * Constructs a new deck with a specified Random instance.
	 *
	 * <p>
	 * Useful for testing with seeded random generators to produce repeatable
	 * shuffle sequences.
	 * </p>
	 *
	 * @param random the Random instance to use for shuffling
	 */
	public Deck(Random random) {
		cards = new Card[DECK_SIZE];
		this.random = random;
		int index = 0;

		/* Populate the deck: iterate suits then face values */
		for (int suit = Card.MIN_SUIT; suit <= Card.MAX_SUIT; suit++) {
			for (int face = Card.MIN_FACE; face <= Card.MAX_FACE; face++) {
				cards[index++] = new Card(suit, face);
			}
		}
		topIndex = 0; // top of deck is index 0
	}

	/**
	 * Shuffles the deck using the Fisher-Yates (repeated swap) algorithm.
	 *
	 * <p>
	 * This method also resets the deal position to the top of the deck, so all 52
	 * cards become available again after shuffling.
	 * </p>
	 */
	public void shuffle() {

		topIndex = 0; // reset so all cards are available after shuffle
		/*
		 * Fisher-Yates shuffle: iterate from last card down to the second, swapping
		 * each card with a randomly chosen card at or before it.
		 */
		for (int i = DECK_SIZE - 1; i > 0; i--) {
			int j = random.nextInt(i + 1); // random index in [0, i]
			Card temp = cards[i];
			cards[i] = cards[j];
			cards[j] = temp;
		}
	}

	/**
	 * Deals the next card from the top of the deck.
	 *
	 * @return the next Card from the deck
	 * @throws IllegalStateException if the deck has no more cards to deal
	 */
	public Card dealCard() {
		if (isEmpty()) { // cannot deal from an empty deck
			throw new IllegalStateException("No more cards in the deck to deal.");
		}
		return cards[topIndex++]; // return current top card, advance pointer
	}

	/**
	 * Returns a card to the bottom of the available portion of the deck.
	 *
	 * <p>
	 * The card is placed at the end of the array. This is only possible if there is
	 * room (i.e., the deck is not completely full).
	 * </p>
	 *
	 * @param card the card to return to the deck
	 * @throws IllegalStateException    if the deck is already full (52 cards)
	 * @throws IllegalArgumentException if card is null
	 */
	public void returnCard(Card card) {
		if (card == null) { // null guard
			throw new IllegalArgumentException("Cannot return a null card.");
		}
		if (topIndex == 0) { // deck is completely full
			throw new IllegalStateException("Deck is full. Cannot return more cards.");
		}
		/*
		 * Place the returned card just before the current topIndex, effectively putting
		 * it back "on top" of the returned-cards pile. We decrement first, then place.
		 */
		cards[--topIndex] = card;
	}

	/**
	 * Reports whether the deck is empty (all cards have been dealt).
	 *
	 * @return {@code true} if there are no cards remaining to deal
	 */
	public boolean isEmpty() {
		return topIndex >= DECK_SIZE; // all cards dealt
	}

	/**
	 * Returns the number of cards remaining in the deck that can be dealt.
	 *
	 * @return the count of undealt cards
	 */
	public int numCardsLeft() {
		return DECK_SIZE - topIndex; // cards from topIndex to end
	}
}
