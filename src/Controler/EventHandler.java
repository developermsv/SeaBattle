package Controler;

import Model.Cell;
import Model.CoreGame;
import Model.Field;
import View.GameWindow;
import View.MyJLabel;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * Created by developermsv on 05.06.2015.
 */
public class EventHandler implements MouseListener, ActionListener {
    GameWindow objView;
    CoreGame objModel;

    public void playSound(Cell.TypeStatusCell statusCell,int indexField) {
        if (statusCell == null) {
            if (indexField == objModel.indexFieldPlayer){
                objView.playSound(objView.ptsProigaral);
            } else {
                objView.playSound(objView.ptsPobeda);
            }
            return;
        }
       switch (statusCell){
           case AFTER_SHOOT:
               objView.playSound(objView.ptsAfterShootWater);
               break;
           case DESTROYED_DECK:
               if (indexField == objModel.indexFieldComputer) {
                   objView.playSound(objView.ptsEstpopadanie1);
               } else {
                   objView.playSound(objView.ptsVnaspopadanie1);
               }
               break;
           case DESTROYED_SHIP:
               if (indexField == objModel.indexFieldComputer) {
                   objView.playSound(objView.ptsKorablprotivnikapoptoplen1);
               } else {
                   objView.playSound(objView.ptsMipeteryalikorabl1);
               }
               break;
       }
    }

    public enum STATUS {NEW_SHOOT_PLAYER, NEW_GAME, STOP, WAIT_PLAYER, NEW_SHOOT_COMPUTER};

    public volatile STATUS statusControler;

    public EventHandler() {
        statusControler = STATUS.WAIT_PLAYER;
    }

    public void showInfoMessage(String s) {
        objView.showMessage(s);
    }

    public void repaintField(Field field) {

        Cell[][] cellsField = field.cellsField;
        for (int i = 0; i < cellsField.length; i++) {
            ImageIcon im = null;
            for (int j = 0; j < cellsField[i].length; j++) {
                switch (cellsField[i][j].getStatusCell()) {
                    case CELL_NEAR_SHIP:
                    case FREE_CELL:
                        im = null;
                        break;
                    case AFTER_SHOOT:
                        im = objView.imAfterShoot;
                        break;
                    case DESTROYED_DECK:
                        im = objView.imDeckDestroy;
                        break;
                    case DECK:
                        if ((objModel.showShip && field.fieldName != objModel.playerName) || (field.fieldName == objModel.playerName))
                            im = objView.imDeck;
                        break;
                    default:
                        im = null;
                }
                if (field.fieldName == objModel.playerName) {
                    objView.repaintLabel(objView.arrayPlayerField[i][j], im);
                } else {
                    objView.repaintLabel(objView.arrayComputerField[i][j], im);
                }

            }
        }
    }

    public STATUS getStatusControler() {
        return statusControler;
    }
    // диалог поздравления победителя и запрос новой игры
    public void showWinner(String s) {
        objView.showWinnerDialog(s);
    }

    public void setStatusControler(STATUS statusControler) {
        this.statusControler = statusControler;
    }

    public CoreGame getObjModel() {
        return objModel;
    }

    public void setObjModel(CoreGame objModel) {
        this.objModel = objModel;
    }

    public GameWindow getObjView() {
        return objView;
    }

    public void setObjView(GameWindow objView) {
        this.objView = objView;
    }

    public void setParamsGame() {
        objModel.autoShooting = objView.chAutoShooting.isSelected();
        objModel.showShip = objView.chShowShip.isSelected();
        objModel.playerName = objView.tePlayerName.getText();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        MyJLabel lb = ((MyJLabel) e.getComponent());
        lb.setIcon(lb.getStateIcon());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        MyJLabel lb = ((MyJLabel) e.getComponent());
        lb.setIcon(objView.imAim);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        MyJLabel lb = ((MyJLabel) e.getComponent());
        String str = lb.getName();
        objModel.setShootRow(Integer.valueOf(Arrays.asList(str.trim().split(" ")).get(0)));
        objModel.setShootCol(Integer.valueOf(Arrays.asList(str.trim().split(" ")).get(1)));
        statusControler = STATUS.NEW_SHOOT_PLAYER;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("btnNewGame") || e.getActionCommand().equals("btnStart2")) {
            if (objView.dialogWinner !=null){
                objView.dialogWinner.dispose();
            }
            objView.newGameWindow();
        }
        if (e.getActionCommand().equals("btnStart")) {
            objView.InitPanelCenter();
            setParamsGame();
            objView.dialogNewGame.dispose();
            statusControler = STATUS.NEW_GAME;
        }
        if (e.getActionCommand().equals("btnCancel")) {
            objView.dialogNewGame.dispose();
        }
        if (e.getActionCommand().equals("btnExit")) {
            System.exit(0);
        }

    }

}
