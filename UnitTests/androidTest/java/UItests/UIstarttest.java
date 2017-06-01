package UItests;

/**
 * Created by noor on 6/1/17.
 */
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import com.example.noor.scproject.MainActivity;
import com.example.noor.scproject.R;
import com.example.noor.scproject.StartActivity;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class UIstarttest {

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
