package cs350.poker.app;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import cs350.poker.model.Card;
import cs350.poker.model.Deck;

/**
 * JUnit 4 tests for the {@link FiveCardDraw} application class.
 *
 * <p>
 * Uses Mockito to mock the Deck for deterministic card dealing, and simulates
 * user input via ByteArrayInputStream. Tests cover the main game loop, card
 * replacement, deck exhaustion, and quitting behavior.
 * </p>
 *
 * @author Poker Demo
 * @version 1.0
 */
public class FiveCardDrawTest {

	private ByteArrayOutputStream outputStream;
	private PrintStream originalOut;

	/**
	 * Captures System.out so we can verify console output.
	 */
	@Before
	public void setUp() {
		outputStream = new ByteArrayOutputStream();
		originalOut = System.out;
		System.setOut(new PrintStream(outputStream));
	}

	/**
	 * Restores System.out after each test.
	 */
	@org.junit.After
	public void tearDown() {
		System.setOut(originalOut);
	}

	/**
	 * Gets captured console output as a string.
	 */
	private String getOutput() {
		return outputStream.toString();
	}

	/**
	 * Creates a Scanner from simulated user input.
	 */
	private Scanner scannerFromInput(String input) {
		return new Scanner(new ByteArrayInputStream(input.getBytes()));
	}

	// ==================== Game Flow Tests ====================

	@Test
	public void testPlayOneRoundAndQuit() {
		// Mock a deck that deals 5 specific cards
		Deck mockDeck = mock(Deck.class);
		when(mockDeck.numCardsLeft()).thenReturn(52, 51, 50, 49, 48, 47);
		when(mockDeck.isEmpty()).thenReturn(false);
		when(mockDeck.dealCard()).thenReturn(new Card(4, 1)) // Ace of Spades
				.thenReturn(new Card(3, 13)) // King of Hearts
				.thenReturn(new Card(2, 12)) // Queen of Diamonds
				.thenReturn(new Card(1, 11)) // Jack of Clubs
				.thenReturn(new Card(4, 10)); // 10 of Spades

		// User keeps all cards, then quits
		String input = "\nn\n"; // Enter (keep all), then 'n' (quit)
		FiveCardDraw game = new FiveCardDraw(mockDeck, scannerFromInput(input));
		game.play();

		String output = getOutput();
		assertThat(output, containsString("Welcome"));
		assertThat(output, containsString("Keeping all cards"));
		assertThat(output, containsString("Goodbye"));

		// Verify deck.shuffle() was called
		verify(mockDeck).shuffle();
		// Verify 5 cards were dealt
		verify(mockDeck, times(5)).dealCard();
	}

	@Test
	public void testPlayWithCardReplacement() {
		Deck mockDeck = mock(Deck.class);
		when(mockDeck.numCardsLeft()).thenReturn(52, 51, 50, 49, 48, 47, 46);
		when(mockDeck.isEmpty()).thenReturn(false);
		when(mockDeck.dealCard()).thenReturn(new Card(1, 2)) // 2 of Clubs
				.thenReturn(new Card(2, 5)) // 5 of Diamonds
				.thenReturn(new Card(3, 8)) // 8 of Hearts
				.thenReturn(new Card(4, 11)) // Jack of Spades
				.thenReturn(new Card(1, 1)) // Ace of Clubs
				.thenReturn(new Card(4, 13)); // King of Spades (replacement)

		// Replace card at index 0, then quit
		String input = "0\nn\n";
		FiveCardDraw game = new FiveCardDraw(mockDeck, scannerFromInput(input));
		game.play();

		String output = getOutput();
		assertThat(output, containsString("Discarded"));
		assertThat(output, containsString("Drew"));

		// 5 initial + 1 replacement = 6 dealCard calls
		verify(mockDeck, times(6)).dealCard();
	}

	@Test
	public void testDeckExhaustedStopsGame() {
		Deck mockDeck = mock(Deck.class);
		// Deck reports not enough cards
		when(mockDeck.numCardsLeft()).thenReturn(3); // less than 5

		String input = ""; // no input needed
		FiveCardDraw game = new FiveCardDraw(mockDeck, scannerFromInput(input));
		game.play();

		String output = getOutput();
		assertThat(output, containsString("Not enough cards"));
		assertThat(output, containsString("Game over"));
	}

