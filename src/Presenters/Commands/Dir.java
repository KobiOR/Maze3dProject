package Presenters.Commands;


import Presenters.Deliver;
import Presenters.Presenter;

import static mazeGenerators.Maze3dGenerator.*;

/**
 * Created by orrko_000 on 12/09/2016.
 *
 */
public class Dir extends Deliver {

    public Dir(Presenter p) {
        super(p);
    }
    @Override
    public void doCommand(String cmd) {
        try {
            pDeliver.getView().display(pDeliver.getModel().dir(cmd));
        } catch (Exception e) {

            System.out.println("Catch NullPointerException");
            System.out.println("Fix your location");
        }
    }
}
