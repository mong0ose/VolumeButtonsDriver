package citrusdev.com.volumebuttonsdriver;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import citrusdev.com.volumebuttonsdriver.tutorial.ImagePagerAdapter;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Администратор on 05.06.2015.
 */
public class TutorialActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_activity);

        ViewPager defaultViewpager = (ViewPager) findViewById(R.id.viewpager_default);

        CircleIndicator defaultIndicator = (CircleIndicator) findViewById(R.id.indicator_default);
        ImagePagerAdapter defaultPagerAdapter = new ImagePagerAdapter(getSupportFragmentManager());
        defaultViewpager.setAdapter(defaultPagerAdapter);
        defaultIndicator.setViewPager(defaultViewpager);
    }
}
