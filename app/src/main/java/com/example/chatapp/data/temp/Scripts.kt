package com.example.chatapp.data.temp

import com.example.chatapp.data.remote.FirestoreCollections.usersColl
import com.example.chatapp.domain.model.Gender
import com.example.chatapp.domain.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

object Scripts {
    suspend fun saveDummyUsers() {
        val userList = listOf(
            User(
                name = "Alice",
                email = "alice@example.com",
                profileImageUrl = null,
                bio = "Hello, I'm Alice!",
                gender = Gender.Female,
                dob = "1990-05-15"
            ),
            User(
                name = "Bob",
                email = "bob@example.com",
                profileImageUrl = null,
                bio = "Hey there, I'm Bob!",
                gender = Gender.Male,
                dob = "1985-10-20"
            ),
            User(
                name = "Charlie",
                email = "charlie@example.com",
                profileImageUrl = null,
                bio = "Hi, I'm Charlie!",
                gender = Gender.Male,
                dob = "1992-07-18"
            ),
            User(
                name = "Diana",
                email = "diana@example.com",
                profileImageUrl = null,
                bio = "Nice to meet you, I'm Diana!",
                gender = Gender.Female,
                dob = "1988-12-03"
            ),
            User(
                name = "Ethan",
                email = "ethan@example.com",
                profileImageUrl = null,
                bio = "Greetings, I'm Ethan!",
                gender = Gender.Male,
                dob = "1993-09-28"
            ),
            User(
                name = "Fiona",
                email = "fiona@example.com",
                profileImageUrl = null,
                bio = "Hi there, I'm Fiona!",
                gender = Gender.Female,
                dob = "1995-04-21"
            ),
            User(
                name = "George",
                email = "george@example.com",
                profileImageUrl = null,
                bio = "Hello, I'm George!",
                gender = Gender.Male,
                dob = "1987-06-12"
            ),
            User(
                name = "Hannah",
                email = "hannah@example.com",
                profileImageUrl = null,
                bio = "Nice to meet you, I'm Hannah!",
                gender = Gender.Female,
                dob = "1991-11-05"
            ),
            User(
                name = "Ian",
                email = "ian@example.com",
                profileImageUrl = null,
                bio = "Hey there, I'm Ian!",
                gender = Gender.Male,
                dob = "1986-02-14"
            ),
            User(
                name = "Jasmine",
                email = "jasmine@example.com",
                profileImageUrl = null,
                bio = "Hello, I'm Jasmine!",
                gender = Gender.Female,
                dob = "1994-08-09"
            )
        )

        val collRef = Firebase.firestore.usersColl()
        val batch = Firebase.firestore.batch()

        userList.forEach { user ->
            batch.set(
                collRef.document(collRef.document().id),
                user
            )
        }

        batch.commit().await()
    }
}
