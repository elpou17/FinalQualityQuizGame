package quizgame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuizGame game = new QuizGame("resources/questions.json");
            game.setVisible(true);
        });
    }
}