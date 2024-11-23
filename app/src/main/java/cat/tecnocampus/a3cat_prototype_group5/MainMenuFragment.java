package cat.tecnocampus.a3cat_prototype_group5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class MainMenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_start_game).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_mainMenuFragment_to_gameMenuFragment)
        );

        view.findViewById(R.id.btn_raffle).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_mainMenuFragment_to_raffleFragment)
        );
    }
}
