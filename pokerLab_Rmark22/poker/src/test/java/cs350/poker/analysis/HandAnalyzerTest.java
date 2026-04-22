package cs350.poker.analysis;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import cs350.poker.model.Card;
import cs350.poker.model.Hand;

/**
 * Comprehensive JUnit 4 tests for the {@link HandAnalyzer} class.
 *
 * <p>
 * Tests cover every poker hand type (Royal Flush through High Card), edge cases
 * like Ace-low straights, near-miss hands, and failure cases. Uses Hamcrest
 * matchers for expressive assertions.
 * </p>
 *
 * @author Poker Demo
 * @version 1.0
 */
public class HandAnalyzerTest {

	// ==================== Helper Methods ====================

	/**
	 * Creates a Hand from an array of (suit, face) pairs.
	 *
	 * @param cards pairs of {suit, face} values
	 * @return a fully populated Hand
	 */
	private Hand makeHand(int[][] cards) {
		Hand hand = new Hand();
		for (int[] card : cards) {
			hand.addCard(new Card(card[0], card[1]));
		}
		return hand;
	}

	// ==================== Royal Flush Tests ====================

	@Test
	public void testRoyalFlushHearts() {
		// 10♥, J♥, Q♥, K♥, A♥
		Hand hand = makeHand(new int[][] { { 3, 10 }, { 3, 11 }, { 3, 12 }, { 3, 13 }, { 3, 1 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isRoyalFlush(), is(true));
		assertThat(analyzer.getHandType(), is(HandAnalyzer.ROYAL_FLUSH));
		assertThat(analyzer.getHandTypeName(), is("Royal Flush"));
	}

	@Test
	public void testRoyalFlushSpades() {
		// 10♠, J♠, Q♠, K♠, A♠
		Hand hand = makeHand(new int[][] { { 4, 10 }, { 4, 11 }, { 4, 12 }, { 4, 13 }, { 4, 1 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isRoyalFlush(), is(true));
	}

	@Test
	public void testRoyalFlushClubs() {
		// A♣, K♣, Q♣, J♣, 10♣ (scrambled order)
		Hand hand = makeHand(new int[][] { { 1, 1 }, { 1, 13 }, { 1, 12 }, { 1, 11 }, { 1, 10 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isRoyalFlush(), is(true));
	}

	// ==================== Straight Flush Tests ====================

	@Test
	public void testStraightFlushLow() {
		// 2♦, 3♦, 4♦, 5♦, 6♦
		Hand hand = makeHand(new int[][] { { 2, 2 }, { 2, 3 }, { 2, 4 }, { 2, 5 }, { 2, 6 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isStraightFlush(), is(true));
		assertThat(analyzer.getHandType(), is(HandAnalyzer.STRAIGHT_FLUSH));
	}

	@Test
	public void testStraightFlushMid() {
		// 7♠, 8♠, 9♠, 10♠, J♠
		Hand hand = makeHand(new int[][] { { 4, 7 }, { 4, 8 }, { 4, 9 }, { 4, 10 }, { 4, 11 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isStraightFlush(), is(true));
	}

	@Test
	public void testStraightFlushWheel() {
		// A♥, 2♥, 3♥, 4♥, 5♥ (Ace-low straight flush / "steel wheel")
		Hand hand = makeHand(new int[][] { { 3, 1 }, { 3, 2 }, { 3, 3 }, { 3, 4 }, { 3, 5 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isStraightFlush(), is(true));
	}

	// ==================== Four of a Kind Tests ====================

	@Test
	public void testFourOfAKindAces() {
		// A♣, A♦, A♥, A♠, K♠
		Hand hand = makeHand(new int[][] { { 1, 1 }, { 2, 1 }, { 3, 1 }, { 4, 1 }, { 4, 13 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isFourOfAKind(), is(true));
		assertThat(analyzer.getHandType(), is(HandAnalyzer.FOUR_OF_A_KIND));
		assertThat(analyzer.getHandTypeName(), is("Four of a Kind"));
	}

	@Test
	public void testFourOfAKindDeuces() {
		// 2♣, 2♦, 2♥, 2♠, 5♣
		Hand hand = makeHand(new int[][] { { 1, 2 }, { 2, 2 }, { 3, 2 }, { 4, 2 }, { 1, 5 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isFourOfAKind(), is(true));
	}

	// ==================== Full House Tests ====================

	@Test
	public void testFullHouseKingsOverThrees() {
		// K♣, K♦, K♥, 3♠, 3♣
		Hand hand = makeHand(new int[][] { { 1, 13 }, { 2, 13 }, { 3, 13 }, { 4, 3 }, { 1, 3 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isFullHouse(), is(true));
		assertThat(analyzer.getHandType(), is(HandAnalyzer.FULL_HOUSE));
	}

	@Test
	public void testFullHouseTwosOverAces() {
		// 2♣, 2♦, 2♥, A♠, A♣
		Hand hand = makeHand(new int[][] { { 1, 2 }, { 2, 2 }, { 3, 2 }, { 4, 1 }, { 1, 1 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isFullHouse(), is(true));
	}

	// ==================== Flush Tests ====================

	@Test
	public void testFlushHearts() {
		// 2♥, 5♥, 7♥, 9♥, Q♥ (all hearts, not a straight)
		Hand hand = makeHand(new int[][] { { 3, 2 }, { 3, 5 }, { 3, 7 }, { 3, 9 }, { 3, 12 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isFlush(), is(true));
		assertThat(analyzer.getHandType(), is(HandAnalyzer.FLUSH));
		assertThat(analyzer.getHandTypeName(), is("Flush"));
	}

	@Test
	public void testFlushClubs() {
		// 3♣, 6♣, 8♣, J♣, K♣
		Hand hand = makeHand(new int[][] { { 1, 3 }, { 1, 6 }, { 1, 8 }, { 1, 11 }, { 1, 13 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isFlush(), is(true));
	}

	@Test
	public void testNearFlushMixedSuits() {
		// 2♥, 5♥, 7♥, 9♥, Q♦ — one card off
		Hand hand = makeHand(new int[][] { { 3, 2 }, { 3, 5 }, { 3, 7 }, { 3, 9 }, { 2, 12 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isFlush(), is(false));
	}

	// ==================== Straight Tests ====================

	@Test
	public void testStraightNormal() {
		// 5♣, 6♦, 7♥, 8♠, 9♣ (mixed suits, consecutive)
		Hand hand = makeHand(new int[][] { { 1, 5 }, { 2, 6 }, { 3, 7 }, { 4, 8 }, { 1, 9 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isStraight(), is(true));
		assertThat(analyzer.getHandType(), is(HandAnalyzer.STRAIGHT));
	}

	@Test
	public void testStraightAceHigh() {
		// 10♣, J♦, Q♥, K♠, A♣ (ace-high straight, mixed suits)
		Hand hand = makeHand(new int[][] { { 1, 10 }, { 2, 11 }, { 3, 12 }, { 4, 13 }, { 1, 1 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isStraight(), is(true));
	}

	@Test
	public void testStraightAceLowWheel() {
		// A♣, 2♦, 3♥, 4♠, 5♣ (wheel / ace-low straight)
		Hand hand = makeHand(new int[][] { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 }, { 1, 5 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isStraight(), is(true));
	}

	@Test
	public void testNotStraightGap() {
		// 5♣, 6♦, 7♥, 9♠, 10♣ (gap at 8)
		Hand hand = makeHand(new int[][] { { 1, 5 }, { 2, 6 }, { 3, 7 }, { 4, 9 }, { 1, 10 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isStraight(), is(false));
	}

	@Test
	public void testNotStraightAceWrapAround() {
		// Q♣, K♦, A♥, 2♠, 3♣ — ace does NOT wrap around
		Hand hand = makeHand(new int[][] { { 1, 12 }, { 2, 13 }, { 3, 1 }, { 4, 2 }, { 1, 3 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isStraight(), is(false));
	}

	// ==================== Three of a Kind Tests ====================

	@Test
	public void testThreeOfAKind() {
		// 9♣, 9♦, 9♥, 4♠, K♣
		Hand hand = makeHand(new int[][] { { 1, 9 }, { 2, 9 }, { 3, 9 }, { 4, 4 }, { 1, 13 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isThreeOfAKind(), is(true));
		assertThat(analyzer.getHandType(), is(HandAnalyzer.THREE_OF_A_KIND));
	}

	@Test
	public void testThreeOfAKindAces() {
		// A♣, A♦, A♥, 7♠, 3♣
		Hand hand = makeHand(new int[][] { { 1, 1 }, { 2, 1 }, { 3, 1 }, { 4, 7 }, { 1, 3 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isThreeOfAKind(), is(true));
	}

	// ==================== Two Pair Tests ====================

	@Test
	public void testTwoPair() {
		// 8♣, 8♦, J♥, J♠, 3♣
		Hand hand = makeHand(new int[][] { { 1, 8 }, { 2, 8 }, { 3, 11 }, { 4, 11 }, { 1, 3 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isTwoPair(), is(true));
		assertThat(analyzer.getHandType(), is(HandAnalyzer.TWO_PAIR));
		assertThat(analyzer.getHandTypeName(), is("Two Pair"));
	}

	@Test
	public void testTwoPairAcesAndKings() {
		// A♣, A♦, K♥, K♠, 2♣
		Hand hand = makeHand(new int[][] { { 1, 1 }, { 2, 1 }, { 3, 13 }, { 4, 13 }, { 1, 2 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isTwoPair(), is(true));
	}

	// ==================== One Pair Tests ====================

	@Test
	public void testOnePair() {
		// 5♣, 5♦, 9♥, J♠, K♣
		Hand hand = makeHand(new int[][] { { 1, 5 }, { 2, 5 }, { 3, 9 }, { 4, 11 }, { 1, 13 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isOnePair(), is(true));
		assertThat(analyzer.getHandType(), is(HandAnalyzer.ONE_PAIR));
	}

	@Test
	public void testOnePairAces() {
		// A♣, A♦, 3♥, 7♠, 10♣
		Hand hand = makeHand(new int[][] { { 1, 1 }, { 2, 1 }, { 3, 3 }, { 4, 7 }, { 1, 10 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isOnePair(), is(true));
	}

	// ==================== High Card Tests ====================

	@Test
	public void testHighCard() {
		// 2♣, 5♦, 8♥, J♠, A♣ — no pair, no straight, no flush
		Hand hand = makeHand(new int[][] { { 1, 2 }, { 2, 5 }, { 3, 8 }, { 4, 11 }, { 1, 1 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isHighCard(), is(true));
		assertThat(analyzer.getHandType(), is(HandAnalyzer.HIGH_CARD));
		assertThat(analyzer.getHandTypeName(), is("High Card"));
	}

	@Test
	public void testHighCardLowCards() {
		// 2♣, 4♦, 6♥, 8♠, 10♣
		Hand hand = makeHand(new int[][] { { 1, 2 }, { 2, 4 }, { 3, 6 }, { 4, 8 }, { 1, 10 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.isHighCard(), is(true));
	}

	// ==================== Mutual Exclusion Tests ====================

	@Test
	public void testOnlyOneHandTypeIsTrue() {
		// A full house should not also be a pair, three of a kind, etc.
		Hand hand = makeHand(new int[][] { { 1, 13 }, { 2, 13 }, { 3, 13 }, { 4, 3 }, { 1, 3 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);

		assertThat(analyzer.isFullHouse(), is(true));
		assertThat(analyzer.isThreeOfAKind(), is(false));
		assertThat(analyzer.isOnePair(), is(false));
		assertThat(analyzer.isTwoPair(), is(false));
		assertThat(analyzer.isFourOfAKind(), is(false));
		assertThat(analyzer.isFlush(), is(false));
		assertThat(analyzer.isStraight(), is(false));
		assertThat(analyzer.isStraightFlush(), is(false));
		assertThat(analyzer.isRoyalFlush(), is(false));
		assertThat(analyzer.isHighCard(), is(false));
	}

	@Test
	public void testRoyalFlushExcludesOtherTypes() {
		Hand hand = makeHand(new int[][] { { 3, 10 }, { 3, 11 }, { 3, 12 }, { 3, 13 }, { 3, 1 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);

		assertThat(analyzer.isRoyalFlush(), is(true));
		assertThat(analyzer.isStraightFlush(), is(false));
		assertThat(analyzer.isFlush(), is(false));
		assertThat(analyzer.isStraight(), is(false));
	}

	// ==================== getHand / toString Tests ====================

	@Test
	public void testGetHandReturnsOriginal() {
		Hand hand = makeHand(new int[][] { { 1, 2 }, { 2, 5 }, { 3, 8 }, { 4, 11 }, { 1, 1 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.getHand(), is(sameInstance(hand)));
	}

	@Test
	public void testToStringContainsHandTypeName() {
		Hand hand = makeHand(new int[][] { { 1, 5 }, { 2, 5 }, { 3, 9 }, { 4, 11 }, { 1, 13 } });
		HandAnalyzer analyzer = new HandAnalyzer(hand);
		assertThat(analyzer.toString(), containsString("One Pair"));
	}

	// ==================== Failure / Edge Cases ====================

	@Test(expected = IllegalArgumentException.class)
	public void testNullHand() {
		new HandAnalyzer(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHandWithLessThanFiveCards() {
		Hand hand = new Hand();
		hand.addCard(new Card(1, 5));
		hand.addCard(new Card(2, 6));
		new HandAnalyzer(hand); // only 2 cards
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHandWithMoreThanFiveCards() {
		Hand hand = new Hand();
		for (int i = 1; i <= 6; i++) {
			hand.addCard(new Card(1, i)); // 6 cards
		}
		new HandAnalyzer(hand);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyHand() {
		new HandAnalyzer(new Hand());
	}

	// ==================== Hand Type Value Ordering ====================

	@Test
	public void testHandTypeConstants() {
		// Verify the ordering: higher type = better hand
		assertThat(HandAnalyzer.HIGH_CARD, is(0));
		assertThat(HandAnalyzer.ONE_PAIR, is(1));
		assertThat(HandAnalyzer.TWO_PAIR, is(2));
		assertThat(HandAnalyzer.THREE_OF_A_KIND, is(3));
		assertThat(HandAnalyzer.STRAIGHT, is(4));
		assertThat(HandAnalyzer.FLUSH, is(5));
		assertThat(HandAnalyzer.FULL_HOUSE, is(6));
		assertThat(HandAnalyzer.FOUR_OF_A_KIND, is(7));
		assertThat(HandAnalyzer.STRAIGHT_FLUSH, is(8));
		assertThat(HandAnalyzer.ROYAL_FLUSH, is(9));
	}
}
