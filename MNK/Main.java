package MNK;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Game game = new Game(false, new HumanPlayer(), new RandomPlayer());
        int result;
        String ans;
        Scanner scanner1 = new Scanner(System.in);
        System.out.println("New Game\n" + "Choose Mode: Match or Freeplay");
        String mode = scanner1.next();
        while (WrongMode(mode)) {
            System.out.println("Don't understand");
            mode = scanner1.next();
        }
        if (mode.equals("Match")) {
        Match match = new Match();
        match.RunMatch();
        } else {
            do {
                result = game.play(new MNK_Game());
                if (result != 0) {
                    System.out.println("Game result: Player " + result + " wins!");
                } else {
                    System.out.println("Game result: DRAW! ");
                }
                System.out.println("==============================");
                System.out.println("Play again?");
                ans = scanner1.next();
                while (WrongAns(ans)) {
                    System.out.println("Don't understand");
                    ans = scanner1.next();
                }
            } while (ans.equals("Yes"));
            System.out.println("Game over!");
        }
        scanner1.close();
    }

    public static boolean WrongAns(String str) {
        return !str.equals("Yes") && !str.equals("No");
    }

    public static boolean WrongMode(String str) {
        return !str.equals("Match") && !str.equals("Freeplay");
    }
}
