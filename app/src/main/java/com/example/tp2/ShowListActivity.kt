package com.example.tp2

import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
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
import com.example.tp2.ui.adapter.ShowListRecyclerViewAdapter
import com.example.tp2.data.source.remote.api.Provider
import com.example.tp2.data.model.ItemToDo
import com.example.tp2.ui.viewmodel.ShowListViewModel
import kotlinx.coroutines.*
import java.lang.Exception

class ShowListActivity: AppCompatActivity(), View.OnClickListener {

    private val activityScope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.Main
    )

    //Recyclerview :  adapter
    private var adapter: ShowListRecyclerViewAdapter? = null
    //lits des éléments affichés
    private var itemList : MutableList<ItemToDo>? = null
    //ViewModel
    private val viewModel by viewModels<ShowListViewModel>()

    // actions interface
    private var btnCreateItem: Button? = null
    private var edtCreateItem: EditText? = null

    private var id: Int? = null
    private var position: Int? = null

    //Préférences de l'app
    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var hash: String? = null










    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(ChoixListActivity.CAT, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        //insertion de la toolbar comme barre d'action de l'activité
        val toolbar: Toolbar =findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        toolbar.title="Show List Activity"

        btnCreateItem = findViewById(R.id.okbutton3)
        edtCreateItem = findViewById(R.id.champitem)
        btnCreateItem!!.setOnClickListener(this)
        edtCreateItem!!.setOnClickListener(this)
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        val smartCastSp = pref
        if (smartCastSp != null){
            editor = smartCastSp.edit()
        }
        val bdl = this.intent.extras
        id = bdl?.getInt("id") //pseudo
        position = bdl?.getInt("position")
        alerter(id.toString())
        alerter(position.toString())
        hash = pref!!.getString("hash", "")

        itemList  = mutableListOf()
        adapter = ShowListRecyclerViewAdapter(itemList!!, this)
        adapter!!.onChange = { item: ItemToDo, fait: Boolean,  ->
            updateItem(id!!, hash!!, item.id, if (fait) 1 else 0)
        }

        //Recyclerview
        val showlistRecyclerView: RecyclerView = findViewById(R.id.showlist_recyclerview)
        showlistRecyclerView.adapter = adapter
        showlistRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)

        viewModel.items.observe(this) {
                viewState ->
            when (viewState) {
                is ShowListViewModel.ViewState.Content -> {
                    itemList?.clear()
                    itemList?.addAll(viewState.items)
                    adapter?.notifyDataSetChanged()
                    Log.i("EDPMR","Lists: ${viewState.items}")
                }
                is ShowListViewModel.ViewState.Error -> {
                    Log.d(ChoixListActivity.CAT, "erreur $this")
                }
            }
        }

        getToDoItems(id!!, hash!!)
    }

    // Crée les actions de la toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun getToDoItems(id: Int, hash: String){
        Log.i("EDPMR","appel getToDoItems")
        viewModel.getItems(id, hash)
    }
//    private fun addItem(id: Int, hash: String, description: String){
//        TODO()
//    }

    private fun updateItem(idList: Int, hash: String, idItem: Int, fait: Int) {
        viewModel.updateItem(idList, hash, idItem, fait)
    }

    private fun addItem(id: Int, hash: String, description: String){
        activityScope.launch {
            try {
                if (verifReseau()){
                    val addItemsResp = Provider.addItem(id, hash, description)
                    Log.i(ChoixListActivity.CAT, addItemsResp.toString())
                    if (addItemsResp.success){
                        Log.i(ChoixListActivity.CAT, "Success")
                        val item: ItemToDo = addItemsResp.item
                        Log.i(ChoixListActivity.CAT, item.toString())
                        adapter!!.itemList.add(item)
                        adapter!!.notifyDataSetChanged()
                    } else{
                        Log.i(ChoixListActivity.CAT, "Erreur de l ajout de l item")
                    }
                }else{
                    Log.i(ChoixListActivity.CAT, "Pas de connexion")
                }
            } catch (e: Exception){
                Log.i(ChoixListActivity.CAT, "Erreur: ${e.message}")
            }
        }
    }

    fun verifReseau(): Boolean {
        // Vérifie la disponibilité du réseau --> si oui changement du statut du btn de connexion
        val cM = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val actNetInf = cM.activeNetworkInfo
        var type = "Aucun réseau détecté"
        var status = false
        if (actNetInf != null) {
            val netState = actNetInf.state
            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0) {
                status = true
                val netType = actNetInf.type
                when (netType) {
                    ConnectivityManager.TYPE_MOBILE -> type = "Réseau mobile détecté"
                    ConnectivityManager.TYPE_WIFI -> type = "Réseau wifi détecté"
                }
            }
        }
        Log.i(ChoixListActivity.CAT, type)
        return status
    }


    override fun onStart() {
        super.onStart()
        Log.i(ChoixListActivity.CAT, "onStart showlist")
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.okbutton3 -> {
                alerter("click sur le bouton Item")
                val description = edtCreateItem!!.text.toString()

                addItem(id!!, hash!!, description)
            }
        }
    }
    // Toast + log
    private fun alerter(s: String?) {
        if (s != null) {
            Log.i(ChoixListActivity.CAT, s)
        }
        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        t.show()
    }

}