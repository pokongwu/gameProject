package de.uniba.georacer.ui.dialogs;

import android.app.Dialog;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.Marker;

import de.uniba.georacer.service.app.GameService;
import de.uniba.ioannidis.christos.georacer.R;

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
            gameService.saveGuess(marker.getTitle(), guess);
            parentDialog.dismiss();
        } catch (NumberFormatException numberFormatException) {
            //TODO snackbar is in background and not sufficient for a warning
            gameService.showSnackbar("Please enter a valid number.");
        }
    }

    private double parseInput(String valueToParse) throws NumberFormatException {
        return Double.parseDouble(valueToParse.replaceAll(",", "."));
    }
}
