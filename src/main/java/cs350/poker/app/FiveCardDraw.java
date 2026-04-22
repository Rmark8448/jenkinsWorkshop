package cs350.poker.app;

import java.util.Scanner;

import cs350.poker.analysis.HandAnalyzer;
import cs350.poker.model.Card;
import cs350.poker.model.Cardi;
import cs350.poker.model.Deck;
import cs350.poker.model.Hand;

/**
 * A console-based Five Card Draw poker demonstration application.
 *
 * <p>
 * The application creates and shuffles a deck of cards, deals a five-card hand,
 * allows the user to discard and replace cards, then evaluates and displays the
 * resulting poker hand type. Play continues until the user quits or the deck
 * runs out of cards.
 * </p>
 *
 * <p>
 * Usage: Run the {@link #main(String[])} method. Interact via console I/O.
 * </p>
 *
 * @author Poker Demo
 * @version 1.0
 */
public class FiveCardDraw {

	/** Number of cards in a standard poker hand. */
	private static final int HAND_SIZE = 5;

	private final Deck deck; // the deck of cards
	private final Scanner scanner; // console input reader

	/**
	 * Constructs a FiveCardDraw game with the given deck and scanner.
	 *
	 * <p>
	 * This constructor supports dependency injection for testability.
	 * </p>
	 *
	 * @param deck    the deck to deal from
	 * @param scanner the scanner for user input
	 */
	public FiveCardDraw(Deck deck, Scanner scanner) {
		this.deck = deck;
		this.scanner = scanner;
	}

	/**
	 * Constructs a FiveCardDraw game with default deck and System.in.
	 */
	public FiveCardDraw() {
		this(new Deck(), new Scanner(System.in));
	}

	/**
	 * Main entry point for the Five Card Draw application.
	 *
	 * @param args command-line arguments (not used)
	 */
	public static void main(String[] args) {
		FiveCardDraw game = new FiveCardDraw();
		game.play();
	}

	/**
	 * Runs the main game loop.
	 *
	 * <p>
	 * Repeatedly shuffles (on first deal), deals a hand, allows replacements,
	 * analyzes the hand, and reports results until the user quits or the deck is
	 * exhausted.
	 * </p>
	 */
	public void play() {
		printWelcome(); // greet the user
		shuffleDeck(); // initial shuffle

		boolean playing = true;
		while (playing) {
			/* Check if enough cards remain for a full hand + potential replacements */
			if (!hasEnoughCards()) {
				System.out.println("\nNot enough cards remaining in the deck. Game over!");
				break;
			}

			Hand hand = dealHand(); // deal 5 cards
			sortAndDisplayHand(hand); // show the hand to the user
			performReplacements(hand); // let user discard/replace
			sortAndDisplayHand(hand); // show updated hand
			analyzeAndReport(hand); // evaluate and display hand type

			playing = promptPlayAgain(); // ask if user wants another round
		}
		printGoodbye(); // farewell message
	}

	// ========== Private Helper Methods ==========

	/**
	 * Prints a welcome banner to the console.
	 */
	private void printWelcome() {
		System.out.println("====================================");
		System.out.println("   Welcome to Five Card Draw Poker  ");
		System.out.println("====================================");
		System.out.println();
	}

	/**
	 * Prints a farewell message to the console.
	 */
	private void printGoodbye() {
		System.out.println("\nThanks for playing Five Card Draw! Goodbye.");
	}

	/**
	 * Shuffles the deck and informs the user.
	 */
	private void shuffleDeck() {
		deck.shuffle();
		System.out.println("Deck shuffled. " + deck.numCardsLeft() + " cards ready.\n");
	}

	/**
	 * Checks whether the deck has enough cards to deal a hand.
	 *
	 * @return {@code true} if at least {@link #HAND_SIZE} cards remain
	 */
	private boolean hasEnoughCards() {
		return deck.numCardsLeft() >= HAND_SIZE; // need at least 5 cards
	}

