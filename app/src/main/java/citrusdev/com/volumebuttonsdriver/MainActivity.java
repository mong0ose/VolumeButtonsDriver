package citrusdev.com.volumebuttonsdriver;

import static citrusdev.com.volumebuttonsdriver.Constant.ITEM;
import static citrusdev.com.volumebuttonsdriver.Constant.DESCR;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private ListView listViewMain;
    private ArrayList<HashMap> list;
    private SettingsContentObserver mSettingsContentObserver;
    private IncomingCallBroadcastReciever mReceiver;

    private String chosenRingtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

//        RecyclerView

        mReceiver = new IncomingCallBroadcastReciever();
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_MEDIA_BUTTON));

        mSettingsContentObserver = new SettingsContentObserver(this,new Handler());
        getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver );

        listViewMain = (ListView) findViewById(R.id.listViewMain);

        generateList();
        final ListViewAdapter adapter = new ListViewAdapter(this, list);

        listViewMain.setAdapter(adapter);
        listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        System.out.println("Vol up");
                        Intent intentTutorial = new Intent(getBaseContext(), TutorialActivity.class);
                        startActivity(intentTutorial);
                        break;
                    case 1:
                        System.out.println("Vol down");
                        break;
                    case 2:
                        System.out.println("Power sw");
                        String inputJSON = readJSONfromRaw();
                        getDataFromJSON(inputJSON);
                        break;
                    case 3:
                        System.out.println("About");
                        System.out.println("" + chosenRingtone);
                        break;
                    case 4:
                        Toast.makeText(getApplicationContext(), "Start Google Maps View!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        break;
                    case 5:
                        Toast.makeText(getApplicationContext(), "Show notification list!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
                        startActivityForResult(intent, 5);
                        break;

                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 5)
        {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null)
            {
                this.chosenRingtone = uri.toString();
            }
            else
            {
                this.chosenRingtone = null;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsContentObserver);
    }

    private void generateList(){
        String[] arrayItems = getResources().getStringArray(R.array.listview_items);
        String[] arrayDescr = getResources().getStringArray(R.array.listview_items_descr);
        list = new ArrayList<>();
        System.out.println("aaa");
        for (int i = 0; i < arrayItems.length; i++){
            HashMap tmp = new HashMap();
            tmp.put(ITEM, arrayItems[i]);
            tmp.put(DESCR, arrayDescr[i]);
            list.add(tmp);
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

    private String readJSONfromRaw(){
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.test);
            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
            System.out.println(json);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }

        return json;
    }

    private List<NewsFromJSON> getDataFromJSON(String jsonStr){

        List<NewsFromJSON> lsJSON = new ArrayList<NewsFromJSON>();
        try {
            JSONObject obj = new JSONObject(jsonStr);
            JSONArray arr = obj.getJSONArray("news");

            for (int i = 0; i < arr.length(); i++) {
                NewsFromJSON json = new NewsFromJSON();
                JSONObject arrayObject = arr.getJSONObject(i);
                json.set_id(arrayObject.getString("id"));
                json.set_category(arrayObject.getString("category"));
                json.set_title(arrayObject.getString("title"));
                json.set_summary(arrayObject.getString("summary"));
                json.set_source(arrayObject.getString("source"));
                json.set_image(arrayObject.getString("image"));
                json.set_published(arrayObject.getString("published"));

                JSONArray geoJSONArray = arrayObject.getJSONArray("geo_codes");

                List<HashMap> geoLocationData = new ArrayList<HashMap>();
                for (int j = 0; j < geoJSONArray.length(); j++) {
                    HashMap data = new HashMap();
                    JSONObject geoObject = geoJSONArray.getJSONObject(j);
                    data.put("longtitude", geoObject.getString("longtitude"));
                    data.put("latitude", geoObject.getString("latitude"));
                    data.put("adress", geoObject.getString("adress"));
                    data.put("country", geoObject.getString("country"));
                    data.put("city", geoObject.getString("city"));

                    geoLocationData.add(data);
                }

                json.set_geocodes(geoLocationData);
                lsJSON.add(json);

                System.out.println(".");
            }
//            json.set_id(arr.);
            System.out.println(".");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lsJSON;
    }


    public class NewsFromJSON{
        private String _id;
        private String _category;
        private String _title;
        private String _summary;
        private String _source;
        private String _image;
        private String _published;
        private List<HashMap> _geocodes;

        public String get_category() {
            return _category;
        }

        public void set_category(String _category) {
            this._category = _category;
        }

        public String get_title() {
            return _title;
        }

        public void set_title(String _title) {
            this._title = _title;
        }

        public String get_summary() {
            return _summary;
        }

        public void set_summary(String _summary) {
            this._summary = _summary;
        }

        public String get_source() {
            return _source;
        }

        public void set_source(String _source) {
            this._source = _source;
        }

        public String get_image() {
            return _image;
        }

        public void set_image(String _image) {
            this._image = _image;
        }

        public String get_published() {
            return _published;
        }

        public void set_published(String _published) {
            this._published = _published;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public List<HashMap> get_geocodes() {
            return _geocodes;
        }

        public void set_geocodes(List<HashMap> _geocodes) {
            this._geocodes = _geocodes;
        }
    }
}
