package it17091244.mad.sliit.bohar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private final String X = MainActivity.class.getSimpleName();
    private ListView listView;

    ArrayList<HashMap<String, String>> List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List = new ArrayList<>();
        listView = findViewById(R.id.list);
        new Get().execute();
    }

    private class Get extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            handler sh = new handler();
            String url = "https://official-joke-api.appspot.com/random_ten";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(X, "Response from url: " + jsonStr);


            if (jsonStr != null) {
                try {

                    JSONArray j = new JSONArray(jsonStr);
                    System.out.println(j);

                    // looping through All Contacts
                    for (int i = 0; i < j.length(); i++) {

                        JSONObject c = j.getJSONObject(i);
                        String id = c.getString("id");
                        String type = c.getString("type");
                        String setup = c.getString("setup");
                        String punchline = c.getString("punchline");

                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("type", type);
                        contact.put("setup", setup);
                        contact.put("punchline", punchline);

                        // adding contact to contact list
                        List.add(contact);

                    }
                } catch (final JSONException e) {
                    Log.e(X,  e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(X, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, List,
                    R.layout.list_item, new String[]{ "setup","punchline"},
                    new int[]{R.id.setup, R.id.punch});
            listView.setAdapter(adapter);
        }
    }
}