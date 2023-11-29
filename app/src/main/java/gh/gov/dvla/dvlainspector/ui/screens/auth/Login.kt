package com.dvla.pvts.dvlainspectorapp.ui.screens.auth

import android.text.TextUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import gh.gov.dvla.dvlainspector.R
import gh.gov.dvla.dvlainspector.ui.viewmodels.AuthorityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authorityViewModel: AuthorityViewModel,
    navController: NavHostController,
    next: String,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loggedIn by authorityViewModel.isLogged.collectAsState()
    val errorMessage by authorityViewModel.error.collectAsState()

    if (loggedIn) {
        navController.navigate(next)
    } else {
        Column {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxWidth(.5f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    Image(
                        painterResource(id = R.drawable.dvla),
                        contentDescription = "DVLA logo",
                        modifier = Modifier
                            .width(150.dp)
                            .height(150.dp)
                            .padding(16.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text = "email") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Mail,
                                contentDescription = "User email",
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = TextFieldDefaults.outlinedTextFieldColors(focusedLeadingIconColor = MaterialTheme.colorScheme.primary)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(text = "password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "User password",
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = TextFieldDefaults.outlinedTextFieldColors(focusedLeadingIconColor = MaterialTheme.colorScheme.primary)
                    )

                    if (!TextUtils.isEmpty(errorMessage)) {
                        Text(
                            text = errorMessage,
                            modifier = Modifier.padding(top = 8.dp),
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Button(
                        onClick = { authorityViewModel.login(email, password) },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(text = "login")
                    }
                }
            }
        }
    }
}