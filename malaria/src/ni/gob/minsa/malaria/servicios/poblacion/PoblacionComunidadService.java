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
 * La implementaci�n de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 08/05/2012
 * @since jdk1.6.0_21
 */
public interface PoblacionComunidadService {

	/**
	 * Retorna la lista de objetos {@link PoblacionComunidad} asociados a un
	 * sector y a�o, independientemente si las comunidades, al momento de la petici�n
	 * se encuentran activas o pasivas.   
	 *
	 * @param pSector C�digo del Sector
	 * @param pA�o    A�o para el cual se solicita la lista de objetos
	 * @param pPlan   <code>true</code> indica que incluir� todas las comunidades,
	 *                <code>false</code> indica que �nicamente incluir� aquellas unidades de salud que posean datos de poblaci�n
	 * @return Lista de objetos {@link PoblacionComunidad}
	 */
	public List<PoblacionAnualComunidad> PoblacionComunidadesPorSector(String pSector, Integer pA�o, boolean pPlan);
	/**
	 * Retorna la lista de objetos {@link PoblacionAnualSector} asociados a la
	 * unidad de salud que atiende los sectores a los cuales pertenecen las comunidades.
	 * 
	 * @param pUnidad C�digo de la unidad de salud
	 * @param pA�o    A�o para el cual se solicita la lista de objetos
	 * @param pPlan   <code>true</code> indica que incluir� todos los sectores,
	 *                <code>false</code> indica que �nicamente incluir� aquellos sectores que posean datos de poblaci�n
	 * @return Lista de objetos {@link PoblacionComunidad}
	 */
	public List<PoblacionAnualSector> PoblacionSectoresPorUnidad(long pUnidad, Integer pA�o, boolean pPlan);

	/**
	 * Retorna la lista de objetos {@link UnidadSituacion} asociados a la entidad administrativa
	 * seleccionada para un a�o espec�fico.  Cada objeto {@link UnidadSituacion} suministra informaci�n
	 * sobre cada unidad de salud con sectores asociados, los que a su vez pueden o no poseer
	 * comunidades asociadas y tener registrada informaci�n poblacional para el a�o espec�fico.
	 * 
	 * @param pEntidadAdtva
	 * @param pA�o
	 * @return
	 */
	public List<UnidadSituacion> SituacionUnidadesPorEntidad(long pEntidadAdtva, Integer pA�o);
	/**
	 * Retorna la lista de objetos {@link ResumenPoblacion} asociados al
	 * resumen de poblaci�n por sectores de una unidad de salud para un a�o espec�fico
	 * 
	 * @param pUnidad C�digo de la unidad de salud
	 * @param pA�o    A�o para el cual se solicita la lista de objetos
	 * @param pPlan   <code>true</code> indica que incluir� todos los sectores,
	 *                <code>false</code> indica que �nicamente incluir� aquellos sectores que posean datos de poblaci�n
	 * @return Lista de objetos {@link ResumenPoblacion}
	 */
	public List<ResumenPoblacion> ResumenPoblacionSectoresPorUnidad(long pUnidad, Integer pA�o, boolean pPlan);

	/**
	 * Retorna la lista de objetos {@link ResumenPoblacion} asociados a la
	 * unidad de salud que atiende los sectores a los cuales pertenecen las comunidades.
	 * 
	 * @param pSector C�digo del sector
	 * @param pA�o    A�o para el cual se solicita la lista de objetos
	 * @param pPlan   <code>true</code> indica que incluir� todas las comunidades,
	 *                <code>false</code> indica que �nicamente incluir� aquellas comunidades que posean datos de poblaci�n
	 * @return Lista de objetos {@link ResumenPoblacion}
	 */
	public List<ResumenPoblacion> ResumenPoblacionComunidadesPorSector(String pSector, Integer pA�o, boolean pPlan);

	/**
	 * Busca un objeto {@link PoblacionComunidad} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * 
	 * @param pPoblacionComunidadId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Encontrar(long pPoblacionComunidadId);
	/**
	 * Busca un objeto {@link PoblacionComunidad} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * 
	 * @param pComunidad C�digo de la comunidad
	 * @param pA�o       A�o al cual se asocia la poblaci�n para la comunidad
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado EncontrarPorCodigo(String pComunidad,Integer pA�o);
	/**
	 * Guarda un objeto {@link PoblacionComunidad} existente en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operaci�n UPDATE en la base de datos
	 * @param pPoblacionComunidad Objeto {@link PoblacionComunidad} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Guardar(PoblacionComunidad pPoblacionComunidad);
	/**
	 * Agrega un objeto {@link PoblacionComunidad} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operaci�n INSERT en la base de datos
	 *  
	 * @param pPoblacionComunidad Objeto {@link PoblacionComunidad} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Agregar(PoblacionComunidad pPoblacionComunidad);
	/**
	 * Elimina el objeto {@link PoblacionComunidad} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operaci�n DELETE en la base de datos
	 * 
	 * @param pPoblacionComunidadId Entero largo con el identificador de los datos 
	 * 								de la poblaci�n de la comunidad en un a�o espec�fico
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Eliminar(long pPoblacionComunidadId);
	
	/**
	 * Elimina los objetos {@link PoblacionComunidad} de la base de datos
	 * que pertenecen a un sector determinado y a un a�o espec�fico
	 * 
	 * @param pSector C�digo del sector
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado EliminarPorSector(String pSector,Integer pA�o);

}
