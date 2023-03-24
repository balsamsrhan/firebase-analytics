package com.example.firebaseanalytics

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class HomeAdapter(var context: Context, private val noteList: ArrayList<HomeClass>) : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    private lateinit var analytics: FirebaseAnalytics
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listhome,
            parent,false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        analytics = Firebase.analytics
        val currentitem = noteList[position]
        holder.namenote.text = currentitem.name
        var imageUri : String? = null
        imageUri = currentitem.image
        Picasso.get().load(imageUri).into(holder.img)

        holder.itemView.setOnClickListener {
            custom_event()
            var intent = Intent(context, MainActivity2::class.java)
            intent.putExtra("id",currentitem.id)
            Log.d("tt","err"+currentitem.id)
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val namenote : TextView = itemView.findViewById(R.id.txtnamenote)
        val img : ImageView = itemView.findViewById(R.id.imgnote)

    }
    fun custom_event(){
        analytics.logEvent("image") {
            param("name_image","b1.png")
            param("select1","selected")
        }
    }

}