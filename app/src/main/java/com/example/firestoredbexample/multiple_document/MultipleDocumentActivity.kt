package com.example.firestoredbexample.multiple_document

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.firestoredbexample.R
import com.example.firestoredbexample.dataclass.Note
import com.example.firestoredbexample.dataclass.NoteMultiple
import com.example.firestoredbexample.toast
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_multiple_document.*

class MultipleDocumentActivity : AppCompatActivity() {

    var KEY_TITLE = "title"
    var KEY_DESCRIPTION = "description"

    //get firestore instance
    var db = FirebaseFirestore.getInstance()
    //create notebook collection reference
    var notebookRef = db.collection("Notebook")
    //create document reference
    var noteRef = db.document("Notebook/My First Note")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_multiple_document)

        addnote_btn.setOnClickListener {

            addNote()
        }
        loadnotes_btn.setOnClickListener {

            loadNotes()
        }

    }


    override fun onStart() {
        super.onStart()

        notebookRef.addSnapshotListener(this,object : EventListener<QuerySnapshot>{
            override fun onEvent(querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException?) {

                if(firebaseFirestoreException != null){
                    return
                }
                var data = ""
                for(i in 0 until querySnapshot!!.size()){

                    var note = querySnapshot.documents.get(i).toObject(NoteMultiple::class.java)


                    var documentId = querySnapshot.documents.get(i).id
                    var title = note.getTitle()
                    var description = note.getDescription()
                    var priority = note.getPriority()

                    data += "ID : "+documentId+"\nTitle : "+title+"\nDescription : "+description+"\nPriority : "+priority+"\n\n";
                }
                text_view_data.setText(data)
            }

        })


    }

    private fun loadNotes() {

        notebookRef
            .whereGreaterThanOrEqualTo("priority",2)
            .orderBy("priority",Query.Direction.DESCENDING)
            .limit(3)
            .get()
            .addOnSuccessListener {
                var data = ""
                for(i in 0 until it.size()){


                    var note = it.documents.get(i).toObject(NoteMultiple::class.java)

                    var documentId = it.documents.get(i).id
                    var title = note.getTitle()
                    var description = note.getDescription()
                    var priority = note.getPriority()

                    data += "ID : "+documentId+"\nTitle : "+title+"\nDescription : "+description+"\nPriority : "+priority+"\n\n";
                }
                text_view_data.setText(data)
            }
    }

    private fun addNote() {

        //get title and notes
        var title = title_et.text.toString()
        var description = desc_et.text.toString()

        if(priority_et.text.toString().isNullOrEmpty()){
            priority_et.setText("0")
        }
        var priority = Integer.valueOf(priority_et.text.toString())

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
        var note = NoteMultiple(priority,title,description)
        notebookRef.add(note)//.addOnSuccessListener()
    }
}
