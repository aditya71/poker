/**
 * Created by Yuval Shabtai on 1/9/2017.
 */
public class Card {

    private final char suit;
    private final int value;

    public Card(char suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    public static Card parse(String card) {
        String valueString = "";
        for (int i = 0; i < card.length(); i++) {
            if (!Character.isDigit(card.charAt(i))) {
                break;
            }

            valueString += card.charAt(i);
        }

        return new Card(card.charAt(card.length() - 1), Integer.parseInt(valueString));
    }
    public char getSuit(){
	return suit;
    }
    public int getValue(){
	return value;
    }

    @Override
    public String toString() {

        return (value < 10 ? "0" : "") + value + suit;
    }
}
