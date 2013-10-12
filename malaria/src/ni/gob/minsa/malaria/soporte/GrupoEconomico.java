package ni.gob.minsa.malaria.soporte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class GrupoEconomico implements Comparable,Serializable {

	private static final long serialVersionUID = 3345030882166879744L;

	public static final GrupoEconomico PRIVADO= new GrupoEconomico("Privado","2");
	public static final GrupoEconomico PUBLICO= new GrupoEconomico("Publico","1");
	public static final GrupoEconomico SEGURIDAD_SOCIAL=new GrupoEconomico("Seguridad Social","3");
	public static final GrupoEconomico TODOS=new GrupoEconomico("Todos los Grupos Económicos","0");
	
	public static final List<GrupoEconomico> VALORES;
	public static final List<GrupoEconomico> VALORES_CON_TODOS;
	public static final Map<String, GrupoEconomico> VALORES_MAPA;

	static {
		
		Map<String, GrupoEconomico> map = new LinkedHashMap<String, GrupoEconomico>(4);
		map.put(PRIVADO.toString(), PRIVADO);
		map.put(PUBLICO.toString(), PUBLICO);
		map.put(SEGURIDAD_SOCIAL.toString(), SEGURIDAD_SOCIAL);
		
		List<GrupoEconomico> mapValues=new ArrayList<GrupoEconomico>(map.values());
		Collections.sort(mapValues);

		VALORES = Collections.unmodifiableList(mapValues);
		VALORES_MAPA = Collections.unmodifiableMap(map);
		
		map.put(TODOS.toString(), TODOS);
		mapValues=new ArrayList<GrupoEconomico>(map.values());
		Collections.sort(mapValues);
		VALORES_CON_TODOS=Collections.unmodifiableList(mapValues);

	}

	private String nombre;
	private String codigo;

	/**
	 * Instancia el objeto GrupoEconomico con la pareja de valores Nombre/Codigo
	 *  
	 * @param pNombre Cadena de caracteres con el nombre del Grupo Economico
	 * @param pCodigo Cadena de caracteres con el código del Grupo Economico
	 */
	private GrupoEconomico(String pNombre, String pCodigo) {
		this.nombre = pNombre;
		this.codigo = pCodigo;
	}
	
	/**
	 * Obtiene el código del grupo económico
	 * 
	 * @return Cadena de caracteres con el código del grupo económico
	 */
	public String getCodigo() {
		return this.codigo;
	}
	/**
	 * Establece el código asociado al grupo económico
	 * 
	 * @param codigo Cadena de caracteres con el código del grupo economico
	 */
	public void setCodigo(String codigo) {
		this.codigo=codigo;
	}
	/**
	 * Obtiene el nombre del objeto GrupoEconomico
	 * 
	 * @return Cadena de caracteres con el nombre del grupo economico
	 */
	public String getNombre() {
		return this.nombre;
	}
	/**
	 * Establece el nombre del objeto GrupoEconomico
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
	 * Compara un objeto GrupoEconomico instanciado con otro
	 * objeto GrupoEconomico.  El resultado de comparación se basará
	 * en el nombre del GrupoEconomico
	 * 
	 * @return -1 si el objeto está antes, 0 si es igual, 1 si está después
	 */
	public int compareTo(Object pObjeto) {
		if (!(pObjeto instanceof  GrupoEconomico)) {
			throw new IllegalArgumentException(pObjeto.getClass().getName());
		}
		return ((GrupoEconomico) pObjeto).getNombre().compareTo(this.nombre);
	}
	/**
	 * Retorna verdadero para otro objeto GrupoEconomico con la misma pareja
	 * de valores Nombre/Codigo
	 * 
	 */
    public boolean equals(Object other) {
        return other instanceof GrupoEconomico && (nombre != null) ? nombre.equals(((GrupoEconomico) other).nombre) : (other == this);
    }
	/**
	 * Retorna el mismo hashcode para cada objeto GrupoEconomico con la mismo nombre
	 * 
	 */
    public int hashCode() {
        return nombre != null ? this.getClass().hashCode() + nombre.hashCode() : super.hashCode();
    }
    /**
     * Encuentra un objeto GrupoEconomico a partir del nombre del sexo
     * 
     * @param nombre Cadena de texto
     * @return Objeto GrupoEconomico
     */
	public GrupoEconomico find(String nombre) {
        return GrupoEconomico.VALORES_MAPA.get(nombre);
    }
	/**
	 * Encuentra un objeto Grupo Economico a partir de su código
	 * 
	 * @param pCodigo
	 * @return Objeto GrupoEconomico
	 */
	public static GrupoEconomico encontrarPorCodigo(String pCodigo) {
		
		for (GrupoEconomico pGrupoEconomico : GrupoEconomico.VALORES) {
			if (pGrupoEconomico.getCodigo().equals(pCodigo)) {
				return pGrupoEconomico;
			}
		}
		return null;
	}
}
