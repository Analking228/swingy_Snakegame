package View;

import View.SubClasses.MovingDir;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GamePlane extends JPanel implements ActionListener{
//    private Image           apple;
    private int             appleX;
    private int             appleY;
    private int             snakeSize;
    private boolean         inGame = true;
    private final int       FIELD_SIZE;
    private final int       CELL_SIZE;
    private final int[]     fieldCellX;
    private final int[]     fieldCellY;
    private final Timer     timer;
    private final MovingDir movingDir;

    public GamePlane(int gameFieldXSize, MovingDir movingDir) {
        FIELD_SIZE = gameFieldXSize;
        CELL_SIZE = FIELD_SIZE / 20;
        this.movingDir = movingDir;
        this.fieldCellX = new int[(FIELD_SIZE / CELL_SIZE) * (FIELD_SIZE / CELL_SIZE)];
        this.fieldCellY = new int[(FIELD_SIZE / CELL_SIZE) * (FIELD_SIZE / CELL_SIZE)];
        timer = new Timer(250, this);
        Action              upAction = new UpAction();
        Action              downAction = new DownAction();
        Action              leftAction = new LeftAction();
        Action              rightAction = new RightAction();
        this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "upAction");
        this.getActionMap().put("upAction", upAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "downAction");
        this.getActionMap().put("downAction", downAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "leftAction");
        this.getActionMap().put("leftAction", leftAction);
        this.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "rightAction");
        this.getActionMap().put("rightAction", rightAction);
        setBackground(Color.black);
//        loadImages();
        initGame();
    }

    public void             initGame() {
        System.out.println(CELL_SIZE);
        this.movingDir.setMovingRight();
        this.snakeSize = 3;
        for (int i = 0; i < this.snakeSize; i++) {
            fieldCellX[i] = CELL_SIZE*3 - i * CELL_SIZE;
            fieldCellY[i] = CELL_SIZE*3;
        }
        timer.start();
        createApple();
        setFocusable(true);
        requestFocusInWindow();
        requestFocus();
    }

    public void             createApple() {
        this.appleX = new Random().nextInt(20) * CELL_SIZE;
        this.appleY = new Random().nextInt(20) * CELL_SIZE;
    }

//    public void             loadImages() {
//        ImageIcon           iia = new ImageIcon("src/main/resources/apple.jpg");
//        this.apple = iia.getImage();
//    }

    public void             restartGame() {
        Component[]         componentList = this.getComponents();
        for(Component c : componentList) {
            if (c instanceof JButton)
                this.remove(c);
        }
        this.revalidate();
        this.repaint();
        this.inGame = true;
        initGame();
    }

    public void             setMovingRight() {
        if (!this.movingDir.getMovingDir().equals("left")) {
            this.movingDir.setMovingRight();
        }
    }

    public void             setMovingLeft() {
        if (!this.movingDir.getMovingDir().equals("right")) {
            this.movingDir.setMovingLeft();
        }
    }

    public void             setMovingUp() {
        if (!this.movingDir.getMovingDir().equals("down")) {
            this.movingDir.setMovingUp();
        }
    }

    public void             setMovingDown() {
        if (!this.movingDir.getMovingDir().equals("up")) {
            this.movingDir.setMovingDown();
        }
    }

    @Override
    protected void          paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.inGame) {
            g.setColor(Color.red);
            g.fillRect(appleX, appleY, CELL_SIZE, CELL_SIZE);
            for (int i = 0; i < this.snakeSize; i++) {
                g.setColor(Color.green);
                g.fillRect(fieldCellX[i], fieldCellY[i], CELL_SIZE, CELL_SIZE);
            }
        } else {
            timer.stop();
            JButton gameOverBtn = new JButton("Try again");
            gameOverBtn.setBounds(FIELD_SIZE/2 - FIELD_SIZE/6, FIELD_SIZE/2 + 15,FIELD_SIZE/3, FIELD_SIZE/10);
            gameOverBtn.addActionListener(e -> restartGame());
            this.add(gameOverBtn);
            String gameOver = "Game Over";
            Font font =  new Font("Arial", Font.BOLD, (int)(FIELD_SIZE/20));
            int stringWidth = g.getFontMetrics(font).stringWidth(gameOver);
            g.setColor(Color.white);
            g.setFont(font);
            g.drawString(gameOver, FIELD_SIZE/2 - (stringWidth/2) , FIELD_SIZE/2 - (FIELD_SIZE/30));
        }
    }

    public void             move() {
        for (int i = this.snakeSize; i > 0; i--) {
            fieldCellX[i] = fieldCellX[i - 1];
            fieldCellY[i] = fieldCellY[i - 1];
        }
        if (this.movingDir.getMovingDir().equals("left"))
            fieldCellX[0] -= CELL_SIZE;
        if (this.movingDir.getMovingDir().equals("right"))
            fieldCellX[0] += CELL_SIZE;
        if (this.movingDir.getMovingDir().equals("up"))
            fieldCellY[0] -= CELL_SIZE;
        if (this.movingDir.getMovingDir().equals("down"))
            fieldCellY[0] += CELL_SIZE;
    }

    public void             checkApple() {
//        System.out.println("Apple pos: " + this.appleX + "-" + this.appleY);
//        System.out.println("Snake head pos:" + this.fieldCellX[0] + "-" + this.fieldCellY[0]);
        if (fieldCellX[0] == appleX && fieldCellY[0] == appleY) {
            this.snakeSize++;
            createApple();
        }
    }

    public void             checkCollisions() {
        for (int i = this.snakeSize; i > 0; i--) {
            if (i > 4 && fieldCellX[0] == fieldCellX[i] && fieldCellY[0] == fieldCellY[i]) {
                inGame = false;
                break;
            }
        }
        if (fieldCellX[0] > FIELD_SIZE)
            inGame = false;
        if (fieldCellY[0] > FIELD_SIZE)
            inGame = false;
        if (fieldCellX[0] < 0)
            inGame = false;
        if (fieldCellY[0] < 0)
            inGame = false;
    }

    @Override
    public void             actionPerformed(ActionEvent e) {
        if (this.inGame) {
            checkApple();
            checkCollisions();
            move();
        }
        this.repaint();
    }

    class                   UpAction extends AbstractAction {
        @Override
        public void         actionPerformed(ActionEvent e) {
            setMovingUp();
        }
    }

    class                   DownAction extends AbstractAction {
        @Override
        public void         actionPerformed(ActionEvent e) {
            setMovingDown();
        }
    }

    class                   LeftAction extends AbstractAction {
        @Override
        public void         actionPerformed(ActionEvent e) {
            setMovingLeft();
        }
    }

    class                   RightAction extends AbstractAction {
        @Override
        public void         actionPerformed(ActionEvent e) {
            setMovingRight();
        }
    }
}