package cs350.poker.analysis;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

import org.junit.Test;

import cs350.poker.model.Card;
import cs350.poker.model.Hand;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

/**
 * Comprehensive JUnit 4 tests for the {@link HandComparator} class.
 *
 * <p>
 * Tests cover comparisons between different hand types, tiebreakers within the
 * same hand type, symmetry, edge cases, and failure scenarios.
 * </p>
 *
 * @author Poker Demo
 * @version 1.0
 */
public class HandComparatorTest {

	// don't need when mocking via mockito
//	private HandComparator comparator;
//
//	@Before
//	public void setUp() {
//		comparator = new HandComparator();
//	}
//
//	private Hand makeHand(int[][] cards) {
//		Hand hand = new Hand();
//		for (int[] card : cards) {
//			hand.addCard(new Card(card[0], card[1]));
//		}
//		return hand;
//	}

	// ---- Mock Helper Methods ----
	/**
	 * Creates a mocked Card with the given suit and face. Stubs getSuitNum(),
	 * getFaceNum(), and getRank().
	 */
	private Card mockCard(int suit, int face) {
		Card mockCard = mock(Card.class);
		when(mockCard.getSuitNum()).thenReturn(suit);
		when(mockCard.getFaceNum()).thenReturn(face);

		// Rank formula
		int rank = (face == 1) ? 12 * 4 + suit : (face - 2) * 4 + suit;
		when(mockCard.getRank()).thenReturn(rank);

		return mockCard;
	}

	/**
	 * Creates a mocked Hand containing the given mocked Cards. Stubs size() to
	 * return 5 and getCards() to return the list.
	 */
	private Hand mockHand(Card... cards) {
		Hand mockHand = mock(Hand.class);
		when(mockHand.size()).thenReturn(5);
		List<Card> cardList = Arrays.asList(cards);
		when(mockHand.getCards()).thenReturn(cardList);
		return mockHand;
	}

	private HandComparator comparatorWithMocks(Hand hand1, HandAnalyzer mock1, Hand hand2, HandAnalyzer mock2) {
		return new HandComparator() {
			@Override
			protected HandAnalyzer createAnalyzer(Hand hand) {
				if (hand == hand1)
					return mock1;
				if (hand == hand2)
					return mock2;
				throw new IllegalArgumentException("Unexpected hand: " + hand);
			}
		};
	}

	// ==================== Different Hand Type Comparisons Mock ====================

	@Test
	public void testRoyalFlushBeatsStraightFlushMock() {
		Hand royalFlush = mockHand(mockCard(3, 10), mockCard(3, 11), mockCard(3, 12), mockCard(3, 13), mockCard(3, 1));
		Hand straightFlush = mockHand(mockCard(2, 5), mockCard(2, 6), mockCard(2, 7), mockCard(2, 8), mockCard(2, 9));

		HandAnalyzer mockRoyal = mock(HandAnalyzer.class);
		when(mockRoyal.getHandType()).thenReturn(HandAnalyzer.ROYAL_FLUSH);

		HandAnalyzer mockStraight = mock(HandAnalyzer.class);
		when(mockStraight.getHandType()).thenReturn(HandAnalyzer.STRAIGHT_FLUSH);

		HandComparator comparator = comparatorWithMocks(royalFlush, mockRoyal, straightFlush, mockStraight);
		assertThat(comparator.compare(royalFlush, straightFlush), greaterThan(0));
	}

	@Test
	public void testStraightFlushBeatsFourOfAKindMock() {
		Hand straightFlush = mockHand(mockCard(4, 5), mockCard(4, 6), mockCard(4, 7), mockCard(4, 8), mockCard(4, 9));
		Hand fourOfAKind = mockHand(mockCard(1, 1), mockCard(2, 1), mockCard(3, 1), mockCard(4, 1), mockCard(1, 13));

		HandAnalyzer mockStraightFlush = mock(HandAnalyzer.class);
		when(mockStraightFlush.getHandType()).thenReturn(HandAnalyzer.STRAIGHT_FLUSH);
		HandAnalyzer mockFour = mock(HandAnalyzer.class);
		when(mockFour.getHandType()).thenReturn(HandAnalyzer.FOUR_OF_A_KIND);

		HandComparator comparator = comparatorWithMocks(straightFlush, mockStraightFlush, fourOfAKind, mockFour);
		assertThat(comparator.compare(straightFlush, fourOfAKind), greaterThan(0));
	}

