// -----------------------------------------------
// ComunidadService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.Sector;

/**
 * Esta clase define el interface para ComunidadService.
 * La implementaci�n de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.  Este interface tambi�n proporciona las operaciones de
 * mantenimiento para los datos de la tabla COMUNIDADES.
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 07/09/2011
 * @since jdk1.6.0_21
 */
public interface ComunidadService {
	
	/**
	 * Metodo que retorna una lista de objeto {@link Comunidad} que posean la
	 * literal de busqueda pNombre como parte del nombre de la comunidad
	 * <p>
	 * @param pCodMunicipio  	Identificador codigo del municipio
	 * @param pNombre			Literal de filtro de busqueda de la comunidad
	 * @param pRegistros		Cantidad de Registros a mostrar
	 * @return					Lista de objetos de Comunidad {@link Comunidad}
	 */
	public List<Comunidad> ComunidadesPorMunicipioNombre(String pCodMunicipio, String pNombre, Integer pRegistros);

	/**
	 * Retorna una lista de objetos {@link Comunidad} que poseen la
	 * literal de b�squeda pNombre como parte del nombre de la comunidad,
	 * independientemente si se encuentran activos o pasivos
	 * <p>
	 * Si se especifica un identificador de sector, el literal �nicamente
	 * lo buscar� en el sector especificado, caso contrario la busqueda se
	 * realizar� en todos los sectores del municipio
	 * <p>
	 * 
	 * @pMunicipioId	Identificador del Municipio
	 * @pSectorId 		Identificador del Sector
	 * @pNombre         Literal del nombre de la Comunidad
	 * @return			Lista de objetos Comunidad
	 */
	public List<Comunidad> ComunidadesPorNombre(long pMunicipioId, long pSectorId, String pNombre, int pPaginaActual, int pTotalPorPagina, int pNumRegistros);

	/**
	 * Retorna una lista de objetos {@link Comunidad} que poseen la
	 * literal de b�squeda pNombre como parte del nombre de la comunidad,
	 * que se encuentran activos y que estan bajo la cobertura de atenci�n
	 * de una unidad de salud, i.e. la comunidad se encuentra en una sector
	 * gestionado por dicha unidad de salud
	 *
	 * @pUnidadId	Identificador de la Unidad de Salud
	 * @pNombre     Literal del nombre de la Comunidad
	 * @return		Lista de objetos Comunidad
	 */
	public List<Comunidad> ComunidadesPorUnidadYNombre(long pUnidadId, String pNombre);

	/**
	 * Retorna la lista de objetos {@link Comunidad} activos, asociados a un
	 * sector que es gestionado por una unidad de salud especificada.  La literal
	 * establecida en <code>pNombre</code> es opcional.
	 * 
	 * @param pUnidadId       Identificador de la Unidad de Salud
	 * @param pNombre         Literal del nombre de la comunidad
	 * @param pTipoArea       U=Urbano, R=Rural, nulo=Urbano/Rural/No definidas
	 * @param pPaginaActual   N�mero de p�gina actual
	 * @param pTotalPorPagina N�mero de registros por p�gina
	 * @param pNumRegistros   Total de registros
	 * @return Lista de objetos Comunidad
	 */
	public List<Comunidad> ComunidadesPorUnidad(long pUnidadId, String pNombre, String pTipoArea, int pPaginaActual, int pTotalPorPagina, int pNumRegistros);

