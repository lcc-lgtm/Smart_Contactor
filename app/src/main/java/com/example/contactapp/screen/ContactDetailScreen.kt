package com.example.contactapp.screen

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import com.example.contactapp.room.Contact
import com.example.contactapp.room.ContactViewModel
import com.example.contactapp.R
import com.example.contactapp.room.ContactDatabase
import com.example.contactapp.room.ContactRepository
import com.example.contactapp.ui.theme.*

//show contact detail screen function
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailScreen(contact: Contact, viewModel: ContactViewModel?, navController: NavController) {
    val context = LocalContext.current.applicationContext

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(100.dp),
                title = {
                    Text(text = "Contact Detail", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                },
                navigationIcon = {
                    Row(){
                        IconButton(
                            modifier = Modifier.size(48.dp),
                            onClick = {
                                Toast.makeText(context, "Back to Contact List...", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.back_action),
                                contentDescription = null
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        IconButton(
                            modifier = Modifier.size(48.dp),
                            onClick = {
                                Toast.makeText(context, "Here's Contact Detail", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.contact_detail),
                                contentDescription = null
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple80,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Purple80,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                onClick = {
                    Toast.makeText(context, "Edit Contact", Toast.LENGTH_SHORT).show()
                    navController.navigate("editContact/${contact.id}")
                }
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Contact", tint = Color.White)
            }
        }
    ){ paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            Image(
                painter = painterResource(id = R.drawable.contactor_bg),
                contentDescription = "background",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(2.dp, Color.White.copy(alpha = 0.9f))
                ) {
                    Column(modifier = Modifier.fillMaxWidth()
                        .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally)
                    {
                        val isNoImage = contact.image.isEmpty()
                        val initial = if (contact.name.isNotEmpty()) contact.name.take(1).uppercase() else "?"

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(3.dp, Purple80, CircleShape),
                            contentAlignment = Alignment.Center
                        ){
                            if (isNoImage) {
                                // no profile image then show first letter of name
                                Box(
                                    modifier = Modifier
                                        .size(128.dp)
                                        .clip(CircleShape)
                                        .background(Purple80),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = initial,
                                        color = Color.White,
                                        fontSize = 75.sp
                                    )
                                }
                            } else {
                                Image(painter = rememberAsyncImagePainter(contact.image),
                                    contentDescription = contact.name,
                                    modifier = Modifier.size(128.dp).clip(CircleShape),
                                    contentScale = ContentScale.Crop)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(modifier = Modifier.fillMaxWidth()
                            .padding(8.dp),
                            colors = CardDefaults.cardColors(Color.White),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, Color.White.copy(alpha = 0.9f)),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()
                                .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Name: ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(contact.name, fontSize = 16.sp)
                            }
                        }
                        Card(modifier = Modifier.fillMaxWidth()
                            .padding(8.dp),
                            colors = CardDefaults.cardColors(Color.White),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, Color.White.copy(alpha = 0.9f)),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()
                                .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Phone: ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(contact.phoneNumber, fontSize = 16.sp)
                            }
                        }
                        Card(modifier = Modifier.fillMaxWidth()
                            .padding(8.dp),
                            colors = CardDefaults.cardColors(Color.White),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, Color.White.copy(alpha = 0.9f)),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()
                                .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Email: ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(contact.email, fontSize = 16.sp)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(Purple80),
                    border = BorderStroke(2.dp, LightPurple),
                    elevation = ButtonDefaults.buttonElevation(8.dp),
                    onClick = {
                        viewModel?.deleteContact(contact)
                        navController.navigate("contactList") {
                            popUpTo(0)
                        }
                    }
                ){
                    Text("Delete Contact")
                }
            }
        }
    }
}