	@Test
	public void testFourOfAKindBeatsFullHouseMock() {
		Hand fourOfAKind = mockHand(mockCard(1, 9), mockCard(2, 9), mockCard(3, 9), mockCard(4, 9), mockCard(1, 5));
		Hand fullHouse = mockHand(mockCard(1, 13), mockCard(2, 13), mockCard(3, 13), mockCard(4, 5), mockCard(1, 5));

		HandAnalyzer mockFour = mock(HandAnalyzer.class);
		when(mockFour.getHandType()).thenReturn(HandAnalyzer.FOUR_OF_A_KIND);
		HandAnalyzer mockFull = mock(HandAnalyzer.class);
		when(mockFull.getHandType()).thenReturn(HandAnalyzer.FULL_HOUSE);

		HandComparator comparator = comparatorWithMocks(fourOfAKind, mockFour, fullHouse, mockFull);
		assertThat(comparator.compare(fourOfAKind, fullHouse), greaterThan(0));
	}

	@Test
	public void testFullHouseBeatsFlushMock() {
		Hand fullHouse = mockHand(mockCard(1, 8), mockCard(2, 8), mockCard(3, 8), mockCard(4, 3), mockCard(1, 3));
		Hand flush = mockHand(mockCard(3, 2), mockCard(3, 5), mockCard(3, 7), mockCard(3, 9), mockCard(3, 12));

		HandAnalyzer mockFull = mock(HandAnalyzer.class);
		when(mockFull.getHandType()).thenReturn(HandAnalyzer.FULL_HOUSE);
		HandAnalyzer mockFlush = mock(HandAnalyzer.class);
		when(mockFlush.getHandType()).thenReturn(HandAnalyzer.FLUSH);

		HandComparator comparator = comparatorWithMocks(fullHouse, mockFull, flush, mockFlush);
		assertThat(comparator.compare(fullHouse, flush), greaterThan(0));
	}

	@Test
	public void testFlushBeatsStraightMock() {
		Hand flush = mockHand(mockCard(1, 2), mockCard(1, 5), mockCard(1, 8), mockCard(1, 11), mockCard(1, 13));
		Hand straight = mockHand(mockCard(1, 5), mockCard(2, 6), mockCard(3, 7), mockCard(4, 8), mockCard(1, 9));

		HandAnalyzer mockFlush = mock(HandAnalyzer.class);
		when(mockFlush.getHandType()).thenReturn(HandAnalyzer.FLUSH);
		HandAnalyzer mockStraight = mock(HandAnalyzer.class);
		when(mockStraight.getHandType()).thenReturn(HandAnalyzer.STRAIGHT);

		HandComparator comparator = comparatorWithMocks(flush, mockFlush, straight, mockStraight);
		assertThat(comparator.compare(flush, straight), greaterThan(0));
	}

	@Test
	public void testStraightBeatsThreeOfAKindMock() {
		Hand straight = mockHand(mockCard(1, 5), mockCard(2, 6), mockCard(3, 7), mockCard(4, 8), mockCard(1, 9));
		Hand trips = mockHand(mockCard(1, 9), mockCard(2, 9), mockCard(3, 9), mockCard(4, 4), mockCard(1, 2));

		HandAnalyzer mockStraight = mock(HandAnalyzer.class);
		when(mockStraight.getHandType()).thenReturn(HandAnalyzer.STRAIGHT);
		HandAnalyzer mockTrips = mock(HandAnalyzer.class);
		when(mockTrips.getHandType()).thenReturn(HandAnalyzer.THREE_OF_A_KIND);

		HandComparator comparator = comparatorWithMocks(straight, mockStraight, trips, mockTrips);
		assertThat(comparator.compare(straight, trips), greaterThan(0));
	}

