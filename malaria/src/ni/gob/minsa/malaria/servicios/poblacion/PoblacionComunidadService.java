// -----------------------------------------------
// SectorService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.poblacion.PoblacionComunidad;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.PoblacionAnualComunidad;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.PoblacionAnualSector;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.ResumenPoblacion;
import ni.gob.minsa.malaria.modelo.poblacion.noEntidad.UnidadSituacion;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en PoblacionComunidadDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 08/05/2012
 * @since jdk1.6.0_21
 */
public interface PoblacionComunidadService {

	/**
	 * Retorna la lista de objetos {@link PoblacionComunidad} asociados a un
	 * sector y año, independientemente si las comunidades, al momento de la petición
	 * se encuentran activas o pasivas.   
	 *
	 * @param pSector Código del Sector
	 * @param pAño    Año para el cual se solicita la lista de objetos
	 * @param pPlan   <code>true</code> indica que incluirá todas las comunidades,
	 *                <code>false</code> indica que únicamente incluirá aquellas unidades de salud que posean datos de población
	 * @return Lista de objetos {@link PoblacionComunidad}
	 */
	public List<PoblacionAnualComunidad> PoblacionComunidadesPorSector(String pSector, Integer pAño, boolean pPlan);
	/**
	 * Retorna la lista de objetos {@link PoblacionAnualSector} asociados a la
	 * unidad de salud que atiende los sectores a los cuales pertenecen las comunidades.
	 * 
	 * @param pUnidad Código de la unidad de salud
	 * @param pAño    Año para el cual se solicita la lista de objetos
	 * @param pPlan   <code>true</code> indica que incluirá todos los sectores,
	 *                <code>false</code> indica que únicamente incluirá aquellos sectores que posean datos de población
	 * @return Lista de objetos {@link PoblacionComunidad}
	 */
	public List<PoblacionAnualSector> PoblacionSectoresPorUnidad(long pUnidad, Integer pAño, boolean pPlan);

	/**
	 * Retorna la lista de objetos {@link UnidadSituacion} asociados a la entidad administrativa
	 * seleccionada para un año específico.  Cada objeto {@link UnidadSituacion} suministra información
	 * sobre cada unidad de salud con sectores asociados, los que a su vez pueden o no poseer
	 * comunidades asociadas y tener registrada información poblacional para el año específico.
	 * 
	 * @param pEntidadAdtva
	 * @param pAño
	 * @return
	 */
	public List<UnidadSituacion> SituacionUnidadesPorEntidad(long pEntidadAdtva, Integer pAño);
	/**
	 * Retorna la lista de objetos {@link ResumenPoblacion} asociados al
	 * resumen de población por sectores de una unidad de salud para un año específico
	 * 
	 * @param pUnidad Código de la unidad de salud
	 * @param pAño    Año para el cual se solicita la lista de objetos
	 * @param pPlan   <code>true</code> indica que incluirá todos los sectores,
	 *                <code>false</code> indica que únicamente incluirá aquellos sectores que posean datos de población
	 * @return Lista de objetos {@link ResumenPoblacion}
	 */
	public List<ResumenPoblacion> ResumenPoblacionSectoresPorUnidad(long pUnidad, Integer pAño, boolean pPlan);

	/**
	 * Retorna la lista de objetos {@link ResumenPoblacion} asociados a la
	 * unidad de salud que atiende los sectores a los cuales pertenecen las comunidades.
	 * 
	 * @param pSector Código del sector
	 * @param pAño    Año para el cual se solicita la lista de objetos
	 * @param pPlan   <code>true</code> indica que incluirá todas las comunidades,
	 *                <code>false</code> indica que únicamente incluirá aquellas comunidades que posean datos de población
	 * @return Lista de objetos {@link ResumenPoblacion}
	 */
	public List<ResumenPoblacion> ResumenPoblacionComunidadesPorSector(String pSector, Integer pAño, boolean pPlan);

	/**
	 * Busca un objeto {@link PoblacionComunidad} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pPoblacionComunidadId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pPoblacionComunidadId);
	/**
	 * Busca un objeto {@link PoblacionComunidad} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pComunidad Código de la comunidad
	 * @param pAño       Año al cual se asocia la población para la comunidad
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado EncontrarPorCodigo(String pComunidad,Integer pAño);
	/**
	 * Guarda un objeto {@link PoblacionComunidad} existente en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operación UPDATE en la base de datos
	 * @param pPoblacionComunidad Objeto {@link PoblacionComunidad} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Guardar(PoblacionComunidad pPoblacionComunidad);
	/**
	 * Agrega un objeto {@link PoblacionComunidad} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pPoblacionComunidad Objeto {@link PoblacionComunidad} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(PoblacionComunidad pPoblacionComunidad);
	/**
	 * Elimina el objeto {@link PoblacionComunidad} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param pPoblacionComunidadId Entero largo con el identificador de los datos 
	 * 								de la población de la comunidad en un año específico
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pPoblacionComunidadId);
	
	/**
	 * Elimina los objetos {@link PoblacionComunidad} de la base de datos
	 * que pertenecen a un sector determinado y a un año específico
	 * 
	 * @param pSector Código del sector
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado EliminarPorSector(String pSector,Integer pAño);

}
