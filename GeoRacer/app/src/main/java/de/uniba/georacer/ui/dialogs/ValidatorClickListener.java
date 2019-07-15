package de.uniba.georacer.ui.dialogs;

import android.app.Dialog;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import de.uniba.georacer.R;
import de.uniba.georacer.service.app.GameService;

public class ValidatorClickListener implements View.OnClickListener {
    private final Dialog parentDialog;
    private final GameService gameService;
    private final Marker marker;

    public ValidatorClickListener(Dialog parentDialog, Marker marker, GameService gameService) {
        this.parentDialog = parentDialog;
        this.gameService = gameService;
        this.marker = marker;
    }

    @Override
    public void onClick(View view) {
        final EditText userInput = parentDialog.findViewById(R.id.userInputDialog);
        try {
            double guess = parseInput(userInput.getText().toString());
            //TODO use different landmarkId
            if (this.marker.isVisible()) {
                this.marker.setIcon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
            gameService.saveGuess(marker.getTitle(), guess);
            parentDialog.dismiss();
        } catch (NumberFormatException numberFormatException) {
            gameService.showSnackbar("Please enter a valid number.");
        }
    }

    private double parseInput(String valueToParse) throws NumberFormatException {
        return Double.parseDouble(valueToParse.replaceAll(",", "."));
    }
}
