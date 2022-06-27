package com.example.tp2

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2.ui.adapter.RecyclerViewAdapter
import com.example.tp2.data.model.ListeToDo
import com.example.tp2.data.model.ProfilListeToDo
import com.example.tp2.ui.viewmodel.ChoixListViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ChoixListActivity : AppCompatActivity(), View.OnClickListener {
   
    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var okbutton2: Button? = null
    private var champListe: EditText? = null

    private var adapter: RecyclerViewAdapter? = null
    private val viewModel by viewModels<ChoixListViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix)

        //insertion de la toolbar comme barre d'action de l'activité
        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        toolbar.title="Choix List Activity"

        pref = PreferenceManager.getDefaultSharedPreferences(this)
        val smartCastSp = pref
        if (smartCastSp != null){
            editor = smartCastSp.edit()
        }
        val bdl = this.intent.extras
        val pseudo = bdl?.getString("string") //pseudo
        val hash = pref!!.getString("hash", "") //hash
        val lists: MutableList<ListeToDo> = mutableListOf()
        adapter = RecyclerViewAdapter(lists, this)
        val recyclerView: RecyclerView = findViewById(R.id.liste)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)

        champListe = findViewById(R.id.champliste)
        okbutton2 = findViewById(R.id.okbutton2)
        okbutton2!!.setOnClickListener(this)



        viewModel.lists.observe(this) {
                viewState ->
            when (viewState) {
                is ChoixListViewModel.ViewState.Content -> {
                    lists.clear()
                    lists.addAll(viewState.lists)
                    adapter?.notifyDataSetChanged()
                    Log.i("EDPMR","Lists: ${viewState.lists}")

                }
                is ChoixListViewModel.ViewState.Error -> {
                    Log.d(CAT, "erreur $this")
                }
            }
        }
        getToDoLists(pseudo.toString(), hash!!)
    }

    private fun getToDoLists(pseudo: String, hash: String){
            Log.d(CAT, "appel getToDo")
            viewModel.getToDoLists(pseudo, hash)
    }

    // Crée les actions de la toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    companion object {
        val CAT: String = "EDPMR"
    }


    override fun onStart(){
        super.onStart()
        Log.i(CAT, "onStart ChoixList")
        val profilListJson = pref!!.getString("profilList", "")

    }

    override fun onClick(v: View) {
        when (v.id){
            R.id.okbutton2 -> {

            }
        }
    }

    private fun alerter(s: String?) {
        if (s != null) {
            Log.i(CAT, s)
        }
        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        t.show()
    }
}
