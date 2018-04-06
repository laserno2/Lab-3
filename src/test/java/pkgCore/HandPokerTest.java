package pkgCore;

import static org.junit.Assert.*;

import org.junit.Test;

import pkgEnum.eHandStrength;
import pkgEnum.eRank;
import pkgEnum.eSuit;

public class HandPokerTest {

	@Test
	public void FourOfAKindTest1() throws Exception {
		System.out.println("Four of a Kind");
		System.out.println("");
		HandPoker hp = new HandPoker();
		hp.AddCard(new Card(eSuit.CLUBS,eRank.TWO));
		hp.AddCard(new Card(eSuit.HEARTS,eRank.TWO));
		hp.AddCard(new Card(eSuit.DIAMONDS,eRank.TWO));
		hp.AddCard(new Card(eSuit.SPADES,eRank.TWO));
		hp.AddCard(new Card(eSuit.CLUBS,eRank.THREE));
		hp.ScoreHand();
		System.out.println("");
		HandScorePoker HSP = hp.getHSP();
		assertEquals(eHandStrength.FourOfAKind,HSP.geteHandStrength());
	}
	@Test
	public void FourOfAKindTest2() throws Exception {
		System.out.println("Four of a Kind");
		System.out.println("");
		HandPoker hp = new HandPoker();
		hp.AddCard(new Card(eSuit.CLUBS,eRank.THREE));
		hp.AddCard(new Card(eSuit.HEARTS,eRank.THREE));
		hp.AddCard(new Card(eSuit.DIAMONDS,eRank.THREE));
		hp.AddCard(new Card(eSuit.SPADES,eRank.THREE));
		hp.AddCard(new Card(eSuit.CLUBS,eRank.TWO));
		hp.ScoreHand();
		System.out.println("");
		HandScorePoker HSP = hp.getHSP();
		assertEquals(HSP.geteHandStrength(),eHandStrength.FourOfAKind);
		
		
	}
	
	
	@Test
	public void ThreeOfAKindTest1() throws Exception {
		System.out.println("Three of a Kind");
		System.out.println("");
		HandPoker hp = new HandPoker();
		hp.AddCard(new Card(eSuit.CLUBS,eRank.TWO));
		hp.AddCard(new Card(eSuit.HEARTS,eRank.TWO));
		hp.AddCard(new Card(eSuit.DIAMONDS,eRank.TWO));
		hp.AddCard(new Card(eSuit.SPADES,eRank.FIVE));
		hp.AddCard(new Card(eSuit.CLUBS,eRank.THREE));
		hp.ScoreHand();
		System.out.println("");
	}
	
	@Test
	public void TwoPairTest1() throws Exception {
		System.out.println("Two Pair");
		System.out.println("");
		HandPoker hp = new HandPoker();
		hp.AddCard(new Card(eSuit.CLUBS,eRank.TWO));
		hp.AddCard(new Card(eSuit.HEARTS,eRank.TWO));
		hp.AddCard(new Card(eSuit.DIAMONDS,eRank.FIVE));
		hp.AddCard(new Card(eSuit.SPADES,eRank.FIVE));
		hp.AddCard(new Card(eSuit.CLUBS,eRank.THREE));
		hp.ScoreHand();
		System.out.println("");
	}
	
	@Test
	public void FullHouseTest1() throws Exception {
		System.out.println("Full House");
		System.out.println("");
		HandPoker hp = new HandPoker();
		hp.AddCard(new Card(eSuit.CLUBS,eRank.TWO));
		hp.AddCard(new Card(eSuit.HEARTS,eRank.TWO));
		hp.AddCard(new Card(eSuit.DIAMONDS,eRank.FIVE));
		hp.AddCard(new Card(eSuit.SPADES,eRank.FIVE));
		hp.AddCard(new Card(eSuit.CLUBS,eRank.FIVE));
		hp.ScoreHand();
		
		assertEquals(hp.isFullHouse(),true);
		
		System.out.println("");
	}

}
