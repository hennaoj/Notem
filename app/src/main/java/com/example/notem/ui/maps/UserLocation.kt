package com.example.notem.ui.maps

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notem.data.reminder.ReminderViewModel
import com.example.notem.data.viewModelProviderFactoryOf
import com.example.notem.rememberMapViewWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun UserLocation(
    navController: NavController
) {
    val mapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val reminderViewModel: ReminderViewModel = viewModel(
        factory = viewModelProviderFactoryOf { ReminderViewModel(context.applicationContext as Application) }
    )

    val reminders = reminderViewModel.readAllData.observeAsState(listOf()).value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(bottom = 0.dp)
    ) {
        HomeAppBar(navController = navController)
        AndroidView({mapView}) { mapView ->
            coroutineScope.launch {
                val map = mapView.awaitMap()
                map.uiSettings.isZoomControlsEnabled = true
                val location = LatLng(65.06, 25.47)

                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(location, 15f)
                )

                for (reminder in reminders) {
                    val markerOptions = MarkerOptions()
                        .title(reminder.message)
                        .position(LatLng(reminder.locationY, reminder.locationX))

                    map.addMarker(markerOptions)
                }

                setMapLongClick(map = map, navController = navController)
            }
        }
    }
}

private fun setMapLongClick(
    map: GoogleMap,
    navController: NavController
) {
    map.setOnMapLongClickListener { latlng ->
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.2f, Lng: %2$.2f",
            latlng.latitude,
            latlng.longitude
        )

        map.addMarker(
            MarkerOptions().position(latlng).title("Reminder location").snippet(snippet)
        ).apply {
            navController
                .previousBackStackEntry
                ?.savedStateHandle
                ?.set("location_data", latlng)
            navController.popBackStack()
        }
    }
}

@Composable
private fun HomeAppBar(
    navController: NavController
) {
    TopAppBar {
        IconButton(
            onClick = { navController.navigate(route = "home") },
            modifier = Modifier.fillMaxWidth(fraction = 0.2f)
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
            )
        }
        IconButton(
            onClick = {  },
            modifier = Modifier.fillMaxWidth(fraction = 0.25f)
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null
            )
        }
        IconButton(
            onClick = { navController.navigate(route = "profile") },
            modifier = Modifier.fillMaxWidth(fraction = 0.3333f)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
        }
        IconButton(
            onClick = { navController.navigate(route = "help") },
            modifier = Modifier.fillMaxWidth(fraction = 0.5f)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null
            )
        }
        IconButton(
            onClick = { navController.navigate(route = "login") },
            modifier = Modifier.fillMaxWidth(fraction = 1f)
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = null
            )
        }
    }
}