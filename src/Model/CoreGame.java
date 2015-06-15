package Model;

import Controler.EventHandler;

/**
 * Created by developermsv on 13.05.2015.
 */
public class CoreGame {
    EventHandler eventHandler;
    private Field[] fields = new Field[2];
    public boolean showShip;
    public boolean autoShooting;
    int shootRow;
    int shootCol;
    private String stringInfo;
    public final int indexFieldPlayer = 0;
    public final int indexFieldComputer = 1;

    private enum resultShoot {BAD_SHOT, CONTINUE_SHOOT, END_GAME}

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public int getShootRow() {
        return shootRow;
    }

    public void setShootRow(int shootRow) {
        this.shootRow = shootRow;
    }

    public int getShootCol() {
        return shootCol;
    }

    public void setShootCol(int shootCol) {
        this.shootCol = shootCol;
    }

    private String nameWinner;
    private String nameWhoShot;

    public String playerName;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    // конструктор класса игры
    public CoreGame(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void newGame() {

        // инициализируем поля игрока и компьютера
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new Field();
            // расставляем суда на поле
            setShips(fields[i]);

        }
        fields[indexFieldPlayer].fieldName = playerName;     // задаем иимя полю
        eventHandler.repaintField(fields[indexFieldPlayer]); // выводим поле на экран
        fields[indexFieldComputer].fieldName = "Компьютер";  // задаем иимя полю
        eventHandler.repaintField(fields[indexFieldComputer]); // выводим поле на экран
        eventHandler.showInfoMessage(playerName.toUpperCase() + ", начинаем игру! Стрелять по полю под надписью 'Компьютер' удачи!");
    }

    // обработка выстрела
    private resultShoot doShoot(int indexField , int shootRow, int shootCol) {
        Field field = fields[indexField];
        resultShoot result = resultShoot.BAD_SHOT;
        Ship shipAtPos;
        stringInfo = "";
        try {
            Cell cell = field.cellsField[shootRow][shootCol];
            switch (cell.getStatusCell()) {
                case FREE_CELL:
                case CELL_NEAR_SHIP:
                    stringInfo = nameWhoShot + "Промах...";
                    cell.setStatusCell(Cell.TypeStatusCell.AFTER_SHOOT);
                    if(!autoShooting) {
                        eventHandler.playSound(Cell.TypeStatusCell.AFTER_SHOOT,indexField);
                    }
                    break;
                case DESTROYED_DECK:
                case AFTER_SHOOT:
                    stringInfo = nameWhoShot + "Сюда уже стреляли...";

                    if (autoShooting) {
                        return resultShoot.CONTINUE_SHOOT;// если режим авто выстрела, то разрешаем сделать еще один выстрел
                    } else {
                       eventHandler.playSound(Cell.TypeStatusCell.AFTER_SHOOT,indexField);
                    }
                    break;
                default:
                    field.amountAllDecks--; // уменьшаем количество оставшихся целых палуб на поле
                    shipAtPos = cell.ship;// получаем судно в позиции попадания
                    // делаем проверку на утопленность
                    if (shipAtPos.destroyOneDeck()) {
                        if(!autoShooting) {
                            eventHandler.playSound(Cell.TypeStatusCell.DESTROYED_DECK,indexField);
                        }
                        stringInfo = nameWhoShot + "Корабль ранен! У Вас еще один выстрел!";
                    } else {
                        if(!autoShooting) {
                            eventHandler.playSound(Cell.TypeStatusCell.DESTROYED_SHIP,indexField);
                        }
                        stringInfo = nameWhoShot + "Корабль потоплен! У Вас еще один выстрел!";
                        field.setAfterShootAroundShip(shipAtPos);
                    }
                    cell.setStatusCell(Cell.TypeStatusCell.DESTROYED_DECK);
                    result = resultShoot.CONTINUE_SHOOT;
            }
            if (field.amountAllDecks == 0) {
                stringInfo = nameWhoShot + "Все Корабли потоплены !!!";
                result = resultShoot.END_GAME;
            }
            return result;
        } finally {
            // выводим поле на экран после обработки выстрела
            eventHandler.repaintField(field);
            // когда авто выстрел лог не ведем
            if (!autoShooting)
                eventHandler.showInfoMessage(stringInfo);
        }
    }

    // у кого не осталось целых палуб на поле тот проиграл
    private boolean checkWinner() {
        boolean result = false;
        for (Field field : fields) {
            if (field.amountAllDecks > 0) nameWinner = field.fieldName;
            else result = true;
        }
        return result;
    }

