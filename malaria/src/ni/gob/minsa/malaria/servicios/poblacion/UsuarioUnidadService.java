// -----------------------------------------------
// UsuarioUnidadService.java
// -----------------------------------------------
package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.seguridad.UsuarioUnidad;


/**
 * Esta clase define el interface para UsuarioUnidadService.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.  Este interface también proporciona las operaciones de
 * mantenimiento para los datos de la tabla USUARIOS_UNIDADES.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 19/03/2012
 * @since jdk1.6.0_21 
 */
public interface UsuarioUnidadService {
	/**
	 * Retorna una lista de objetos {@link UsuarioUnidad} que
	 * representa a todos las unidades activas a la cuales
	 * tiene autorización un usuario miembro del sistema seleccioando
	 * 
	 * @param pUsuarioId Identificador del usuario
	 * @return Lista de objetos {@link UsuarioUnidad}
	 */
	public List<UsuarioUnidad> UnidadesPorUsuario(long pUsuarioId);

	/**
	 * Retorna una lista de objetos {@link UsuarioUnidad} que
	 * representa a todos las unidades activas a la cuales
	 * tiene autorización un usuario miembro del sistema PAI, que
	 * dependen de una entidad administriva y que corresponden a un
	 * tipo de unidad específico
	 * 
	 * @param pUsuarioId Identificador del usuario
	 * @param pEntidadId Identificador de la Entidad Administrativa
	 * @param pTipoUnidadId Identificador del tipo de unidad
	 * 
	 * @return Lista de objetos {@link UsuarioUnidad}
	 */
	public List<UsuarioUnidad> UnidadesPorUsuario(long pUsuarioId,long pEntidadId, long pTipoUnidadId);

	/**
	 * Busca un objeto {@link UsuarioUnidad} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pUsuarioUnidadId Entero largo con el identificador del objeto {@link UsuarioUnidad}
	 * @return Objeto InfoResultado con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pUsuarioUnidadId);
	
	/**
	 * Busca un objeto {@link UsuarioUnidad} en la base de datos, si el objeto
	 * no es encontrado retorna <code>null</code>
	 * 
	 * @param pUsuarioId  Identificador del usuario
	 * @param pUnidadId   Identificador de la unidad
	 * @return
	 */
	public UsuarioUnidad Encontrar(long pUsuarioId, long pUnidadId);
	
}
