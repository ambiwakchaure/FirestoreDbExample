package com.example.firestoredbexample.single_document

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.firestoredbexample.R
import com.example.firestoredbexample.dataclass.Note
import com.example.firestoredbexample.toast
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
        updatedesc_btn.setOnClickListener {

            updateDescription()
        }
        deletedesc_btn.setOnClickListener {

            deleteDescription()
        }
        deletenote_btn.setOnClickListener {

            deleteNote()
        }
    }

    private fun deleteNote() {
        noteRef.delete()
    }
    private fun deleteDescription() {

        //create note hashmap
        //method 1
       /* var note = HashMap<String,Any>()
        note.put(KEY_DESCRIPTION,FieldValue.delete())
        noteRef.update(note)*/

        //method 2
        noteRef.update(KEY_DESCRIPTION,FieldValue.delete())//.addOnSuccessListener()//we can it also
    }


    //update description
    private fun updateDescription() {

        //get description
        var description = desc_et.text.toString()

        //method 1
        //create note hashmap
        /*var note = HashMap<String,Any>()
        note.put(KEY_DESCRIPTION,description)
        noteRef.set(note, SetOptions.merge()) //merge only descriptions*/

        //method 2
        noteRef.update(KEY_DESCRIPTION,description)

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

                    //method 2 using custom object
                    var note = it.toObject(Note::class.java)
                    var title = note.getTitle()
                    var description = note.getDescription()
                    text_view_data.setText("Title : "+title+"\nDescription : "+description)

                    //method 1
                    /*var title = it.getString(KEY_TITLE)
                    var description = it.getString(KEY_DESCRIPTION)
                    text_view_data.setText("Title : "+title+"\nDescription : "+description)*/
                }
                else
                {
                    text_view_data.setText("")
                }
            }
        })
    }



    private fun loadNotes() {


        noteRef
            .get()
            .addOnSuccessListener {

                if(it.exists()){

                    //method 2
                    var note = it.toObject(Note::class.java)

                    var title = note.getTitle()
                    var description = note.getDescription()
                    text_view_data.setText("Title : "+title+"\nDescription : "+description)

                    //method 1
                    /*var title = it.getString(KEY_TITLE)
                    var description = it.getString(KEY_DESCRIPTION)
                    text_view_data.setText("Title : "+title+"\nDescription : "+description)*/
                }
                else{
                    toast("Document not exists")
                }
            }
            .addOnFailureListener {

                com.example.firestoredbexample.error(it.toString())
            }
    }

    private fun saveNotes() {

       //get title and notes
        var title = title_et.text.toString()
        var description = desc_et.text.toString()


        //method 1
        /*var note = HashMap<String,Any>()
        note.put(KEY_TITLE,title)
        note.put(KEY_DESCRIPTION,description)


        noteRef
            .set(note)
            .addOnSuccessListener {

                toast("Note saved")
            }
            .addOnFailureListener {

                error(it.toString())
            }*/

        //method 2
        var note = Note(title,description)
        noteRef
            .set(note)
            .addOnSuccessListener {

                toast("Note saved")
            }
            .addOnFailureListener {

                com.example.firestoredbexample.error(it.toString())
            }
    }
}
