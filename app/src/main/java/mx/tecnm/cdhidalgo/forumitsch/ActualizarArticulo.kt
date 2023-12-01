package mx.tecnm.cdhidalgo.forumitsch

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import mx.tecnm.cdhidalgo.forumitsch.dataClass.Noticia
import mx.tecnm.cdhidalgo.forumitsch.databinding.ActivityActualizarArticuloBinding
import mx.tecnm.cdhidalgo.forumitsch.databinding.ActivityCrearArticuloBinding
import java.text.DateFormat
import java.util.Calendar

class ActualizarArticulo : AppCompatActivity() {

    private lateinit var btnAtras: ImageButton
    private lateinit var binding: ActivityActualizarArticuloBinding
    var imageURL: String? = null
    var uri: Uri? = null
    var key =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActualizarArticuloBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnAtras = findViewById(R.id.btnRegresar)

        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val data = result.data
                uri = data!!.data
                binding.actualizarImagen.setImageURI(uri)
            }
        }

        val bundle = intent.extras
        if(bundle != null){
            binding.actualizarDesc.setText(bundle.getString("Descripcion"))
            binding.actualizarTitulo.setText(bundle.getString("Titulo"))
            binding.actualizarCat.setText(bundle.getString("Categoria"))
            key = bundle.getString("Key").toString()
            imageURL = bundle.getString("Imagen")!!
            Glide.with(this).load(bundle.getString("Imagen")).into(binding.actualizarImagen)
        }

        binding.actualizarImagen.setOnClickListener{
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        btnAtras.setOnClickListener{
            val intent = Intent (this,Principal::class.java)
            startActivity(intent)
        }

        binding.btnUpdate.setOnClickListener {
            saveData()
            val intent = Intent(this, Principal::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
    }

    private fun saveData() {
        val storageReference = FirebaseStorage.getInstance().reference.child("Imagenes de noticias")

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        if (uri != null) {
            storageReference.child(uri!!.lastPathSegment!!)
            storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isComplete);
                val urlImage = uriTask.result
                imageURL = urlImage.toString()
                updateData()
                dialog.dismiss()
            }.addOnFailureListener{
                dialog.dismiss()
            }
        } else {
            updateData()
            dialog.dismiss()
        }
    }

    private fun updateData(){
        val titulo = binding.actualizarTitulo.text.toString()
        val desc = binding.actualizarDesc.text.toString()
        val categoria = binding.actualizarCat.text.toString()

        val noticia = Noticia(titulo, desc, categoria, imageURL, key)

        FirebaseDatabase.getInstance().getReference("Noticias").child(key)
            .setValue(noticia).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.addOnFailureListener{ e ->
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}