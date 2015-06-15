package Model;

/**
 * Created by developermsv on 13.05.2015.
 */
public class Cell {
    // состо€ни€ €чейки в объекте
    public enum TypeStatusCell {
        FREE_CELL,      // свободна€
        CELL_NEAR_SHIP, // р€дом с судном
        AFTER_SHOOT,    // после выстрела
        DESTROYED_DECK, // уничтоженна€ палуба
        DECK,  // цела€ палуба
        DESTROYED_SHIP // палуба уничтоженного корабл€
    };
    private TypeStatusCell statusCell;   //  содержит состо€ние €чейки
    public Ship ship;                    // содержит ссылку на объект судна
    public Cell (TypeStatusCell statusCell)
    {
        this.statusCell = statusCell;
    }
    public TypeStatusCell getStatusCell() {
        return statusCell;
    }

    public void setStatusCell(TypeStatusCell newStatusCell) {
        // установить €чейку, как AFTER_SHOOT можно, только во FREE €чейку
        if (statusCell != TypeStatusCell.FREE_CELL && newStatusCell ==  TypeStatusCell.AFTER_SHOOT){
            return;
        }
        statusCell = newStatusCell;
    }
}
