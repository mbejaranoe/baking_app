package com.example.android.mbejaranoe.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.app.Instrumentation.ActivityResult;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Created by Manolo on 19/11/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeDetailActivityTest {

    public static final String RECIPE_NAME_KEY = "name";
    public static final String STEP_INDEX_KEY = "stepIndex";
    public static final String RECIPE_ID_KEY = "recipe_Id";
    public static final String SHOULD_AUTO_PLAY_KEY = "shouldAutoPlay";
    public static final String RESUME_POSITION_KEY = "resumePosition";

    @Rule
    public IntentsTestRule<RecipeDetailActivity> mIntentTestRule =
            new IntentsTestRule<RecipeDetailActivity>(RecipeDetailActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Intent result = new Intent(targetContext, MainActivity.class);
            result.putExtra(RECIPE_NAME_KEY, "Nutella Pie");
            result.putExtra(RECIPE_ID_KEY, 1);
            return result;
        }
    };

    @Before
    public void stubExternalIntents(){
        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void clickRecipeDetailRecyclerViewItem_OpensStepDetailsActivity(){

        // check the recyclerview is displayed
        onView(withId(R.id.recyclerview_recipe_detail)).check(matches(isDisplayed()));

        // scroll to the last item position
        RecyclerView recyclerView = (RecyclerView) mIntentTestRule.getActivity().findViewById(R.id.recyclerview_recipe_detail);
        onView(withId(R.id.recyclerview_recipe_detail))
                .perform(RecyclerViewActions.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1));

        // click on that item
        onView(withId(R.id.recyclerview_recipe_detail)).perform(click());

        // check the proper intent has been sent
        intended(hasComponent(StepDetailActivity.class.getName()));
        intended(hasExtraWithKey(STEP_INDEX_KEY));
        intended(hasExtraWithKey(RECIPE_ID_KEY));
        intended(hasExtraWithKey(SHOULD_AUTO_PLAY_KEY));
        intended(hasExtraWithKey(RESUME_POSITION_KEY));
    }
}
