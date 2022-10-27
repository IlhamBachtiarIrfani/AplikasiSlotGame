package com.ilhamirfan.aplikasislotgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ImageView imgSlot1;
    private ImageView imgSlot2;
    private ImageView imgSlot3;
    private Button btnGet;
    private TextView tvHasil;
    ArrayList<String> arrayUrl = new ArrayList<>();
    boolean isPlay=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGet = findViewById(R.id.btn_get);
        imgSlot1 = findViewById(R.id.img_slot1);
        imgSlot2 = findViewById(R.id.img_slot2);
        imgSlot3 = findViewById(R.id.img_slot3);
        tvHasil = findViewById(R.id.tv_hasil);
        ExecutorService execGetImage =
                Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlay = !isPlay;

                if (isPlay) {
                    btnGet.setText("Stop");
                    execGetImage.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final String txt =
                                        loadStringFromNetwork("https://mocki.io/v1/821f1b13-fa9a-43aa-ba9a-9e328df8270e");
                                try {
                                    JSONArray jsonArray = new
                                            JSONArray(txt);
                                    for (int i = 0; i <
                                            jsonArray.length(); i++) {
                                        JSONObject jsonObject =
                                                jsonArray.getJSONObject(i);

                                        arrayUrl.add(jsonObject.getString("url"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                while (isPlay) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Random _random = new Random();
                                            int i;

                                            i = _random.nextInt(arrayUrl.size());
                                            Glide.with(MainActivity.this)

                                                    .load(arrayUrl.get(i))
                                                    .into(imgSlot1);

                                            i = _random.nextInt(arrayUrl.size());
                                            Glide.with(MainActivity.this)

                                                    .load(arrayUrl.get(i))
                                                    .into(imgSlot2);
                                            i = _random.nextInt(arrayUrl.size());

                                            Glide.with(MainActivity.this)

                                                    .load(arrayUrl.get(i))
                                                    .into(imgSlot3);
                                            tvHasil.setText(txt);
                                        }
                                    });
                                    try {
                                        Thread.sleep(200);}
                                    catch (InterruptedException e) {
                                        e.printStackTrace(); }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    btnGet.setText("Start");
                }
            }
        });
    }
    private String loadStringFromNetwork(String s) throws
            IOException {
        final URL myUrl = new URL(s);
        final InputStream in = myUrl.openStream();
        final StringBuilder out = new StringBuilder();
        final byte[] buffer = new byte[1024];
        try {
            for (int ctr; (ctr = in.read(buffer)) != -1; ) {out.append(new String(buffer, 0, ctr));
            }
        } catch (IOException e) {
            throw new RuntimeException("Gagal mendapatkan text",
                    e);
        }
        final String yourFileAsAString = out.toString();
        return yourFileAsAString;
    }

}