package com.example.studentregistration

// These lines bring in the tools we need, like taking tools out of a toolbox.
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentregistration.ui.theme.StudentRegistrationTheme
import kotlin.math.roundToInt

// The three programmes shown in the dropdown list.
private val programmes = listOf(
    "Software Engineering Technology",
    "Computer Science",
    "Information Technology"
)

// This is the whole registration form screen.
@Composable
fun RegistrationForm(modifier: Modifier = Modifier) {

    // ---------- THE FORM'S "WHITEBOARD" (state) ----------
    // Each variable below remembers one value. When it changes, the screen redraws itself.

    var fullName by remember { mutableStateOf("") }            // text field: starts empty
    var rollNumber by remember { mutableStateOf("") }          // text field: starts empty
    var email by remember { mutableStateOf("") }               // text field: starts empty

    var programme by remember { mutableStateOf("") }           // chosen programme: empty means "none chosen yet"
    var programmeMenuOpen by remember { mutableStateOf(false) } // is the dropdown list open right now?

    var studyMode by remember { mutableStateOf("") }           // radio group: empty means neither is chosen

    var interestMobile by remember { mutableStateOf(false) }   // checkbox 1: starts unchecked
    var interestWeb by remember { mutableStateOf(false) }      // checkbox 2: starts unchecked
    var interestTesting by remember { mutableStateOf(false) }  // checkbox 3: starts unchecked

    var courseUpdates by remember { mutableStateOf(false) }    // switch: starts off
    var studyHours by remember { mutableIntStateOf(0) }        // slider: whole hours, starts at 0

    var showErrors by remember { mutableStateOf(false) }       // becomes true after Register is pressed
    var showSuccess by remember { mutableStateOf(false) }      // true only after a valid Register

    // ---------- VALIDATION (checking the required values) ----------
    // trim() removes spaces at the start and end, so "   " counts as empty.
    val nameMissing = fullName.trim().isEmpty()
    val rollMissing = rollNumber.trim().isEmpty()
    val emailMissing = email.trim().isEmpty()
    val programmeMissing = programme.isEmpty()
    val studyModeMissing = studyMode.isEmpty()

    // The form is valid only when every required value is present.
    // Interests, course updates and study hours are optional, so they are not checked.
    val formIsValid = !nameMissing && !rollMissing && !emailMissing &&
            !programmeMissing && !studyModeMissing

    // ---------- THE SCREEN ----------
    // Column puts everything one under the other.
    // verticalScroll lets the user scroll the form, imePadding moves it up when the keyboard opens.
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)   // equal gap between all items
    ) {

        // Heading of the screen.
        Text(
            text = "Student Registration",
            style = MaterialTheme.typography.headlineMedium
        )

        // ----- Full name -----
        OutlinedTextField(
            value = fullName,
            // When the user types: save the text and hide the success message.
            onValueChange = { fullName = it; showSuccess = false },
            label = { Text("Full name") },
            singleLine = true,
            // Show the red error only after Register was pressed and the value is missing.
            isError = showErrors && nameMissing,
            supportingText = { if (showErrors && nameMissing) Text("Full name is required") },
            modifier = Modifier.fillMaxWidth()
        )

        // ----- Roll number -----
        OutlinedTextField(
            value = rollNumber,
            onValueChange = { rollNumber = it; showSuccess = false },
            label = { Text("Roll number") },
            singleLine = true,
            isError = showErrors && rollMissing,
            supportingText = { if (showErrors && rollMissing) Text("Roll number is required") },
            modifier = Modifier.fillMaxWidth()
        )

        // ----- Email (uses the email keyboard) -----
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; showSuccess = false },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = showErrors && emailMissing,
            supportingText = { if (showErrors && emailMissing) Text("Email is required") },
            modifier = Modifier.fillMaxWidth()
        )

        // ----- Programme dropdown -----
        Text(text = "Programme", style = MaterialTheme.typography.titleMedium)
        Box(modifier = Modifier.fillMaxWidth()) {
            // This button shows the current choice. Tapping it opens the list.
            OutlinedButton(
                onClick = { programmeMenuOpen = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Before choosing, show "Select programme".
                Text(
                    text = if (programme.isEmpty()) "Select programme" else programme,
                    modifier = Modifier.weight(1f)
                )
                Text(text = "▼")
            }
            // The list that opens below the button.
            DropdownMenu(
                expanded = programmeMenuOpen,
                onDismissRequest = { programmeMenuOpen = false }   // tap outside = close
            ) {
                programmes.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            programme = option          // save the chosen programme
                            programmeMenuOpen = false   // close the list
                            showSuccess = false
                        }
                    )
                }
            }
        }
        if (showErrors && programmeMissing) {
            Text(
                text = "Please select a programme",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // ----- Study mode (radio group: only one can be chosen) -----
        Text(text = "Study mode", style = MaterialTheme.typography.titleMedium)
        RadioRow(
            label = "On campus",
            selected = studyMode == "On campus",
            onSelect = { studyMode = "On campus"; showSuccess = false }
        )
        RadioRow(
            label = "Online",
            selected = studyMode == "Online",
            onSelect = { studyMode = "Online"; showSuccess = false }
        )
        if (showErrors && studyModeMissing) {
            Text(
                text = "Please select a study mode",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // ----- Interests (checkboxes: each one works on its own) -----
        Text(text = "Interests", style = MaterialTheme.typography.titleMedium)
        CheckRow(
            label = "Mobile Development",
            checked = interestMobile,
            onCheckedChange = { interestMobile = it; showSuccess = false }
        )
        CheckRow(
            label = "Web Development",
            checked = interestWeb,
            onCheckedChange = { interestWeb = it; showSuccess = false }
        )
        CheckRow(
            label = "Software Testing",
            checked = interestTesting,
            onCheckedChange = { interestTesting = it; showSuccess = false }
        )

        // ----- Receive course updates (switch, starts off) -----
        // The whole row is tappable, not only the small switch.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = courseUpdates,
                    onValueChange = { courseUpdates = it; showSuccess = false },
                    role = Role.Switch
                )
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Receive course updates",
                modifier = Modifier.weight(1f)
            )
            Switch(checked = courseUpdates, onCheckedChange = null)
        }

        // ----- Weekly study hours (slider with whole numbers from 0 to 20) -----
        Text(
            text = "Weekly study hours: $studyHours",
            style = MaterialTheme.typography.titleMedium
        )
        Slider(
            value = studyHours.toFloat(),
            // The slider gives a decimal number, so we round it to a whole hour.
            onValueChange = { studyHours = it.roundToInt(); showSuccess = false },
            valueRange = 0f..20f,
            steps = 19      // 19 stops between 0 and 20 means whole hours only
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "0")
            Text(text = "20")
        }

        // ----- Buttons -----
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Register: start showing error messages, and show success only if everything is valid.
            Button(
                onClick = {
                    showErrors = true
                    showSuccess = formIsValid
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Register")
            }

            // Reset: put every value back to how it was at the start and hide all messages.
            OutlinedButton(
                onClick = {
                    fullName = ""
                    rollNumber = ""
                    email = ""
                    programme = ""
                    programmeMenuOpen = false
                    studyMode = ""
                    interestMobile = false
                    interestWeb = false
                    interestTesting = false
                    courseUpdates = false
                    studyHours = 0
                    showErrors = false
                    showSuccess = false
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Reset")
            }
        }

        // ----- Success message (the entered values stay visible above it) -----
        if (showSuccess) {
            Text(
                text = "Registration form completed successfully",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

// One radio option: a round button plus its label.
// The whole row is tappable, not only the small circle.
@Composable
private fun RadioRow(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onSelect, role = Role.RadioButton)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)   // the row handles the tap
        Text(text = label, modifier = Modifier.padding(start = 8.dp))
    }
}

// One checkbox option: a box plus its label.
// The whole row is tappable, not only the small box.
@Composable
private fun CheckRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(value = checked, onValueChange = onCheckedChange, role = Role.Checkbox)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = null)   // the row handles the tap
        Text(text = label, modifier = Modifier.padding(start = 8.dp))
    }
}

// A preview so we can see the form inside Android Studio without running the app.
@Preview(showBackground = true)
@Composable
fun RegistrationFormPreview() {
    StudentRegistrationTheme {
        RegistrationForm()
    }
}