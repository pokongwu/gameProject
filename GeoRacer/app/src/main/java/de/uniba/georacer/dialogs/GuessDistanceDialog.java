package de.uniba.georacer.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.Marker;

import de.uniba.georacer.GameService;
import de.uniba.ioannidis.christos.georacer.R;

public class GuessDistanceDialog {
    public void showDialog(Context context, Marker marker, final GameService gameService) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.guess_input_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setTitle(String.format("Guess the distance to %s",marker.getTitle()))
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        gameService.safeGuess();
                        //TODO parse to int / double
                        System.out.println("your guess: " + userInputDialogEditText.getText().toString());
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }
}
