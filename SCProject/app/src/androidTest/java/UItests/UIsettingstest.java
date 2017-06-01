package UItests;

/**
 * Created by noor on 6/1/17.
 */
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.preference.Preference;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.example.noor.scproject.MainActivity;
import com.example.noor.scproject.R;
import com.example.noor.scproject.StartActivity;
import com.example.noor.scproject.settings.SettingsActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.PreferenceMatchers.withKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
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




