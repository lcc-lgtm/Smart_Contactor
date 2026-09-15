package com.example.contactapp.screen

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import coil.compose.rememberAsyncImagePainter
import com.example.contactapp.room.Contact
import com.example.contactapp.room.ContactViewModel
import com.example.contactapp.R
import com.example.contactapp.ui.theme.LightPurple
import com.example.contactapp.ui.theme.Purple80
import java.io.File
import java.io.FileOutputStream

//contact screen list function
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(viewModel: ContactViewModel?, navController: NavController){
    val context = LocalContext.current.applicationContext

    Scaffold(
        topBar = {
            TopAppBar(modifier = Modifier.height(100.dp),
                title = {
                    Text(text = "Contact List", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Here's Contact List", Toast.LENGTH_SHORT).show()
                    }){
                        Icon(painter = painterResource(id = R.drawable.contact_list), contentDescription = null)
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple80,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White))
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Purple80,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                onClick = {
                    Toast.makeText(context, "Add Contact", Toast.LENGTH_SHORT).show()
                    navController.navigate("addContact")
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Contact",tint = Color.White)
            }
        }
    ){paddingValues ->
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
            Card(
                modifier = Modifier
                    .fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.6f)
                ),
                border = BorderStroke(2.dp, Purple80.copy(alpha = 0.5f))
            ) {
                val contacts by viewModel?.allContacts!!.observeAsState(initial = emptyList())

                if (contacts.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No contacts available",
                            color = Purple80,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.padding(8.dp)){
                        items(contacts) { contact ->
                            ContactItem(contact = contact){
                                navController.navigate("contactDetail/${contact.id}")
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val personText = if (contacts.size == 1) "1 contact person" else "${contacts.size} contact persons"
                                HorizontalDivider(modifier = Modifier.padding(10.dp).fillMaxWidth(), color = Purple80)
                                Text(
                                    text = "Now you have $personText",
                                    color = Purple80,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}

//show contact list function
@Composable
fun ContactItem(contact: Contact, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.6f))
    ){
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isNoImage = contact.image.isEmpty()
            val initial = if (contact.name.isNotEmpty()) contact.name.take(1).uppercase() else "?"

            if (isNoImage) {
                // no profile image then show first letter of name
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Purple80),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initial,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // if has profile image then show
                Image(
                    painter = rememberAsyncImagePainter(contact.image),
                    contentDescription = contact.name,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }


            Spacer(modifier = Modifier.width(16.dp))
            Text(contact.name)
        }
    }
}

//add contact screen function
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactScreen(viewModel: ContactViewModel?, navController: NavController) {
    val context = LocalContext.current.applicationContext

    var imageUri: Uri? by remember {
        mutableStateOf<Uri?>(null)
    }
    var name by remember {
        mutableStateOf("")
    }
    var phoneNumber by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }

    // helps to pick image from gallery
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(100.dp),
                title = {
                    Text(text = "Add Contact", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                },
                navigationIcon = {
                    Row(){
                        IconButton(
                            onClick = {
                                Toast.makeText(context, "Back to Contact List...", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.back_action),
                                contentDescription = "back to contact list"
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        IconButton(
                            onClick = {
                                Toast.makeText(context, "Here's Add Contact", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_contact),
                                contentDescription = "back to contact list"
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
                onClick = {
                    Toast.makeText(context, "Cancelling Add Contact process...", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            ) {
                Icon(painter = painterResource(id = R.drawable.close_back), contentDescription = "Cancel", tint = Color.White)
            }
        }
    ) { paddingValues ->
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val isProfileIcon = imageUri == null
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(128.dp)
                        .border(3.dp, Purple80, CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    if (isProfileIcon) {
                        Icon(
                            painter = painterResource(id = R.drawable.no_image),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Purple80
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = null,
                            modifier = Modifier
                                .size(128.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // format to only open image, no video...
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

                    if (imageUri != null) {
                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                imageUri = null
                                Toast.makeText(context, "Image removed", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            border = BorderStroke(2.dp, Purple80),
                            elevation = ButtonDefaults.buttonElevation(8.dp)
                        ){
                            Text(text = "Remove Image", color = Purple80)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // (3) text field for name, phone number, email
                TextField(value = name, onValueChange = { name = it },
                    label = { Text(text = "Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(3.dp, Color.White.copy(alpha = 0.9f)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.9f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.DarkGray,
                        focusedIndicatorColor = Color.White.copy(alpha = 0.9f),
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color(0xFF6200EE),
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.DarkGray
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                TextField(value = phoneNumber, onValueChange = { phoneNumber = it },
                    label = { Text(text = "Phone Number") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(3.dp, Color.White.copy(alpha = 0.9f)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.9f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.DarkGray,
                        focusedIndicatorColor = Color.White.copy(alpha = 0.9f),
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color(0xFF6200EE),
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.DarkGray
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                TextField(value = email, onValueChange = { email = it },
                    label = { Text(text = "Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(3.dp, Color.White.copy(alpha = 0.9f)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.9f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.DarkGray,
                        focusedIndicatorColor = Color.White.copy(alpha = 0.9f),
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color(0xFF6200EE),
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.DarkGray
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Add contact BUTTON
                Button(
                    onClick = {
                        val finalImagePath = if (imageUri != null) {
                            copyUriToInternalStorage(context = context, uri = imageUri!!, fileName = "$name.jpg") ?: ""
                        } else {
                            "" // no image then pass null
                        }

                        viewModel?.addContact(finalImagePath, name, phoneNumber, email)
                        navController.navigate("contactList") {
                            popUpTo(0)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Purple80),
                    border = BorderStroke(2.dp, LightPurple),
                    elevation = ButtonDefaults.buttonElevation(8.dp)
                ) {
                    Text(text = "Add Contact")
                }
            }
        }
    }
}

fun copyUriToInternalStorage(context: Context, uri: Uri, fileName: String): String? {
    val file = File(context.filesDir, fileName)
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream) }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnAddContactScreen(){
    AddContactScreen(
        viewModel = null,
        navController = NavController(LocalContext.current)
    )
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnContactListScreen(){
    ContactListScreen(
        viewModel = null,
        navController = NavController(LocalContext.current)
    )
}