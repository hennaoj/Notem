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
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notem.data.reminder.ReminderViewModel
import com.example.notem.data.user.User
import com.example.notem.data.user.UserViewModel
import com.example.notem.data.viewModelProviderFactoryOf
import com.example.notem.rememberMapViewWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
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
    val userViewModel: UserViewModel = viewModel(
        factory = viewModelProviderFactoryOf { UserViewModel(context.applicationContext as Application) }
    )

    val reminders = reminderViewModel.readAllData.observeAsState(listOf()).value

    val users = userViewModel.readAllData.observeAsState(listOf()).value

    val userLat =  remember { mutableStateOf(0.toDouble()) }
    val userLon =  remember { mutableStateOf(0.toDouble()) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var passWord by remember { mutableStateOf("") }
    var id: Long = 0

    for (i in users.indices) {
        if (users[i].loggedIn) {
            userLat.value = users[i].userY
            userLon.value = users[i].userX
            firstName = users[i].first
            lastName = users[i].last
            username = users[i].userName
            id = users[i].userId
            passWord = users[i].passWord
        }
    }

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
                val location = LatLng(userLat.value, userLon.value)

                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(location, 15f)
                )

                for (reminder in reminders) {
                    val markerOptions = MarkerOptions()
                        .title(reminder.message)
                        .position(LatLng(reminder.locationY, reminder.locationX))

                    map.addMarker(markerOptions)
                }


                var m = map.addMarker(
                    MarkerOptions()
                        .position(LatLng(userLat.value, userLon.value))
                        .title("Your location")
                        .icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                )

                m?.position = LatLng(userLat.value, userLon.value)


                setMapLongClick(
                    map = map,
                    navController = navController,
                    userLat = userLat,
                    userLon = userLon,
                    marker = m,
                    first = firstName,
                    last = lastName,
                    username = username,
                    viewModel = userViewModel,
                    id = id,
                    password = passWord,
                )
            }
        }
    }
}

private fun setMapLongClick(
    map: GoogleMap,
    navController: NavController,
    userLat: MutableState<Double>,
    userLon: MutableState<Double>,
    marker: Marker?,
    first: String,
    last: String,
    username: String,
    password: String,
    viewModel: UserViewModel,
    id: Long,
) {
    map.setOnMapLongClickListener { latlng ->

        userLat.value = latlng.latitude
        userLon.value = latlng.longitude

        marker?.position = LatLng(userLat.value, userLon.value)

        viewModel.updateUser(user = User(
            userId = id,
            userName = username,
            first = first,
            last = last,
            passWord = password,
            loggedIn = true,
            userX = userLon.value,
            userY = userLat.value
        )
        )

        navController.navigate(route = "userLocation")

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