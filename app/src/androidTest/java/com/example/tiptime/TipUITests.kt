import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.performTextInput

import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import java.text.NumberFormat
import com.example.splitcash.ui.theme.SplitCashTheme
import com.example.splitcash.TipTimeLayout

class TipUITests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun calculate_20_percent_tip() {
        composeTestRule.setContent {
            SplitCashTheme {
                Surface(Modifier.fillMaxSize()) {
                    TipTimeLayout()
                }
            }
        }

        // Type 10 into the first editable field
        composeTestRule.onAllNodes(hasSetTextAction())[0]
            .performTextInput("10")

        // Type 20 into the second editable field
        composeTestRule.onAllNodes(hasSetTextAction())[1]
            .performTextInput("20")

        val expectedTip = NumberFormat.getCurrencyInstance().format(2)
        composeTestRule.onNodeWithText("Tip Amount: $expectedTip")
            .assertExists("Tip Value did not match!")
    }
}