	@Test
	public void testInvalidInputKeepsAllCards() {
		Deck mockDeck = mock(Deck.class);
		when(mockDeck.numCardsLeft()).thenReturn(52, 51, 50, 49, 48, 47);
		when(mockDeck.isEmpty()).thenReturn(false);
		when(mockDeck.dealCard()).thenReturn(new Card(1, 5)).thenReturn(new Card(2, 5)).thenReturn(new Card(3, 9))
				.thenReturn(new Card(4, 11)).thenReturn(new Card(1, 13));

		// Invalid input "abc", then quit
		String input = "abc\nn\n";
		FiveCardDraw game = new FiveCardDraw(mockDeck, scannerFromInput(input));
		game.play();

		String output = getOutput();
		assertThat(output, containsString("Invalid input"));
		assertThat(output, containsString("Keeping all cards"));
	}

	@Test
	public void testOutOfBoundsIndexKeepsAllCards() {
		Deck mockDeck = mock(Deck.class);
		when(mockDeck.numCardsLeft()).thenReturn(52, 51, 50, 49, 48, 47);
		when(mockDeck.isEmpty()).thenReturn(false);
		when(mockDeck.dealCard()).thenReturn(new Card(1, 5)).thenReturn(new Card(2, 5)).thenReturn(new Card(3, 9))
				.thenReturn(new Card(4, 11)).thenReturn(new Card(1, 13));

		// Index 9 is out of bounds for a 5-card hand
		String input = "9\nn\n";
		FiveCardDraw game = new FiveCardDraw(mockDeck, scannerFromInput(input));
		game.play();

		String output = getOutput();
		assertThat(output, containsString("Invalid input"));
	}

	@Test
	public void testMultipleRounds() {
		Deck mockDeck = mock(Deck.class);
		// Enough cards for two rounds
		when(mockDeck.numCardsLeft()).thenReturn(52) // initial check
				.thenReturn(51).thenReturn(50).thenReturn(49).thenReturn(48).thenReturn(47) // deal round 1
				.thenReturn(47) // check for round 2
				.thenReturn(46).thenReturn(45).thenReturn(44).thenReturn(43).thenReturn(42) // deal round 2
				.thenReturn(42); // final

		when(mockDeck.isEmpty()).thenReturn(false);
		when(mockDeck.dealCard())
				// Round 1
				.thenReturn(new Card(1, 2)).thenReturn(new Card(2, 3)).thenReturn(new Card(3, 4))
				.thenReturn(new Card(4, 5)).thenReturn(new Card(1, 6))
				// Round 2
				.thenReturn(new Card(1, 7)).thenReturn(new Card(2, 8)).thenReturn(new Card(3, 9))
				.thenReturn(new Card(4, 10)).thenReturn(new Card(1, 11));

		// Keep all, play again, keep all, quit
		String input = "\ny\n\nn\n";
		FiveCardDraw game = new FiveCardDraw(mockDeck, scannerFromInput(input));
		game.play();

		// Should have dealt 10 cards total (2 rounds × 5)
		verify(mockDeck, times(10)).dealCard();
	}

	@Test
	public void testWelcomeAndGoodbyeMessages() {
		Deck mockDeck = mock(Deck.class);
		when(mockDeck.numCardsLeft()).thenReturn(3); // not enough to play

		FiveCardDraw game = new FiveCardDraw(mockDeck, scannerFromInput(""));
		game.play();

		String output = getOutput();
		assertThat(output, containsString("Welcome to Five Card Draw Poker"));
		assertThat(output, containsString("Goodbye"));
	}

	// ==================== Constructor Tests ====================

	@Test
	public void testDefaultConstructorCreatesGame() {
		// Just verify it doesn't throw
		FiveCardDraw game = new FiveCardDraw();
		assertThat(game, is(notNullValue()));
	}

	@Test
	public void testParameterizedConstructor() {
		Deck deck = new Deck();
		Scanner scanner = new Scanner(System.in);
		FiveCardDraw game = new FiveCardDraw(deck, scanner);
		assertThat(game, is(notNullValue()));
	}
}
