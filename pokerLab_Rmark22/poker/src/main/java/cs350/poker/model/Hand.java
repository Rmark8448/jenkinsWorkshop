package cs350.poker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a hand of playing cards, such as in a poker game.
 *
 * <p>
 * A hand can hold zero or more {@link Card} objects. Cards can be added,
 * removed, found, and sorted. The hand supports sorting by rank (which accounts
 * for Ace-high) and by face value (natural numeric order).
 * </p>
 *
 * @author Poker Demo
 * @version 1.0
 */
public class Hand {

	protected List<Card> cards; // dynamic collection of cards in the hand

	/**
	 * Constructs an empty hand with no cards.
	 */
	public Hand() {
		cards = new ArrayList<>(); // start empty
	}

	/**
	 * Adds the specified card to this hand.
	 *
	 * @param card the card to add
	 * @throws IllegalArgumentException if card is null
	 */
	public void addCard(Card card) {
		if (card == null) { // null guard
			throw new IllegalArgumentException("Cannot add a null card to the hand.");
		}
		cards.add(card);
	}

	/**
	 * Removes the first occurrence of a card with the same face value and suit as
	 * the specified card from this hand.
	 *
	 * @param card the card to remove (matched by face value and suit)
	 * @return {@code true} if the card was found and removed, {@code false}
	 *         otherwise
	 * @throws IllegalArgumentException if card is null
	 */
	public boolean removeCard(Cardi card) {
		if (card == null) { // null guard
			throw new IllegalArgumentException("Cannot remove a null card.");
		}
		/* Search for an exact match (same face AND same suit) */
		for (int i = 0; i < cards.size(); i++) {
			Cardi c = cards.get(i);
			if (c.getFaceNum() == card.getFaceNum() && c.getSuitNum() == card.getSuitNum()) {
				cards.remove(i);
				return true; // found and removed
			}
		}
		return false; // not found
	}

	/**
	 * Removes the card at the specified index from this hand.
	 *
	 * @param index the zero-based index of the card to remove
	 * @return the removed Card
	 * @throws IndexOutOfBoundsException if index is out of range
	 */
	public Cardi removeCard(int index) {
		if (index < 0 || index >= cards.size()) { // bounds check
			throw new IndexOutOfBoundsException("Index " + index + " out of range for hand of size " + cards.size());
		}
		return cards.remove(index);
	}

	/**
	 * Finds and returns the first card in this hand that matches the specified
	 * card's face value and suit.
	 *
	 * @param card the card to search for (by face and suit)
	 * @return the matching Card, or {@code null} if not found
	 */
	public Card findCard(Cardi card) {
		if (card == null)
			return null; // nothing to find
		for (Card c : cards) {
			if (c.getFaceNum() == card.getFaceNum() && c.getSuitNum() == card.getSuitNum()) {
				return c; // exact match found
			}
		}
		return null; // not in this hand
	}

	/**
	 * Returns the card at the specified index in this hand.
	 *
	 * @param index the zero-based index
	 * @return the Card at the given index
	 * @throws IndexOutOfBoundsException if index is out of range
	 */
	public Card getCard(int index) {
		if (index < 0 || index >= cards.size()) { // bounds check
			throw new IndexOutOfBoundsException("Index " + index + " out of range for hand of size " + cards.size());
		}
		return cards.get(index);
	}

	/**
	 * Sorts the cards in this hand by rank (Aces high, Deuces low).
	 *
	 * <p>
	 * Uses the natural ordering defined by {@link Card#compareTo(Card)}.
	 * </p>
	 */
	public void sortByRank() {
		Collections.sort(cards); // natural ordering uses Card.compareTo
	}

	/**
	 * Sorts the cards in this hand by face value (Ace=1, 2, 3, ..., King=13).
	 *
	 * <p>
	 * This is a numeric sort on the face value field, treating Ace as 1 (the
	 * lowest). For Ace-high sorting, use {@link #sortByRank()} instead.
	 * </p>
	 */
	public void sortByFaceValue() {
		cards.sort(Comparator.comparingInt(Card::getFaceNum)); // ascending face value
	}

	/**
	 * Returns the number of cards in this hand whose face value matches the
	 * specified value.
	 *
	 * <p>
	 * For example, {@code numberOf(12)} returns the count of Queens.
	 * </p>
	 *
	 * @param faceValue the face value to count (1=Ace, 11=Jack, 12=Queen, 13=King)
	 * @return the number of cards with that face value
	 */
	public int numberOf(int faceValue) {
		int count = 0;
		for (Cardi c : cards) {
			if (c.getFaceNum() == faceValue) { // match on face value
				count++;
			}
		}
		return count;
	}

	/**
	 * Returns the number of cards currently in this hand.
	 *
	 * @return the card count
	 */
	public int size() {
		return cards.size();
	}

	/**
	 * Reports whether this hand contains no cards.
	 *
	 * @return {@code true} if the hand is empty
	 */
	public boolean isEmpty() {
		return cards.isEmpty();
	}

	/**
	 * Removes all cards from this hand, returning it to the empty state.
	 */
	public void clear() {
		cards.clear();
	}

	/**
	 * Returns an unmodifiable view of the cards in this hand.
	 *
	 * <p>
	 * This is useful for iteration without risking modification.
	 * </p>
	 *
	 * @return an unmodifiable list of cards
	 */
	public List<Card> getCards() {
		return Collections.unmodifiableList(cards); // defensive copy
	}

	/**
	 * Returns a readable string representation of this hand.
	 *
	 * <p>
	 * Each card is listed on a line with its index for easy reference.
	 * </p>
	 *
	 * @return a formatted string showing all cards in the hand
	 */
	@Override
	public String toString() {
		if (cards.isEmpty()) {
			return "[Empty Hand]";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cards.size(); i++) {
			sb.append(String.format("  [%d] %s%n", i, cards.get(i))); // indexed list
		}
		return sb.toString().stripTrailing(); // remove trailing newline
	}
}
