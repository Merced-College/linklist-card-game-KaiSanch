import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Scanner;

public class CardGame {
    private static LinkedList<Card> cardList = new LinkedList<>();  // Deck of cards

    public static void main(String[] args) {
        // Load cards from the file
        loadCards("cards.txt");

        // Shuffle the deck
        Collections.shuffle(cardList);

        // Create player and dealer hands
        LinkedList<Card> playerHand = new LinkedList<>();
        LinkedList<Card> dealerHand = new LinkedList<>();

        // Deal initial cards
        playerHand.add(cardList.removeFirst());
        dealerHand.add(cardList.removeFirst());
        playerHand.add(cardList.removeFirst());
        dealerHand.add(cardList.removeFirst());

        // Display initial hands
        System.out.println("Your hand: " + playerHand);
        System.out.println("Dealer's visible card: " + dealerHand.getFirst());

        // Player's turn
        Scanner scanner = new Scanner(System.in);  // Create scanner here
        boolean playerBust = false;
        try {
            while (true) {
                System.out.print("Your total: " + calculateHandValue(playerHand) + ". Do you want to hit (H) or stand (S)? ");
                String choice = scanner.nextLine().toUpperCase();

                if (choice.equals("H")) {
                    Card newCard = cardList.removeFirst();
                    System.out.println("You drew: " + newCard);
                    playerHand.add(newCard);

                    if (calculateHandValue(playerHand) > 21) {
                        System.out.println("You busted!");
                        playerBust = true;
                        break;
                    }
                } else if (choice.equals("S")) {
                    break;
                } else {
                    System.out.println("Invalid choice. Please enter H or S.");
                }
            }

            // Dealer's turn
            if (!playerBust) {
                System.out.println("\nDealer's hand: " + dealerHand);
                while (calculateHandValue(dealerHand) < 17) {
                    Card newCard = cardList.removeFirst();
                    System.out.println("Dealer draws: " + newCard);
                    dealerHand.add(newCard);
                }

                if (calculateHandValue(dealerHand) > 21) {
                    System.out.println("Dealer busted! You win!");
                } else {
                    // Compare 
                    int playerTotal = calculateHandValue(playerHand);
                    int dealerTotal = calculateHandValue(dealerHand);

                    System.out.println("\nYour total: " + playerTotal);
                    System.out.println("Dealer's total: " + dealerTotal);

                    if (playerTotal > dealerTotal) {
                        System.out.println("You win!");
                    } else if (playerTotal < dealerTotal) {
                        System.out.println("Dealer wins!");
                    } else {
                        System.out.println("It's a tie!");
                    }
                }
            }
        } finally {
            scanner.close();  //close scanner
        }
    }

    // load cards from file
    private static void loadCards(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 4) {
                    String suit = details[0].trim();
                    String name = details[1].trim();
                    int value = Integer.parseInt(details[2].trim());
                    String pic = details[3].trim();

                    Card card = new Card(suit, name, value, pic);
                    cardList.add(card);
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // cSalculate the total value of a hand
    private static int calculateHandValue(LinkedList<Card> hand) {
        int totalValue = 0;
        int aceCount = 0;

        for (Card card : hand) {
            totalValue += card.getValue();
            if (card.getName().equalsIgnoreCase("Ace")) {
                aceCount++;
            }
        }

        while (totalValue > 21 && aceCount > 0) {
            totalValue -= 10;
            aceCount--;
        }

        return totalValue;
    }
}
