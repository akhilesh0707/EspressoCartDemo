package com.demo.espresso.ui

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.demo.espresso.R
import com.demo.espresso.data.DummyProductData
import com.demo.espresso.ui.adapter.ProductListAdapter
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.*
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4ClassRunner::class)
class ProductListFragmentTest {

    val LIST_ITEM_IN_TEST = 2
    val LIST_ITEM_IN_TEST_THREE = 3

    val PRODUCTS_IN_TEST = DummyProductData.products[LIST_ITEM_IN_TEST]
    val PRODUCTS_IN_TEST_THREE = DummyProductData.products[LIST_ITEM_IN_TEST_THREE]

    @get:Rule
    var activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun stage1_testIsProductListFragmentVisibleOnAppLaunch() {
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun stage2_testSelectListItemIsDetailFragmentVisible() {
        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ProductListAdapter.ProductViewHolder>(
                    LIST_ITEM_IN_TEST, click()
                )
            )

        // Confirm nav to DetailFragment and display title
        onView(withId(R.id.textViewProductName)).check(matches(withText(PRODUCTS_IN_TEST.productName)))
    }

    @Test
    fun stage3_testBackNavigationToProductListFragment() {
        // Click list item #LIST_ITEM_IN_TEST
        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ProductListAdapter.ProductViewHolder>(
                    LIST_ITEM_IN_TEST,
                    click()
                )
            )

        // wait for scroll
        Thread.sleep(500)

        // Confirm nav to DetailFragment and display title
        onView(withId(R.id.textViewProductName)).check(matches(withText(PRODUCTS_IN_TEST.productName)))

        //Navigate to previous product list screen
        pressBack()

        // Confirm ProductListFragment in view
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun stage4_testNavToDetailFragmentAndAddToCartQuantitySingleProduct() {
        // Click list item #LIST_ITEM_IN_TEST
        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ProductListAdapter.ProductViewHolder>(
                    LIST_ITEM_IN_TEST,
                    click()
                )
            )

        // wait for scroll
        Thread.sleep(500)

        // Confirm nav to DetailFragment and display title
        onView(withId(R.id.textViewProductName)).check(matches(withText(PRODUCTS_IN_TEST.productName)))

        // Add product to cart and finish activity with success message
        onView(withId(R.id.buttonAddCart)).perform(click())

        // Confirm product add successfully and toast is visible
        onView(withText(R.string.product_added)).inRoot(withDecorView(not(`is`(activityTestRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
    }

    @Test
    fun stage5_testNavToDetailFragmentAndAddToCartQuantityMultipleProduct() {
        // Click list item #LIST_ITEM_IN_TEST
        onView(withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ProductListAdapter.ProductViewHolder>(
                    LIST_ITEM_IN_TEST_THREE,
                    click()
                )
            )

        // wait for scroll
        Thread.sleep(500)

        // Confirm nav to DetailFragment and display title
        onView(withId(R.id.textViewProductName)).check(matches(withText(PRODUCTS_IN_TEST_THREE.productName)))

        //Selected quantity from spinner
        onView(withId(R.id.spinnerQuantity)).perform(click())
        onData(anything()).atPosition(3).perform(click());

        // Nav to DirectorsFragment
        onView(withId(R.id.buttonAddCart)).perform(click())

        // Confirm product add successfully and toast is visible
        onView(withText(R.string.product_added)).inRoot(withDecorView(not(`is`(activityTestRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
    }

    @Test
    fun stage6_navToCheckOutScreenAndGoBackWithoutBuy() {
        // Nav to DirectorsFragment
        onView(withId(R.id.relativeLayoutCart)).perform(click())

        // wait for buy screen
        Thread.sleep(500)

        //Navigate to back press
        pressBack()

        // Confirm nav to ListProductFragment and check product is is visible
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun stage7_navToCheckOutScreenAndBuy() {
        // Nav to DirectorsFragment
        onView(withId(R.id.relativeLayoutCart)).perform(click())

        // Buy product on click of buy button
        onView(withId(R.id.buttonBuy)).perform(click())

        // Confirm display massage successfully
        onView(withId(R.id.textViewSuccess)).check(matches(isDisplayed()))
    }
}