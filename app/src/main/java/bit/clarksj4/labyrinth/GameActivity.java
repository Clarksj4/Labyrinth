package bit.clarksj4.labyrinth;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import bit.clarksj4.labyrinth.Engine.AndroidGame;
import bit.clarksj4.labyrinth.Labyrinth.LabyrinthWorldLoader;

public class GameActivity extends AppCompatActivity
{
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private AccelerometerListener accelerometerListener;

    private SurfaceView gameView;
    private AndroidGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get sensor manager and accelerometer
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //If there is no accelerometer, exit the game
        if (accelerometer == null)
            finish();

        // SurfaceView where the game is played
        gameView = (SurfaceView)findViewById(R.id.gameSurfaceView);
        gameView.getHolder().addCallback(new SurfaceHandler());

        game = new AndroidGame(this, gameView);
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

        // Create and register a new accelerometer listener
        accelerometerListener = new AccelerometerListener();
        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);

        game.setIsPaused(false);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // Unregister accelerometer listener
        sensorManager.unregisterListener(accelerometerListener);

        game.setIsPaused(true);
    }

    private class AccelerometerListener implements SensorEventListener
    {
        @Override
        public void onSensorChanged(SensorEvent event) { game.accelerometerInput(event.values); }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { /* Nothing! */ }
    }

    private class SurfaceHandler implements SurfaceHolder.Callback
    {
        @Override
        public void surfaceCreated(SurfaceHolder holder)
        {
            if  (game.getIsStarted())
                game.setIsPaused(false);
            else
                game.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { /* Nothing! */ }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) { game.setIsPaused(true); }
    }
}
