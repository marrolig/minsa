// -----------------------------------------------
// ComunidadReferenciaService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.poblacion.ComunidadReferencia;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en ComunidadReferenciaDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 19/05/2012
 * @since jdk1.6.0_21
 */
public interface ComunidadReferenciaService {

	/**
	 * Retorna la lista de objetos {@link ComunidadReferencia} que pertenecen
	 * a un municipio   
	 *
	 * @param pMunicipioId Identificador del municipio
	 * @return Lista de objetos {@link ComunidadReferencia}
	 */
	public List<ComunidadReferencia> SitiosReferenciasPorMunicipio(long pMunicipioId);
	/**
	 * Retorna la lista de objetos {@link ComunidadReferencia} que pertenecen
	 * a las comunidades que estan bajo la cobertura de una unidad de salud.   
	 *
	 * @param pUnidadId Identificador de la unidad de salud
	 * @return Lista de objetos {@link ComunidadReferencia}
	 */
	public List<ComunidadReferencia> SitiosReferenciaPorUnidad(long pUnidadId);

	/**
	 * Busca un objeto {@link ComunidadReferencia} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pComunidadReferenciaId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pComunidadReferenciaId);
	/**
	 * Guarda un objeto {@link ComunidadReferencia} existente en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operación UPDATE en la base de datos
	 * @param pComunidadReferencia Objeto {@link ComunidadReferencia} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Guardar(ComunidadReferencia pComunidadReferencia);
	/**
	 * Agrega un objeto {@link ComunidadReferencia} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pComunidadReferencia Objeto {@link ComunidadReferencia} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(ComunidadReferencia pComunidadReferencia);
	/**
	 * Elimina el objeto {@link ComunidadReferencia} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param pComunidadReferenciaId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pComunidadReferenciaId);
	

}
