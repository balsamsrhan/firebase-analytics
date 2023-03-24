package com.example.firebaseanalytics

import android.app.ProgressDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detailes.*
import java.util.*

class Detailes : AppCompatActivity() {
    private lateinit var analytics: FirebaseAnalytics
    lateinit var progressDialog: ProgressDialog
    var startTime : Long = 0
    var endTime : Long = 0
    var timeSpent : Long = 0
    var secondsSpent : Long = 0
    var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailes)

        startTime = System.currentTimeMillis()
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("جاري التحميل")
        progressDialog.setCancelable(true)
        progressDialog.show()

        var txtname = findViewById<TextView>(R.id.txtna)
        var txtdec = findViewById<TextView>(R.id.txtdes)
        var txtnumber = findViewById<TextView>(R.id.txtnum)
        var img = findViewById<ImageView>(R.id._imageuploadmeal)
        analytics = Firebase.analytics
        screen_track("Activity3","Detailes")
        var tv_namer=intent!!.getStringExtra("NotesName")
        db.collection("notes").whereEqualTo("name",tv_namer).get().addOnSuccessListener {
        txtname.setText(it.documents.get(0).get("name").toString())
        txtdec.setText(it.documents.get(0).get("dec").toString())
        txtnumber.setText(it.documents.get(0).get("numchar").toString())
            Picasso.get().load(
                it.documents.get(0).get("image")
                    .toString()
            ).into(img)
            progressDialog.dismiss()
        }.addOnFailureListener {
            Log.w(ContentValues.TAG, "Error getting documents.", it)
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
            "Name page" to "Detailes",
            "time" to secondsSpent

        )

        db.collection("screen")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }
}