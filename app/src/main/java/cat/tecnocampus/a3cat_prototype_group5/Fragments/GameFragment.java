package cat.tecnocampus.a3cat_prototype_group5.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Random;
import cat.tecnocampus.a3cat_prototype_group5.R;

public class GameFragment extends Fragment {

    private ImageButton btnBack, gameCanvas;
    private TextView currentScore, currentRecord, endGameMessage, endGameScore;
    private LinearLayout endGameBubble;
    private Button btnPlayAgain, btnEnterRaffle;
    private SharedPreferences sharedPreferences;
    private int recordScore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);


        btnBack = view.findViewById(R.id.btn_back);
        gameCanvas = view.findViewById(R.id.game_canvas);
        currentScore = view.findViewById(R.id.current_score_number);
        currentRecord = view.findViewById(R.id.current_record_number);
        endGameMessage = view.findViewById(R.id.end_game_message);
        endGameScore = view.findViewById(R.id.end_game_score);
        endGameBubble = view.findViewById(R.id.end_game_bubble);
        btnPlayAgain = view.findViewById(R.id.btn_play_again);
        btnEnterRaffle = view.findViewById(R.id.btn_enter_raffle);


        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        recordScore = sharedPreferences.getInt("record_score", 0);
        currentRecord.setText(String.valueOf(recordScore));


        btnBack.setOnClickListener(v -> getActivity().onBackPressed());
        gameCanvas.setOnClickListener(v -> handleGameCanvasClick());
        btnPlayAgain.setOnClickListener(v -> resetGame());
        btnEnterRaffle.setOnClickListener(v -> goToRaffleScreen());

        return view;
    }

    private void handleGameCanvasClick() {
        Random random = new Random();
        int newScore = random.nextInt(46) + 10; //Genera números aleatoris entre 10 i 55
        currentScore.setText(String.valueOf(newScore));

        if (newScore > recordScore) {
            recordScore = newScore;
            sharedPreferences.edit().putInt("record_score", recordScore).apply();
            currentRecord.setText(String.valueOf(recordScore));
            endGameMessage.setText(R.string.string_new_record_message);
        } else {
            endGameMessage.setText(R.string.string_game_over_message);
        }

        endGameScore.setText(getString(R.string.score) + String.valueOf(newScore));
        endGameBubble.setVisibility(View.VISIBLE);
        gameCanvas.setEnabled(false);
    }

    private void resetGame() {
        endGameBubble.setVisibility(View.GONE);
        currentScore.setText("0");
        gameCanvas.setEnabled(true);
    }

    private void goToRaffleScreen() {
        RaffleFragment raffleFragment = new RaffleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("record_score", recordScore);
        raffleFragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, raffleFragment)
            .addToBackStack(null)
            .commit();
    }
}