	/**
	 * Retorna la lista de objetos {@link Comunidad} asociados a un
	 * sector de salud, independientemente si se encuentran
	 * activos o pasivos
	 * 
	 * @pSectorId Identificador del Sector
	 * @return Lista de objetos Comunidad
	 */
	public List<Comunidad> ComunidadesPorSector(long pSectorId, int pPaginaActual, int pTotalPorPagina, int pNumRegistros);
	/**
	 * Retorna la lista de objetos {@link Sector} asociados a una
	 * unidad de salud y que se encuentran activos.
	 * 
	 * @param pUnidadId Identificador de la unidad de salud
	 * @return Lista de objetos Sector
	 */
	public List<Comunidad> ComunidadesPorSectorActivos(long pSectorId);
	/**
	 * Retorna la lista de objetos {@link Comunidad} asociados a un
	 * municipio, independientemente si se encuentran
	 * activos o pasivos
	 * 
	 * @return Lista de objetos Comunidad
	 */
	public List<Comunidad> ComunidadesPorMunicipio(long pMunicipioId,int pPaginaActual, int pTotalPorPagina, int pNumRegistros);

	/**
	 * Retorna una lista de objetos {@link Comunidad} que corresponden a
	 * las comunidades activas vinculados a un municipio
	 * 
	 * @param pMunicipioId
	 * @return Lista de objetos {@link Comunidad}
	 */
	public List<Comunidad> ComunidadesPorMunicipioActivos(long pMunicipioId);
	/**
	 * Busca un objeto {@link Comunidad} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * 
	 * @param pComunidadId Entero largo con el identificador de la Comunidad
	 * @return Objeto InfoResultado con el resultado de la operaci�n
	 */
	public InfoResultado Encontrar(long pComunidadId);
	/**
	 * Busca un objeto {@link Comunidad} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * 
	 * @param pCodigo Cadena de caracteres con el c�digo de la comunidad
	 * @return Objeto InfoResultado con el resultado de la operaci�n
	 */
	public InfoResultado Encontrar(String pCodigo);

	/**
	 * Guarda un objeto Comunidad existente en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link JPADataResult}.
	 * No se actualizar� el codigo institucional asignado
	 * <p>
	 * Realiza una operaci�n UPDATE en la base de datos
	 * @param pComunidad Objeto Comunidad a ser almacenado en la base de datos
	 * @return Objeto InfoResultado con el resultado de la operaci�n
	 */
	public InfoResultado Guardar(Comunidad pComunidad);
	/**
	 * Agrega un objeto Comunidad en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link JPADataResult}
	 * <p>
	 * Realiza una operaci�n INSERT en la base de datos
	 *  
	 * @param pComunidad Objeto Comunidad a ser agregado en la base de datos
	 * @return Objeto InfoResultado con el resultado de la operaci�n
	 */
	public InfoResultado Agregar(Comunidad pComunidad);
	/**
	 * Elimina el objeto Comunidad de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operaci�n DELETE en la base de datos
	 * 
	 * @param pComunidadId Entero largo con el identificador de la Comunidad
	 * @return Objeto InfoResultado con el resultado de la operaci�n
	 */
	public InfoResultado Eliminar(long pComunidadId);
	
	/**
	 * Realiza el conteo de las comunidades existentes por municipio o sector.
	 * <p>
	 * Si SectorId=0 se contaran todos las comunidades del municipio, caso contrario
	 * las comunidades del sector.
	 * <p>
	 * Si pNombre=null no se realizar� la b�squeda de comunidades
	 * 
	 * @param pMunicipioId Entero largo con el identificador del municipio
	 * @param pSectorId Entero largo con el identificador del sector
	 * @param pNombre Literal para la b�squeda de comunidades
	 * @return
	 */
	public int ContarComunidades(long pMunicipioId, long pSectorId, String pNombre);
	
	/**
	 * Realiza el conteo de las comunidades existentes, atendidas por una unidad
	 * de salud.  Si pNombre es nulo, no se realizar� la b�squeda de comunidades
	 * 
	 * @param pUnidadId Identificador de la unidad de salud
	 * @param pNombre   Literal para la b�squeda de comunidades
	 * @param pTipoArea U=Urbano, R=Rural, nulo=Urbano/Rural/No definido
	 * @return
	 */
	public int ContarComunidadesPorUnidad(long pUnidadId, String pNombre, String pTipoArea);
	
}
