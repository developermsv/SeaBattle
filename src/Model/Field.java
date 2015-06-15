package Model;
/**
 * Created by developermsv on 13.05.2015.
 */
public class Field {
    // �������� ����
    private final int AMOUNT_SHIPS = 10; // ���������� ��������
    private final int MAX_AMOUNT_DEKCS = 4; // ������������ ���-�� ����� �������
    static final int FIELD_SIZE = 10;    // ������ ���� ��� ����
    public String fieldName; // ������������ ����
    public Cell [][] cellsField; // ������� ������ ����
    public Ship[] ships; // ������ ����� ����
    int amountAllDecks; // ����� ���������� ����� �����
    // ����������� ������ ����
    public Field() {
        cellsField = new Cell[FIELD_SIZE][FIELD_SIZE];
        for (int i = 0; i < cellsField.length; i++) {
            for (int j = 0; j < cellsField[i].length; j++) {
                cellsField[i][j] =   new Cell(Cell.TypeStatusCell.FREE_CELL);
            }
        }
        createObjectsShip(); // ������� ������� ����� ����
    }
    // ������� ������� ����� � ����������� �� ���-�� AMOUNT_SHIPS
    // ������ ������ �� �������
    // 1 - 4 ������
    // 2 - 3 ������
    // 3 - 2 ������
    // 4 - 4 ������
    // ����� �������������� ������� �����, �� ��������� ���� �� �����������
    void createObjectsShip() {
        ships = new Ship[AMOUNT_SHIPS];
        int amoutDecks = MAX_AMOUNT_DEKCS;
        int i = 0;
        do {
            for (int j = 0; j != MAX_AMOUNT_DEKCS - amoutDecks + 1; j++) {
                ships[i] = new Ship(amoutDecks, (int) (Math.random() * 2) != 0);
                amountAllDecks +=amoutDecks;
                i++;
            }
            amoutDecks--;
        } while (i < ships.length);
    }

// ������������� ����� �� ����
    public void setShipToField(Ship ship) {
        if (ship.isHorizontal){
            for (int i = ship.leftUp.getX(); i <= ship.rightDown.getX(); i++) {
                Cell cellField = cellsField[ship.leftUp.getY()][i];
                cellField.setStatusCell(Cell.TypeStatusCell.DECK);
                cellField.ship = ship;
            }
        } else {
            for (int i = ship.leftUp.getY(); i <= ship.rightDown.getY(); i++) {
                Cell cellField = cellsField[i][ship.leftUp.getX()];
                cellField.setStatusCell(Cell.TypeStatusCell.DECK);
                cellField.ship = ship;
            }
        }
    }
    // ������������� ������ �������� ������ �����, ���� ��� ��������
    public void setNewStatusAroundCell(int row,int col){
        //����
        if (row-1 >= 0 && col-1 >=0){
            cellsField[row-1][col-1].setStatusCell(Cell.TypeStatusCell.AFTER_SHOOT);
        }
        if (row-1 >= 0){
            cellsField[row-1][col].setStatusCell(Cell.TypeStatusCell.AFTER_SHOOT);
        }
        if (row-1 >= 0 && col+1 < FIELD_SIZE){
            cellsField[row-1][col+1].setStatusCell(Cell.TypeStatusCell.AFTER_SHOOT);
        }
        //��������
        if (col-1 >=0){
            cellsField[row][col-1].setStatusCell(Cell.TypeStatusCell.AFTER_SHOOT);
        }
        if (col+1 <FIELD_SIZE){
            cellsField[row][col+1].setStatusCell(Cell.TypeStatusCell.AFTER_SHOOT);
        }
        //���
        if (row+1 < FIELD_SIZE && col-1 >=0){
            cellsField[row+1][col-1].setStatusCell(Cell.TypeStatusCell.AFTER_SHOOT);
        }
        if (row+1  < FIELD_SIZE){
            cellsField[row+1][col].setStatusCell(Cell.TypeStatusCell.AFTER_SHOOT);
        }
        if (row+1 < FIELD_SIZE && col+1 < FIELD_SIZE){
            cellsField[row+1][col+1].setStatusCell(Cell.TypeStatusCell.AFTER_SHOOT);
        }

    }

    public void setAfterShootAroundShip(Ship shipAtPos) {
        for (int i = 0; i < cellsField.length; i++) {
            for (int j = 0; j < cellsField[i].length; j++) {
                if (cellsField[i][j].ship == shipAtPos ) {
                    setNewStatusAroundCell(i, j); // ���������� ������� ������ ������
                }
            }
        }


    }
}
