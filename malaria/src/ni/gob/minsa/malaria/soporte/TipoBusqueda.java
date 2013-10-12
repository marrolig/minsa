// -----------------------------------------------
// TipoBusqueda.java
// -----------------------------------------------
package ni.gob.minsa.malaria.soporte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* Clase que implementa la lista de valores y valores de
* la variable tipo de búsqueda en variables estáticas
*
* <p>
* @author Marlon Arróliga
* @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
* @version 1.0, &nbsp; 20/11/2012
* @since jdk1.6.0_21
*/
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TipoBusqueda implements Comparable,Serializable {

	private static final long serialVersionUID = -1216591206712818497L;

	public static final TipoBusqueda ACTIVA = new TipoBusqueda("Activa", "1");
	public static final TipoBusqueda PASIVA= new TipoBusqueda("Pasiva", "0");
		
	public static final List<TipoBusqueda> VALORES;
	public static final List<TipoBusqueda> VALORES_EXT;
	public static final Map<String, TipoBusqueda> VALORES_MAPA;
		
	static {
			
			Map<String, TipoBusqueda> map = new LinkedHashMap<String, TipoBusqueda>(2);
			map.put(ACTIVA.toString(), ACTIVA);
			map.put(PASIVA.toString(), PASIVA);
			
			Map<String, TipoBusqueda> mapExt = new LinkedHashMap<String, TipoBusqueda>(2);
			mapExt.put(ACTIVA.toString(), ACTIVA);
			mapExt.put(PASIVA.toString(), PASIVA);
			
			List<TipoBusqueda> mapValues=new ArrayList<TipoBusqueda>(map.values());
			Collections.sort(mapValues);

			VALORES = Collections.unmodifiableList(mapValues);
			VALORES_MAPA = Collections.unmodifiableMap(map);

			List<TipoBusqueda> mapExtValues=new ArrayList<TipoBusqueda>(mapExt.values());
			Collections.sort(mapExtValues);
			
			VALORES_EXT=Collections.unmodifiableList(mapExtValues);
	}

	private String nombre;
	private String codigo;
	/**
	* Instancia el objeto TipoBusqueda con la pareja de valores Nombre/Codigo
	*  
	* @param pNombre Cadena de caracteres con el nombre del tipo de búsqueda
	* @param pCodigo Cadena de caracteres con el código del tipo de búsqueda
	*/
	private TipoBusqueda(String pNombre, String pCodigo) {
			this.nombre = pNombre;
			this.codigo = pCodigo;
	}
	/**
	* Obtiene el código del tipo de búsqueda
	*/
	public String getCodigo() {
			return this.codigo;
	}
	/**
	* Establece el código asociado al tipo de búsqueda
	*/
	public void setCodigo(String codigo) {
			this.codigo=codigo;
	}
	/**
	* Obtiene el nombre del objeto TipoBusqueda
	*/
	public String getNombre() {
			return this.nombre;
	}
	/**
	* Establece el nombre del objeto TipoBusqueda
	*/
	public void setNombre(String nombre) {
			this.nombre=nombre;
	}
	/**
	* (non-Javadoc)
	* @see java.lang.Object#toString()
	*/
	public String toString() {
			return this.nombre;
	}
	/**
	* Compara un objeto TipoBusqueda instanciado con otro
	* objeto TipoBusqueda.  El resultado de comparación se basará
	* en el nombre del tipo de búsqueda
	* 
	* @return -1 si el objeto está antes, 0 si es igual, 1 si está después
	*/
	public int compareTo(Object pObjeto) {
			if (!(pObjeto instanceof  TipoBusqueda)) {
				throw new IllegalArgumentException(pObjeto.getClass().getName());
			}
			return ((TipoBusqueda) pObjeto).getNombre().compareTo(this.nombre);
	}
	/**
	* Retorna verdadero para otro objeto TipoBusqueda con la misma nombre
	*/
	public boolean equals(Object other) {
	        return other instanceof TipoBusqueda && (nombre != null) ? nombre.equals(((TipoBusqueda) other).nombre) : (other == this);
	}
	/**
	* Retorna el mismo hashcode para cada objeto TipoBusqueda con la mismo nombre
	*/
	public int hashCode() {
	        return nombre != null ? this.getClass().hashCode() + nombre.hashCode() : super.hashCode();
	}
	/**
	* Encuentra un objeto TipoBusqueda a partir del nombre del tipo de búsqueda
	* 
	* @param nombre Cadena de texto
	* @return Objeto TipoBusqueda
	*/
	public TipoBusqueda find(String nombre) {
	        return TipoBusqueda.VALORES_MAPA.get(nombre);
	}
	/**
	* Encuentra un objeto TipoBusqueda a partir de su código
	* 
	* @param pCodigo
	* @return Objeto TipoBusqueda
	*/
	public static TipoBusqueda encontrarPorCodigo(String pCodigo) {
			
			for (TipoBusqueda pTipo : TipoBusqueda.VALORES) {
				if (pTipo.getCodigo().equals(pCodigo)) {
					return pTipo;
				}
			}
			return null;
	}
}