	@Test
	public void testThreeOfAKindBeatsTwoPairMock() {
		Hand trips = mockHand(mockCard(1, 7), mockCard(2, 7), mockCard(3, 7), mockCard(4, 4), mockCard(1, 2));
		Hand twoPair = mockHand(mockCard(1, 1), mockCard(2, 1), mockCard(3, 13), mockCard(4, 13), mockCard(1, 9));

		HandAnalyzer mockTrips = mock(HandAnalyzer.class);
		when(mockTrips.getHandType()).thenReturn(HandAnalyzer.THREE_OF_A_KIND);
		HandAnalyzer mockTwoPair = mock(HandAnalyzer.class);
		when(mockTwoPair.getHandType()).thenReturn(HandAnalyzer.TWO_PAIR);

		HandComparator comparator = comparatorWithMocks(trips, mockTrips, twoPair, mockTwoPair);
		assertThat(comparator.compare(trips, twoPair), greaterThan(0));
	}

	@Test
	public void testTwoPairBeatsOnePairMock() {
		Hand twoPair = mockHand(mockCard(1, 8), mockCard(2, 8), mockCard(3, 5), mockCard(4, 5), mockCard(1, 3));
		Hand onePair = mockHand(mockCard(1, 1), mockCard(2, 1), mockCard(3, 13), mockCard(4, 12), mockCard(1, 10));

		HandAnalyzer mockTwoPair = mock(HandAnalyzer.class);
		when(mockTwoPair.getHandType()).thenReturn(HandAnalyzer.TWO_PAIR);
		HandAnalyzer mockOnePair = mock(HandAnalyzer.class);
		when(mockOnePair.getHandType()).thenReturn(HandAnalyzer.ONE_PAIR);

		HandComparator comparator = comparatorWithMocks(twoPair, mockTwoPair, onePair, mockOnePair);
		assertThat(comparator.compare(twoPair, onePair), greaterThan(0));
	}

	@Test
	public void testOnePairBeatsHighCardMock() {
		Hand onePair = mockHand(mockCard(1, 2), mockCard(2, 2), mockCard(3, 5), mockCard(4, 8), mockCard(1, 11));
		Hand highCard = mockHand(mockCard(1, 1), mockCard(2, 13), mockCard(3, 12), mockCard(4, 10), mockCard(1, 8));

		HandAnalyzer mockOnePair = mock(HandAnalyzer.class);
		when(mockOnePair.getHandType()).thenReturn(HandAnalyzer.ONE_PAIR);
		HandAnalyzer mockHigh = mock(HandAnalyzer.class);
		when(mockHigh.getHandType()).thenReturn(HandAnalyzer.HIGH_CARD);

		HandComparator comparator = comparatorWithMocks(onePair, mockOnePair, highCard, mockHigh);
		assertThat(comparator.compare(onePair, highCard), greaterThan(0));
	}

	// ==================== Same Type Tiebreakers Mock ====================

	@Test
	public void testHighCardTiebreakerHigherKickerMock() {
		Hand higher = mockHand(mockCard(2, 1), mockCard(2, 13), mockCard(3, 10), mockCard(4, 7), mockCard(1, 3));
		Hand lower = mockHand(mockCard(1, 1), mockCard(1, 12), mockCard(4, 10), mockCard(3, 7), mockCard(2, 3));

		HandAnalyzer mockHigher = mock(HandAnalyzer.class);
		when(mockHigher.getHandType()).thenReturn(HandAnalyzer.HIGH_CARD);
		HandAnalyzer mockLower = mock(HandAnalyzer.class);
		when(mockLower.getHandType()).thenReturn(HandAnalyzer.HIGH_CARD);

		HandComparator comparator = comparatorWithMocks(higher, mockHigher, lower, mockLower);
		assertThat(comparator.compare(higher, lower), greaterThan(0));
	}

