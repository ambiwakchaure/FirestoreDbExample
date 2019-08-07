package com.example.firestoredbexample.dataclass

data class NoteMultiple(
    private val priority : Int,
    private var title : String,
    private var description : String) {

    constructor() : this(
        0,
        "",
        "")

    public fun getTitle() : String{
        return  title
    }
    public fun getDescription() : String{
        return  description
    }
    public fun getPriority() : Int{
        return  priority
    }



}