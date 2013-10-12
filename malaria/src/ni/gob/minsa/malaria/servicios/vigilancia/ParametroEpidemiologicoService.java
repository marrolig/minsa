// -----------------------------------------------
// ParametroEpidemiologicoService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.vigilancia;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.vigilancia.ParametroEpidemiologico;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en ParametroEpidemiologicoDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 24/05/2012
 * @since jdk1.6.0_21
 */
public interface ParametroEpidemiologicoService {

	/**
	 * Busca un objeto {@link ParametroEpidemiologico} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pParametroEpidemiologicoId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pParametroEpidemiologicoId);
	
	/**
	 * Guarda un objeto {@link ParametroEpidemiologico} existente en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operación UPDATE en la base de datos
	 * @param pParametroEpidemiologico Objeto {@link ParametroEpidemiologico} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Guardar(ParametroEpidemiologico pParametroEpidemiologico);
	/**
	 * Agrega un objeto {@link ParametroEpidemiologico} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pParametroEpidemiologico Objeto {@link ParametroEpidemiologico} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(ParametroEpidemiologico pParametroEpidemiologico);
	/**
	 * Elimina el objeto {@link ParametroEpidemiologico} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param pParametroEpidemiologicoId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pParametroEpidemiologicoId);
	
	/**
	 * Retorna una lista de objetos {@link ParametroEpidemiologico}.
	 * 
	 * @param pPasivo        <code>true</code> incluye únicamente los pasivos;
	 * 						 <code>false</code> incluye únicamente los activos;
	 * 						 <code>null</code> lista todos
	 * @return Lista de objetos {@link ParametroEpidemiologico}
	 */
	public List<ParametroEpidemiologico> Listar(Boolean pPasivo); 

	/**
	 * Retorna una lista de objetos {@link ParametroEpidemiologico} activos, donde se
	 * incluye el Parametro Epidemiologico con el código <code>pParametroEpidemiologico</code>,
	 * el cual puede o no estar activo.
	 * 
	 * @param pParametroEpidemiologico  Código del Parametro Epidemiologico que será incluido en la lista
	 * @return Lista de objetos {@link ParametroEpidemiologico}
	 */
	public List<ParametroEpidemiologico> Listar(String pParametroEpidemiologico);

	/**
	 * Busca un objeto {@link ParametroEpidemiologico} en la base de datos mediante
	 * el código asignado a dicho Parámetro Epidemiológico.  Retorna <code>null</code>
	 * si no encuentra ningún parámetro epidemiológico
	 * 
	 * @param pCodigo Código asignado al parámetro epidemiológico
	 * @return Objeto {@link ParametroEpidemiologico} o <code>null</code> si no encuentra
	 */
	public ParametroEpidemiologico EncontrarPorCodigo(String pCodigo);

}
