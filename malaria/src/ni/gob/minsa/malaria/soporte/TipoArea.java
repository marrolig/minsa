// -----------------------------------------------
// TipoArea.java
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
* la variable tipo de área en variables estáticas
*
* <p>
* @author Marlon Arróliga
* @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
* @version 1.0, &nbsp; 07/09/2011
* @since jdk1.6.0_21
*/
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TipoArea implements Comparable,Serializable {

	
	private static final long serialVersionUID = 8988646184881899158L;
	public static final TipoArea URBANO = new TipoArea("Urbano", "U");
	public static final TipoArea RURAL= new TipoArea("Rural", "R");
	public static final TipoArea INDEFINIDO = new TipoArea("", "0");
		
	public static final List<TipoArea> VALORES;
	public static final List<TipoArea> VALORES_EXT;
	public static final Map<String, TipoArea> VALORES_MAPA;
		
	static {
			
			Map<String, TipoArea> map = new LinkedHashMap<String, TipoArea>(3);
			map.put(URBANO.toString(), URBANO);
			map.put(RURAL.toString(), RURAL);
			
			Map<String, TipoArea> mapExt = new LinkedHashMap<String, TipoArea>(3);
			mapExt.put(INDEFINIDO.toString(), INDEFINIDO);
			mapExt.put(URBANO.toString(), URBANO);
			mapExt.put(RURAL.toString(), RURAL);
			
			List<TipoArea> mapValues=new ArrayList<TipoArea>(map.values());
			Collections.sort(mapValues);

			VALORES = Collections.unmodifiableList(mapValues);
			VALORES_MAPA = Collections.unmodifiableMap(map);

			List<TipoArea> mapExtValues=new ArrayList<TipoArea>(mapExt.values());
			Collections.sort(mapExtValues);
			
			VALORES_EXT=Collections.unmodifiableList(mapExtValues);
	}

	private String nombre;
	private String codigo;
	/**
	* Instancia el objeto TipoArea con la pareja de valores Nombre/Codigo
	*  
	* @param pNombre Cadena de caracteres con el nombre del tipo de área
	* @param pCodigo Cadena de caracteres con el código del tipo de área
	*/
	private TipoArea(String pNombre, String pCodigo) {
			this.nombre = pNombre;
			this.codigo = pCodigo;
	}
	/**
	* Obtiene el código del tipo de área
	* 
	* @return Cadena de caracteres con el código del tipo de área
	*/
	public String getCodigo() {
			return this.codigo;
	}
	/**
	* Establece el código asociado al tipo de área
	* 
	* @param codigo Cadena de caracteres con el código del tipo de área
	*/
	public void setCodigo(String codigo) {
			this.codigo=codigo;
	}
	/**
	* Obtiene el nombre del objeto TipoArea
	* 
	* @return Cadena de caracteres con el nombre del tipo de área
	*/
	public String getNombre() {
			return this.nombre;
	}
	/**
	* Establece el nombre del objeto TipoArea
	* 
	* @param nombre Cadena de caracteres con el nombre
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
	* Compara un objeto TipoArea instanciado con otro
	* objeto TipoArea.  El resultado de comparación se basará
	* en el nombre del tipo de área
	* 
	* @return -1 si el objeto está antes, 0 si es igual, 1 si está después
	*/
	public int compareTo(Object pObjeto) {
			if (!(pObjeto instanceof  TipoArea)) {
				throw new IllegalArgumentException(pObjeto.getClass().getName());
			}
			return ((TipoArea) pObjeto).getNombre().compareTo(this.nombre);
	}
	/**
	* Retorna verdadero para otro objeto TipoArea con la misma pareja
	* de valores Nombre/Codigo
	* 
	*/
	public boolean equals(Object other) {
	        return other instanceof TipoArea && (nombre != null) ? nombre.equals(((TipoArea) other).nombre) : (other == this);
	}
	/**
	* Retorna el mismo hashcode para cada objeto TipoArea con la mismo nombre
	* 
	*/
	public int hashCode() {
	        return nombre != null ? this.getClass().hashCode() + nombre.hashCode() : super.hashCode();
	}
	/**
	* Encuentra un objeto TipoAra a partir del nombre del tipo de área
	* 
	* @param nombre Cadena de texto
	* @return Objeto TipoArea
	*/
	public TipoArea find(String nombre) {
	        return TipoArea.VALORES_MAPA.get(nombre);
	}
	/**
	* Encuentra un objeto TipoArea a partir de su código
	* 
	* @param pCodigo
	* @return Objeto TipoArea
	*/
	public static TipoArea encontrarPorCodigo(String pCodigo) {
			
			for (TipoArea pTipo : TipoArea.VALORES) {
				if (pTipo.getCodigo().equals(pCodigo)) {
					return pTipo;
				}
			}
			return null;
	}
}
