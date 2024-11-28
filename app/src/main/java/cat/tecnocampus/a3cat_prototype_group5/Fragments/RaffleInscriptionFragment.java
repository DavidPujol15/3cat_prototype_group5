package cat.tecnocampus.a3cat_prototype_group5.Fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    private int recordScore;
    private FirebaseFirestore db;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_raffle_inscription, container, false);

        // Initialize UI components
        nameEditText = view.findViewById(R.id.et_name);
        surnameEditText = view.findViewById(R.id.et_surname);
        emailEditText = view.findViewById(R.id.et_email);
        birthDateEditText = view.findViewById(R.id.et_birth_date);
        Button inscriptionButton = view.findViewById(R.id.btn_confirm_participation);

        Bundle bundle = getArguments();
        if (bundle != null) {
            recordScore = bundle.getInt("record_score", 0);
        }

        db = FirebaseFirestore.getInstance();

        inscriptionButton.setOnClickListener(v -> saveInscription());

        return view;
    }

    private void saveInscription() {
        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String birthDate = birthDateEditText.getText().toString();

        if (!isValidName(name)) {
            Toast.makeText(getContext(), "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isAdult(birthDate)) {
            Toast.makeText(getContext(), "You must be at least 18 years old to participate", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> player = new HashMap<>();
        player.put("name", name);
        player.put("surname", surname);
        player.put("email", email);
        player.put("score", recordScore);

        // Add a new document with a generated ID
        db.collection("players")
                .add(player)
                .addOnSuccessListener(documentReference -> Toast.makeText(getContext(), "Inscription successful", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error adding document", Toast.LENGTH_SHORT).show());
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