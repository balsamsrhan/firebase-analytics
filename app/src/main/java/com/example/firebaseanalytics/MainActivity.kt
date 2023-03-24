package com.example.firebaseanalytics

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var rec : RecyclerView
    private lateinit var noteArrayList : ArrayList<HomeClass>
    private lateinit var analytics: FirebaseAnalytics
    lateinit var progressDialog: ProgressDialog
    var startTime : Long = 0
    var endTime : Long = 0
    var timeSpent : Long = 0
    var secondsSpent : Long = 0
    var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startTime = System.currentTimeMillis()
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("جاري التحميل")
        progressDialog.setCancelable(true)
        progressDialog.show()
        analytics = Firebase.analytics
        rec = findViewById(R.id.recyclerView)
        screen_track("Activity","MainActivity")
        rec.layoutManager = LinearLayoutManager(this)
        rec.setHasFixedSize(true)
        noteArrayList = arrayListOf()

        db.collection("category").get().addOnSuccessListener {
            if (!it.isEmpty){
                for (data in it.documents){
                    val not : HomeClass? = data.toObject(HomeClass::class.java)
                    if (not != null){
                        noteArrayList.add(not)
                    }
                }
                rec.adapter = HomeAdapter(this,noteArrayList)
            }
            progressDialog.dismiss()
        }

            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    fun screen_track(screenClass:String , screenName : String){
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW){
            param(FirebaseAnalytics.Param.SCREEN_CLASS , screenClass)
            param(FirebaseAnalytics.Param.SCREEN_NAME , screenName)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        endTime = System.currentTimeMillis()
        timeSpent = endTime - startTime
        secondsSpent = (timeSpent / 1000)

        //Log.e("f","h"+secondsSpent)

        val user = hashMapOf(
            "Name page" to "MainActivity",
            "time" to secondsSpent

        )

        db.collection("screen")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    }