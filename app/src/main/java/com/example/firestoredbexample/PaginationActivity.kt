package com.example.firestoredbexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.firestoredbexample.dataclass.NoteMultiple
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_merge_query.*

class PaginationActivity : AppCompatActivity() {


    //get firestore instance
    var db = FirebaseFirestore.getInstance()
    //create notebook collection reference
    var notebookRef = db.collection("Notebook")


    var data = ""
    lateinit var lastResult: DocumentSnapshot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merge_query)

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
                //contain only changed document
                //so we dont go to the whole dataset document
                for(dc in querySnapshot!!.documentChanges)
                {
                    var documentSnapshot = dc.document

                    var documentId = documentSnapshot.id
                    var oldIndex = dc.oldIndex
                    var newIndex = dc.newIndex

                    when(dc.type){
                        DocumentChange.Type.ADDED ->
                            text_view_data.append("\nAdded : "+documentId+"\nOld Index : "+oldIndex+"\nNew Index : "+newIndex)
                        DocumentChange.Type.MODIFIED ->
                            text_view_data.append("\nModified : "+documentId+"\nOld Index : "+oldIndex+"\nNew Index : "+newIndex)
                        DocumentChange.Type.REMOVED ->
                            text_view_data.append("\nRemoved : "+documentId+"\nOld Index : "+oldIndex+"\nNew Index : "+newIndex)
                    }
                }

            }

        })


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
