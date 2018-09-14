package ganteng.hendrawd.footballmatchschedule.view.activity


import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest
import android.view.View
import android.view.ViewGroup
import com.jakewharton.espresso.OkHttp3IdlingResource
import ganteng.hendrawd.footballmatchschedule.R.id.*
import ganteng.hendrawd.footballmatchschedule.network.RequestHelper
import ganteng.hendrawd.footballmatchschedule.view.adapter.MatchAdapter
import ganteng.hendrawd.footballmatchschedule.view.model.LeagueModel
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class MatchListActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(
            MatchListActivity::class.java,
            true,
            false
    )
    private val idlingResource = OkHttp3IdlingResource.create(
            "okhttp",
            RequestHelper.OK_HTTP_CLIENT
    )

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun mainActivityTest() {
        mActivityTestRule.launchActivity(
                Intent().apply {
                    val leagueModel = LeagueModel("4328", "English Premier League")
                    putExtra(MatchListActivity.KEY_LEAGUE, leagueModel)
                }
        )

        // Klik item recycler view dengan index ke-14
        onView(withId(rv)).perform(actionOnItemAtPosition<MatchAdapter.ViewHolder>(14, click()))

        // Memastikan menu item favorite ditampilkan
        // Klik menu item favorite
        onView(
                allOf(
                        withId(add_to_favorite),
                        withContentDescription("Favorites"),
                        childAtPosition(
                                childAtPosition(
                                        withId(action_bar),
                                        2
                                ), 0
                        ),
                        isDisplayed()
                )
        ).perform(click())

        // Klik tombol back
        pressBack()

        // Memastikan navigation menu item favorite ditampilkan
        // Klik navigation menu item favorite
        onView(
                allOf(
                        withId(menu_favorites),
                        childAtPosition(
                                childAtPosition(
                                        withId(navigation),
                                        0
                                ), 2
                        ),
                        isDisplayed()
                )
        ).perform(click())

        // Klik item pertama dari list favorite
        onView(withId(rv)).perform(actionOnItemAtPosition<MatchAdapter.ViewHolder>(0, click()))

        // Klik menu item favorite
        onView(
                allOf(
                        withId(add_to_favorite),
                        withContentDescription("Favorites"),
                        childAtPosition(
                                childAtPosition(
                                        withId(action_bar),
                                        2
                                ), 0
                        ),
                        isDisplayed()
                )
        ).perform(click())

        // Klik tombol back
        pressBack()
    }

    private fun childAtPosition(parentMatcher: Matcher<View>, position: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return (parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position))
            }
        }
    }
}
