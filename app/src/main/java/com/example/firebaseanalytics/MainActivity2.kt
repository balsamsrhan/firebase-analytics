package com.example.firebaseanalytics

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity2 : AppCompatActivity() {
    lateinit var rec: RecyclerView
     var startTime : Long = 0
    var endTime : Long = 0
    var timeSpent : Long = 0
    var secondsSpent : Long = 0
    var db = Firebase.firestore
    private lateinit var noteArrayList: ArrayList<noteid>
    private lateinit var analytics: FirebaseAnalytics
    val myid = noteid()
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        startTime = System.currentTimeMillis()
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("جاري التحميل")
        progressDialog.setCancelable(true)
        progressDialog.show()

        analytics = Firebase.analytics
        rec = findViewById(R.id.recyclerView1)
        rec.layoutManager = LinearLayoutManager(this)
        rec.setHasFixedSize(true)
        noteArrayList = arrayListOf()
        screen_track("Activity2","MainActivity2")
        var intentid = intent.getIntExtra("id", 0)
        //var id = myid.id

        db.collection("notes").get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (data in it.documents) {
                    if (intentid == data.getLong("id")?.toInt()) {
                        val not: noteid? = data.toObject(noteid::class.java)
                        if (not != null) {
                            noteArrayList.add(not)
                        }
                    }
                }
                rec.adapter = CategoryAdapter(this, noteArrayList)
            }
            progressDialog.dismiss()

        }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }


    }


    fun screen_track(screenClass: String, screenName: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
         endTime = System.currentTimeMillis()
         timeSpent = endTime - startTime
         secondsSpent = (timeSpent / 1000)

       //Log.e("f","h"+secondsSpent)

        val user = hashMapOf(
            "Name page" to "MainActivity2",
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


