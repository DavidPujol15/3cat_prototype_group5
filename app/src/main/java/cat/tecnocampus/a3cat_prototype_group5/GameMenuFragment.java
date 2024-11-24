package cat.tecnocampus.a3cat_prototype_group5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GameMenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_game, container, false);

        Button playButton = view.findViewById(R.id.btn_play);
        Button raffleButton = view.findViewById(R.id.btn_enter_raffle);

        playButton.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new GameFragment())
                .addToBackStack(null)
                .commit();
        });

        raffleButton.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new RaffleFragment())
                .addToBackStack(null)
                .commit();
        });

        return view;
    }
}