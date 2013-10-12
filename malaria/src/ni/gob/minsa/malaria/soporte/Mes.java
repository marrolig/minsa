// -----------------------------------------------
// Mes.java
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
 * cada mes del año en variables estáticas
 *
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 10/11/2010
 * @since jdk1.6.0_21
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Mes implements Comparable,Serializable {

	private static final long serialVersionUID = -2275467155028232200L;

	public static final Mes MES_NULO=new Mes(" ","00");
	public static final Mes ENERO = new Mes("Enero", "01");
	public static final Mes FEBRERO= new Mes("Febrero", "02");
	public static final Mes MARZO=new Mes("Marzo","03");
	public static final Mes ABRIL=new Mes("Abril","04");
	public static final Mes MAYO=new Mes("Mayo","05");
	public static final Mes JUNIO=new Mes("Junio","06");
	public static final Mes JULIO=new Mes("Julio","07");
	public static final Mes AGOSTO=new Mes("Agosto","08");
	public static final Mes SEPTIEMBRE=new Mes("Septiembre","09");
	public static final Mes OCTUBRE=new Mes("Octubre","10");
	public static final Mes NOVIEMBRE=new Mes("Noviembre","11");
	public static final Mes DICIEMBRE=new Mes("Diciembre","12");
	
	public static final List<Mes> VALORES;
	public static final List<Mes> VALORES_CON_NULO;
	public static final Map<String, Mes> VALORES_MAPA;

	static {
		
		Map<String, Mes> map = new LinkedHashMap<String, Mes>(12);
		map.put(ENERO.toString(), ENERO);
		map.put(FEBRERO.toString(), FEBRERO);
		map.put(MARZO.toString(), MARZO);
		map.put(ABRIL.toString(), ABRIL);
		map.put(MAYO.toString(), MAYO);
		map.put(JUNIO.toString(), JUNIO);
		map.put(JULIO.toString(), JULIO);
		map.put(AGOSTO.toString(), AGOSTO);
		map.put(SEPTIEMBRE.toString(), SEPTIEMBRE);
		map.put(OCTUBRE.toString(), OCTUBRE);
		map.put(NOVIEMBRE.toString(), NOVIEMBRE);
		map.put(DICIEMBRE.toString(), DICIEMBRE);
		
		List<Mes> mapValues=new ArrayList<Mes>(map.values());
		Collections.sort(mapValues);
		
		VALORES = Collections.unmodifiableList(mapValues);
		VALORES_MAPA = Collections.unmodifiableMap(map);
		
		map.put(MES_NULO.toString(), MES_NULO);
		
		List<Mes> mapValues2=new ArrayList<Mes>(map.values());
		Collections.sort(mapValues2);
		
		VALORES_CON_NULO= Collections.unmodifiableList(mapValues2);
	}

	private String nombre;
	private String codigo;
	/**
	 * Instancia el objeto Mes con la pareja de valores Nombre/Codigo
	 *  
	 * @param pNombre Cadena de caracteres con el nombre del mes
	 * @param pCodigo Cadena de caracteres con el código del mes
	 */
	private Mes(String pNombre, String pCodigo) {
		this.nombre = pNombre;
		this.codigo = pCodigo;
	}
	/**
	 * Obtiene el código del mes
	 * 
	 * @return Cadena de caracteres con el código del mes
	 */
	public String getCodigo() {
		return this.codigo;
	}
	/**
	 * Establece el código asociado al mes, con el formato 99
	 * 
	 * @param codigo Cadena de caracteres con el código del mes
	 */
	public void setCodigo(String codigo) {
		this.codigo=codigo;
	}
	/**
	 * Obtiene el nombre del objeto Mes
	 * 
	 * @return Cadena de caracteres con el nombre del mes
	 */
	public String getNombre() {
		return this.nombre;
	}
	/**
	 * Establece el nombre del objeto Sexo
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
	 * Compara un objeto Mes instanciado con otro
	 * objeto Mes.  El resultado de comparación se basará
	 * en la representación entera del código del mes
	 * 
	 * @return -1 si el objeto está antes, 0 si es igual, 1 si está después
	 */
	public int compareTo(Object pObjeto) {
		if (!(pObjeto instanceof  Mes)) {
			throw new IllegalArgumentException(pObjeto.getClass().getName());
		}
		return Integer.valueOf(this.codigo)-Integer.valueOf(((Mes) pObjeto).getCodigo());
	}
	/**
	 * Retorna verdadero para otro objeto Mes con la misma pareja
	 * de valores Nombre/Codigo
	 * 
	 */
	public boolean equals(Object other) {
        return other instanceof Mes && (nombre != null) ? nombre.equals(((Mes) other).nombre) : (other == this);
    }
	/**
	 * Retorna el mismo hashcode para cada objeto Mes con la mismo nombre
	 * 
	 */
    public int hashCode() {
        return nombre != null ? this.getClass().hashCode() + nombre.hashCode() : super.hashCode();
    }
    /**
     * Encuentra un objeto Mes a partir del nombre del mes
     * 
     * @param nombre Cadena de texto
     * @return Objeto Mes
     */
	public static Mes encuentraPorNombre(String nombre) {
        return Mes.VALORES_MAPA.get(nombre);
    }
	/**
	 * Retorna el objeto {@link Mes} según su código según el formato 99
	 * 
	 * @param pCodigo Cadena de caracteres que representan el código del mes
	 * @return Objeto {@link Mes}
	 */
	public static Mes encuentraPorCodigo(String pCodigo) {
		
		for (Mes pMes : Mes.VALORES) {
			if (pMes.getCodigo().equals(pCodigo)) {
				return pMes;
			}
		}
		return null;
	}
	
}
