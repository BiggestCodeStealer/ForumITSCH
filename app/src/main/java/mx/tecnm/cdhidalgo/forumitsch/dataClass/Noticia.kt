package mx.tecnm.cdhidalgo.forumitsch.dataClass

class Noticia {
    var titulo: String? = null
    var descripcion: String? = null
    var categoria: String? = null
    var imagen: String? = null
    var key: String? = null

    constructor(titulo: String?, descripcion: String?, categoria: String?, imagen: String?, key:String?){
        this.titulo = titulo
        this.descripcion = descripcion
        this.categoria = categoria
        this.imagen = imagen
        this.key = key
    }
    constructor(){

    }
}
