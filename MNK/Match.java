package MNK;
import java.util.Scanner;

public class Match {

    public void RunMatch() {
        Scanner scan = new Scanner(System.in);
        final Game game = new Game(false, new RandomPlayer(), new RandomPlayer());
        int winscore;
        int round = 0;
        int result;
        int[] points = new int[]{0, 0};
        F1:
        while (true) {
            do {
                try {
                    System.out.println("Enter the number of winscore");
                    winscore = Integer.parseInt(scan.next());
                    if (!isMatchImpossible(winscore)) {
                        break F1;
                    }
                } catch (NumberFormatException inp) {
                    System.out.println("Wrong input. Try again");
                    break;
                }
                System.out.println("Impossible Match");
            } while (isMatchImpossible(winscore));
        }
        do {
            result = game.play(new MNK_Game());
            if (result != 0) {
                round++;
                if (round % 2 == 0) {
                    result = 3 - result;
                }
                points[result - 1]++;
                System.out.println("Game result: Player " + result + " wins!");
                System.out.println("==============================");
                Player temp = game.player1;
                game.player1 = game.player2;
                game.player2 = temp;
            } else {
                System.out.println("Game result: DRAW! ");
                System.out.println("==============================");
            }
        } while (points[0] < winscore && points[1] < winscore);
        System.out.println("==============================");
        int winner = points[result - 1] > points[2 - result] ? result : 3 - result;
        System.out.println("Match result: Player " + winner + " wins!");
        System.out.println("==============================");
        scan.close();
    }

    public static boolean isMatchImpossible(int k) {
        return k < 1;
    }
}
