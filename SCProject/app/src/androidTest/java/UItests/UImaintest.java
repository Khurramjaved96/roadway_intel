package UItests;

/**
 * Created by noor on 6/1/17.
 */
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
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.example.noor.scproject.MainActivity;
import com.example.noor.scproject.R;
import com.example.noor.scproject.StartActivity;
import com.example.noor.scproject.settings.SettingsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UImaintest {

    @Rule
    public IntentsTestRule<StartActivity> mActivityRule = new IntentsTestRule<>(
            StartActivity.class);
    private StartActivity mainActivity;



    @Before
    public void setActivity() {
        mainActivity = mActivityRule.getActivity();
    }



    @Test
    public void main_ActivityTest(){

        onView(withId(R.id.start))
                .perform(click());
        intended(hasComponent(MainActivity.class.getName()));
    }



}
