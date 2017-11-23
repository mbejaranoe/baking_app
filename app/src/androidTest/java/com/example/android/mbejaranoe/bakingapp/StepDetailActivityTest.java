package com.example.android.mbejaranoe.bakingapp;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Created by Manolo on 23/11/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class StepDetailActivityTest {

    public static final String STEP_INDEX_KEY = "stepIndex";
    public static final String RECIPE_ID_KEY = "recipe_Id";
    public static final String SHOULD_AUTO_PLAY_KEY = "shouldAutoPlay";
    public static final String RESUME_POSITION_KEY = "resumePosition";

    @Rule
    public IntentsTestRule<StepDetailActivity> mIntentTestRule =
            new IntentsTestRule<StepDetailActivity>(StepDetailActivity.class){
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, MainActivity.class);
                    result.putExtra(STEP_INDEX_KEY, 2);
                    result.putExtra(RECIPE_ID_KEY, 1);
                    result.putExtra(SHOULD_AUTO_PLAY_KEY, false);
                    result.putExtra(RESUME_POSITION_KEY, 0);
                    return result;
                }
            };

    @Before
    public void stubExternalIntents(){
        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void clickPlayPauseButton(){

        // check the play button is displayed
        onView(withId(R.id.exo_play)).check(matches(isDisplayed()));

        // check the play button is clickable
        onView(withId(R.id.exo_play)).perform(click());

        // check the pause button is displayed
        onView(withId(R.id.exo_pause)).check(matches(isDisplayed()));

        // check the play button is clickable
        onView(withId(R.id.exo_pause)).perform(click());
    }

    @Test
    public void clickPrevNextButton() {

        // check the play button is displayed
        onView(withId(R.id.next_step_button)).check(matches(isDisplayed()));

        // check the play button is clickable
        onView(withId(R.id.next_step_button)).perform(click());

        // check the pause button is displayed
        onView(withId(R.id.prev_step_button)).check(matches(isDisplayed()));

        // check the play button is clickable
        onView(withId(R.id.prev_step_button)).perform(click());

    }
}
