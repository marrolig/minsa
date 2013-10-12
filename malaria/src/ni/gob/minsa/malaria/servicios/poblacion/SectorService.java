// -----------------------------------------------
// 
// -----------------------------------------------
package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.poblacion.Sector;


/**
 * Esta clase define el interface para SectorService.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.  Este interface también proporciona las operaciones de
 * mantenimiento para los datos de la tabla SECTORES del
 * esquema GENERAL
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 24/11/2010
 * @since jdk1.6.0_21
 */
public interface SectorService {
	/**
	 * Retorna la lista de objetos {@link Sector} asociados a una
	 * unidad de salud, independientemente si se encuentran
	 * activos o pasivos
	 * 
	 * @return Lista de objetos Sector
	 */
	public List<Sector> SectoresPorUnidad(long pUnidadId);
	/**
	 * Retorna la lista de objetos {@link Sector} asociados a una
	 * unidad de salud y que se encuentran activos.
	 * 
	 * @return Lista de objetos Sector
	 */
	public List<Sector> SectoresPorUnidadActivos(long pUnidadId);
	/**
	 * Retorna la lista de objetos {@link Sector} asociados a un
	 * municipio, independientemente si se encuentran
	 * activos o pasivos
	 * 
	 * @return Lista de objetos Sector
	 */
	public List<Sector> SectoresPorMunicipio(long pMunicipioId);
	/**
	 * Busca un sector mediante su identificador único de fila y
	 * el resultado es retornado mediante un objeto {@link InfoResultado}
	 *  
	 * @param pSecotrId Identificador del Sector
	 * @return Objeto {@link InfoResultado}
	 */
	public InfoResultado Encontrar(long pSectorId);

}
