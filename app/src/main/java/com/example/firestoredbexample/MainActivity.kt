package com.example.firestoredbexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var KEY_TITLE = "title"
    var KEY_DESCRIPTION = "description"

    //get firestore instance
    var db = FirebaseFirestore.getInstance()
    //create document reference
    var noteRef = db.document("Notebook/My First Note")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        submit_btn.setOnClickListener {

            saveNotes()
        }
        loadnote_btn.setOnClickListener {

            loadNotes()
        }
    }

    override fun onStart() {
        super.onStart()

        //listen for new changes in document
        noteRef.addSnapshotListener(this,object : EventListener<DocumentSnapshot>{
            override fun onEvent(it: DocumentSnapshot?, p1: FirebaseFirestoreException?) {

                if(p1 != null){
                    toast("Error while loading!")
                    return
                }
                if(it!!.exists()){

                    var title = it.getString(KEY_TITLE)
                    var description = it.getString(KEY_DESCRIPTION)
                    text_view_data.setText("Title : "+title+"\nDescription : "+description)
                }
            }
        })
    }



    private fun loadNotes() {


        noteRef
            .get()
            .addOnSuccessListener {

                if(it.exists()){

                    var title = it.getString(KEY_TITLE)
                    var description = it.getString(KEY_DESCRIPTION)
                    text_view_data.setText("Title : "+title+"\nDescription : "+description)
                }
                else{
                    toast("Document not exists")
                }
            }
            .addOnFailureListener {

                error(it.toString())
            }
    }

    private fun saveNotes() {

       //get title and notes
        var title = title_et.text.toString()
        var description = desc_et.text.toString()

        //create note hashmap
        var note = HashMap<String,Any>()
        note.put(KEY_TITLE,title)
        note.put(KEY_DESCRIPTION,description)


        noteRef
            .set(note)
            .addOnSuccessListener {

                toast("Note saved")
            }
            .addOnFailureListener {

                error(it.toString())
            }
    }
}
