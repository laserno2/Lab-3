package pkgCore;

import java.util.ArrayList;
import java.util.Collections;
import pkgConstants.*;
import pkgEnum.eCardNo;
import pkgEnum.eHandStrength;
import pkgEnum.eRank;
import pkgEnum.eSuit;
import pkgException.HandException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class HandPoker extends Hand {

	private ArrayList<CardRankCount> CRC = null;
	private HandScorePoker HSP;

	public HandPoker() {
		this.setHS(new HandScorePoker());
	}

	protected ArrayList<CardRankCount> getCRC() {
		return CRC;
	}
	
	public HandScorePoker getHSP() {
		return (HandScorePoker) this.getHS();
	}

	@Override
	public HandScore ScoreHand() throws Exception {

		Collections.sort(super.getCards());
		Frequency();
		HSP = new HandScorePoker();

		if (super.getCards().size() != 5) {
			throw new HandException("Not five cards", this);
		}

		try {
			Class<?> c = Class.forName("pkgCore.HandPoker");

			for (eHandStrength hstr : eHandStrength.values()) {
				Method meth = c.getDeclaredMethod(hstr.getEvalMethod(), null);
				meth.setAccessible(true);
				Object o = meth.invoke(this, null);

				// If o = true, that means the hand evaluated- skip the rest of
				// the evaluations
				if ((Boolean) o) {
					break;
				}
			}

			Method methGetHandScorePoker = c.getDeclaredMethod("getHSP", null);
			HSP = (HandScorePoker) methGetHandScorePoker.invoke(this, null);

		} catch (ClassNotFoundException x) {
			x.printStackTrace();
		} catch (IllegalAccessException x) {
			x.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return HSP;
	}

	public boolean isRoyalFlush() {
		boolean bisRoyalFlush=false;
		if(isStraightFlush() && super.getCards().get(0).geteRank() == eRank.ACE && super.getCards().get(1).geteRank() == eRank.KING) {
			bisRoyalFlush=true;
			HandScorePoker HSP = (HandScorePoker) this.getHS();
			HSP.seteHandStrength(eHandStrength.RoyalFlush);
			int iGetCard = this.getCRC().get(0).getiCardPosition();
			HSP.setHiCard(this.getCards().get(iGetCard));
			HSP.setLoCard(null);
			HSP.setKickers(FindTheKickers(this.getCRC()));
			HSP.getKickers().remove(0);
			this.setHS(HSP);
		};
		return bisRoyalFlush;
	}

	public boolean isStraightFlush() {
		boolean bisStraightFlush=false;
		if (isFlush() && isStraight()) {
			HandScorePoker HSP = (HandScorePoker) this.getHS();
			HSP.seteHandStrength(eHandStrength.StraightFlush);
			int iGetCard = this.getCRC().get(0).getiCardPosition();
			HSP.setHiCard(this.getCards().get(iGetCard));
			HSP.setLoCard(null);
			HSP.setKickers(FindTheKickers(this.getCRC()));
			HSP.getKickers().remove(0);
			this.setHS(HSP);
			bisStraightFlush = true;
		}
		return bisStraightFlush;
	}
	
	public boolean isFourOfAKind() {
		boolean bisFourOfAKind = false;
		if (this.getCRC().size() == 2) {
			if (this.getCRC().get(0).getiCnt() == Constants.FOUR_OF_A_KIND) {
				HandScorePoker HSP = (HandScorePoker) this.getHS();
				HSP.seteHandStrength(eHandStrength.FourOfAKind);
				int iGetCard = this.getCRC().get(0).getiCardPosition();
				HSP.setHiCard(this.getCards().get(iGetCard));
				HSP.setLoCard(null);
				HSP.setKickers(FindTheKickers(this.getCRC()));
				HSP.getKickers().remove(0);
				this.setHS(HSP);
				bisFourOfAKind = true;
			}
		}
		return bisFourOfAKind;
	}

	public boolean isFullHouse() {
		boolean bisFullHouse = false;
		if (this.getCRC().size() == 2) {
			if ((this.getCRC().get(0).getiCnt() == Constants.THREE_OF_A_KIND)&&(this.getCRC().get(1).getiCnt() == Constants.TWO_OF_A_KIND)) {
				HandScorePoker HSP = (HandScorePoker) this.getHS();
				HSP.seteHandStrength(eHandStrength.FullHouse);
				int iGetCard = this.getCRC().get(0).getiCardPosition();
				HSP.setHiCard(this.getCards().get(iGetCard));
				HSP.setLoCard(this.getCards().get(iGetCard+1));
				HSP.setKickers(FindTheKickers(this.getCRC()));
				HSP.getKickers().remove(0);
				HSP.getKickers().remove(0);
				this.setHS(HSP);
				bisFullHouse = true;
			}
		}
		return bisFullHouse;
	}

	public boolean isFlush() {
		boolean bisFlush = false;

		int iCardCnt = super.getCards().size();
		int iSuitCnt = 0;

		for (eSuit eSuit : eSuit.values()) {
			for (Card c : super.getCards()) {
				if (eSuit == c.geteSuit()) {
					iSuitCnt++;
				}
			}
			if (iSuitCnt > 0)
				break;
		}

		if (iSuitCnt == iCardCnt) {
			bisFlush = true;
			HandScorePoker HSP = (HandScorePoker) this.getHS();
			HSP.seteHandStrength(eHandStrength.Flush);
			int iGetCard = this.getCRC().get(0).getiCardPosition();
			HSP.setHiCard(super.getCards().get(iGetCard));
			HSP.setLoCard(null);
			HSP.setKickers(FindTheKickers(this.getCRC()));
			HSP.getKickers().remove(0);
			this.setHS(HSP);
		}
		else
			bisFlush = false;

		return bisFlush;
	}
	//TODO: Fix isStraight
	public boolean isStraight() {
		boolean bisStraight = true;
		int a = 0;
		if (super.getCards().get(0).geteRank() == eRank.ACE) {
			a=1;
		}
		for (; a< super.getCards().size();a++) {
			if (super.getCards().get(a).geteRank().getiCardValue()-1 != super.getCards().get(a).geteRank().getiCardValue()) {
				bisStraight = false;
				break;
			}else if((super.getCards().get(1).geteRank() == eRank.ACE)&&(super.getCards().get(1).geteRank() == eRank.ACE)) {
				//high card is 5
			}
		}
		return bisStraight;
	}

	// This is how to implement one of the 'counting' hand types. Testing to see if
	// there are three of the same rank.
	public boolean isThreeOfAKind() {
		boolean bisThreeOfAKind = false;
		if (this.getCRC().size() == 3) {
			if (this.getCRC().get(0).getiCnt() == Constants.THREE_OF_A_KIND) {
				HandScorePoker HSP = (HandScorePoker) this.getHS();
				HSP.seteHandStrength(eHandStrength.ThreeOfAKind);
				int iGetCard = this.getCRC().get(0).getiCardPosition();
				HSP.setHiCard(this.getCards().get(iGetCard));
				HSP.setLoCard(null);
				HSP.setKickers(FindTheKickers(this.getCRC()));
				HSP.getKickers().remove(0);
				this.setHS(HSP);
				bisThreeOfAKind = true;
			}
		}
		return bisThreeOfAKind;
	}

	public boolean isTwoPair() {
		boolean bisTwoPair = false;
		if (this.getCRC().size() == 3) {
			if ((this.getCRC().get(0).getiCnt() == Constants.TWO_OF_A_KIND) &&(this.getCRC().get(1).getiCnt() == Constants.TWO_OF_A_KIND)) {
				HandScorePoker HSP = (HandScorePoker) this.getHS();
				HSP.seteHandStrength(eHandStrength.TwoPair);
				int iGetCard = this.getCRC().get(0).getiCardPosition();
				HSP.setHiCard(this.getCards().get(iGetCard));
				HSP.setLoCard(this.getCards().get(iGetCard+1));
				HSP.setKickers(FindTheKickers(this.getCRC()));
				HSP.getKickers().remove(0);
				HSP.getKickers().remove(0);
				this.setHS(HSP);
				bisTwoPair = true;
			}
		}
		return bisTwoPair;
	}

	public boolean isPair() {
		boolean bisPair = false;
		if (this.getCRC().size() == 4) {
			if (this.getCRC().get(0).getiCnt() == Constants.TWO_OF_A_KIND) {
				HandScorePoker HSP = (HandScorePoker) this.getHS();
				HSP.seteHandStrength(eHandStrength.Pair);
				int iGetCard = this.getCRC().get(0).getiCardPosition();
				HSP.setHiCard(this.getCards().get(iGetCard));
				HSP.setLoCard(null);
				HSP.setKickers(FindTheKickers(this.getCRC()));
				HSP.getKickers().remove(0);
				this.setHS(HSP);
				bisPair = true;
			}
		}
		return bisPair;
	}

	public boolean isHighCard() {
		if (this.getCRC().size() == 5) {
			if (this.getCRC().get(0).getiCnt() == Constants.ONE_OF_A_KIND) {
				HandScorePoker HSP = (HandScorePoker) this.getHS();
				HSP.seteHandStrength(eHandStrength.HighCard);
				int iGetCard = this.getCRC().get(0).getiCardPosition();
				HSP.setHiCard(this.getCards().get(iGetCard));
				HSP.setLoCard(null);
				HSP.setKickers(FindTheKickers(this.getCRC()));
				HSP.getKickers().remove(0);
				this.setHS(HSP);
				return true;
			}
		}
		return false;
	}

	private ArrayList<Card> FindTheKickers(ArrayList<CardRankCount> CRC) {
		ArrayList<Card> kickers = new ArrayList<Card>();

		for (CardRankCount crcCheck : CRC) {
			if (crcCheck.getiCnt() == 1) {
				kickers.add(this.getCards().get(crcCheck.getiCardPosition()));
			}
		}

		return kickers;
	}

	private void Frequency() {
		CRC = new ArrayList<CardRankCount>();
		int iCnt = 0;
		int iPos = 0;
		for (eRank eRank : eRank.values()) {
			iCnt = (CountRank(eRank));
			if (iCnt > 0) {
				iPos = FindCardRank(eRank);
				CRC.add(new CardRankCount(eRank, iCnt, iPos));
			}
		}
		Collections.sort(CRC);
	}

	private int CountRank(eRank eRank) {
		int iCnt = 0;
		for (Card c : super.getCards()) {
			if (c.geteRank() == eRank) {
				iCnt++;
			}
		}
		return iCnt;
	}

	private int FindCardRank(eRank eRank) {
		int iPos = 0;

		for (iPos = 0; iPos < super.getCards().size(); iPos++) {
			if (super.getCards().get(iPos).geteRank() == eRank) {
				break;
			}
		}
		return iPos;
	}

}
