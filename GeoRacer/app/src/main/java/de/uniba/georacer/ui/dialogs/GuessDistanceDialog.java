package de.uniba.georacer.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.Marker;

import de.uniba.georacer.service.app.GameService;
//TODO check where this file is defined and move package to georacer
import de.uniba.ioannidis.christos.georacer.R;

public class GuessDistanceDialog {
    public void showDialog(Context context, Marker marker, final GameService gameService) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.guess_input_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        alertDialogBuilderUserInput
                .setTitle(String.format("Guess the distance to %s",marker.getTitle()))
                .setCancelable(false)
                .setPositiveButton("OK", (dialogBox, id) -> {
                    // logic is in validatorClickListener in order to prevent dismiss on wrong input
                })

                .setNegativeButton("Cancel",
                        (dialogBox, id) -> dialogBox.cancel());

        AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        ValidatorClickListener validatorClickListener = new ValidatorClickListener(alertDialog, marker, gameService);
        positiveButton.setOnClickListener(validatorClickListener);
    }
}
