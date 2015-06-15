package Model;

/**
 * Created by developermsv on 13.05.2015.
 */
public class Cell {
    // ��������� ������ � �������
    public enum TypeStatusCell {
        FREE_CELL,      // ���������
        CELL_NEAR_SHIP, // ����� � ������
        AFTER_SHOOT,    // ����� ��������
        DESTROYED_DECK, // ������������ ������
        DECK,  // ����� ������
        DESTROYED_SHIP // ������ ������������� �������
    };
    private TypeStatusCell statusCell;   //  �������� ��������� ������
    public Ship ship;                    // �������� ������ �� ������ �����
    public Cell (TypeStatusCell statusCell)
    {
        this.statusCell = statusCell;
    }
    public TypeStatusCell getStatusCell() {
        return statusCell;
    }

    public void setStatusCell(TypeStatusCell newStatusCell) {
        // ���������� ������, ��� AFTER_SHOOT �����, ������ �� FREE ������
        if (statusCell != TypeStatusCell.FREE_CELL && newStatusCell ==  TypeStatusCell.AFTER_SHOOT){
            return;
        }
        statusCell = newStatusCell;
    }
}
