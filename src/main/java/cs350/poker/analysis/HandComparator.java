package cs350.poker.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cs350.poker.model.Cardi;
import cs350.poker.model.Hand;

/**
 * Compares two five-card poker hands to determine which hand wins.
 *
 * <p>
 * Implements {@link Comparator} for {@link Hand} objects. The comparison first
 * checks the hand type (e.g., flush beats straight). If both hands share the
 * same type, a tiebreaker is applied based on the highest relevant card ranks.
 * </p>
 *
 * <p>
 * Comparison result semantics:
 * </p>
 * <ul>
 * <li>Negative: hand1 loses to hand2</li>
 * <li>Zero: tie</li>
 * <li>Positive: hand1 beats hand2</li>
 * </ul>
 *
 * @author Poker Demo
 * @version 1.0
 */
public class HandComparator implements Comparator<Hand> {

	protected HandAnalyzer createAnalyzer(Hand hand) {
		return new HandAnalyzer(hand);
	}

	/**
	 * Constructs a new HandComparator.
	 */
	public HandComparator() {
		// no initialization needed
	}

	/**
	 * Compares two poker hands to determine the winner.
	 *
	 * <p>
	 * First compares by hand type (pair, flush, etc.). If equal, breaks the tie by
	 * comparing card ranks from highest to lowest.
	 * </p>
	 *
	 * @param hand1 the first hand
	 * @param hand2 the second hand
	 * @return negative if hand1 loses, positive if hand1 wins, zero for tie
	 * @throws IllegalArgumentException if either hand is null or not exactly 5
	 *                                  cards
	 */
	@Override
	public int compare(Hand hand1, Hand hand2) {
		if (hand1 == null || hand2 == null) {
			throw new IllegalArgumentException("Hands cannot be null.");
		}

		HandAnalyzer analyzer1 = createAnalyzer(hand1);
		HandAnalyzer analyzer2 = createAnalyzer(hand2);

		int type1 = analyzer1.getHandType();
		int type2 = analyzer2.getHandType();

		/* If hand types differ, the higher type wins */
		if (type1 != type2) {
			return Integer.compare(type1, type2);
		}

		/* Same hand type — use tiebreaker */
		return compareSameType(hand1, hand2, type1);
	}

	/**
	 * Breaks a tie between two hands of the same type by comparing individual card
	 * ranks from highest to lowest.
	 *
	 * <p>
	 * For pair-based hands, the paired cards are compared first, followed by the
	 * remaining kickers. For other hand types, cards are simply compared in
	 * descending rank order.
	 * </p>
	 *
	 * @param hand1    first hand
	 * @param hand2    second hand
	 * @param handType the shared hand type
	 * @return negative, zero, or positive comparison result
	 */
	private int compareSameType(Hand hand1, Hand hand2, int handType) {
		/*
		 * Get rank-sorted cards in descending order for both hands. This allows
		 * comparing from highest card down.
		 */
		List<Integer> ranks1 = getSortedRanksDescending(hand1);
		List<Integer> ranks2 = getSortedRanksDescending(hand2);

		/* Compare rank by rank from highest to lowest */
		for (int i = 0; i < ranks1.size(); i++) {
			int cmp = Integer.compare(ranks1.get(i), ranks2.get(i));
			if (cmp != 0) {
				return cmp; // first difference determines winner
			}
		}
		return 0; // perfect tie
	}

	/**
	 * Extracts card ranks from a hand and returns them sorted in descending order.
	 *
	 * @param hand the hand to extract ranks from
	 * @return a list of card ranks sorted highest first
	 */
	private List<Integer> getSortedRanksDescending(Hand hand) {
		List<Integer> ranks = new ArrayList<>();
		for (Cardi c : hand.getCards()) {
			ranks.add(c.getRank());
		}
		ranks.sort(Collections.reverseOrder()); // descending
		return ranks;
	}
}
