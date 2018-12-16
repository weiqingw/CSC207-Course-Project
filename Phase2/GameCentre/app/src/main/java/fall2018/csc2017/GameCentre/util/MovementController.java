package fall2018.csc2017.GameCentre.util;

import android.content.Context;
import android.widget.Toast;

public class MovementController {

    /**
     * The BoardManager to control.
     */
    private BoardManagerForBoardGames boardManager = null;


    /**
     * Default constructor for movement controller.
     */
    MovementController() {
    }

    /**
     * Setter function for the boardManager.
     */
    public void setBoardManager(BoardManagerForBoardGames boardManager) {
        this.boardManager = boardManager;
    }

    /**
     * Performs tap movement for the game.
     */
    void processTapMovement(Context context, int position) {
        if (boardManager.isValidTap(position)) {
            boardManager.makeMove(position);
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }

    }
}
