// -----------------------------------------------
// FactorRiesgoService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.vigilancia;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.vigilancia.FactorRiesgo;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en FactorRiesgoDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 24/05/2012
 * @since jdk1.6.0_21
 */
public interface FactorRiesgoService {

	/**
	 * Busca un objeto {@link FactorRiesgo} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pFactorRiesgoId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pFactorRiesgoId);
	
	/**
	 * Guarda un objeto {@link FactorRiesgo} existente en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operación UPDATE en la base de datos
	 * @param pFactorRiesgo Objeto {@link FactorRiesgo} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Guardar(FactorRiesgo pFactorRiesgo);
	/**
	 * Agrega un objeto {@link FactorRiesgo} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pFactorRiesgo Objeto {@link FactorRiesgo} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(FactorRiesgo pFactorRiesgo);
	/**
	 * Elimina el objeto {@link FactorRiesgo} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param pFactorRiesgoId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pFactorRiesgoId);
	
	/**
	 * Retorna una lista de objetos {@link FactorRiesgo}.
	 * 
	 * @param pPasivo        <code>true</code> incluye únicamente los pasivos;
	 * 						 <code>false</code> incluye únicamente los activos;
	 * 						 <code>null</code> lista todos
	 * @return Lista de objetos {@link FactorRiesgo}
	 */
	public List<FactorRiesgo> Listar(Boolean pPasivo); 

	/**
	 * Retorna una lista de objetos {@link FactorRiesgo} activos, donde se
	 * incluye el factor de riesgo con el código <code>pFactorRiesgo</code>,
	 * el cual puede o no estar activo.
	 * 
	 * @param pFactorRiesgo  Código del factor de riesgo que será incluido en la lista
	 * @return Lista de objetos {@link FactorRiesgo}
	 */
	public List<FactorRiesgo> Listar(String pFactorRiesgo);
	
	/**
	 * Busca un objeto {@link FactorRiesgo} en la base de datos mediante
	 * el código asignado a dicho Factor de Riesgo.  Retorna <code>null</code>
	 * si no encuentra ningún factor de riesgo
	 * 
	 * @param pCodigo Código asignado al factor de riesgo
	 * @return Objeto {@link FactorRiesgo} o <code>null</code> si no encuentra
	 */
	public FactorRiesgo EncontrarPorCodigo(String pCodigo);

}
