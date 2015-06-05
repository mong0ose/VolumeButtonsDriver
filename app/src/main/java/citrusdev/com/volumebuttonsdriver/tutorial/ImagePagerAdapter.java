package citrusdev.com.volumebuttonsdriver.tutorial;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.Random;

public class ImagePagerAdapter extends FragmentPagerAdapter {

    private int pagerCount = 5;

    private Random random = new Random();

    public ImagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override public Fragment getItem(int i) {
        return ImageFragment.newInstance(0xff000000 | random.nextInt(0x00ffffff));
    }

    @Override public int getCount() {
        return pagerCount;
    }
}