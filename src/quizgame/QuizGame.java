package quizgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class QuizGame extends JFrame {
    private List<Question> questions;
    private int currentQuestion = 0;
    private int score = 0;
    private int timeLeft = 60;
    private JLabel questionLabel, timerLabel;
    private JButton[] optionButtons = new JButton[4];
    private Timer timer;

    public QuizGame(String questionsFile) {
        setTitle("Quiz de Calidad de Software");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        questionLabel = new JLabel("", JLabel.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(questionLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 2));
        for (int i = 0; i < 4; i++) {
            int idx = i;
            optionButtons[i] = new JButton();
            optionButtons[i].addActionListener(e -> checkAnswer(idx));
            centerPanel.add(optionButtons[i]);
        }
        add(centerPanel, BorderLayout.CENTER);

        timerLabel = new JLabel("Tiempo: 60", JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(timerLabel, BorderLayout.SOUTH);

        loadQuestions(questionsFile);
        showQuestion();
    }

    private void loadQuestions(String path) {
        try (FileReader reader = new FileReader(path)) {
            questions = new Gson().fromJson(reader, new TypeToken<List<Question>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showQuestion() {
        if (currentQuestion >= questions.size()) {
            endGame();
            return;
        }

        Question q = questions.get(currentQuestion);
        questionLabel.setText((currentQuestion + 1) + ". " + q.question);
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(q.options[i]);
            optionButtons[i].setEnabled(true);
        }

        timeLeft = 60;
        timerLabel.setText("Tiempo: " + timeLeft);
        if (timer != null) timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    timeLeft--;
                    timerLabel.setText("Tiempo: " + timeLeft);
                    if (timeLeft <= 0) {
                        timer.cancel();
                        currentQuestion++;
                        showQuestion();
                    }
                });
            }
        }, 1000, 1000);
    }

    private void checkAnswer(int selected) {
        timer.cancel();
        Question q = questions.get(currentQuestion);
        if (selected == q.answer) {
            score++;
        }
        currentQuestion++;
        showQuestion();
    }

    private void endGame() {
        JOptionPane.showMessageDialog(this, "Juego terminado. Tu puntuación: " + score + "/" + questions.size());
        System.exit(0);
    }
}