package UItests;

/**
 * Created by noor on 6/1/17.
 */
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import com.example.noor.scproject.MainActivity;
import com.example.noor.scproject.R;
import com.example.noor.scproject.settings.SettingsActivity;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UImaintest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);
    private MainActivity mainActivity;



    @Before
    public void setActivity() {
        mainActivity = mActivityRule.getActivity();
    }
   @Test
    public void settings_Activity() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Settings"))
                .perform(click());
        //Check if intent with Activity 2 it's been launched
        intended(hasComponent(SettingsActivity.class.getName()));
    }



  @Test
    public void gallery_Activity() {
        //openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withId(R.id.action_gallery))
                .perform(click());

    }



}
