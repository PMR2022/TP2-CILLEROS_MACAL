package com.example.tp2

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.tp2.data.ToDoRepository
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val activityScope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.Main
    )
    private val todoRepository by lazy{
        ToDoRepository.newInstance(application)
    }
    private val CAT = "EDPMR"
    private var modifPseudo: EditText? = null
    private var modifPassword: EditText? = null
    private var checkRemember: CheckBox? = null
    private var okbutton: Button? = null
    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null


    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //insertion de la toolbar comme barre d'action de l'activité
        val toolbar: Toolbar =findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        toolbar.title="Main Activity"

        Log.i(CAT, "onCreate")
        getUIElements()
        initializeSp()
        setOnClickListeners()
    }

    // Crée les actions de la toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id){
            R.id.action_settings -> {
                alerter("Menu : click sur les préférences")
                val iGP = Intent(this, SettingsActivity::class.java)
                startActivity(iGP)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View){
        when (v.id){
            R.id.okbutton -> {
                alerter(modifPseudo!!.text.toString())
                alerter(modifPassword!!.text.toString())
                //on enregistre le login dans les préférences
                if(checkRemember!!.isChecked){
                    editor!!.putString("login", modifPseudo!!.text.toString())
                    editor!!.putString("pass", modifPassword!!.text.toString())
                    editor!!.commit()
                }
                login(modifPseudo!!.text.toString(), modifPassword!!.text.toString())
            }
            R.id.cbRemember -> {
                alerter("click sur CB")
                //On clique sur la case : il faut mettre à jour les préférences
                editor!!.putBoolean("remember", checkRemember!!.isChecked)
                editor!!.commit()
                if(!checkRemember!!.isChecked){
                    // on supprime le login de préférences
                    editor!!.putString("login", "")
                    editor!!.putString("pass", "")
                    editor!!.commit()
                }
            }
            R.id.pseudo -> alerter("Veuillez entrer votre pseudo")
        }
    }

    private fun login(pseudo: String, pass: String) {
        activityScope.launch{
            try{
                val profil = todoRepository.authenticate(pseudo, pass)
                val hash = profil?.hash
                Log.d("EDPMR", hash.toString())
                editor!!.putString("hash", hash)
                editor!!.commit()
                // Fabrication des données
                val bdl = Bundle()
                bdl.putString("string",pseudo)
                //Changer d'activité
                val versChoixList: Intent
                versChoixList = Intent(this@MainActivity, ChoixListActivity::class.java)
                versChoixList.putExtras(bdl)
                startActivity(versChoixList)
            } catch (e: Exception){
                Log.d(CAT, "Erreur requete: ${e.message}")
                alerter(e.message.toString())
            }
        }
    }




    fun getUIElements(){
        //get ui elements
        okbutton = findViewById(R.id.okbutton)
        modifPseudo = findViewById(R.id.pseudo)
        modifPassword = findViewById(R.id.pass)
        checkRemember = findViewById(R.id.cbRemember)
    }
    fun initializeSp(){
        //initialize sp and editor
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        val smartCastSp = pref
        if (smartCastSp != null){
            editor = smartCastSp.edit()
        }
    }
    fun setOnClickListeners(){

        //set on click listeners
        val smartCastBtn = okbutton
        if(smartCastBtn != null){
            smartCastBtn.setOnClickListener(this)
        }
        val smartCastCheckBox = checkRemember
        if(smartCastCheckBox != null){
            smartCastCheckBox.setOnClickListener(this)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i(CAT, "onResume")
    }


    override fun onStart() {
        super.onStart()
        Log.i(CAT, "onStart")

        // Relire les préférences partagées de l'application
        val cbR = pref!!.getBoolean("remember", true)

        //acutaliser l'état de la case á cocher
        checkRemember!!.isChecked = cbR

        //SI la case est cochée, on utilise les préférences pour définir le login
        if(checkRemember!!.isChecked){
            val pseudo = pref!!.getString("login", "login inconnu")
            val pass = pref!!.getString("pass", "pass inconnu")
            modifPseudo!!.setText(pseudo)
            modifPassword!!.setText(pass)
        } else{
            // Sinon, le champ doit etre vide
            modifPseudo!!.setText("")
            modifPassword!!.setText("")
        }
    }
    fun verifReseau(): Boolean {
        // On vérifie si le réseau est disponible,
        // si oui on change le statut du bouton de connexion
        val CM = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val actNetInf = CM.activeNetworkInfo
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
        Log.i(CAT, type)
        return status
    }
    override fun onRestart() {
        super.onRestart()
        Log.i(CAT, "onRestart")
    }
    private fun alerter(s: String) {
        Log.i(CAT, s)
        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        t.show()
    }
}

