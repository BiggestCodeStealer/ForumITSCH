package mx.tecnm.cdhidalgo.forumitsch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import mx.tecnm.cdhidalgo.forumitsch.adaptadores.AdaptadorNoticias
import mx.tecnm.cdhidalgo.forumitsch.dataClass.Noticia
import mx.tecnm.cdhidalgo.forumitsch.databinding.ActivityPrincipalBinding

class Principal : AppCompatActivity() {

    private lateinit var binding:ActivityPrincipalBinding
    private lateinit var listaNoticias: ArrayList<Noticia>
    private lateinit var adaptadorNoticias: AdaptadorNoticias

    var databaseReference:DatabaseReference? = null
    var eventListener:ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gridLayoutManager = GridLayoutManager(this@Principal, 1)
        binding.rvNoticias.layoutManager = gridLayoutManager

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        listaNoticias = ArrayList()
        adaptadorNoticias = AdaptadorNoticias(this@Principal, listaNoticias)
        binding.rvNoticias.adapter = adaptadorNoticias
        databaseReference = FirebaseDatabase.getInstance().getReference("Noticias")
        dialog.show()

        eventListener = databaseReference!!.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listaNoticias.clear()
                for(itemSnapshot in snapshot.children){
                    val noticia = itemSnapshot.getValue(Noticia::class.java)
                    if(noticia != null){
                        noticia.key = itemSnapshot.key
                        listaNoticias.add(noticia)
                    }
                }
                listaNoticias.reverse()
                adaptadorNoticias.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        })

        binding.btnCrear.visibility = View.INVISIBLE
        if(usuario.rol == "admin"){
            binding.btnCrear.visibility = View.VISIBLE
            binding.btnCrear.setOnClickListener{
                val intent = Intent(this, CrearArticulo::class.java)
                startActivity(intent)
            }
        }

        binding.btnUsuario.setOnClickListener {
            val intent = Intent(this, Actualizar::class.java)
            startActivity(intent)
        }

        binding.buscar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText)
                return true
            }
        })
    }

    override fun onBackPressed() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    fun searchList(text: String){
        val searchList = ArrayList<Noticia>()
        for (noticia in listaNoticias){
            if(noticia.titulo?.lowercase()?.contains(text.lowercase()) == true ||
                noticia.categoria?.lowercase()?.contains(text.lowercase()) == true ||
                noticia.key?.lowercase()?.contains(text.lowercase()) == true){
                searchList.add(noticia)
            }
        }
        adaptadorNoticias.buscarNoticia(searchList)
    }
}