// -----------------------------------------------
// EntidadAdtvaService.java
// -----------------------------------------------
package ni.gob.minsa.malaria.servicios.estructura;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;


/**
 * Esta clase define el interface para EntidadAdtvaService.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.  Este interface también proporciona las operaciones de
 * mantenimiento para los datos de la tabla ENTIDADES_ADTVAS del
 * esquema GENERAL.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 04/11/2010
 * @since jdk1.6.0_21
 */
public interface EntidadAdtvaService {
	/**
	 * Retorna un listado de objetos {@link EntidadAdtva} que se
	 * encuentran activos
	 * 
	 * @return Lista de objetos EntidadAdtva
	 */
	public List<EntidadAdtva> EntidadesAdtvasActivas();
	
	/**
	 * Busca un objeto {@link EntidadAdtva} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pEntidadAdtvaId Entero largo con el identificador de la Entidad Administrativa
	 * @return Objeto InfoResultado con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pEntidadAdtvaId);

}
