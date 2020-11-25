package jamie.itsligo.runningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //timer setup
    CountUpTimer timer;
    TextView counterTimer;
    int counterToApproveShowBtn = 0;

    //step counter setup
    // experimental values for hi and lo magnitude limits
    private final double HI_STEP = 11.0;     // upper mag limit
    private final double LO_STEP = 9.0;      // lower mag limit
    boolean highLimit = false;      // detect high limit
    int counter = 0;                // step counter

    TextView tvSteps;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //timer code
        counterTimer = findViewById(R.id.tvTime);

        timer = new CountUpTimer(300000) {  // should be high for the run (ms)
            public void onTick(int second) {
                counterTimer.setText(String.valueOf(second));
            }
        };

        //step counter code
        tvSteps = findViewById(R.id.tvMetersRun);

        // we are going to use the sensor service
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    // timer methods
    public void doStart(View view) {
        counter = 0;
        tvSteps.setText(String.valueOf(counter));
        timer.start();
        mSensorManager.registerListener(this, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
        Toast.makeText(this, "Started counting", Toast.LENGTH_LONG).show();
    }

    public void doStop(View view) {
        counterToApproveShowBtn++;
        timer.cancel();
        mSensorManager.unregisterListener(this);    // turn off listener to save power
        Toast.makeText(this, "Stopped Run", Toast.LENGTH_LONG).show();
    }

    public void doReset(View view) {
        counterTimer.setText("0");
        timer.cancel();
        counter = 0;
        tvSteps.setText(String.valueOf(counter));
        Toast.makeText(this, "Reset", Toast.LENGTH_LONG).show();
    }

    public void doShow(View view) {
        if (counterToApproveShowBtn >= 1)
        {
            counterToApproveShowBtn = 0;

            Intent resultPageIntent = new Intent(view.getContext(), RunPage.class);

            int stepsToPass = Integer.valueOf(tvSteps.getText().toString());
            int timeToPass = Integer.valueOf(counterTimer.getText().toString());


            resultPageIntent.putExtra("stepsPassed", stepsToPass);
            resultPageIntent.putExtra("timePassed", timeToPass);

            startActivity(resultPageIntent);
        }
        else if (counterToApproveShowBtn == 0)
        {
            Toast.makeText(this, "You must stop the run first!", Toast.LENGTH_LONG).show();
        }
    }

    //step counter methods
    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // get a magnitude number using Pythagorus's Theorem
        double mag = round(Math.sqrt((x*x) + (y*y) + (z*z)), 2);

        // for me! if msg > 11 and then drops below 9, we have a step
        // you need to do your own mag calculating
        if ((mag > HI_STEP) && (highLimit == false)) {
            highLimit = true;
        }
        if ((mag < LO_STEP) && (highLimit == true)) {
            // we have a step
            counter++;
            tvSteps.setText(String.valueOf(counter));
            highLimit = false;
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not used
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}