package sweeper;

import javax.swing.*;

public class Game {

    private Bomb bomb;
    private Flag flag;
    private GameState state;

    public Game(int cols, int rows, int bombs) {
        Ranges.setSize(new Coord(cols, rows));
        bomb = new Bomb(bombs);
        flag = new Flag();
    }

    public void start() {
        bomb.start();
        flag.start();
        state = GameState.PLAYED;
    }

    public GameState getState() {
        return state;
    }

    public Box getBox(Coord coord) {
        if(flag.get(coord) == Box.OPENED) {
            return bomb.get(coord);
        }
        else {
            return flag.get(coord);
        }
    }

    public void pressLeftButton(Coord coord) {
        if(gameOver()) return;
        openBox(coord);
        checkWinner();
    }

    private boolean gameOver() {
        switch(state) {
            case PLAYED:
                return false;
            case BOMBED:
                JOptionPane.showMessageDialog(null,
                                        "<html><h3>YOU LOSE!</h3></html>",
                                            "Bombed",
                                                JOptionPane.INFORMATION_MESSAGE,
                                                new ImageIcon(getClass().getResource("/img/littleIcon.png")));
                break;
            case WINNER:
                JOptionPane.showMessageDialog(null,
                                            "<html><h3>CONGRATULATIONS!</h3></html>",
                                                "Winner",
                                            JOptionPane.INFORMATION_MESSAGE,
                                             new ImageIcon(getClass().getResource("/img/littleIcon.png")));
                break;
        }
        start();
        return true;
    }

    private void checkWinner() {
        if(state == GameState.PLAYED) {
            if(flag.getCountOfClosedBoxes() == bomb.getTotalBombs()) {
                state = GameState.WINNER;
            }
        }
    }

    private void openBox(Coord coord) {

        switch (flag.get(coord)) {

            case OPENED: setOpenedToClosedBoxesAroundNumber(coord); return;
            case FLAGED: return;
            case CLOSED:
                switch(bomb.get(coord)) {
                    case ZERO: openBoxesAround(coord); return;
                    case BOMB: openBombs(coord); return;
                    default  : flag.setOpenedToBox(coord); return;
                }
        }
    }

    private void setOpenedToClosedBoxesAroundNumber(Coord coord) {
        if(bomb.get(coord) != Box.BOMB) {
            if(flag.getCountFlagedBoxesAroundNumber(coord) == bomb.get(coord).getNumber()) {
                for (Coord around : Ranges.getCoordAround(coord)) {
                    if(flag.get(around) == Box.CLOSED) {
                        openBox(around);
                    }
                }
            }
        }

    }

    private void openBombs(Coord bombed) {
        state = GameState.BOMBED;
        flag.setBombedToBox(bombed);
        for (Coord coord : Ranges.getAllCoords()) {
            if(bomb.get(coord) == Box.BOMB) {
                flag.setOpenedToClosedBombBox(coord);
            }
            else {
                flag.setNoBombToFlagedSafeBox(coord);
            }
        }
    }

    private void openBoxesAround(Coord coord) {
        flag.setOpenedToBox(coord);
        for (Coord around : Ranges.getCoordAround(coord)) {
            openBox(around);
        }
    }

    public void pressRightButton(Coord coord) {
        if(gameOver()) return;
        flag.toggleFlagedToBox(coord);
    }
}
