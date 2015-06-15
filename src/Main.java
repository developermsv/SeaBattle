import Controler.EventHandler;
import Model.CoreGame;
import View.GameWindow;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by developermsv on 13.05.2015.
 */
public class Main {
    public static EventHandler eventHandler;
    public static void main(String[] args) {
        // создаем контролер центральный обработчик событий
        eventHandler = new EventHandler();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    eventHandler.setObjView(new GameWindow(eventHandler));
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        CoreGame objModel = new CoreGame(eventHandler);
        eventHandler.setObjModel(objModel);
        objModel.startThreadModel();
        //game.NewGame();
    }
}
