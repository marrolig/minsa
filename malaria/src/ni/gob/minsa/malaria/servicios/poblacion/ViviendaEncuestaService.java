// -----------------------------------------------
// ViviendaEncuestaService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.poblacion.ViviendaEncuesta;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en ViviendaEncuestaDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 04/06/2012
 * @since jdk1.6.0_21
 */
public interface ViviendaEncuestaService {

	/**
	 * Retorna la lista de objetos {@link ViviendaEncuesta} asociados
	 * a una vivienda
	 *
	 * @param pVivienda     Código de la vivienda
	 * @return Lista de objetos {@link ViviendaEncuesta}
	 */
	public List<ViviendaEncuesta> EncuestasPorVivienda(String pVivienda);

	/**
	 * Busca un objeto {@link ViviendaEncuesta} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pViviendaEncuestaId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pViviendaEncuestaId);
	
	/**
	 * Guarda un objeto {@link ViviendaEncuesta} existente en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operación UPDATE en la base de datos
	 * @param pViviendaEncuesta Objeto {@link ViviendaEncuesta} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Guardar(ViviendaEncuesta pViviendaEncuesta);
	/**
	 * Agrega un objeto {@link ViviendaEncuesta} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pViviendaEncuesta Objeto {@link ViviendaEncuesta} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(ViviendaEncuesta pViviendaEncuesta);
	/**
	 * Elimina el objeto {@link ViviendaEncuesta} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param pViviendaEncuestaId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pViviendaEncuestaId);

}
