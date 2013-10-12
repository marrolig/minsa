// -----------------------------------------------
// UsuarioEntidadService.java
// -----------------------------------------------
package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.seguridad.UsuarioEntidad;


/**
 * Esta clase define el interface para UsuarioEntidadService.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.  Este interface también proporciona las operaciones de
 * mantenimiento para los datos de la tabla USUARIOS_ENTIDADES.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 19/03/2012
 * @since jdk1.6.0_21
 */
public interface UsuarioEntidadService {
	/**
	 * Retorna una lista de objetos {@link UsuarioEntidad} asociados
	 * a un usuario, con lo cual se obtienen todas las entidades
	 * administrativas a los cuales se ha autorizado a dicho usuario
	 * 
	 * @param pUsuarioId Identificador del usuario
	 * @return Lista de Objetos {@link UsuarioEntidad}
	 */
	public List<UsuarioEntidad> EntidadesPorUsuario(long pUsuarioId);
	/**
	 * Busca la asociación entre un usuario y una entidad administrativa,
	 * mediante su identificador y retorna el objeto encontrado mediante
	 * el objeto InfoResultado.
	 * 
	 * @param pUsuarioEntidadId Identificador de la asociación entre el usuario y la entidad
	 * @return Objeto InfoResultado con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pUsuarioEntidadId);

}
