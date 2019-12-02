package com.schibsted.spain.barista.sample

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.schibsted.spain.barista.assertion.BaristaAssistiveTextAssertions.assertAssistiveText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HelperTextTest {

  @get:Rule
  var activityRule = ActivityTestRule(HintAndErrorActivity::class.java)

  @Test
  fun assertHelperTextByString() {
    assertAssistiveText(R.id.texthelper_inputlayout, "This is a sample helper text")
  }

  @Test
  fun assertHelperTextByResource() {
    assertAssistiveText(R.id.texthelper_inputlayout, R.string.text_helper_text)
  }
}