	@Test
	public void testPairTiebreakerHigherPairMock() {
		Hand kingsHigh = mockHand(mockCard(1, 13), mockCard(2, 13), mockCard(3, 5), mockCard(4, 3), mockCard(1, 2));
		Hand jacksHigh = mockHand(mockCard(1, 11), mockCard(2, 11), mockCard(3, 5), mockCard(4, 3), mockCard(1, 2));

		HandAnalyzer mockKings = mock(HandAnalyzer.class);
		when(mockKings.getHandType()).thenReturn(HandAnalyzer.ONE_PAIR);
		HandAnalyzer mockJacks = mock(HandAnalyzer.class);
		when(mockJacks.getHandType()).thenReturn(HandAnalyzer.ONE_PAIR);

		HandComparator comparator = comparatorWithMocks(kingsHigh, mockKings, jacksHigh, mockJacks);
		assertThat(comparator.compare(kingsHigh, jacksHigh), greaterThan(0));
	}

	@Test
	public void testIdenticalHandsTieMock() {
		Hand hand1 = mockHand(mockCard(1, 2), mockCard(1, 5), mockCard(1, 8), mockCard(1, 11), mockCard(1, 1));
		Hand hand2 = mockHand(mockCard(1, 2), mockCard(1, 5), mockCard(1, 8), mockCard(1, 11), mockCard(1, 1));

		HandAnalyzer mock1 = mock(HandAnalyzer.class);
		when(mock1.getHandType()).thenReturn(HandAnalyzer.HIGH_CARD);
		HandAnalyzer mock2 = mock(HandAnalyzer.class);
		when(mock2.getHandType()).thenReturn(HandAnalyzer.HIGH_CARD);

		HandComparator comparator = comparatorWithMocks(hand1, mock1, hand2, mock2);
		assertThat(comparator.compare(hand1, hand2), is(0));
	}

	// ==================== Symmetry & Transitivity Mock ====================

	@Test
	public void testSymmetryMock() {
		Hand better = mockHand(mockCard(1, 1), mockCard(2, 1), mockCard(3, 1), mockCard(4, 1), mockCard(1, 13));
		Hand worse = mockHand(mockCard(1, 5), mockCard(2, 5), mockCard(3, 9), mockCard(4, 11), mockCard(1, 13));

		HandAnalyzer mockBetter = mock(HandAnalyzer.class);
		when(mockBetter.getHandType()).thenReturn(HandAnalyzer.FOUR_OF_A_KIND);
		HandAnalyzer mockWorse = mock(HandAnalyzer.class);
		when(mockWorse.getHandType()).thenReturn(HandAnalyzer.HIGH_CARD);

		HandComparator comparator = comparatorWithMocks(better, mockBetter, worse, mockWorse);
		assertThat(comparator.compare(better, worse), greaterThan(0));
		assertThat(comparator.compare(worse, better), lessThan(0));
	}

	@Test
	public void testReflexiveMock() {
		Hand hand = mockHand(mockCard(1, 5), mockCard(2, 6), mockCard(3, 7), mockCard(4, 8), mockCard(1, 9));

		HandAnalyzer mockAnalyzer = mock(HandAnalyzer.class);
		when(mockAnalyzer.getHandType()).thenReturn(HandAnalyzer.HIGH_CARD);

		HandComparator comparator = comparatorWithMocks(hand, mockAnalyzer, hand, mockAnalyzer);
		assertThat(comparator.compare(hand, hand), is(0));
	}

	// ==================== Failure Cases Mock ====================

