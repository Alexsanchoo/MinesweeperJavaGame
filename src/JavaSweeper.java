import sweeper.Box;
import sweeper.Coord;
import sweeper.Game;
import sweeper.Ranges;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JavaSweeper extends JFrame {

    private Game game;

    private JPanel panel;
    private JLabel label;
    private JMenuBar menuBar;

    private int COLS = 9;
    private int ROWS = 9;
    private int BOMBS = 10;
    private final int IMAGE_SIZE = 50;
    private final String[][] menuGameItems = { {"Game", ""},
                                            {"New", "F2"},
                                            {"Beginner", ""},
                                            {"Intermediate", ""},
                                            {"Expert", ""},
                                            {"Exit", ""}};

    public static void main(String[] args) {
        new JavaSweeper(9,9,10);
    }

    private JavaSweeper(int cols, int rows, int bombs) {
        COLS = cols;
        ROWS = rows;
        BOMBS = bombs;
        game = new Game(COLS, ROWS, BOMBS);
        game.start();
        setImages();
        initLabel();
        initPanel();
        initMenuBar();
        initFrame();
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();
        JMenu menu = createGameMenu();
        menuBar.add(menu);
        add(menuBar, BorderLayout.NORTH);

        //New F2
        menu.getItem(0).addActionListener(event -> {
            game.start();
            panel.repaint();
        });

        //Beginner
        menu.getItem(1).addActionListener(event -> {
            dispose();
            new JavaSweeper(9,9,10);
        });

        //Intermediate
        menu.getItem(2).addActionListener(event -> {
            dispose();
            new JavaSweeper(16,16,40);
        });

        //Expert
        menu.getItem(3).addActionListener(event -> {
            dispose();
            new JavaSweeper(30,16,99);
        });

        //Exit
        menu.getItem(4).addActionListener(event -> {
            dispose();
        });
    }

    private JMenu createGameMenu() {
        JMenu menu = new JMenu(menuGameItems[0][0]);
        for(int i = 1; i < menuGameItems.length; i++) {
            JMenuItem item = new JMenuItem(menuGameItems[i][0]);
            item.setAccelerator(KeyStroke.getKeyStroke(menuGameItems[i][1]));
            menu.add(item);
        }
        return menu;
    }

    private void initLabel() {
        label = new JLabel("Welcome!");
        add(label, BorderLayout.SOUTH);
    }


    private void initPanel() {
        panel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for(Coord coord : Ranges.getAllCoords()) {
                    g.drawImage((Image)game.getBox(coord).image,
                            coord.x * IMAGE_SIZE, coord.y * IMAGE_SIZE ,this);
                }
            }
        };

        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / IMAGE_SIZE;
                int y = e.getY() / IMAGE_SIZE;
                Coord coord = new Coord(x,y);
                switch(e.getButton()) {
                    case MouseEvent.BUTTON1 :
                        game.pressLeftButton(coord);
                        break;

                    case MouseEvent.BUTTON2:
                        game.start();
                        break;

                    case MouseEvent.BUTTON3:
                        game.pressRightButton(coord);
                        break;

                    default: break;
                }
                label.setText(getMessage());
                panel.repaint();
            }
        });

        panel.setPreferredSize(new Dimension(
                Ranges.getSize().x*IMAGE_SIZE,
                Ranges.getSize().y*IMAGE_SIZE));
        add(panel);
    }

    private String getMessage() {
        switch(game.getState()) {
            case PLAYED: return "Think, MORE THINK!";
            case BOMBED: return "YOU LOSE!";
                case WINNER: return "CONGRATULATIONS!";
            default: return "Welcome!";
        }
    }

    private void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Java Sweeper");
        setResizable(false);
        setVisible(true);
        setIconImage(getImage("icon"));
        pack();
        setLocationRelativeTo(null);
    }

    private void setImages() {
        for (Box box : Box.values()) {
            box.image = getImage(box.name().toLowerCase());
        }
    }

    private Image getImage(String name) {
        String filename = "img/" + name + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        return icon.getImage();
    }
}
