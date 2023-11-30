package mx.tecnm.cdhidalgo.forumitsch

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import mx.tecnm.cdhidalgo.forumitsch.dataClass.Noticia
import mx.tecnm.cdhidalgo.forumitsch.databinding.ActivityCrearArticuloBinding
import java.text.DateFormat
import java.util.Calendar

class ActualizarArticulo : AppCompatActivity() {
/*
    private lateinit var binding: ActivityCrearArticuloBinding
    var imageURL: String? = null
    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearArticuloBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val data = result.data
                uri = data!!.data
                binding.subirImagen.setImageURI(uri)
            }else {
                Toast.makeText(this, "No selecciono una imagen", Toast.LENGTH_SHORT).show()
            }
        }

        binding.subirImagen.setOnClickListener{
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        binding.btnSubir.setOnClickListener {
            saveData()
        }
        */
    }
    private fun saveData(){
        val storageReference = FirebaseStorage.getInstance().reference.child("Imagenes de noticias")
            .child(uri!!.lastPathSegment!!)

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            imageURL = urlImage.toString()
            uploadData()
            dialog.dismiss()
        }.addOnFailureListener{
            dialog.dismiss()
        }
    }
    private fun uploadData(){
        val titulo = binding.subirTitulo.text.toString()
        val desc = binding.subirDesc.text.toString()
        val categoria = binding.subirCat.text.toString()

        val noticia = Noticia(titulo, desc, categoria, imageURL)
        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        FirebaseDatabase.getInstance().getReference("Noticias").child(currentDate)
            .setValue(noticia).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.addOnFailureListener{ e ->
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

 */
}