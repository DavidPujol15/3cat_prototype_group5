package cat.tecnocampus.a3cat_prototype_group5.Fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import cat.tecnocampus.a3cat_prototype_group5.R;

public class RaffleInscriptionFragment extends Fragment {

    private EditText nameEditText, emailEditText, surnameEditText, birthDateEditText;
    private TextView nameErrorTextView, surnameErrorTextView, emailErrorTextView, birthDateErrorTextView;
    private int recordScore;
    private FirebaseFirestore db;
    private ImageButton btnBack;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_raffle_inscription, container, false);

        nameEditText = view.findViewById(R.id.et_name);
        surnameEditText = view.findViewById(R.id.et_surname);
        emailEditText = view.findViewById(R.id.et_email);
        birthDateEditText = view.findViewById(R.id.et_birth_date);
        nameErrorTextView = view.findViewById(R.id.tv_name_error);
        surnameErrorTextView = view.findViewById(R.id.tv_surname_error);
        emailErrorTextView = view.findViewById(R.id.tv_email_error);
        birthDateErrorTextView = view.findViewById(R.id.tv_birth_date_error);
        Button inscriptionButton = view.findViewById(R.id.btn_confirm_participation);
        btnBack = view.findViewById(R.id.btn_back);

        Bundle bundle = getArguments();
        if (bundle != null) {
            recordScore = bundle.getInt("record_score", 0);
        }

        db = FirebaseFirestore.getInstance();

        inscriptionButton.setOnClickListener(v -> saveInscription());
        btnBack.setOnClickListener(v -> getActivity().onBackPressed());
        return view;
    }

    private void saveInscription() {
        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String birthDate = birthDateEditText.getText().toString();


        if (!isValid(name, surname, email, birthDate)) {
            return;
        }

        db.collection("players")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        emailErrorTextView.setText("Correu ja registrat prèviament");
                        emailErrorTextView.setVisibility(View.VISIBLE);
                    } else {
                        Map<String, Object> player = new HashMap<>();
                        player.put("name", name);
                        player.put("surname", surname);
                        player.put("email", email);
                        player.put("score", recordScore);

                        db.collection("players")
                                .add(player)

                                .addOnSuccessListener(documentReference ->{
                                    Toast.makeText(getContext(), "Inscriptió feta correctament", Toast.LENGTH_SHORT).show();
                                    getParentFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container, new RaffleConfirmationFragment())
                                            .addToBackStack(null)
                                            .commit();
                                })
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error adding document", Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private boolean isValid(String name, String surname, String email, String birthDate) {
        Boolean isValid = true;

        if (!isValidName(name)) {
            nameErrorTextView.setText("Si us plau, introdueix un nom vàlid");
            nameErrorTextView.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            nameErrorTextView.setVisibility(View.GONE);
        }

        if(!isValidName(surname)){
            surnameErrorTextView.setText("Si us plau introdueix un cognom vàlid");
            surnameErrorTextView.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            surnameErrorTextView.setVisibility(View.GONE);
        }

        if (!isValidEmail(email)) {
            emailErrorTextView.setText("Si us plau introdueix un email vàlid");
            emailErrorTextView.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            emailErrorTextView.setVisibility(View.GONE);
        }

        if (!isAdult(birthDate)) {
            birthDateErrorTextView.setText("Has de tenir mínim 18 anys per participar");
            birthDateErrorTextView.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            birthDateErrorTextView.setVisibility(View.GONE);
        }
        return isValid;
    }

    private boolean isValidName(String name) {
        return Pattern.matches("^[a-zA-Z\\s]+$", name);
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isAdult(String birthDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = sdf.parse(birthDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.YEAR, 18);
            return cal.before(Calendar.getInstance());
        } catch (ParseException e) {
            return false;
        }
    }


}