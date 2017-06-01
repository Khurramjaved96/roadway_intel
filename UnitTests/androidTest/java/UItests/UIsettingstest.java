package UItests;

/**
 * Created by noor on 6/1/17.
 */

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.preference.Preference;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import com.example.noor.scproject.settings.SettingsActivity;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.PreferenceMatchers.withKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UIsettingstest {

    @Rule
    public IntentsTestRule<SettingsActivity> mActivityRule = new IntentsTestRule<>(
            SettingsActivity.class);
    private SettingsActivity mainActivity;



    @Before
    public void setActivity() {
        mainActivity = mActivityRule.getActivity();
    }



    @Test
    public void ipdisplay_Test(){

        // Check if is displayed
        onData(allOf(is(instanceOf(Preference.class)), withKey("ip0"))).check(matches(isDisplayed()));


    }

    @Test
    public void portdisplay_Test(){

        // Check if is displayed
        onData(allOf(is(instanceOf(Preference.class)), withKey("port0"))).check(matches(isDisplayed()));


    }





}




