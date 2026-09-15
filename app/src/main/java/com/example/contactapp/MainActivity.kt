package com.example.contactapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels

import androidx.room.Room
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contactapp.room.ContactDatabase
import com.example.contactapp.room.ContactRepository
import com.example.contactapp.room.ContactViewModel
import com.example.contactapp.room.ContactViewModelFactory
import com.example.contactapp.screen.AddContactScreen
import com.example.contactapp.screen.ContactDetailScreen
import com.example.contactapp.screen.ContactListScreen
import com.example.contactapp.screen.EditContactScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
            "contact_database"
        ).build()

        val repository = ContactRepository(database.contactDao())

        val viewModel: ContactViewModel by viewModels { ContactViewModelFactory(repository) }



        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "contactList") {
                //create route for navigation
                composable("contactList") { ContactListScreen(viewModel, navController) }
                composable("addContact") { AddContactScreen(viewModel, navController) }
                composable("contactDetail/{contactId}") { backStackEntry ->
                    val contactId = backStackEntry.arguments?.getString("contactId")?.toInt()
                    val contact =
                        viewModel.allContacts.observeAsState(initial = emptyList()).value.find { it.id == contactId }
                    contact?.let { ContactDetailScreen(it, viewModel, navController) }
                }
                composable("editContact/{contactId}") { backStackEntry ->
                    val contactId = backStackEntry.arguments?.getString("contactId")?.toInt()
                    val contact =
                        viewModel.allContacts.observeAsState(initial = emptyList()).value.find { it.id == contactId }
                    contact?.let { EditContactScreen(it, viewModel, navController) }
                }
            }
        }
    }
}

