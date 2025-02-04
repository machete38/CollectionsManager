package com.example.collectionsmanager

data class User(val name: String, val age: Int) : Comparable<User>{
    override fun compareTo(other: User): Int {
        return this.name.compareTo(other.name)
    }

    override fun toString(): String {
        return "$name ($age)"
    }
}
