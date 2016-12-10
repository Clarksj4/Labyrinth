package bit.clarksj4.labyrinth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import bit.clarksj4.labyrinth.Engine.Game;
import bit.clarksj4.labyrinth.Labyrinth.LabyrinthWorldLoader;

public class GameActivity extends AppCompatActivity
{
    private SurfaceView gameView;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // SurfaceView where the game is played
        gameView = (SurfaceView)findViewById(R.id.gameSurfaceView);
        gameView.getHolder().addCallback(new SurfaceHandler());

        game = Game.Android(this, gameView);
        game.loadWorld(new LabyrinthWorldLoader(game));
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // Get decor view of top level window
        View decorView = getWindow().getDecorView();

        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);

        // Stop screen from dimming during play
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        game.resume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        game.pause();
    }

    private class SurfaceHandler implements SurfaceHolder.Callback
    {
        @Override
        public void surfaceCreated(SurfaceHolder holder)
        {
            if  (game.isStarted()) game.pause();
            else game.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { /* Nothing! */ }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) { game.pause(); }
    }
}
