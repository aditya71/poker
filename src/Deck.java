import java.util.Stack;

/**
 * Created by Yuval Shabtai on 1/9/2017.
 */
public class Deck extends Stack<Card> {

    public Deck() {
        Card[] cards = new Card[52];

        for(int i = 0; i < cards.length; i++) {
            if (i < 13) {
                cards[i] = new Card('S', i % 13 + 1);
            } else if (i < 26) {
                cards[i] = new Card('H', i % 13 + 1);
            } else if (i < 39) {
                cards[i] = new Card('D', i % 13 + 1);
            } else if (i < 52) {
                cards[i] = new Card('C', i % 13 + 1);
            }
        }

        OneTimeRandom rnd = new OneTimeRandom(52);

        for(int i = 0; i < 52; i++) {
            int randomIndex = rnd.nextInt();
            add(cards[randomIndex]);
        }
    }

    private class OneTimeRandom {

        int currentIndex;
        int[] numbers;

        public OneTimeRandom(int numbers) {
            this.numbers = new int[numbers];
            currentIndex = 0;
            for(int i = 0; i < this.numbers.length; i++) {
                this.numbers[i] = i;
            }
        }

        public int nextInt() {
            int randomIndex = currentIndex + (int)(Math.random() * (numbers.length - currentIndex));

            int temp = numbers[randomIndex];
            numbers[randomIndex] = numbers[currentIndex];
            numbers[currentIndex] = temp;

            currentIndex += 1;
            return numbers[currentIndex - 1];
        }

    }
}
