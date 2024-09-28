import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class fb extends JPanel implements ActionListener, KeyListener {
    int w = 360;
    int h = 640;

    //images
    Image bgImg;
    Image bImg;
    Image tImg;
    Image boImg;

    int bx = w / 8;
    int by = w / 2;
    int bw = 34;
    int bh = 24;

    class Bd {
        int x = bx;
        int y = by;
        int w = bw;
        int h = bh;
        Image img;

        Bd(Image img) {
            this.img = img;
        }
    }

    int px = w;
    int py = 0;
    int pw = 64;
    int ph = 512;

    class Pp {
        int x = px;
        int y = py;
        int w = pw;
        int h = ph;
        Image img;
        boolean p = false;

        Pp(Image img) {
            this.img = img;
        }
    }

    // logic
    Bd b;
    int vx = -4;
    int vy = 0;
    int g = 1;

    ArrayList<Pp> pps;
    Random rand = new Random();

    Timer gameT;
    Timer pipeT;
    boolean over = false;
    double sc = 0;

    fb() {
        setPreferredSize(new Dimension(w, h));

        setFocusable(true);
        addKeyListener(this);

        bgImg = new ImageIcon(getClass().getResource("./bg.png")).getImage();
        bImg = new ImageIcon(getClass().getResource("./fbimg.png")).getImage();
        tImg = new ImageIcon(getClass().getResource("./top.png")).getImage();
        boImg = new ImageIcon(getClass().getResource("./bot.png")).getImage();

        b = new Bd(bImg);
        pps = new ArrayList<>();
        pipeT = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPipes();
            }
        });
        pipeT.start();

        // game timer
        gameT = new Timer(1000 / 60, this);
        gameT.start();
    }

    void addPipes() {
        int pyRand = (int) (py - ph / 4 - Math.random() * (ph / 2));
        int open = h / 4;

        Pp tp = new Pp(tImg);
        tp.y = pyRand;
        pps.add(tp);

        Pp bp = new Pp(boImg);
        bp.y = tp.y + ph + open;
        pps.add(bp);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // background
        g.drawImage(bgImg, 0, 0, this.w, this.h, null);

        // bird
        g.drawImage(bImg, b.x, b.y, b.w, b.h, null);

        // pipes
        for (int i = 0; i < pps.size(); i++) {
            Pp p = pps.get(i);
            g.drawImage(p.img, p.x, p.y, p.w, p.h, null);
        }

        // score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (over) {
            g.drawString("Game Over: " + String.valueOf((int) sc), 10, 35);
        } else {
            g.drawString(String.valueOf((int) sc), 10, 35);
        }
    }

    public void move() {
        vy += g;
        b.y += vy;
        b.y = Math.max(b.y, 0); // apply gravity

        // pipes
        for (int i = 0; i < pps.size(); i++) {
            Pp p = pps.get(i);
            p.x += vx;

            if (!p.p && b.x > p.x + p.w) {
                sc += 0.5;
                p.p = true;
            }
            if (checkCollision(b, p)) {
                over = true;
            }
        }

        if (b.y > h) {
            over = true;
        }
    }

    boolean checkCollision(Bd a, Pp p) {
        return a.x < p.x + p.w && a.x + a.w > p.x && a.y < p.y + p.h && a.y + a.h > p.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (over) {
            pipeT.stop();
            gameT.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            vy = -9;

            if (over) {
                b.y = by;
                vy = 0;
                pps.clear();
                over = false;
                sc = 0;
                gameT.start();
                pipeT.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
