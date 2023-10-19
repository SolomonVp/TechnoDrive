package re.demo.technodrive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private final String TASK = "https://jsonplaceholder.typicode.com/posts/%s";

    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

    }

    public void onClickShowPosts(View view) {
        String userId = editText.getText().toString().trim();
        if (!userId.isEmpty()) {
            DownLoadTask task = new DownLoadTask();
            String url = String.format(TASK, userId);
            task.execute(url);
        }
    }

    private class DownLoadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder result = new StringBuilder();
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String line = reader.readLine();
                while (line != null) {
                    result.append(line);
                    line = reader.readLine();
                }
                return result.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String userId = jsonObject.getString("userId");
                String id = jsonObject.getString("id");
                String title = jsonObject.getString("title");
                String body = jsonObject.getString("body");

                String posts = String.format("ПОЛЬЗОВАТЕЛЬ: %s\n\nНОМЕР СООБЩЕНИЯ: %s\n\nЗАГОЛОВОК: %s\n\nСОДЕРЖАНИЕ: %s", userId, id, title, body);
                textView.setText(posts);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}