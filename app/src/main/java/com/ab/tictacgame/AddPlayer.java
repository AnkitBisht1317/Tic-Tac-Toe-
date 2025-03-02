package com.ab.tictacgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddPlayer extends AppCompatActivity {
    EditText playerOneInput, playerTwoInput;
    Button startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        playerOneInput = findViewById(R.id.playerOneName);
        playerTwoInput = findViewById(R.id.playerTwoName);
        startGameButton = findViewById(R.id.startGameBtn);

        startGameButton.setOnClickListener(view -> {
            String playerOne = playerOneInput.getText().toString().trim();
            String playerTwo = playerTwoInput.getText().toString().trim();

            if (playerOne.isEmpty()) playerOne = "Player One";
            if (playerTwo.isEmpty()) playerTwo = "Player Two";

            // Pass the names to MainActivity
            Intent intent = new Intent(AddPlayer.this, MainActivity.class);
            intent.putExtra("playerOneName", playerOne);
            intent.putExtra("playerTwoName", playerTwo);
            startActivity(intent);
            finish(); // Close AddPlayer activity
        });
    }
}
