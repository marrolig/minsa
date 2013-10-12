// -----------------------------------------------
// UnidadAccesoService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.estructura;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.estructura.UnidadAcceso;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en UnidadAccesoDA.
 * La implementaci�n de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 22/09/2012
 * @since jdk1.6.0_21
 */
public interface UnidadAccesoService {

	/**
	 * Busca un objeto {@link UnidadAcceso} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * 
	 * @param pUnidadAccesoId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Encontrar(long pUnidadAccesoId);
	
	/**
	 * Guarda un objeto {@link UnidadAcceso} existente en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operaci�n UPDATE en la base de datos
	 * @param pUnidadAcceso Objeto {@link UnidadAcceso} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Guardar(UnidadAcceso pUnidadAcceso);
	/**
	 * Agrega un objeto {@link UnidadAcceso} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operaci�n INSERT en la base de datos
	 *  
	 * @param pUnidadAcceso Objeto {@link UnidadAcceso} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Agregar(UnidadAcceso pUnidadAcceso);
	/**
	 * Elimina el objeto {@link UnidadAcceso} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operaci�n DELETE en la base de datos
	 * 
	 * @param pUnidadAccesoId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Eliminar(long pUnidadAccesoId);
	
	/**
	 * Busca un objeto {@link UnidadAcceso} en la base de datos mediante
	 * el c�digo asignado a dicha unidad de salud.  Retorna <code>null</code>
	 * si no encuentra ning�n acceso asociado a la unidad.
	 * 
	 * @param pCodigo C�digo asignado a la unidad de salud
	 * @return Objeto {@link UnidadAcceso} o <code>null</code> si no encuentra
	 */
	public UnidadAcceso EncontrarPorUnidad(long pCodigo);

}
