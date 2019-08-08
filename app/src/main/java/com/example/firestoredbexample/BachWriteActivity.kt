package com.example.firestoredbexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.firestoredbexample.dataclass.NoteMultiple
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_bach_write.*

class BachWriteActivity : AppCompatActivity() {

    //get firestore instance
    var db = FirebaseFirestore.getInstance()
    //create notebook collection reference
    var notebookRef = db.collection("Notebook")


    var data = ""
    lateinit var lastResult: DocumentSnapshot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bach_write)

        supportActionBar!!.setTitle("Batch Write")
        addnote_btn.setOnClickListener {

            addNote()
        }
        loadnotes_btn.setOnClickListener {

            loadNotes()
        }

        executeBatchWrite()

    }

    private fun executeBatchWrite() {

        var batch = db.batch()
        //create document reference
        var doc1 = notebookRef.document("New Note")
        batch.set(doc1,NoteMultiple(1,"Title 1","Description 1"))

        var doc2 = notebookRef.document("4f50L8NpBoMByg6DrI5y")
        batch.update(doc2,"title","Updated Note")

        var doc3 = notebookRef.document("4f50L8NpBoMByg6DrI5y")
        batch.delete(doc3)

        var doc4 = notebookRef.document()
        batch.set(doc4,NoteMultiple(1,"Added Note","Added Note"))

        batch
            .commit()
            .addOnSuccessListener {
                text_view_data.setText("")
            }
            .addOnFailureListener {
                text_view_data.setText(""+it.toString())
            }
    }

    private fun loadNotes() {


        var query : Query
        try
        {
            if(lastResult == null)
            {
                query = notebookRef
                    .orderBy("priority")
                    .limit(3)
            }
            else
            {
                query = notebookRef
                    .orderBy("priority")
                    .startAfter(lastResult)
                    .limit(3)
            }
            loadDataa(query)

        }
        catch (e : Exception)
        {
            query = notebookRef
                .orderBy("priority")
                .limit(3)

            loadDataa(query)
        }

    }

    fun loadDataa(query : Query){

        query.get()
            .addOnSuccessListener {

                for(i in 0 until it.size()){


                    var note = it.documents.get(i).toObject(NoteMultiple::class.java)

                    var documentId = it.documents.get(i).id
                    var title = note!!.getTitle()
                    var description = note.getDescription()
                    var priority = note.getPriority()

                    data += "ID : "+documentId+"\nTitle : "+title+"\nDescription : "+description+"\nPriority : "+priority+"\n\n";
                }
                if(it.size() > 0){
                    data += "-------------------------------\n\n"
                    text_view_data.append(data)
                    text_view_data.setText(data)
                    var documentSnapshot = it.documents.get(it.size() - 1)
                    lastResult = documentSnapshot
                }

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
