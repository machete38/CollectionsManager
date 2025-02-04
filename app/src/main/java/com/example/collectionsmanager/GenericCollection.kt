package com.example.collectionsmanager

class GenericCollection<T : Comparable<T>> {
    private val items = mutableListOf<T>()

    fun addItem(item: T) {
        items.add(item)
    }

    fun getItems(): List<T> = items.toList()

    fun sortItems(){
        items.sort()
    }

    fun filterItems(predicate: (T) -> Boolean): List<T>{
        return items.filter(predicate)
    }
}