package de.uniba.georacer.ui.FinishList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.uniba.georacer.R;
import de.uniba.georacer.model.app.Result;

/**
 * initial source: https://www.journaldev.com/10416/android-listview-with-custom-adapter-example-tutorial
 * @author Ludwig Leuschner 
 */

public class CustomAdapter extends ArrayAdapter<Result>{

    private List<Result> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView round;
        TextView landmarkName1;
        TextView guessedDistanceLandmark1;
        TextView actualDistanceLandmark1;
        TextView differenceDistanceLandmark1;
        TextView landmarkName2;
        TextView guessedDistanceLandmark2;
        TextView actualDistanceLandmark2;
        TextView differenceDistanceLandmark2;
        TextView landmarkName3;
        TextView guessedDistanceLandmark3;
        TextView actualDistanceLandmark3;
        TextView differenceDistanceLandmark3;
        TextView landmarkName4;
        TextView guessedDistanceLandmark4;
        TextView actualDistanceLandmark4;
        TextView differenceDistanceLandmark4;
        TextView avgGuessingError;
        TextView waypointGuessingError;
    }

    public CustomAdapter(List<Result> data, Context context) {
        super(context, R.layout.row_game_finish, data);
        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Result result = getItem(position);
        ViewHolder viewHolder;
        final View view;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_game_finish, parent, false);
            viewHolder.round = convertView.findViewById(R.id.fow_finish_round_number);
            viewHolder.landmarkName1 = convertView.findViewById(R.id.row_finish_landmark_name_1);
            viewHolder.guessedDistanceLandmark1 = convertView.findViewById(R.id.row_finish_guessed_1);
            viewHolder.actualDistanceLandmark1 = convertView.findViewById(R.id.row_finish_actual_1);
            viewHolder.differenceDistanceLandmark1 = convertView.findViewById(R.id.row_finish_difference_1);
            viewHolder.landmarkName2 = convertView.findViewById(R.id.row_finish_landmark_name_2);
            viewHolder.guessedDistanceLandmark2 = convertView.findViewById(R.id.row_finish_guessed_2);
            viewHolder.actualDistanceLandmark2 = convertView.findViewById(R.id.row_finish_actual_2);
            viewHolder.differenceDistanceLandmark2 = convertView.findViewById(R.id.row_finish_difference_2);
            viewHolder.landmarkName3 = convertView.findViewById(R.id.row_finish_landmark_name_3);
            viewHolder.guessedDistanceLandmark3 = convertView.findViewById(R.id.row_finish_guessed_3);
            viewHolder.actualDistanceLandmark3 = convertView.findViewById(R.id.row_finish_actual_3);
            viewHolder.differenceDistanceLandmark3 = convertView.findViewById(R.id.row_finish_difference_3);
            viewHolder.landmarkName4 = convertView.findViewById(R.id.row_finish_landmark_name_4);
            viewHolder.guessedDistanceLandmark4 = convertView.findViewById(R.id.row_finish_guessed_4);
            viewHolder.actualDistanceLandmark4 = convertView.findViewById(R.id.row_finish_actual_4);
            viewHolder.differenceDistanceLandmark4 = convertView.findViewById(R.id.row_finish_difference_4);
            viewHolder.avgGuessingError = convertView.findViewById(R.id.row_finish_avg_error);
            viewHolder.waypointGuessingError = convertView.findViewById(R.id.row_finish_circular_lateration_error);

            view=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            view=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        view.startAnimation(animation);
        lastPosition = position;

        viewHolder.round.setText(result.getRound());
        viewHolder.landmarkName1.setText(result.getLandmarkName1());
        viewHolder.guessedDistanceLandmark1.setText(result.getGuessedDistanceLandmark1());
        viewHolder.actualDistanceLandmark1.setText(result.getActualDistanceLandmark1());
        viewHolder.differenceDistanceLandmark1.setText(result.getDifferenceDistanceLandmark1());
        viewHolder.landmarkName2.setText(result.getLandmarkName2());
        viewHolder.guessedDistanceLandmark2.setText(result.getGuessedDistanceLandmark2());
        viewHolder.actualDistanceLandmark2.setText(result.getActualDistanceLandmark2());
        viewHolder.differenceDistanceLandmark2.setText(result.getDifferenceDistanceLandmark2());
        viewHolder.landmarkName3.setText(result.getLandmarkName3());
        viewHolder.guessedDistanceLandmark3.setText(result.getGuessedDistanceLandmark3());
        viewHolder.actualDistanceLandmark3.setText(result.getActualDistanceLandmark3());
        viewHolder.differenceDistanceLandmark3.setText(result.getDifferenceDistanceLandmark3());
        viewHolder.landmarkName4.setText(result.getLandmarkName4());
        viewHolder.guessedDistanceLandmark4.setText(result.getGuessedDistanceLandmark4());
        viewHolder.actualDistanceLandmark4.setText(result.getActualDistanceLandmark4());
        viewHolder.differenceDistanceLandmark4.setText(result.getDifferenceDistanceLandmark4());
        viewHolder.avgGuessingError.setText(result.getAvgGuessingError());
        viewHolder.waypointGuessingError.setText(result.getWaypointGuessingError());
        return convertView;
    }
}

