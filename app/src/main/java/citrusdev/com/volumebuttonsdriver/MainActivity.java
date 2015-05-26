package citrusdev.com.volumebuttonsdriver;

import static citrusdev.com.volumebuttonsdriver.Constant.ITEM;
import static citrusdev.com.volumebuttonsdriver.Constant.DESCR;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {
    private ListView listViewMain;
    private ArrayList<HashMap> list;
    private SettingsContentObserver mSettingsContentObserver;
    private IncomingCallBroadcastReciever mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

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
                        break;
                    case 1:
                        System.out.println("Vol down");
                        break;
                    case 2:
                        System.out.println("Power sw");
                        break;
                    case 3:
                        System.out.println("About");
                        break;
                    case 4:
                        Toast.makeText(getApplicationContext(), "Start Google Maps View!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        break;
                    case 5:
                        Toast.makeText(getApplicationContext(), "Start Yandex Maps View!", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
            }
        });
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
}
