package bit.clarksj4.labyrinth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import bit.clarksj4.labyrinth.Engine.Game;
import bit.clarksj4.labyrinth.Labyrinth.LabyrinthWorldLoader;

public class GameActivity extends AppCompatActivity
{
    private SurfaceView gameView;
    private Game game;
    private boolean surfaceViewCreated;
    private boolean appClosing;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // SurfaceView where the game is played
        gameView = (SurfaceView)findViewById(R.id.gameSurfaceView);
        gameView.getHolder().addCallback(new SurfaceHandler());

        // Instantiate game
        // Load world
        // Give game a display in surfaceChanged

        game = Game.Android(this, gameView);
        game.loadWorld(new LabyrinthWorldLoader());

        Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume()
    {
        Toast.makeText(this, "Resumed", Toast.LENGTH_SHORT).show();
//        setWindowParams();
//
        // App is resuming after sleeping, and the surface view is still alive
        if (surfaceViewCreated)
            game.resume();

        super.onResume();
    }

    @Override
    protected void onPause()
    {
        Toast.makeText(this, "Paused", Toast.LENGTH_SHORT).show();

        game.pause();                               // Pause the game

        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        Toast.makeText(this, "Destroyed", Toast.LENGTH_SHORT).show();
        if (appClosing)
        {
            game.pause();
            game.stop();                            // Stop the game
            game = null;
        }

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(this, "Back pressed, exitActivity called", Toast.LENGTH_SHORT).show();

            appClosing = true;
            ExitActivity.exitApplication(this);     // Quit the app

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void setWindowParams()
    {
        // Get decor view of top level window
        View decorView = getWindow().getDecorView();

        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);

        // Stop screen from dimming during play
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private class SurfaceHandler implements SurfaceHolder.Callback
    {
        @Override
        public void surfaceCreated(SurfaceHolder holder)
        {
            Toast.makeText(GameActivity.this, "surface created", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
        {
            surfaceViewCreated = true;
            setWindowParams();

            // First time the surface has been created?
            if (!game.isStarted())
                game.start();       // Start the game

            if(game.isPaused())
                game.resume();


            Toast.makeText(GameActivity.this, "surface changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder)
        {
            surfaceViewCreated = false;
            Toast.makeText(GameActivity.this, "surface destroyed", Toast.LENGTH_SHORT).show();
        }
    }
}
