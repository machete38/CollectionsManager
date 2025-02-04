package com.example.collectionsmanager

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collectionsmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val stringCollection = GenericCollection<String>()
    private val intCollection = GenericCollection<Int>()
    private val userCollection = GenericCollection<User>()

    private lateinit var adapter: GenericAdapter<Any>
    private lateinit var currentCollection: GenericCollection<*>

    private var currentCollectionType: CollectionType = CollectionType.STRING
    enum class CollectionType {
        STRING, INT, USER
    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = GenericAdapter(emptyList())
        currentCollection = stringCollection

        stringCollection.addItem("String")
        intCollection.addItem(12)
        userCollection.addItem(User("user", 21))
        setupSpinner()
        setupButtons()
        setupRV()

    }

    private fun setupButtons() {
        binding.addButton.setOnClickListener {
            val input = binding.editText.text.toString()
            when (currentCollectionType) {
                CollectionType.STRING -> (currentCollection as GenericCollection<String>).addItem(
                    input
                )

                CollectionType.INT -> (currentCollection as GenericCollection<Int>).addItem(
                    input.toIntOrNull() ?: 0
                )

                CollectionType.USER -> {
                    val parts = input.split(",")
                    if (parts.size == 2) {
                        val name = parts[0].trim()
                        val age = parts[1].trim().toIntOrNull() ?: 0
                        (currentCollection as GenericCollection<User>).addItem(User(name, age))
                    }
                }

            }
            updateRV()
            binding.editText.text.clear()
        }
        binding.sortButton.setOnClickListener {
            when (currentCollectionType) {
                CollectionType.STRING -> stringCollection.sortItems()
                CollectionType.INT -> intCollection.sortItems()
                CollectionType.USER -> userCollection.sortItems()
            }
            updateRV()
        }

        binding.filterButton.setOnClickListener {
            when (currentCollectionType) {
                CollectionType.STRING -> {
                    val filtered = stringCollection.filterItems { it.length > 3 }
                    updateRV(filtered)
                }
                CollectionType.INT -> {
                    val filtered = intCollection.filterItems { it > 10 }
                    updateRV(filtered)
                }
                CollectionType.USER -> {
                    val filtered = userCollection.filterItems { it.age > 18 }
                    updateRV(filtered)
                }
            }
        }
    }

    private fun setupSpinner() = with(binding) {
        ArrayAdapter.createFromResource(
            this@MainActivity,
            R.array.collection_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        currentCollection = stringCollection
                        currentCollectionType = CollectionType.STRING
                    }
                    1 -> {
                        currentCollection = intCollection
                        currentCollectionType = CollectionType.INT
                    }
                    2 -> {
                        currentCollection =  userCollection
                        currentCollectionType = CollectionType.USER
                    }

                }
                updateRV()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }


        }
    }

    private fun setupRV() = with(binding) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
        updateRV()
    }

    private fun updateRV(items: List<Any>? = null) {
        val displayItems = items ?: currentCollection.getItems()
        adapter.updateItems(displayItems)
    }
}