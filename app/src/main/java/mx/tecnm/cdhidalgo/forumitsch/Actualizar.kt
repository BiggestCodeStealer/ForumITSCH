package mx.tecnm.cdhidalgo.forumitsch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.forumitsch.dataClass.Usuario

class Actualizar : AppCompatActivity() {

    private lateinit var btnActualizar:Button
    private lateinit var btnEliminar:Button

    private lateinit var nombre:TextInputLayout
    private lateinit var apellidos:TextInputLayout
    private lateinit var carrera: Spinner
    private lateinit var pass:TextInputLayout
    private lateinit var rol:String

    var _carrera = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar)

        //Acceso a la base de datos
        val baseDeDatos = Firebase.firestore

        btnActualizar = findViewById(R.id.btnActualizar)
        btnEliminar = findViewById(R.id.btnEliminar)
        nombre = findViewById(R.id.uNombre)
        apellidos = findViewById(R.id.uApellidos)
        carrera = findViewById(R.id.uCarrera)
        pass = findViewById(R.id.uPass)
        rol = usuario.rol

        val arreglo_carreras = resources.getStringArray(R.array.carreras)
        val adaptadorCarreras = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            arreglo_carreras
        )
        carrera.adapter = adaptadorCarreras
        carrera.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                _carrera = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnActualizar.setOnClickListener{
            val email = usuario.correo
            val psw = pass.editText?.text

            val usuario = Usuario(email, nombre.editText?.text.toString(),
                apellidos.editText?.text.toString(), _carrera, rol)

            val confirmaDialogo = AlertDialog.Builder(it.context)
            confirmaDialogo.setTitle("Confirmar datos:")
            confirmaDialogo.setMessage("""
                Usuario: ${nombre.editText?.text} ${apellidos.editText?.text}
                Carrera: ${_carrera}
                Correo: ${email}
                Contraseña: ${pass.editText?.text}
            """.trimIndent())
            confirmaDialogo.setPositiveButton("Confirmar"){confirmaDialogo,which->
                if(email.isNotEmpty()&&psw.toString().isNotEmpty()){
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                        email,psw.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                            val user = FirebaseAuth.getInstance().currentUser
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(nombre.editText?.text.toString())
                                .build()
                            user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val intent = Intent(this,Login::class.java).apply {
                                        baseDeDatos.collection("usuarios").document(
                                            email).set(usuario)
                                    }
                                    startActivity(intent)
                                }else{
                                    notificacion()
                                }
                            }
                        }
                    }
                } else{
                    Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
                }
            }
            confirmaDialogo.setNegativeButton("Editar datos"){confirmaDialogo,which->
                confirmaDialogo.cancel()
            }
            confirmaDialogo.show()
        }

        btnEliminar.setOnClickListener{
            val email = usuario.correo
            val psw = pass.editText?.text


            val confirmaDialogo = AlertDialog.Builder(it.context)
            confirmaDialogo.setTitle("¿Está seguro de que quiere eliminar su cuenta?")
            confirmaDialogo.setPositiveButton("Confirmar"){confirmaDialogo,which->
            if(email.isNotEmpty()&&psw.toString().isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    email,psw.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        val user = FirebaseAuth.getInstance().currentUser
                        user?.delete()?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this,Login::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                }
            }else{
                Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
            }
            }
            confirmaDialogo.setNegativeButton("Cancelar"){confirmaDialogo,which->
                confirmaDialogo.cancel()
            }
            confirmaDialogo.show()
        }

    }

    private fun notificacion() {
        val notiDialogo = AlertDialog.Builder(this@Actualizar)
        notiDialogo.setTitle("Error")
        notiDialogo.setMessage("Se ha producido un Error en la AUTENTICACION")
        notiDialogo.setPositiveButton("Aceptar", null)
        notiDialogo.show()
    }
}