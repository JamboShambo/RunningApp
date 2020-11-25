package jamie.itsligo.runningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RunPage extends AppCompatActivity {

    String counterValuePassed;
    TextView tvMetersRun,tvCaloriesBurn,tvTimetaken,tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_page);

        tvMetersRun = findViewById(R.id.tvMetersRun);
        tvCaloriesBurn = findViewById(R.id.tvCaloriesBurn);
        tvTimetaken = findViewById(R.id.tvTimetaken);
        tvDate = findViewById(R.id.tvDate);

        int stepsNowPassedOver = getIntent().getIntExtra("stepsPassed", 0 );
        int timeNowPassedOver = getIntent().getIntExtra("timePassed", 0);

        //calculation
        double NumOfMetersRun = stepsNowPassedOver*0.8;
        double NumOfCaloriesBurned = stepsNowPassedOver*0.04;

        String stepsNowPassedOverString = String.valueOf(NumOfMetersRun);
        String timeNowPassedOverString = String.valueOf(timeNowPassedOver);
        String CaloriesCalculated = String.valueOf(NumOfCaloriesBurned);

        tvMetersRun.setText(String.valueOf(stepsNowPassedOverString));
        tvTimetaken.setText(String.valueOf(timeNowPassedOverString));
        tvCaloriesBurn.setText(String.valueOf(CaloriesCalculated));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(new Date());
        tvDate.setText(String.valueOf(date));

    }

    public void doBack(View view) {
        Intent mainActivityIntent = new Intent(view.getContext(), MainActivity.class);
        startActivity(mainActivityIntent);
    }
}