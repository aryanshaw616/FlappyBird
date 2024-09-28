import javax.swing.*;

public class run {
    public static void main(String[] args) throws Exception {
        int w = 360;
        int h = 640;
        
        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(w, h);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fb flappyBird = new fb();  
        frame.add(flappyBird);
        frame.pack();
        flappyBird.requestFocus();
        frame.setVisible(true);
    }
}
