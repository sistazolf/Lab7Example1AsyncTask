package th.ac.tu.siit.its333.lab7example1asynctask;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    boolean isRunning = false;
    CheckPrime task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClicked(View v) {
        if (!isRunning) {
            EditText etPrime = (EditText) findViewById(R.id.etPrime);
            long x = Long.valueOf(etPrime.getText().toString());
            task = new CheckPrime();
            task.execute(x);

            Button b = (Button) findViewById(R.id.button);
            b.setText("Cancel");
        }
        else {
            task.cancel(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class CheckPrime extends AsyncTask<Long, Long, Boolean> {

        @Override
        protected void onPreExecute() {
            isRunning = true;
        }

        @Override
        protected Boolean doInBackground(Long... params) {
            long n = 0;
            long x = params[0];

            // Check if x is a prime number (the dumbest way)
            for (long i = 2; i < x; i++) {
                if (x % i == 0) {
                    return false; // not a prime number
                }

                // Check if the user has cancelled this task
                if (isCancelled()) {
                    return false;
                }

                // Update the progress every 1000 numbers
                n++;
                if (n % 1000 == 0) {
                    publishProgress(n);
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            TextView tvProgress = (TextView)findViewById(R.id.tvProgress);
            tvProgress.setText(values[0] + " numbers checked");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Button b = (Button)findViewById(R.id.button);
            b.setText("Start");

            TextView tvResult = (TextView)findViewById(R.id.tvResult);
            if (result) {
                tvResult.setText("It is a prime number!");
            }
            else {
                tvResult.setText("Sorry, it is not");
            }
            isRunning = false;
        }

        @Override
        protected void onCancelled(Boolean result) {
            super.onCancelled(result);
            Button b = (Button)findViewById(R.id.button);
            b.setText("Start");

            TextView tvResult = (TextView)findViewById(R.id.tvResult);
            tvResult.setText("The calculation was cancelled.");
            isRunning = false;
        }
    }

}