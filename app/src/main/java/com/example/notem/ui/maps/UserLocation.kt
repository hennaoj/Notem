package com.example.notem.ui.maps

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
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
import com.example.notem.data.reminder.Reminder
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
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
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

    val userLat =  remember { mutableStateOf(0.toDouble())}
    val userLon =  remember { mutableStateOf(0.toDouble()) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var passWord by remember { mutableStateOf("") }
    var profilePic by remember { mutableStateOf("") }
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
            profilePic = users[i].profilePic
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
                    val title = if (reminder.reminderTime != SimpleDateFormat("dd-MM-yyyy HH:mm").parse(
                            "01-01-1970 00:00", ParsePosition(0)).time) {
                        "${reminder.message} (${reminder.reminderTime.formatToString()})"
                    } else {
                        reminder.message
                    }

                    if (reminder.locationX != 0.toDouble()) {
                        val markerOptions = MarkerOptions()
                            .title(title)
                            .position(LatLng(reminder.locationY, reminder.locationX))

                        map.addMarker(markerOptions)
                    }
                }

                val userMarkerOptions = MarkerOptions()
                    .position(LatLng(userLat.value, userLon.value))
                    .title("Your location")
                    .icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))

                val m: Marker? = map.addMarker(userMarkerOptions)

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
                    reminders = reminders,
                    reminderViewModel = reminderViewModel,
                    profilePic = profilePic
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
    reminders: List<Reminder>,
    reminderViewModel: ReminderViewModel,
    profilePic: String
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
            userY = userLat.value,
            profilePic = profilePic
        ))


        for (reminder in reminders) {
            if (reminder.locationX != 0.toDouble()) {
                if (Distance(
                        userLat = userLat.value,
                        userLon = userLon.value,
                        reminderLat = reminder.locationY,
                        reminderLon = reminder.locationX
                    ) < 100
                ) {
                    //if a time has not been set to a reminder, a notification will be sent every time the user
                    //changes their location close to the reminder location
                    //notification sent only if sendNotification is true
                    if (reminder.reminderTime == SimpleDateFormat("dd-MM-yyyy HH:mm").parse(
                            "01-01-1970 00:00",
                            ParsePosition(0)
                        ).time && reminder.sendNotification
                    ) {
                        reminderViewModel.setReminderNotification(
                            delay = 0,
                            reminder = reminder,
                            daily = false,
                            weekly = false
                        )
                    }
                    //if a time and a notification have been set, a notification will be created to launch at the correct
                    //time because the user's location has changed to be close to the reminder location
                    else {
                        val difference: Long = (reminder.reminderTime - Date().time) / 1000
                        if (reminder.sendNotification && (difference > 0)) {
                            reminderViewModel.setReminderNotification(
                                delay = difference,
                                reminder = reminder,
                                daily = false,
                                weekly = false
                            )
                        }
                    }
                } else {

                    //because the deletion of notifications is not implemented before sending them
                    //we need to cancel all notifications that depend on location when the user is
                    //moved further than 100 meters from the location

                    val difference: Long = (reminder.reminderTime - Date().time) / 1000
                    reminderViewModel.cancelReminderNotification(
                        creationTime = reminder.creationTime,
                        delay = difference
                    )

                    //changing the creationtime so that once a new notification is set
                    //it wont get canceled because the id of a notification comes from
                    //the creationtime
                    val updateReminder = Reminder(
                        reminderId = reminder.reminderId,
                        message = reminder.message,
                        locationX = reminder.locationX,
                        locationY = reminder.locationY,
                        reminderTime = reminder.reminderTime,
                        creationTime = Date().time,
                        creatorId = reminder.creatorId,
                        sendNotification = reminder.sendNotification,
                        icon = reminder.icon
                    )

                    reminderViewModel.updateReminder(
                        reminder = updateReminder
                    )

                }
            }
        }

        navController.navigate(route = "userLocation")

    }
}

private fun Distance(
    userLat: Double,
    userLon: Double,
    reminderLat: Double,
    reminderLon: Double
): Float {
    val results= FloatArray(1)
    Location.distanceBetween(userLat, userLon, reminderLat, reminderLon, results)

    return results[0]
}

private fun Long.formatToString(): String {
    return SimpleDateFormat("EEE, d MMM yyyy, 'klo' HH:mm", Locale.getDefault()).format(this)
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