    //расстановка судов
    public void setShips(Field field) {
        Point leftUp = new Point();
        for (Ship ship : field.ships) {
            do {
                // генерируем координаты (с учетом размера поля) для размещения cудна в поз. Х, Y
                if (ship.isHorizontal) {
                    leftUp.setX((int) (Math.random() * (Field.FIELD_SIZE - ship.getAmountDecks() - 1)));
                    leftUp.setY((int) (Math.random() * (Field.FIELD_SIZE - 1)));
                } else {
                    leftUp.setX((int) (Math.random() * (Field.FIELD_SIZE - 1)));
                    leftUp.setY((int) (Math.random() * (Field.FIELD_SIZE - ship.getAmountDecks() - 1)));
                }
                ship.setShipPosition(leftUp); // до проверки устанавливаем координаты
                // проверяем возможность размещения судна по данным координатам
                // если не подходят координаты, генерируем новые
            } while (!checkAvailabilityPosition(field.ships, leftUp, ship));
            field.setShipToField(ship);
        }
    }

    //проверка возможности установки судна на поле, в позицию X,Y
    public boolean checkAvailabilityPosition(Ship[] ships, Point leftUp, Ship shipIn) {
        for (Ship ship : ships) {
            if (ship != shipIn && ship.isIntersect(shipIn)) return false;
        }
        return true;
    }

    // выстрел компа
    private void doAutoShoot(int indexField) {
        //do {
            shootRow = (int) (Math.random() * (Field.FIELD_SIZE - 1));
            shootCol = (int) (Math.random() * (Field.FIELD_SIZE - 1));
        //}   while fields[indexField].cellsField[shootRow,shootCol].
    }

    // старт модели игры
    public void startThreadModel() {
        do {
            switch (eventHandler.getStatusControler()) {
                case NEW_GAME:
                    newGame();
                    if (autoShooting) {
                        doAutoShoot(indexFieldComputer);
                        eventHandler.setStatusControler(EventHandler.STATUS.NEW_SHOOT_PLAYER);
                    } else  {
                        eventHandler.setStatusControler(EventHandler.STATUS.WAIT_PLAYER);
                    }

                    break;
                case NEW_SHOOT_PLAYER: // выстрел игрока
                    nameWhoShot = "Выстрелил '"+fields[indexFieldPlayer].fieldName+"': ";
                    // обрабатываем выстрел по полю компа
                    switch (doShoot(indexFieldComputer, shootRow, shootCol)) {
                        case CONTINUE_SHOOT:
                            // игрок попал, ожидаем следукющий выстрел от игрока
                            if (autoShooting) {
                                doAutoShoot(indexFieldComputer);
                                eventHandler.setStatusControler(EventHandler.STATUS.NEW_SHOOT_PLAYER);
                            } else  {
                                eventHandler.setStatusControler(EventHandler.STATUS.WAIT_PLAYER);
                            }
                            break;
                        case BAD_SHOT:
                            // промах стреляет комп
                            eventHandler.setStatusControler(EventHandler.STATUS.NEW_SHOOT_COMPUTER);
                            break;
                        default:
                            eventHandler.setStatusControler(EventHandler.STATUS.WAIT_PLAYER);
                            eventHandler.playSound(null,indexFieldComputer);
                            eventHandler.showWinner("Поздравляем, "+ fields[indexFieldPlayer].fieldName + " вы победили! Будем играть еще?");
                    }
                    break;
                case NEW_SHOOT_COMPUTER: // выстрел компа
                    doAutoShoot(indexFieldPlayer);
                    nameWhoShot = "Выстрелил '"+fields[indexFieldComputer].fieldName+"': ";
                    // обрабатываем выстрел по полю игрока
                    switch (doShoot(indexFieldPlayer, shootRow, shootCol)) {
                        case CONTINUE_SHOOT:
                            // попадение, делаем ожидаем следующий выстрел компа
                            eventHandler.setStatusControler(EventHandler.STATUS.NEW_SHOOT_COMPUTER);
                            break;
                        case BAD_SHOT:
                            // промах, ожидаем выстрела игрока
                            // игрок попал, ожидаем следукющий выстрел от игрока
                            if (autoShooting) {
                                doAutoShoot(indexFieldPlayer);
                                eventHandler.setStatusControler(EventHandler.STATUS.NEW_SHOOT_PLAYER);
                            } else  {
                                eventHandler.setStatusControler(EventHandler.STATUS.WAIT_PLAYER);
                            }
                            break;
                        default:
                            eventHandler.setStatusControler(EventHandler.STATUS.WAIT_PLAYER);
                            eventHandler.playSound(null,indexFieldPlayer);
                            eventHandler.showWinner("Победил '"+fields[indexFieldComputer].fieldName+ "'! Никогда не сдавайтесь! Будем играть еще?");
                    }
                    break;
            }
            try {
                if (autoShooting) {
                    Thread.sleep(2);        //Приостанавливает поток на 2 мск
                } else {
                    Thread.sleep(100);        //Приостанавливает поток на 100 мск
                }

            } catch (InterruptedException e) {
            }
        } while (eventHandler.getStatusControler() != EventHandler.STATUS.STOP);
    }
}
