package com.example.studentregistration

// These lines bring in the tools we need, like taking tools out of a toolbox.
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.studentregistration.ui.theme.StudentRegistrationTheme

// MainActivity is the front door of the app. It is the first thing that opens.
class MainActivity : ComponentActivity() {

    // onCreate runs once when the app starts.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Let the app draw under the phone's top and bottom bars (modern full-screen look).
        enableEdgeToEdge()

        // setContent tells the app what to draw on the screen.
        setContent {
            // The theme gives the app its colours and text styles.
            StudentRegistrationTheme {
                // Scaffold is the basic page frame. innerPadding keeps our content
                // away from the phone's top and bottom bars.
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Show our registration form inside the frame.
                    RegistrationForm(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}