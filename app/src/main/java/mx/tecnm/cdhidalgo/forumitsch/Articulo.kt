package mx.tecnm.cdhidalgo.forumitsch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.bumptech.glide.Glide
import com.github.clans.fab.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import mx.tecnm.cdhidalgo.forumitsch.databinding.ActivityArticuloBinding

class Articulo : AppCompatActivity() {

    var imageURL = ""
    var key = ""
    private lateinit var binding:ActivityArticuloBinding
    lateinit var btnBorrarA:FloatingActionButton
    lateinit var btnActualizarA:FloatingActionButton
    lateinit var btnFlotante:FloatingActionButton

    private lateinit var btnAtras: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticuloBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnAtras = findViewById(R.id.btnRegresar)
        btnBorrarA = findViewById(R.id.btnBorrarA)
        btnActualizarA = findViewById(R.id.btnActualizarA)
        btnFlotante = findViewById(R.id.btnActualizarA)

        val bundle = intent.extras
        if(bundle != null){
            binding.descArticulo.text = bundle.getString("Descripcion")
            binding.tituloArticulo.text = bundle.getString("Titulo")
            binding.catArticulo.text = bundle.getString("Categoria")
            binding.fechaArticulo.text = bundle.getString("Key").toString()
            key = bundle.getString("Key").toString()
            imageURL = bundle.getString("Imagen")!!
            Glide.with(this).load(bundle.getString("Imagen")).into(binding.imagenArticulo)
        }

        binding.btnFlotante.visibility = View.INVISIBLE
        if(usuario.rol == "admin"){
            binding.btnFlotante.visibility = View.VISIBLE
        }

        btnBorrarA.setOnClickListener {
            val reference = FirebaseDatabase.getInstance().getReference("Noticias")
            val storage = FirebaseStorage.getInstance()

            val storageReference = storage.getReferenceFromUrl(imageURL)
            storageReference.delete().addOnSuccessListener {
                reference.child(key).removeValue()
                Toast.makeText(this@Articulo, "Eliminado", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, Principal::class.java))
                finish()
            }
        }

        btnAtras.setOnClickListener{
            val intent = Intent (this,Principal::class.java)
            startActivity(intent)
        }

        btnActualizarA.setOnClickListener {
            val intent = Intent(this, ActualizarArticulo::class.java)
                .putExtra("Imagen", imageURL)
                .putExtra("Titulo", binding.tituloArticulo.text)
                .putExtra("Descripcion", binding.descArticulo.text)
                .putExtra("Categoria", binding.catArticulo.text)
                .putExtra("Key", key)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
    }
}