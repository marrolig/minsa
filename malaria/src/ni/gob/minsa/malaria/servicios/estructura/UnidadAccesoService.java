// -----------------------------------------------
// UnidadAccesoService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.estructura;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.estructura.UnidadAcceso;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en UnidadAccesoDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 22/09/2012
 * @since jdk1.6.0_21
 */
public interface UnidadAccesoService {

	/**
	 * Busca un objeto {@link UnidadAcceso} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pUnidadAccesoId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pUnidadAccesoId);
	
	/**
	 * Guarda un objeto {@link UnidadAcceso} existente en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operación UPDATE en la base de datos
	 * @param pUnidadAcceso Objeto {@link UnidadAcceso} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Guardar(UnidadAcceso pUnidadAcceso);
	/**
	 * Agrega un objeto {@link UnidadAcceso} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pUnidadAcceso Objeto {@link UnidadAcceso} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(UnidadAcceso pUnidadAcceso);
	/**
	 * Elimina el objeto {@link UnidadAcceso} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param pUnidadAccesoId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pUnidadAccesoId);
	
	/**
	 * Busca un objeto {@link UnidadAcceso} en la base de datos mediante
	 * el código asignado a dicha unidad de salud.  Retorna <code>null</code>
	 * si no encuentra ningún acceso asociado a la unidad.
	 * 
	 * @param pCodigo Código asignado a la unidad de salud
	 * @return Objeto {@link UnidadAcceso} o <code>null</code> si no encuentra
	 */
	public UnidadAcceso EncontrarPorUnidad(long pCodigo);

}
