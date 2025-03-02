package com.ab.tictacgame;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    boolean isPlayerOneTurn = true;
    int[] gameState = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[][] winningPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };

    ImageView[] cells = new ImageView[9];
    int[] cellIds = {R.id.image1, R.id.image2, R.id.image3, R.id.image4, R.id.image5, R.id.image6, R.id.image7, R.id.image8, R.id.image9};

    TextView playerTurnText, playerOneDisplay, playerTwoDisplay;
    String playerOne, playerTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerTurnText = findViewById(R.id.playerTurn);
        playerOneDisplay = findViewById(R.id.playerOneName);
        playerTwoDisplay = findViewById(R.id.playerTwoName);

        // Get player names from AddPlayer activity
        Intent intent = getIntent();
        playerOne = intent.getStringExtra("playerOneName");
        playerTwo = intent.getStringExtra("playerTwoName");

        // Set default names if null
        if (playerOne == null || playerOne.trim().isEmpty()) {
            playerOne = "Player One";
        }
        if (playerTwo == null || playerTwo.trim().isEmpty()) {
            playerTwo = "Player Two";
        }

        playerOneDisplay.setText(playerOne);
        playerTwoDisplay.setText(playerTwo);

        updatePlayerTurn();

        // Initialize cells
        for (int i = 0; i < 9; i++) {
            cells[i] = findViewById(cellIds[i]);
            final int index = i;
            cells[i].setOnClickListener(view -> makeMove(index, (ImageView) view));
        }
    }

    private void makeMove(int index, ImageView cell) {
        if (gameState[index] == 0) {
            gameState[index] = isPlayerOneTurn ? 1 : 2;
            cell.setImageResource(isPlayerOneTurn ? R.drawable.cross : R.drawable.circle);
            isPlayerOneTurn = !isPlayerOneTurn;
            updatePlayerTurn();
            checkWinner();
        }
    }

    private void updatePlayerTurn() {
        playerTurnText.setText(isPlayerOneTurn ? playerOne + "'s Turn" : playerTwo + "'s Turn");
    }

    private void checkWinner() {
        for (int[] position : winningPositions) {
            if (gameState[position[0]] != 0 &&
                    gameState[position[0]] == gameState[position[1]] &&
                    gameState[position[1]] == gameState[position[2]]) {

                String winner = (gameState[position[0]] == 1) ? "🎉 " + playerOne + " Wins! 🎉" : "🏆 " + playerTwo + " Wins! 🏆";

                disableBoard();
                showWinnerDialog(winner);
                return;
            }
        }

        boolean isBoardFull = true;
        for (int state : gameState) {
            if (state == 0) {
                isBoardFull = false;
                break;
            }
        }

        if (isBoardFull) {
            showWinnerDialog("🤝 It's a Tie! 🤝");
        }
    }

    private void showWinnerDialog(String message) {
        TextView titleView = new TextView(this);
        titleView.setText("🎊 Game Over 🎊");
        titleView.setTextSize(22);
        titleView.setTextColor(Color.WHITE);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setGravity(Gravity.CENTER);
        titleView.setPadding(20, 20, 20, 20);

        TextView messageView = new TextView(this);
        messageView.setText(message + "\n\nWould you like to play again?");
        messageView.setTextSize(18);
        messageView.setTextColor(Color.WHITE);
        messageView.setGravity(Gravity.CENTER);
        messageView.setPadding(30, 30, 30, 30);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 30, 30, 30);
        layout.addView(messageView);

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setCustomTitle(titleView)
                .setView(layout)
                .setPositiveButton("Restart", (dialog1, which) -> resetGame())
                .setNegativeButton("Exit", (dialog12, which) -> finish())
                .setCancelable(false)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dialog.getButton(AlertDialog.BUTTON_NEGATIVE).getLayoutParams();
            params.weight = 1;
            params.gravity = Gravity.START;
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setLayoutParams(params);

            params = (LinearLayout.LayoutParams) dialog.getButton(AlertDialog.BUTTON_POSITIVE).getLayoutParams();
            params.weight = 1;
            params.gravity = Gravity.END;
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setLayoutParams(params);
        });

        dialog.show();
    }

    private void disableBoard() {
        for (ImageView cell : cells) {
            cell.setEnabled(false);
        }
    }

    private void resetGame() {
        // Reset gameState
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 0;
        }

        // Reset UI elements
        for (ImageView cell : cells) {
            cell.setImageDrawable(null);
            cell.setEnabled(true);
        }

        isPlayerOneTurn = true;
        updatePlayerTurn();
    }
}
