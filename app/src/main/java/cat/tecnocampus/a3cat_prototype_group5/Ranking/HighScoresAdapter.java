package cat.tecnocampus.a3cat_prototype_group5.Ranking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import cat.tecnocampus.a3cat_prototype_group5.R;

public class HighScoresAdapter extends RecyclerView.Adapter<HighScoresAdapter.HighScoresViewHolder> {

    private List<Player> players;

    public HighScoresAdapter(List<Player> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public HighScoresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_high_score, parent, false);
        return new HighScoresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HighScoresViewHolder holder, int position) {
        Player player = players.get(position);
        holder.positionText.setText(String.valueOf(position + 1));
        holder.nameText.setText(player.getName());
        holder.scoreText.setText(String.valueOf(player.getScore()));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    static class HighScoresViewHolder extends RecyclerView.ViewHolder {
        TextView positionText;
        TextView nameText;
        TextView scoreText;

        HighScoresViewHolder(@NonNull View itemView) {
            super(itemView);
            positionText = itemView.findViewById(R.id.position_text);
            nameText = itemView.findViewById(R.id.name_text);
            scoreText = itemView.findViewById(R.id.score_text);
        }
    }
}