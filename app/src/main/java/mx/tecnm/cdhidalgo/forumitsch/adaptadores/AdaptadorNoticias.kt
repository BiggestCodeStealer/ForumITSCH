package mx.tecnm.cdhidalgo.forumitsch.adaptadores

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import mx.tecnm.cdhidalgo.forumitsch.R
import com.bumptech.glide.Glide
import mx.tecnm.cdhidalgo.forumitsch.Articulo
import mx.tecnm.cdhidalgo.forumitsch.dataClass.Noticia

class AdaptadorNoticias (private val context: android.content.Context, var listaNoticias:ArrayList<Noticia>):
    RecyclerView.Adapter<AdaptadorNoticias.NoticiaViewHolder>(){

    var onProductClick:((Noticia)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): NoticiaViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.card_noticia,parent,false)
        return NoticiaViewHolder(vista)
    }

    override fun onBindViewHolder(holder: NoticiaViewHolder, position: Int) {
        Glide.with(context).load(listaNoticias[position].imagen).into(holder.recImagen)
        holder.recTitulo.text = listaNoticias[position].titulo
        holder.recDesc.text = listaNoticias[position].descripcion
        holder.recCat.text = listaNoticias[position].categoria

        holder.recCard.setOnClickListener{
            val intent = Intent(context, Articulo::class.java)
            intent.putExtra("Imagen", listaNoticias[holder.adapterPosition].imagen)
            intent.putExtra("Titulo", listaNoticias[holder.adapterPosition].titulo)
            intent.putExtra("Descripcion", listaNoticias[holder.adapterPosition].descripcion)
            intent.putExtra("Categoria", listaNoticias[holder.adapterPosition].categoria)
            intent.putExtra("Key", listaNoticias[holder.adapterPosition].key)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listaNoticias.size
    }

    fun buscarNoticia(searchList: List<Noticia>){
        listaNoticias = searchList as ArrayList<Noticia>
        notifyDataSetChanged()

    }

    class NoticiaViewHolder (itemView:View):RecyclerView.ViewHolder(itemView){
        var recImagen: ImageView
        var recTitulo: TextView
        var recDesc: TextView
        var recCat: TextView
        var recCard: CardView

        init {
            recImagen = itemView.findViewById(R.id.recImagen)
            recTitulo = itemView.findViewById(R.id.recTitulo)
            recDesc = itemView.findViewById(R.id.recDesc)
            recCat = itemView.findViewById(R.id.recCat)
            recCard = itemView.findViewById(R.id.recCard)
        }
    }


}