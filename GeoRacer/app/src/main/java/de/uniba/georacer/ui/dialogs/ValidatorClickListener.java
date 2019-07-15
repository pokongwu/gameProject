package de.uniba.georacer.ui.dialogs;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import de.uniba.georacer.R;
import de.uniba.georacer.service.app.DialogGameServiceProxy;

/**
 * Custom ClickListener in order to catch a wrong formatted input. If an NumberFormatException is
 * thrown, the alert dialog won't disappear.
 *
 * @author Ludwig
 */
public class ValidatorClickListener implements View.OnClickListener {
    private final Dialog parentDialog;
    private final DialogGameServiceProxy dialogGameServiceProxy;
    private final Marker marker;

    public ValidatorClickListener(Dialog parentDialog, Marker marker, DialogGameServiceProxy dialogGameServiceProxy) {
        this.parentDialog = parentDialog;
        this.dialogGameServiceProxy = dialogGameServiceProxy;
        this.marker = marker;
    }

    @Override
    public void onClick(View view) {
        final EditText userInput = parentDialog.findViewById(R.id.userInputDialog);
        try {
            double guess = parseInput(userInput.getText().toString());
            if (this.marker.isVisible()) {
                this.marker.setIcon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
            dialogGameServiceProxy.saveGuess(marker.getTitle(), guess);
            parentDialog.dismiss();
        } catch (NumberFormatException numberFormatException) {
            Log.e("error", numberFormatException.getMessage());
        }
    }

    private double parseInput(String valueToParse) throws NumberFormatException {
        return Double.parseDouble(valueToParse.replaceAll(",", "."));
    }
}
