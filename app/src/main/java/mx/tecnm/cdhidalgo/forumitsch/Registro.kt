package mx.tecnm.cdhidalgo.forumitsch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.forumitsch.dataClass.Usuario

class Registro : AppCompatActivity() {

    private lateinit var btnRegistrarme:Button
    private lateinit var btnYaEstoyRegistrado:Button

    private lateinit var nombre:TextInputLayout
    private lateinit var apellidos:TextInputLayout
    private lateinit var carrera:Spinner
    private lateinit var correo:TextInputLayout
    private lateinit var pass:TextInputLayout
    private lateinit var rol:String

    var _carrera = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        //Acceso a la base de datos
        val baseDeDatos = Firebase.firestore

        btnRegistrarme = findViewById(R.id.btnRegistrarme)
        btnYaEstoyRegistrado = findViewById(R.id.btnYaEstoyRegistrado)
        nombre = findViewById(R.id.nombre)
        apellidos = findViewById(R.id.apellidos)
        carrera = findViewById(R.id.carrera)
        correo = findViewById(R.id.email_registro)
        pass = findViewById(R.id.password_registro)
        rol = "comun"

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

        btnRegistrarme.setOnClickListener{
            val email = correo.editText?.text
            val psw = pass.editText?.text

            val usuario = Usuario(email.toString(), nombre.editText?.text.toString(),
                apellidos.editText?.text.toString(), _carrera, rol)

            val confirmaDialogo = AlertDialog.Builder(it.context)
            confirmaDialogo.setTitle("Confirmar datos:")
            confirmaDialogo.setMessage("""
                Usuario: ${nombre.editText?.text} ${apellidos.editText?.text}
                Carrera: ${_carrera}
                Correo: ${correo.editText?.text}
                Contraseña: ${pass.editText?.text}
            """.trimIndent())
            confirmaDialogo.setPositiveButton("Confirmar"){confirmaDialogo,which->
                if(email.toString().isNotEmpty()&&psw.toString().isNotEmpty()){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        email.toString(),psw.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                            val intent = Intent(this,Login::class.java).apply {
                                baseDeDatos.collection("usuarios").document(
                                    email.toString()).set(usuario)
                            }
                            startActivity(intent)
                        }else{
                            notificacion()
                        }
                    }
                }
            }
            confirmaDialogo.setNegativeButton("Editar datos"){confirmaDialogo,which->
                confirmaDialogo.cancel()
            }
            confirmaDialogo.show()
        }

        btnYaEstoyRegistrado.setOnClickListener{
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
        }
    }

    private fun notificacion() {
        val notiDialogo = AlertDialog.Builder(this)
        notiDialogo.setTitle("Error")
        notiDialogo.setMessage("Se ha producido un Error en la AUTENTICACION")
        notiDialogo.setPositiveButton("Aceptar", null)
        notiDialogo.show()
    }
}