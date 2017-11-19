package com.example.android.mbejaranoe.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Manolo on 18/11/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeActivityTest {

    public static final String RECIPE_ID_KEY = "recipe_Id";

    @Rule
    public IntentsTestRule<MainActivity> mIntentTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void clickRecipeRecyclerViewItem_OpensRecipeDetailsActivity(){

        onView(withId(R.id.recyclerview_recipe)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        intended(hasExtraWithKey(RECIPE_ID_KEY));
    }
}