	/**
	 * Deals a new five-card hand from the deck.
	 *
	 * @return a Hand containing 5 cards
	 */
	private Hand dealHand() {
		Hand hand = new Hand();
		System.out.println("--- Dealing a new hand ---");
		for (int i = 0; i < HAND_SIZE; i++) {
			hand.addCard(deck.dealCard()); // deal one card at a time
		}
		return hand;
	}

	/**
	 * Sorts the hand by rank and displays it to the user.
	 *
	 * @param hand the hand to sort and display
	 */
	private void sortAndDisplayHand(Hand hand) {
		hand.sortByRank(); // organize by rank for readability
		System.out.println("\nYour hand:");
		System.out.println(hand);
	}

	/**
	 * Prompts the user to select cards to discard, then replaces them with new
	 * cards from the deck.
	 *
	 * <p>
	 * The user enters card indices (0-based) separated by spaces. Pressing Enter
	 * with no input keeps all cards.
	 * </p>
	 *
	 * @param hand the hand in which to perform replacements
	 */
	private void performReplacements(Hand hand) {
		System.out.println("\nEnter indices of cards to replace (e.g., '0 2 4'), or press Enter to keep all:");
		String input = scanner.nextLine().trim();

		if (input.isEmpty()) { // user keeps all cards
			System.out.println("Keeping all cards.");
			return;
		}

		/* Parse the indices from user input */
		String[] tokens = input.split("\\s+");
		int[] indices = parseIndices(tokens, hand.size());

		if (indices == null) { // invalid input
			System.out.println("Invalid input. Keeping all cards.");
			return;
		}

		/* Replace selected cards (process in reverse to preserve indices) */
		replaceCards(hand, indices);
	}

	/**
	 * Parses and validates card indices from user input tokens.
	 *
	 * @param tokens   the string tokens to parse
	 * @param handSize the current hand size for bounds validation
	 * @return an array of valid indices sorted descending, or null if invalid
	 */
	private int[] parseIndices(String[] tokens, int handSize) {
		int[] indices = new int[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			try {
				indices[i] = Integer.parseInt(tokens[i]);
				if (indices[i] < 0 || indices[i] >= handSize) {
					return null; // out of bounds
				}
			} catch (NumberFormatException e) {
				return null; // not a number
			}
		}

		/* Sort descending so removals don't shift subsequent indices */
		java.util.Arrays.sort(indices);
		reverseArray(indices);
		return indices;
	}

	/**
	 * Reverses an integer array in place.
	 *
	 * @param arr the array to reverse
	 */
	private void reverseArray(int[] arr) {
		for (int left = 0, right = arr.length - 1; left < right; left++, right--) {
			int temp = arr[left];
			arr[left] = arr[right];
			arr[right] = temp;
		}
	}

	/**
	 * Replaces cards at the specified indices with new cards from the deck.
	 *
	 * <p>
	 * Indices must be sorted in descending order to prevent index shifting.
	 * </p>
	 *
	 * @param hand    the hand to modify
	 * @param indices the indices of cards to replace (sorted descending)
	 */
	private void replaceCards(Hand hand, int[] indices) {
		for (int idx : indices) {
			if (deck.isEmpty()) { // no more replacement cards
				System.out.println("Deck is empty! Cannot replace more cards.");
				break;
			}
			Cardi discarded = hand.removeCard(idx); // remove old card
			System.out.println("Discarded: " + discarded);
			Card newCard = deck.dealCard(); // draw replacement
			hand.addCard(newCard); // add to hand
			System.out.println("Drew: " + newCard);
		}
	}

	/**
	 * Analyzes the hand using a HandAnalyzer and reports the result.
	 *
	 * @param hand the hand to analyze
	 */
	private void analyzeAndReport(Hand hand) {
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		System.out.println("\n*** Your hand is: " + analyzer.getHandTypeName() + " ***");
		System.out.println("Cards remaining in deck: " + deck.numCardsLeft());
	}

	/**
	 * Prompts the user to play another round.
	 *
	 * @return {@code true} if the user wants to continue
	 */
	private boolean promptPlayAgain() {
		System.out.print("\nDeal again? (y/n): ");
		String answer = scanner.nextLine().trim().toLowerCase();
		return answer.startsWith("y"); // any input starting with 'y'
	}
}
