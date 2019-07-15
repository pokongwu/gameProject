package de.uniba.georacer.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.Marker;

import de.uniba.georacer.R;
import de.uniba.georacer.service.app.DialogGameServiceProxy;

/**
 * Shows an input dialog for the user to enter his guess for the provided landmark.
 *
 * @author Ludwig
 */
public class GuessDistanceDialog {
    public void showDialog(Context context, Marker marker, DialogGameServiceProxy dialogGameServiceProxy) {
        View mView = View.inflate(context,R.layout.guess_input_dialog,null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);
        final EditText userInput = mView.findViewById(R.id.userInputDialog);
        userInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        userInput.setText(dialogGameServiceProxy.getGuess(marker.getTitle()));

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
        ValidatorClickListener validatorClickListener = new ValidatorClickListener(alertDialog, marker, dialogGameServiceProxy);
        positiveButton.setOnClickListener(validatorClickListener);
    }
}
