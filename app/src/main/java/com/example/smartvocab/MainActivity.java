package com.example.smartvocab;

import static com.google.android.material.color.utilities.MaterialDynamicColors.error;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    EditText word;
    ImageView Search, speaker, speaker1;

    TextView meaning, antonym, synonym, example;
    String audiouri;
    TextToSpeech t1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);

        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        Search = findViewById(R.id.ImageSearch);
        word = findViewById(R.id.searchWord);
        speaker=findViewById(R.id.speaker);
        speaker1=findViewById(R.id.speaker1);

        meaning=findViewById(R.id.textMeaning);
        antonym=findViewById(R.id.textAntonym);
        synonym=findViewById(R.id.textSynonym);




        speaker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String toSpeak= meaning.getText().toString();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        t1= new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i !=TextToSpeech.ERROR)
                {
                    t1.setLanguage(Locale.UK);
                }
            }
        });



        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(audiouri!=null)
                {
                    String toSpeak= word.getText().toString();
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH,null);

                }
            }
        });




        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searcord = word.getText().toString().trim();
                getMeaning(searcord);
            }
        });




        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id==R.id.optHome) {
                    loadFragment(new Content_main());
                }else if (id==R.id.optHistory) {
                    loadFragment(new AFragment());
                } else if (id==R.id.optStarred) {
                    loadFragment(new BFragment());
                } else if (id==R.id.optBuy) {
                    loadFragment(new CFragment());
                }else if (id==R.id.optHelp) {
                    loadFragment(new DFragment());
                }else if (id==R.id.optAbout) {
                    loadFragment(new EFragment());
                }else if (id==R.id.optLookup) {
                    loadFragment(new FFragment());
                }else if (id==R.id.optSettings) {
                    loadFragment(new GFragment());
                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

    }


    private void getMeaning(String Main_word)
    {
        String URL="https://api.dictionaryapi.dev/api/v2/entries/en/"+Main_word;

        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    JSONObject jsonObject= response.getJSONObject(0);
                    String word_x= jsonObject.getString("word");
                  //  String origin= jsonObject.getString("origin");

                   // word.setText(word_x);
                   // meaning.setText(origin);

                    JSONArray jsonArray= jsonObject.getJSONArray("phonetics");
                    JSONObject jsonObject1=jsonArray.getJSONObject(0);
                    String url= jsonObject1.getString("audio");

                  audiouri=url;


                    JSONArray jsonArray1=jsonObject.getJSONArray("meanings");
                    JSONObject jsonObject2=jsonArray1.getJSONObject(1);

                    JSONArray jsonArray2=jsonObject2.getJSONArray("definitions");
                    JSONObject jsonObject3=jsonArray2.getJSONObject(3);

                    String Mean=jsonObject3.getString("definition");
                    meaning.setText(Mean);

                   String example1=jsonObject3.getString("example");
                   antonym.setText(example1);
                    JSONObject jsonObject4=jsonArray1.getJSONObject(1);
                  //  JSONArray jsonArray3=jsonObject2.getJSONArray("synonyms");

                    String exam=jsonObject4.getString("synonyms");
                    synonym.setText(exam);


                   /* String synonymsArray = String.valueOf(jsonObject3.getJSONArray("synonyms"));

// Now, you can loop through the synonymsArray to get each synonym.
                   synonym.setText(synonymsArray);*/
              /*      JSONArray synonymsArray = jsonObject3.getJSONArray("synonyms");

// Create a StringBuilder to store the synonyms.
                    StringBuilder synonymsBuilder = new StringBuilder();

// Loop through the synonyms array and append each synonym to the StringBuilder.
                    for (int i = 0; i < synonymsArray.length(); i++) {
                        String synonym = synonymsArray.getString(i);
                        synonymsBuilder.append(synonym);
                        if (i < synonymsArray.length() - 1) {
                            synonymsBuilder.append(", "); // Add a comma and space for separation.
                        }
                    }*/

// Now, set the text for your view with the concatenated synonyms.
                  //  synonym.setText(synonymsBuilder.toString());







                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadFragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.container, fragment);
        ft.commit();

    }
}