package cat.tecnocampus.a3cat_prototype_group5.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.MotionTelltales;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

import cat.tecnocampus.a3cat_prototype_group5.R;
import cat.tecnocampus.a3cat_prototype_group5.Ranking.HighScoresAdapter;
import cat.tecnocampus.a3cat_prototype_group5.Ranking.Player;

public class RaffleFragment extends Fragment {

    private FirebaseFirestore db;
    private HighScoresAdapter adapter;
    private List<Player> players;
    private int recordScore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_raffle, container, false);

        TextView tv_inscription_error = view.findViewById(R.id.tv_inscription_error);

        RecyclerView recyclerView = view.findViewById(R.id.high_scores_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Button raffleButton = view.findViewById(R.id.btn_inscription);
        ImageButton backButton = view.findViewById(R.id.btn_back);

        if (getArguments() != null) {
            recordScore = getArguments().getInt("record_score", 0);
        }

        raffleButton.setOnClickListener(v -> {
            if (recordScore == 0) {
                tv_inscription_error.setText("No pots inscriure't sense haver jugat");
                tv_inscription_error.setVisibility(View.VISIBLE);
            } else {
                RaffleInscriptionFragment raffleInscriptionFragment = new RaffleInscriptionFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("record_score", recordScore);
                raffleInscriptionFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, raffleInscriptionFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        backButton.setOnClickListener(v -> getActivity().onBackPressed());

        players = new ArrayList<>();
        adapter = new HighScoresAdapter(players);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadHighScores();

        return view;
    }

    private void loadHighScores() {
        db.collection("players")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firestore", "Dades obtingudes correctament");
                        players.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                // Validar campos del documento
                                String name = document.getString("name");
                                String surname = document.getString("surname");
                                Long scoreLong = document.getLong("score");

                                // Si alguno de los campos es nulo, manejar el caso
                                if (name == null || surname == null || scoreLong == null) {
                                    Log.e("Firestore", "Falten camps al document: " + document.getId());
                                    continue;
                                }

                                int score = scoreLong.intValue();

                                // Crear el objeto Player y añadirlo a la lista
                                players.add(new Player(name, score, surname));
                            } catch (Exception e) {
                                Log.e("Firestore", "Error al processar el document: " + document.getId(), e);
                            }
                        }

                        // Notificar al adaptador que los datos han cambiado
                        adapter.notifyDataSetChanged();
                    } else {
                        // Log de error si la consulta falla
                        Log.e("Firestore", "Error al obtenir dades", task.getException());
                        Toast.makeText(getContext(), "Error al carregar dades: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}