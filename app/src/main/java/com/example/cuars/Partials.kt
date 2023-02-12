package com.example.cuars

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cuars.models.SharedViewModel
import com.example.cuars.viewModels.DetailViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun FloatingButton(
    navController: NavController,
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        ExtendedFloatingActionButton(
            text = {
                Text(text = "Report", color = Color.White)
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Report FAB",
                    tint = Color.White,
                )
            },
            onClick = {
                navController.navigate(Screen.FormScreenAdd.route)
            },
        )
        ExtendedFloatingActionButton(
            text = {
                Text(text = "Quick Aid", color = Color.White)
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = "Aid FAB",
                    tint = Color.White,
                )
            },
            onClick = {
                navController.navigate(Screen.AidScreen.route)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextInput(
    label: Int,
    value: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean,
    onValueChange: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }

    Row(horizontalArrangement = Arrangement.Center) {
        OutlinedTextField(
            label = {
                Text(
                    stringResource(id = label)
                )
            },
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            singleLine = singleLine
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextAreaInput(
    label: Int,
    value: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean,
    maxLines:Int,
    onValueChange: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }

    Row(horizontalArrangement = Arrangement.Center) {
        OutlinedTextField(
            label = {
                Text(
                    stringResource(id = label)
                )
            },
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            singleLine = singleLine,
            maxLines = maxLines
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNumberField(
    label: Int,
    value: String,
    singleLine: Boolean,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {

    Row(horizontalArrangement = Arrangement.Center) {
        OutlinedTextField(
            label = {
                Text(
                    stringResource(id = label)
                )
            },
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarSlave(
    topBarTitle: Int, navController: NavController
) {
    var mDisplayMenu by remember { mutableStateOf(false) }

    val mContext = LocalContext.current

    TopAppBar(
        title = {
            Text(text = stringResource(id = topBarTitle))
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
            }
        },
        actions = {
            IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
                Icon(Icons.Default.MoreVert, "")
            }

            DropdownMenu(expanded = mDisplayMenu, onDismissRequest = { mDisplayMenu = false }) {
                DropdownMenuItem(text = { Text(text = user?.displayName.toString()) },
                    onClick = { /*TODO*/ })

                DropdownMenuItem(text = {
                    Text(text = "Logout")
                }, onClick = {
                    Toast.makeText(mContext, "Logout", Toast.LENGTH_SHORT).show()
                    Firebase.auth.signOut()
                })
            }
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarMain(topBarTitle: Int) {

    val contextForToast = LocalContext.current.applicationContext

    var mDisplayMenu by remember { mutableStateOf(false) }

    val mContext = LocalContext.current
    CenterAlignedTopAppBar(
        title = {
            Text(
                stringResource(id = topBarTitle), maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
                Icon(Icons.Default.MoreVert, "")
            }

            DropdownMenu(expanded = mDisplayMenu, onDismissRequest = { mDisplayMenu = false }) {
                DropdownMenuItem(text = { Text(text = user?.displayName.toString()) },
                    onClick = { /*TODO*/ })

                DropdownMenuItem(text = {
                    Text(text = "Logout")
                }, onClick = {
                    Toast.makeText(mContext, "Logout", Toast.LENGTH_SHORT).show()
                    Firebase.auth.signOut()
                    System.exit(-1)
                })
            }
        },
    )
}