package cs350.poker.model;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * Comprehensive JUnit 4 tests for the {@link Hand} class.
 *
 * <p>
 * Tests cover construction, adding/removing/finding cards, sorting, counting,
 * querying, string representation, and edge/failure cases. Uses Hamcrest
 * matchers throughout for expressive assertions.
 * </p>
 *
 * @author Poker Demo
 * @version 1.0
 */
public class HandTest {

	private Hand hand;
	private Card aceOfSpades;
	private Card kingOfHearts;
	private Card queenOfDiamonds;
	private Cardi jackOfClubs;
	private Cardi tenOfSpades;
	private Cardi mockTwoOfClubs;
	
	private Card mockCard;
	
	//making mock hand
	private class TestHand extends Hand {
		
		public TestHand(Card ... tcards) {
		this.cards = Arrays.asList(tcards);
	}
};



	/**
	 * Sets up a fresh hand and some common cards before each test.
	 */
	@Before
	public void setUp() {
		hand = new Hand();
		aceOfSpades = new Card(4, 1);
		kingOfHearts = new Card(3, 13);
		queenOfDiamonds = new Card(2, 12);
		jackOfClubs = new Card(1, 11);
		tenOfSpades = new Card(4, 10);

		// example of mocking a object (a card in this example) using Mockito
		mockTwoOfClubs = Mockito.mock(Cardi.class);
		Mockito.when(mockTwoOfClubs.getRank()).thenReturn(1);
		Mockito.when(mockTwoOfClubs.getSuitNum()).thenReturn(1);
		Mockito.when(mockTwoOfClubs.getFaceNum()).thenReturn(2);
		
		// example of mocking a object (a card in this example)
//		private class DummyCard implements Cardi {
	//
//			@Override
//			public int getSuitNum() {
//				return 1;
//			}
	//
//			@Override
//			public int getFaceNum() {
//				return 2;
//			}
	//
//			@Override
//			public String getSuitString() {
//				return Card.CLUBS;
//			}
	//
//			@Override
//			public String getFaceString() {
//				return "2";
//			}
	//
//			@Override
//			public int getRank() {
//				return 1;
//			}
	//
//		}
		
		
		// another example of "mocking" 
//		twoOfClubs = new Cardi() {
//
//			@Override
//			public int getSuitNum() {
//				return 1;
//			}
//
//			@Override
//			public int getFaceNum() {
//				return 2;
//			}
//
//			@Override
//			public String getSuitString() {
//				return Card.CLUBS;
//			}
//
//			@Override
//			public String getFaceString() {
//				return "2";
//			}
//
//			@Override
//			public int getRank() {
//				return 1;
//			}	
//		};	
	}
	
	

	// ==================== Construction Tests ====================

	@Test
	public void testNewHandIsEmpty() {
		assertThat(hand.isEmpty(), is(true));
		assertThat(hand.size(), is(0));
	}

	// ==================== addCard Tests ====================

	@Test
	public void testSizeWhenOneCard() {
		//hand.addCard(aceOfSpades);
		
		
		//going to mock addCard
		hand = new TestHand(mockCard);
		
		assertThat(hand.size(), is(1));
	}

