package cat.tecnocampus.a3cat_prototype_group5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import cat.tecnocampus.a3cat_prototype_group5.Ranking.HighScoresAdapter;
import cat.tecnocampus.a3cat_prototype_group5.Ranking.Player;

public class RaffleFragment extends Fragment {

    private FirebaseFirestore db;
    private HighScoresAdapter adapter;
    private List<Player> players;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_raffle, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.high_scores_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        players = new ArrayList<>();
        adapter = new HighScoresAdapter(players);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadHighScores();

        return view;
    }

    private void loadHighScores() {
        db.collection("players")
            .orderBy("score", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    players.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String name = document.getString("name");
                        int score = document.getLong("score").intValue();
                        players.add(new Player(name, score));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
    }
}