package com.example.firestoredbexample.dataclass

data class Note(
    private var title : String,
    private var description : String) {

    constructor() : this(
        "",
        "")

    public fun getTitle() : String{
        return  title
    }
    public fun getDescription() : String{
        return  description
    }


}