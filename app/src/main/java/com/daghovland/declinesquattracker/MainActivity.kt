package com.daghovland.declinesquattracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daghovland.declinesquattracker.data.SquatSet
import com.daghovland.declinesquattracker.ui.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val GOAL = 6
private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // lets the UI draw behind status/nav bars
        setContent {
            MaterialTheme {
                SquatTrackerScreen()
            }
        }
    }
}

@Composable
fun SquatTrackerScreen(vm: MainViewModel = viewModel()) {
    // collectAsStateWithLifecycle only collects while the app is in the foreground —
    // stops collecting when backgrounded, resumes on return. Battery-friendly.
    val sets by vm.todaySets.collectAsStateWithLifecycle()
    val count = sets.size
    val goalReached = count >= GOAL
    val goalColor = Color(0xFF2E7D32)  // Material green-800

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            // — Counter ——————————————————————————————————————————————
            Text(
                text = "$count / $GOAL",
                fontSize = 80.sp,
                fontWeight = FontWeight.Bold,
                color = if (goalReached) goalColor else MaterialTheme.colorScheme.onSurface
            )
            if (goalReached) {
                Text(
                    text = "Done for today!",
                    fontSize = 20.sp,
                    color = goalColor,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.weight(1f))

            // — Big log button ————————————————————————————————————————
            Button(
                onClick = { vm.logSet() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (goalReached) goalColor else MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Log a set", fontSize = 28.sp)
            }

            Spacer(Modifier.height(32.dp))

            // — Timestamp list ————————————————————————————————————————
            if (sets.isNotEmpty()) {
                Text(
                    text = "Today's sets",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(Modifier.height(8.dp))
                // LazyColumn = RecyclerView equivalent; only renders visible items
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(sets, key = { it.id }) { set ->
                        Text(
                            text = set.timestamp.toTimeString(),
                            modifier = Modifier.padding(vertical = 4.dp),
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

private fun Long.toTimeString(): String = timeFormat.format(Date(this))
