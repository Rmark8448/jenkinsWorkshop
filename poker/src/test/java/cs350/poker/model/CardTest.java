package cs350.poker.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Assert;
import org.junit.Test;

public class CardTest {

	private class TestCard extends Card {

		public TestCard(int s, int f) {
			super(s, f);
		}

		public int getFace() {
			return this.face;
		}

		public int getSuit() {
			return this.suit;
		}
	}

	// ----- Constructor Tests -----
	@Test
	public void test_constructor_valid() {
		TestCard c = new TestCard(1, 2);
		Assert.assertNotNull(c);
		Assert.assertEquals(1, c.getSuit());
		Assert.assertEquals(2, c.getFace());
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_constructor_invalid_suit_low() {
		new TestCard(0, 5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_constructor_invalid_suit_high() {
		new TestCard(5, 5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_constructor_invalid_face_low() {
		new TestCard(2, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_constructor_invalid_face_high() {
		new TestCard(2, 14);
	}

	// ----- Getter Tests ----

	@Test
	public void test_getSuitNum() {
		Cardi c = new Card(3, 8);
		Assert.assertEquals(3, c.getSuitNum());
	}

	@Test
	public void test_getFaceNum() {
		Cardi c = new Card(4, 11);
		Assert.assertEquals(11, c.getFaceNum());
	}

	@Test
	public void test_getSuitString() {
		Cardi c = new Card(3, 7);
		Assert.assertEquals("Hearts", c.getSuitString());
	}

	@Test
	public void test_getFaceString() {
		Cardi c = new Card(2, 12);
		Assert.assertEquals("Queen", c.getFaceString());
	}

	// ---- Rank Tests ----

	@Test
	public void testAllRanksUniqueAndCorrect() {
		boolean[] seen = new boolean[53]; // index 1..52
		for (int suit = 1; suit <= 4; suit++) {
			for (int face = 1; face <= 13; face++) {
				Cardi c = new Card(suit, face);
				int rank = c.getRank();
				// Check uniqueness
				Assert.assertFalse("Duplicate rank " + rank, seen[rank]);
				seen[rank] = true;
				// Check formula
				int expected;
				if (face == 1) { // Ace
					expected = 12 * 4 + suit;
				} else {
					expected = (face - 2) * 4 + suit;
				}
				Assert.assertEquals("Incorrect rank for " + c, expected, rank);
			}
		}
		// Verify all ranks 1-52
		for (int i = 1; i <= 52; i++) {
			Assert.assertTrue("Rank " + i + " missing", seen[i]);
		}
	}

	@Test
	public void test_rank_deuce_lowest() {
		Cardi c = new Card(1, 2);
		Assert.assertEquals(1, c.getRank());
	}

	@Test
	public void test_rank_ace_highest() {
		Cardi c = new Card(4, 1);
		Assert.assertEquals(52, c.getRank());
	}

	@Test
	public void testGetRank_queenOfHearts() {
		Cardi queenHearts = new Card(3, 12);
		Assert.assertEquals(43, queenHearts.getRank());
	}

	@Test
	public void test_rank_mid_card() {
		Cardi c = new Card(2, 5);
		int expected = (5 - 2) * 4 + 2;
		Assert.assertEquals(expected, c.getRank());
	}

	@Test
	public void testGetRank_twoOfClubs() {
		Card c = new Card(1, 2);
		int expected = (2 - 2) * 4 + c.suit;
		Assert.assertEquals(expected, c.getRank());
	}

	// --- compareTo Tests ----

	// negative if this card has lower rank, 0 if equal, positive if higher

	@Test
	public void test_compareTo_lessThan() {
		Card low = new Card(1, 2);
		Card high = new Card(4, 1);
		Assert.assertTrue(low.compareTo(high) < 0);
	}

	@Test
	public void test_compareTo_greaterThan() {
		Card high = new Card(4, 1);
		Card low = new Card(1, 2);
		Assert.assertTrue(high.compareTo(low) > 0);
	}

	@Test
	public void test_compareTo_equal() {
		Card c1 = new Card(3, 10);
		Card c2 = new Card(3, 10);
		Assert.assertEquals(0, c1.compareTo(c2));
	}

	// --- faceEquals Tests -----

	@Test
	public void test_faceEquals_true() {
		Card c1 = new Card(1, 7);
		Card c2 = new Card(4, 7);
		Assert.assertTrue(c1.faceEquals(c2));
	}

	@Test
	public void test_faceEquals_false() {
		Card c1 = new Card(1, 7);
		Card c2 = new Card(4, 9);
		Assert.assertFalse(c1.faceEquals(c2));
	}

	@Test
	public void test_faceEquals_null() {
		Card c1 = new Card(1, 7);
		Assert.assertFalse(c1.faceEquals(null));
	}

	// ---- suit String Tests ----
	@Test
	public void testGetSuitString_AllSuits() {
		// Suit 1 = Clubs, 2 = Diamonds, 3 = Hearts, 4 = Spades
		String[] expected = { "", "Clubs", "Diamonds", "Hearts", "Spades" }; // index 0 unused
		for (int suit = 1; suit <= 4; suit++) {
			Cardi c = new Card(suit, 5); // any face works
			Assert.assertEquals("Suit " + suit, expected[suit], c.getSuitString());
		}
	}

	// ---- face String Tests ----
	@Test
	public void testGetFaceString_AllFaces() {
		// Face 1 = Ace, 2 = "2", 3 = "3", ... 10 = "10", 11 = "Jack", 12 = "Queen", 13
		// = "King"
		String[] expected = { "", // 0 = unused
				"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
		for (int face = 1; face <= 13; face++) {
			Cardi c = new Card(1, face); // any suit works
			Assert.assertEquals("Face " + face, expected[face], c.getFaceString());
		}
	}

	// ----- equals(object) test ----

	@Test
	public void testEquals_SameObject() {
		Card c = new Card(2, 5);
		assertTrue(c.equals(c));
	}

	@Test
	public void testEquals_EqualCards() {
		Card c1 = new Card(3, 10);
		Card c2 = new Card(3, 10);
		assertTrue(c1.equals(c2));
		assertTrue(c2.equals(c1));
	}

	@Test
	public void testEquals_DifferentSuit() {
		Card c1 = new Card(1, 7);
		Card c2 = new Card(2, 7);
		assertFalse(c1.equals(c2));
	}

	@Test
	public void testEquals_DifferentFace() {
		Card c1 = new Card(4, 8);
		Card c2 = new Card(4, 9);
		assertFalse(c1.equals(c2));
	}

	@Test
	public void testEquals_Null() {
		Card c = new Card(1, 1);
		assertFalse(c.equals(null));
	}
}