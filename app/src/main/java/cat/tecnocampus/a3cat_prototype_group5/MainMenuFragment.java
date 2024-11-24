package cat.tecnocampus.a3cat_prototype_group5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainMenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_option).setOnClickListener(v -> {
            Fragment gameMenuFragment = new GameMenuFragment();
            ((MainActivity) getActivity()).loadFragment(gameMenuFragment);
        });

        view.findViewById(R.id.btn_raffle).setOnClickListener(v -> {
            Fragment raffleFragment = new RaffleFragment();
            ((MainActivity) getActivity()).loadFragment(raffleFragment);
        });
    }
}