//edit contact screen function
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactScreen(contact: Contact, viewModel: ContactViewModel?, navController: NavController) {
    val context = LocalContext.current.applicationContext
    var imageUri by remember {
        mutableStateOf(contact.image)
    }
    var name by remember {
        mutableStateOf(contact.name)
    }
    var phoneNumber by remember {
        mutableStateOf(contact.phoneNumber)
    }
    var email by remember {
        mutableStateOf(contact.email)
    }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        // open gallery for picture
        uri?.let {newUri ->
            val internalPath = copyUriToInternalStorage(context, newUri, "$name.jpg")
            internalPath?.let {path -> imageUri = path }
        }
    }

    // Top App Bar
    // Floating action button
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(100.dp),
                title = {
                    Text(text = "Edit Contact", fontSize = 18.sp,fontWeight = FontWeight.ExtraBold)
                },
                navigationIcon = {
                    Row(){
                        IconButton(
                            onClick = {
                                Toast.makeText(context, "Back to Contact Detail...", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.back_action),
                                contentDescription = "Back to Contact Detail"
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        IconButton(
                            onClick = { Toast.makeText(context, "Here's Edit Contact", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.edit_contact),
                                contentDescription = null
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple80,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) {paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            Image(
                painter = painterResource(id = R.drawable.contactor_bg),
                contentDescription = "background",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val isProfileIcon = imageUri.isEmpty()
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(3.dp, Purple80, CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    if (isProfileIcon) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(128.dp)
                                .background(LightPurple),
                            contentAlignment = Alignment.Center
                        ){
                            Icon(
                                painter = painterResource(id = R.drawable.no_image),
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Purple80
                            )
                        }
                    } else {
                        Image(painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = null,
                            modifier = Modifier
                                .size(128.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { launcher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = Purple80),
                        border = BorderStroke(2.dp, LightPurple),
                        elevation = ButtonDefaults.buttonElevation(8.dp)
                    ){
                        Text(text = "Choose Image")
                    }

                    // if current image has Url, then show remove image button
                    if (imageUri.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                imageUri = "" // clear image url
                                Toast.makeText(context, "Image removed", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEAC7F7)),
                            border = BorderStroke(2.dp, Purple80),
                            elevation = ButtonDefaults.buttonElevation(8.dp)
                        ){
                            Text(text = "Remove Image", fontWeight = FontWeight.Bold, color = Purple80)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // (3) text field for name, phone number, email
                // Text fields as edit text, where user enters input.
                TextField(value=name, onValueChange = {name = it},
                    label = {Text(text= "Name")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, Color.White.copy(alpha = 0.3f)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.9f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.3f),

                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.DarkGray,

                        focusedIndicatorColor = Color.White.copy(alpha = 0.9f),
                        unfocusedIndicatorColor = Color.White.copy(alpha = 0.3f),

                        cursorColor = Color(0xFF6200EE),

                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.DarkGray
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                TextField(value=phoneNumber, onValueChange = {phoneNumber = it},
                    label = {Text(text= "Phone Number")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, Color.White.copy(alpha = 0.3f)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.9f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.3f),

                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.DarkGray,

                        focusedIndicatorColor = Color.White.copy(alpha = 0.9f),
                        unfocusedIndicatorColor = Color.White.copy(alpha = 0.3f),

                        cursorColor = Color(0xFF6200EE),

                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.DarkGray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(value=email, onValueChange = {email = it},
                    label = {Text(text= "Email")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, Color.White.copy(alpha = 0.3f)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.9f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.3f),

                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.DarkGray,

                        focusedIndicatorColor = Color.White.copy(alpha = 0.9f),
                        unfocusedIndicatorColor = Color.White.copy(alpha = 0.3f),

                        cursorColor = Color(0xFF6200EE),

                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.DarkGray
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val updateContact = contact.copy(image = imageUri, name = name, phoneNumber = phoneNumber, email = email)
                        viewModel?.updateContact(contact.id, imageUri, name, phoneNumber, email)
                        navController.navigate("contactList") {
                            popUpTo(0)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Purple80),
                    border = BorderStroke(2.dp, LightPurple),
                    elevation = ButtonDefaults.buttonElevation(8.dp)
                ) {
                    Text(text = "Update Contact")
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnEditContactScreen(){
    EditContactScreen(
        contact = Contact(0, "", "", "", ""),
        viewModel = null,
        navController = NavController(LocalContext.current)
    )
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnContactDetailScreen(){
    ContactDetailScreen(
        contact = Contact(0, "", "", "", ""),
        viewModel = null,
        navController = NavController(LocalContext.current)
    )
}