	@Test
	public void testAddMultipleCards() {
//		hand.addCard(aceOfSpades);
//		hand.addCard(kingOfHearts);
//		hand.addCard(queenOfDiamonds);
		//done using mock instead
		hand = new TestHand (kingOfHearts,aceOfSpades,queenOfDiamonds);
		assertThat(hand.size(), is(3));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddNullCard() {
		hand.addCard(null);
	}

	@Test
	public void testAddCardPreservesOrder() {
//		hand.addCard(aceOfSpades);
//		hand.addCard(kingOfHearts);
		//done using mock instead
		hand = new TestHand (aceOfSpades,kingOfHearts);
		assertThat(hand.getCard(0).toString(), is("Ace of Spades"));
		assertThat(hand.getCard(1).toString(), is("King of Hearts"));
	}

	// ==================== removeCard(Card) Tests ====================

	@Test
	public void testRemoveCardByObject() {
		hand.addCard(aceOfSpades);
		hand.addCard(kingOfHearts);
		boolean removed = hand.removeCard(aceOfSpades);
		assertThat(removed, is(true));
		assertThat(hand.size(), is(1));
	}

	@Test
	public void testRemoveCardNotFound() {
		hand.addCard(kingOfHearts);
		boolean removed = hand.removeCard(aceOfSpades); // not in hand
		assertThat(removed, is(false));
		assertThat(hand.size(), is(1)); // unchanged
	}

	@Test
	public void testRemoveCardFromEmptyHand() {
		boolean removed = hand.removeCard(aceOfSpades);
		assertThat(removed, is(false));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveNullCard() {
		hand.removeCard((Cardi) null);
	}

	@Test
	public void testRemoveCardMatchesExactSuitAndFace() {
		hand.addCard(aceOfSpades); // Ace of Spades
		Cardi aceOfHearts = new Card(3, 1); // Ace of Hearts
		boolean removed = hand.removeCard(aceOfHearts); // different suit
		assertThat(removed, is(false)); // should NOT match (different suit)
		assertThat(hand.size(), is(1));
	}

	// ==================== removeCard(int) Tests ====================

	@Test
	public void testRemoveCardByIndex() {
		hand.addCard(aceOfSpades);
		hand.addCard(kingOfHearts);
		Cardi removed = hand.removeCard(0); // remove first
		assertThat(removed.toString(), is("Ace of Spades"));
		assertThat(hand.size(), is(1));
	}

	@Test
	public void testRemoveCardByLastIndex() {
		hand.addCard(aceOfSpades);
		hand.addCard(kingOfHearts);
		Cardi removed = hand.removeCard(1); // remove last
		assertThat(removed.toString(), is("King of Hearts"));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testRemoveCardByNegativeIndex() {
		hand.addCard(aceOfSpades);
		hand.removeCard(-1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testRemoveCardByIndexTooLarge() {
		hand.addCard(aceOfSpades);
		hand.removeCard(1); // only index 0 exists
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testRemoveCardByIndexFromEmptyHand() {
		hand.removeCard(0);
	}

	// ==================== findCard Tests ====================

	@Test
	public void testFindCardExists() {
		hand.addCard(aceOfSpades);
		hand.addCard(kingOfHearts);
		Card found = hand.findCard(aceOfSpades);
		assertThat(found, is(notNullValue()));
		assertThat(found.toString(), is("Ace of Spades"));
	}

	@Test
	public void testFindCardNotExists() {
		hand.addCard(kingOfHearts);
		Card found = hand.findCard(aceOfSpades);
		assertThat(found, is(nullValue()));
	}

	@Test
	public void testFindCardNull() {
		hand.addCard(aceOfSpades);
		assertThat(hand.findCard(null), is(nullValue()));
	}

	@Test
	public void testFindCardEmptyHand() {
		assertThat(hand.findCard(aceOfSpades), is(nullValue()));
	}

	// ==================== getCard Tests ====================

	@Test
	public void testGetCardValidIndex() {
		hand.addCard(aceOfSpades);
		assertThat(hand.getCard(0), is(notNullValue()));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetCardInvalidIndex() {
		hand.getCard(0); // empty hand
	}

	// ==================== sortByRank Tests ====================

	@Test
	public void testSortByRankAscending() {
		hand.addCard(aceOfSpades); // highest rank
		hand.addCard(new Card(1, 2)); // lowest rank (2 of Clubs)
		hand.addCard(kingOfHearts); // high rank

		hand.sortByRank();

		// Should be: 2♣, K♥, A♠ (ascending rank)
		assertThat(hand.getCard(0).getFaceNum(), is(2)); // Two first
		assertThat(hand.getCard(1).getFaceNum(), is(13)); // King second
		assertThat(hand.getCard(2).getFaceNum(), is(1)); // Ace last (highest)
	}

	@Test
	public void testSortByRankEmptyHand() {
		hand.sortByRank(); // should not throw on empty hand
		assertThat(hand.size(), is(0));
	}

	@Test
	public void testSortByRankSingleCard() {
		hand.addCard(aceOfSpades);
		hand.sortByRank();
		assertThat(hand.size(), is(1));
		assertThat(hand.getCard(0).toString(), is("Ace of Spades"));
	}

	@Test
	public void testSortByRankFiveCards() {
		hand.addCard(aceOfSpades);
		hand.addCard(new Card(1, 3)); // 3 of Clubs
		hand.addCard(new Card(2, 7)); // 7 of Diamonds
		hand.addCard(new Card(3, 13)); // King of Hearts
		hand.addCard(new Card(1, 2)); // 2 of Clubs

		hand.sortByRank();

		// Expected order by rank ascending: 2♣, 3♣, 7♦, K♥, A♠
		assertThat(hand.getCard(0).getFaceNum(), is(2));
		assertThat(hand.getCard(1).getFaceNum(), is(3));
		assertThat(hand.getCard(2).getFaceNum(), is(7));
		assertThat(hand.getCard(3).getFaceNum(), is(13));
		assertThat(hand.getCard(4).getFaceNum(), is(1)); // Ace is highest rank
	}

	// ==================== sortByFaceValue Tests ====================

	@Test
	public void testSortByFaceValue() {
		hand.addCard(kingOfHearts); // face 13
		hand.addCard(aceOfSpades); // face 1
		hand.addCard(new Card(1, 5)); // face 5

		hand.sortByFaceValue();

		// Ascending by face: Ace(1), 5, King(13)
		assertThat(hand.getCard(0).getFaceNum(), is(1));
		assertThat(hand.getCard(1).getFaceNum(), is(5));
		assertThat(hand.getCard(2).getFaceNum(), is(13));
	}

	@Test
	public void testSortByFaceValueAceLow() {
		// sortByFaceValue treats Ace as 1 (lowest)
		hand.addCard(new Card(1, 2)); // 2
		hand.addCard(aceOfSpades); // Ace (1)
		hand.sortByFaceValue();
		assertThat(hand.getCard(0).getFaceNum(), is(1)); // Ace first
		assertThat(hand.getCard(1).getFaceNum(), is(2)); // 2 second
	}

	// ==================== numberOf Tests ====================

	@Test
	public void testNumberOfReturnsCorrectCount() {
		hand.addCard(new Card(1, 12)); // Queen of Clubs
		hand.addCard(new Card(2, 12)); // Queen of Diamonds
		hand.addCard(new Card(3, 12)); // Queen of Hearts
		hand.addCard(aceOfSpades);

		assertThat(hand.numberOf(12), is(3)); // three queens
		assertThat(hand.numberOf(1), is(1)); // one ace
	}

	@Test
	public void testNumberOfReturnsZeroForAbsent() {
		hand.addCard(aceOfSpades);
		assertThat(hand.numberOf(5), is(0)); // no fives
	}

	@Test
	public void testNumberOfEmptyHand() {
		assertThat(hand.numberOf(1), is(0));
	}

	@Test
	public void testNumberOfFourOfAKind() {
		for (int suit = 1; suit <= 4; suit++) {
			hand.addCard(new Card(suit, 7)); // all four sevens
		}
		assertThat(hand.numberOf(7), is(4));
	}

	// ==================== size / isEmpty / clear Tests ====================

	@Test
	public void testSizeAfterOperations() {
		assertThat(hand.size(), is(0));
		hand.addCard(aceOfSpades);
		assertThat(hand.size(), is(1));
		hand.addCard(kingOfHearts);
		assertThat(hand.size(), is(2));
		hand.removeCard(0);
		assertThat(hand.size(), is(1));
	}

	@Test
	public void testIsEmptyAfterAddAndRemove() {
		hand.addCard(aceOfSpades);
		assertThat(hand.isEmpty(), is(false));
		hand.removeCard(0);
		assertThat(hand.isEmpty(), is(true));
	}

	@Test
	public void testClear() {
		hand.addCard(aceOfSpades);
		hand.addCard(kingOfHearts);
		hand.clear();
		assertThat(hand.isEmpty(), is(true));
		assertThat(hand.size(), is(0));
	}

	// ==================== getCards Tests ====================

	@Test
	public void testGetCardsReturnsUnmodifiableList() {
		hand.addCard(aceOfSpades);
		List<Card> cards = hand.getCards();
		try {
			cards.add(kingOfHearts); // should throw
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
			// expected — list is unmodifiable
		}
	}

	@Test
	public void testGetCardsReturnsCorrectCards() {
		hand.addCard(aceOfSpades);
		hand.addCard(kingOfHearts);
		List<Card> cards = hand.getCards();
		assertThat(cards, hasSize(2));
		assertThat(cards.get(0).toString(), is("Ace of Spades"));
	}

	// ==================== toString Tests ====================

	@Test
	public void testToStringEmptyHand() {
		assertThat(hand.toString(), is("[Empty Hand]"));
	}

	@Test
	public void testToStringContainsCardNames() {
		hand.addCard(aceOfSpades);
		hand.addCard(kingOfHearts);
		String str = hand.toString();
		assertThat(str, containsString("Ace of Spades"));
		assertThat(str, containsString("King of Hearts"));
	}

	@Test
	public void testToStringContainsIndices() {
		hand.addCard(aceOfSpades);
		hand.addCard(kingOfHearts);
		String str = hand.toString();
		assertThat(str, containsString("[0]"));
		assertThat(str, containsString("[1]"));
	}

	@Test
	public void testToStringSingleCard() {
		hand.addCard(aceOfSpades);
		String str = hand.toString();
		assertThat(str, containsString("[0]"));
		assertThat(str, containsString("Ace of Spades"));
	}
}
