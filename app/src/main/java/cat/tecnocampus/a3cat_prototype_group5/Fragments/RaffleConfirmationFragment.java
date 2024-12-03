package cat.tecnocampus.a3cat_prototype_group5.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cat.tecnocampus.a3cat_prototype_group5.R;

import android.content.Intent;
import android.widget.Toast;

public class RaffleConfirmationFragment extends Fragment {

    private LinearLayout confirmationBuddle, sharingBuddle;

    private Button btnReturntoMain;
    private ImageButton btnShare,btnWhatsapp,btnX;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_raffle_confirmation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        confirmationBuddle = view.findViewById(R.id.confirmation_bubble);
        sharingBuddle = view.findViewById(R.id.sharing_bubble);
        btnReturntoMain = view.findViewById(R.id.button_return_to_main);
        btnShare = view.findViewById(R.id.button_share);
        btnWhatsapp = view.findViewById(R.id.button_whatsapp);
        btnX = view.findViewById(R.id.button_x);


        btnShare.setOnClickListener(v -> handleShareClick());
        btnWhatsapp.setOnClickListener(v -> handleWhatsappClick());
        btnX.setOnClickListener(v -> handleXClick());
        btnReturntoMain.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MainMenuFragment())
                .addToBackStack(null)
                .commit();
        });
    }

    private void handleShareClick() {
        confirmationBuddle.setVisibility(View.GONE);
        sharingBuddle.setVisibility(View.VISIBLE);
    }

    private void handleWhatsappClick() {
        String message = getString(R.string.social_media_message) + " " + "\n EVA: https://www.3cat.cat/3cat/eva/";
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");

        try {
            startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "WhatsApp is not installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleXClick() {
        String message = getString(R.string.social_media_message) + " " +
                         "\n EVA: https://www.3cat.cat/3cat/eva/" +
                         "\n\n#EVAxFCB #NouCampNou #SorteigFCB #JaSocDins";
        String url = "https://twitter.com/intent/tweet?text=" + Uri.encode(message);

        Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        sendIntent.setPackage("com.twitter.android");

        try {
            startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "X is not installed.", Toast.LENGTH_SHORT).show();
        }
    }
}