	@Test(expected = IllegalArgumentException.class)
	public void testCompareNullFirstHandMock() {
		Hand hand = mockHand(mockCard(1, 2), mockCard(2, 5), mockCard(3, 8), mockCard(4, 11), mockCard(1, 1));

		// For null tests, we can use any comparator since the null check happens first.
		HandComparator comparator = new HandComparator(); // default constructor is fine
		comparator.compare(null, hand);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCompareNullSecondHandMock() {
		Hand hand = mockHand(mockCard(1, 2), mockCard(2, 5), mockCard(3, 8), mockCard(4, 11), mockCard(1, 1));

		HandComparator comparator = new HandComparator();
		comparator.compare(hand, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCompareBothNullMock() {
		HandComparator comparator = new HandComparator();
		comparator.compare(null, null);
	}

//	// ==================== Different Hand Type Comparisons ====================
//
//	@Test
//	public void testRoyalFlushBeatsStraightFlush() {
//		Hand royalFlush = makeHand(new int[][] { { 3, 10 }, { 3, 11 }, { 3, 12 }, { 3, 13 }, { 3, 1 } });
//		Hand straightFlush = makeHand(new int[][] { { 2, 5 }, { 2, 6 }, { 2, 7 }, { 2, 8 }, { 2, 9 } });
//		assertThat(comparator.compare(royalFlush, straightFlush), greaterThan(0));
//	}
//
//	@Test
//	public void testStraightFlushBeatsFourOfAKind() {
//		Hand straightFlush = makeHand(new int[][] { { 4, 5 }, { 4, 6 }, { 4, 7 }, { 4, 8 }, { 4, 9 } });
//		Hand fourOfAKind = makeHand(new int[][] { { 1, 1 }, { 2, 1 }, { 3, 1 }, { 4, 1 }, { 1, 13 } });
//		assertThat(comparator.compare(straightFlush, fourOfAKind), greaterThan(0));
//	}
//
//	@Test
//	public void testFourOfAKindBeatsFullHouse() {
//		Hand fourOfAKind = makeHand(new int[][] { { 1, 9 }, { 2, 9 }, { 3, 9 }, { 4, 9 }, { 1, 5 } });
//		Hand fullHouse = makeHand(new int[][] { { 1, 13 }, { 2, 13 }, { 3, 13 }, { 4, 5 }, { 1, 5 } });
//		assertThat(comparator.compare(fourOfAKind, fullHouse), greaterThan(0));
//	}
//
//	@Test
//	public void testFullHouseBeatsFlush() {
//		Hand fullHouse = makeHand(new int[][] { { 1, 8 }, { 2, 8 }, { 3, 8 }, { 4, 3 }, { 1, 3 } });
//		Hand flush = makeHand(new int[][] { { 3, 2 }, { 3, 5 }, { 3, 7 }, { 3, 9 }, { 3, 12 } });
//		assertThat(comparator.compare(fullHouse, flush), greaterThan(0));
//	}
//
//	@Test
//	public void testFlushBeatsStraight() {
//		Hand flush = makeHand(new int[][] { { 1, 2 }, { 1, 5 }, { 1, 8 }, { 1, 11 }, { 1, 13 } });
//		Hand straight = makeHand(new int[][] { { 1, 5 }, { 2, 6 }, { 3, 7 }, { 4, 8 }, { 1, 9 } });
//		assertThat(comparator.compare(flush, straight), greaterThan(0));
//	}
//
//	@Test
//	public void testStraightBeatsThreeOfAKind() {
//		Hand straight = makeHand(new int[][] { { 1, 5 }, { 2, 6 }, { 3, 7 }, { 4, 8 }, { 1, 9 } });
//		Hand trips = makeHand(new int[][] { { 1, 9 }, { 2, 9 }, { 3, 9 }, { 4, 4 }, { 1, 2 } });
//		assertThat(comparator.compare(straight, trips), greaterThan(0));
//	}
//
//	@Test
//	public void testThreeOfAKindBeatsTwoPair() {
//		Hand trips = makeHand(new int[][] { { 1, 7 }, { 2, 7 }, { 3, 7 }, { 4, 4 }, { 1, 2 } });
//		Hand twoPair = makeHand(new int[][] { { 1, 1 }, { 2, 1 }, { 3, 13 }, { 4, 13 }, { 1, 9 } });
//		assertThat(comparator.compare(trips, twoPair), greaterThan(0));
//	}
//
//	@Test
//	public void testTwoPairBeatsOnePair() {
//		Hand twoPair = makeHand(new int[][] { { 1, 8 }, { 2, 8 }, { 3, 5 }, { 4, 5 }, { 1, 3 } });
//		Hand onePair = makeHand(new int[][] { { 1, 1 }, { 2, 1 }, { 3, 13 }, { 4, 12 }, { 1, 10 } });
//		assertThat(comparator.compare(twoPair, onePair), greaterThan(0));
//	}
//
//	@Test
//	public void testOnePairBeatsHighCard() {
//		Hand onePair = makeHand(new int[][] { { 1, 2 }, { 2, 2 }, { 3, 5 }, { 4, 8 }, { 1, 11 } });
//		Hand highCard = makeHand(new int[][] { { 1, 1 }, { 2, 13 }, { 3, 12 }, { 4, 10 }, { 1, 8 } });
//		assertThat(comparator.compare(onePair, highCard), greaterThan(0));
//	}
//
//	// ==================== Same Type Tiebreakers ====================
//
//	@Test
//	public void testHighCardTiebreakerHigherKicker() {
//		// Both high card; first has K as second kicker, second has Q
//		Hand higher = makeHand(new int[][] { { 2, 1 }, { 2, 13 }, { 3, 10 }, { 4, 7 }, { 1, 3 } });
//		Hand lower = makeHand(new int[][] { { 1, 1 }, { 1, 12 }, { 4, 10 }, { 3, 7 }, { 2, 3 } });
//		assertThat(comparator.compare(higher, lower), greaterThan(0));
//	}
//
//	@Test
//	public void testPairTiebreakerHigherPair() {
//		Hand kingsHigh = makeHand(new int[][] { { 1, 13 }, { 2, 13 }, { 3, 5 }, { 4, 3 }, { 1, 2 } });
//		Hand jacksHigh = makeHand(new int[][] { { 1, 11 }, { 2, 11 }, { 3, 5 }, { 4, 3 }, { 1, 2 } });
//		assertThat(comparator.compare(kingsHigh, jacksHigh), greaterThan(0));
//	}
//
//	@Test
//	public void testIdenticalHandsTie() {
//		// Same cards in both hands = exact tie
//		Hand hand1 = makeHand(new int[][] { { 1, 2 }, { 1, 5 }, { 1, 8 }, { 1, 11 }, { 1, 1 } });
//		Hand hand2 = makeHand(new int[][] { { 1, 2 }, { 1, 5 }, { 1, 8 }, { 1, 11 }, { 1, 1 } });
//		assertThat(comparator.compare(hand1, hand2), is(0));
//	}
//
//	// ==================== Symmetry & Transitivity ====================
//
//	@Test
//	public void testSymmetry() {
//		Hand better = makeHand(new int[][] { { 1, 1 }, { 2, 1 }, { 3, 1 }, { 4, 1 }, { 1, 13 } });
//		Hand worse = makeHand(new int[][] { { 1, 5 }, { 2, 5 }, { 3, 9 }, { 4, 11 }, { 1, 13 } });
//		assertThat(comparator.compare(better, worse), greaterThan(0));
//		assertThat(comparator.compare(worse, better), lessThan(0));
//	}
//
//	@Test
//	public void testReflexive() {
//		Hand hand = makeHand(new int[][] { { 1, 5 }, { 2, 6 }, { 3, 7 }, { 4, 8 }, { 1, 9 } });
//		assertThat(comparator.compare(hand, hand), is(0));
//	}
//
//	// ==================== Failure Cases ====================
//
//	@Test(expected = IllegalArgumentException.class)
//	public void testCompareNullFirstHand() {
//		Hand hand = makeHand(new int[][] { { 1, 2 }, { 2, 5 }, { 3, 8 }, { 4, 11 }, { 1, 1 } });
//		comparator.compare(null, hand);
//	}
//
//	@Test(expected = IllegalArgumentException.class)
//	public void testCompareNullSecondHand() {
//		Hand hand = makeHand(new int[][] { { 1, 2 }, { 2, 5 }, { 3, 8 }, { 4, 11 }, { 1, 1 } });
//		comparator.compare(hand, null);
//	}
//
//	@Test(expected = IllegalArgumentException.class)
//	public void testCompareBothNull() {
//		comparator.compare(null, null);
//	}
}
