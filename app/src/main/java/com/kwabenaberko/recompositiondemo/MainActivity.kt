package com.kwabenaberko.recompositiondemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kwabenaberko.recompositiondemo.ui.theme.RecompositionDemoTheme
import kotlinx.coroutines.delay

const val TAG = "Recomposition"

class Ref(var value: Int)

@Composable
inline fun LogCompositions(tag: String, msg: String) {
    if (BuildConfig.DEBUG) {
        val ref = remember { Ref(0) }
        SideEffect { ref.value += 1 }
        Log.d(tag, "Compositions: $msg ${ref.value}")
    }
}


data class Employee(
    val name: String,
    val age: Int,
    val occupation: String
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecompositionDemoTheme {
                LogCompositions(TAG, "SetContent Recomposed")
                App()
            }
        }
    }
}

@Composable
fun App() {
    var employee by remember {
        mutableStateOf(
            Employee(name = "Anthony", age = 99, occupation = "Biochemist")
        )
    }
//    var list by remember { mutableStateOf(IntRange(2, 10).map { it }) }
//
//    LaunchedEffect(Unit) {
//        delay(2000L)
//        list = IntRange(1, 10).map { it }
//    }

    LogCompositions(TAG, "App Recomposed")

    Occupation(employee)

    Column {
        LogCompositions(TAG, "Column Recomposed")
        Name(employee)
        Age(employee)
        Button(onClick = {
            employee = employee.copy(name = "John")
            Log.d(TAG, "${employee}")
        }) {
            LogCompositions(TAG, "Button Recomposed")
            Text(text = "Update")
        }
//        LazyColumn(modifier = Modifier.fillMaxWidth()) {
//            items(
//                items = list,
//            ) { number ->
//                Item(number)
//            }
//        }
    }
}

//@Composable
//fun Item(number: Int) {
//    LogCompositions(TAG, msg = "Item Number: $number Recomposed")
//    Text(text = "$number")
//}

@Composable
fun Age(employee: Employee) {
    LogCompositions(TAG, "Age Recomposed")
    Text(text = eligibilityText(employee.age))
}

@Composable
fun Occupation(employee: Employee) {
    LogCompositions(TAG, "Occupation Recomposed")
    Text(text = employee.occupation)
}

@Composable
fun Name(employee: Employee) {
    LogCompositions(TAG, "Name Recomposed")
    Text(text = employee.name)
}

@Composable
@ReadOnlyComposable
fun eligibilityText(age: Int) = if (age > 18) {
    stringResource(R.string.eligible_to_vote)
} else {
    stringResource(R.string.not_eligible_to_vote)
}
