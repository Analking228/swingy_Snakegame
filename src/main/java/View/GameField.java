package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener{
    private final int   FIELD_SIZE = 320;
    private final int   CELL_SIZE = 16;
    private Image       dot;
    private Image       apple;
    private int         appleX;
    private int         appleY;
    private int[]       fieldX = new int[(FIELD_SIZE / CELL_SIZE) * (FIELD_SIZE / CELL_SIZE)];
    private int[]       fieldY = new int[(FIELD_SIZE / CELL_SIZE) * (FIELD_SIZE / CELL_SIZE)];
    private int         snakeSize;
    private Timer       timer;
    private boolean     left = false;
    private boolean     right = true;
    private boolean     up = false;
    private boolean     down = false;
    private boolean     inGame = true;

    public GameField() {
        setBackground(Color.black);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void     initGame() {
        this.snakeSize = 3;
        for (int i = 0; i < this.snakeSize; i++) {
            fieldX[i] = 48 - i * CELL_SIZE;
            fieldY[i] = 48;
        }
        timer = new Timer(250, this);
        timer.start();
        createApple();
    }

    public void     createApple() {
        this.appleX = new Random().nextInt(20) * CELL_SIZE;
        this.appleY = new Random().nextInt(20) * CELL_SIZE;
    }

    public void     loadImages() {
        ImageIcon   iia = new ImageIcon("src/main/resources/apple.jpg");
        this.apple = iia.getImage();
        ImageIcon   iid = new ImageIcon("src/main/resources/field.jpg");
        this.dot = iid.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.inGame) {
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < this.snakeSize; i++) {
                g.drawImage(dot, fieldX[i], fieldY[i], this);
            }
        } else {
            String gameOver = "Game Over";
            Font font =  new Font("Arial", Font.BOLD, 16);
            g.setColor(Color.white);
            g.setFont(font);
            g.drawString(gameOver, 115, FIELD_SIZE/2 - 8);
        }
    }

    public void     move() {
        for (int i = this.snakeSize; i > 0; i--) {
            fieldX[i] = fieldX[i - 1];
            fieldY[i] = fieldY[i - 1];
        }
        if (left)
            fieldX[0] -= CELL_SIZE;
        if (right)
            fieldX[0] += CELL_SIZE;
        if (up)
            fieldY[0] -= CELL_SIZE;
        if (down)
            fieldY[0] += CELL_SIZE;
    }

    public void     checkApple() {
        if (fieldX[0] == appleX && fieldY[0] == appleY) {
            this.snakeSize++;
            createApple();
        }
    }

    public void     checkCollisions() {
        for (int i = this.snakeSize; i > 0; i--) {
            if (i > 4 && fieldX[0] == fieldX[i] && fieldY[0] == fieldY[i]) {
                inGame = false;
                break;
            }
        }

        if (fieldX[0] > FIELD_SIZE)
            inGame = false;
        if (fieldY[0] > FIELD_SIZE)
            inGame = false;
        if (fieldX[0] < 0)
            inGame = false;
        if (fieldY[0] < 0)
            inGame = false;
    }

    @Override
    public void     actionPerformed(ActionEvent e) {
        if (this.inGame) {
            checkApple();
            checkCollisions();
            move();
        }
        this.repaint();
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_UP && !down) {
                left = false;
                up = true;
                right = false;
            }
            if (key == KeyEvent.VK_DOWN && !up) {
                left = false;
                down = true;
                right = false;
            }
        }
    }
}
