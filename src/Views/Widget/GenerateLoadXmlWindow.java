package Views.Widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import java.io.File;

/**
 * Created by orrko_000 on 08/10/2016.
 */
public class GenerateLoadXmlWindow extends DialogWindow {

    @Override
    protected void initWidgets() {
        FileDialog fd = new FileDialog(shell, SWT.OPEN);
        fd.setText("Open");
        fd.setFilterPath("C:/");
        String[] filterExt = {"*.xml"};
        fd.setFilterExtensions(filterExt);
        String selected = fd.open();
        if(selected!=null) {
            File f = new File(selected);
            setChanged();
            notifyObservers("load_properties "+f.toString());

        }
        while (!shell.isDisposed()) {
            if (!shell.getDisplay().readAndDispatch())
                shell.getDisplay().sleep();
        }
        shell.getDisplay().dispose();
    }
}
