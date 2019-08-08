package com.example.firestoredbexample

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        crud_btn.setOnClickListener {
            startActivity(Intent(this,CrudActivity::class.java))
        }

        bwrite_btn.setOnClickListener {
            startActivity(Intent(this,BachWriteActivity::class.java))
        }

        mdoc_btn.setOnClickListener {
            startActivity(Intent(this,MultipleDocumentActivity::class.java))
        }

        pagination_btn.setOnClickListener {
            startActivity(Intent(this,PaginationActivity::class.java))
        }

        subcoll_btn.setOnClickListener {
            startActivity(Intent(this,SubCollectionsActivity::class.java))
        }

        trans_btn.setOnClickListener {
            startActivity(Intent(this,TransactionActivity::class.java))
        }
    }
}
