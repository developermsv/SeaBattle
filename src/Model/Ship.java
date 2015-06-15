package Model;

/**
 * Created by developermsv on 13.05.2015.
 */
public class Ship {
    boolean isHorizontal; // позиция судна вертикальная или горизонтальная
    private int amountDecks;
    Point leftUp;
    Point rightDown;

    int getAmountDecks() {
        return amountDecks;
    }

    // конструктор судна
    public Ship(int amountDecks, boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
        this.amountDecks = amountDecks;
        leftUp = new Point();
        rightDown = new Point();
    }

    public void setShipPosition(Point leftUp) {
        this.leftUp.setX(leftUp.getX());
        this.leftUp.setY(leftUp.getY());

        if (isHorizontal) {
            rightDown.setX(leftUp.getX() + amountDecks - 1);
            rightDown.setY(leftUp.getY());
        } else {
            rightDown.setX(leftUp.getX());
            rightDown.setY(leftUp.getY() + amountDecks - 1);
        }
    }
    boolean isIntersect(Ship ship) {
        if (ship.rightDown.getY() + 1 < leftUp.getY()) {
            return false;
        }
        if (ship.leftUp.getY() - 1 > rightDown.getY()) {
            return false;
        }
        if (ship.rightDown.getX() + 1 < leftUp.getX()) {
            return false;
        }
        if (ship.leftUp.getX() - 1 > rightDown.getX()) {
            return false;
        }
        return true;
    }

    boolean destroyOneDeck() {
        amountDecks--;
        return (amountDecks > 0);
    }
}
