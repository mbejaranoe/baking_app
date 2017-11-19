package com.example.android.mbejaranoe.bakingapp;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Manolo on 19/11/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeDetailActivityTest {

    public static final String STEP_INDEX_KEY = "stepIndex";
    public static final String RECIPE_ID_KEY = "recipe_Id";
    public static final String SHOULD_AUTO_PLAY_KEY = "shouldAutoPlay";
    public static final String RESUME_POSITION_KEY = "resumePosition";

    @Rule
    public IntentsTestRule<RecipeDetailActivity> mIntentTestRule = new IntentsTestRule<>(RecipeDetailActivity.class);

    @Test
    public void clickRecipeDetailRecyclerViewItem_OpensStepDetailsActivity(){

        onView(withId(R.id.recyclerview_recipe_detail)).check(matches(isDisplayed()));
        // determine the position in the recycler_view where the first step will be displayed
        // and scroll to that position
        // onView(withId(R.id.recyclerview_recipe_detail)).perform(RecyclerViewActions.scrollToPosition(position));
        // click on that item
        // onView(withId(R.id.recyclerview_recipe_detail)).perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));
        intended(hasComponent(StepDetailActivity.class.getName()));
        intended(hasExtraWithKey(STEP_INDEX_KEY));
        intended(hasExtraWithKey(RECIPE_ID_KEY));
        intended(hasExtraWithKey(SHOULD_AUTO_PLAY_KEY));
        intended(hasExtraWithKey(RESUME_POSITION_KEY));
    }


}
