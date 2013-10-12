// -----------------------------------------------
// ViviendaRiesgoService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.poblacion.ViviendaRiesgo;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en ViviendaRiesgoDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 06/06/2012
 * @since jdk1.6.0_21
 */
public interface ViviendaRiesgoService {

	/**
	 * Retorna la lista de objetos {@link ViviendaRiesgo} asociados
	 * a una vivienda
	 *
	 * @param pVivienda     Código de la vivienda
	 * @return Lista de objetos {@link ViviendaRiesgo}
	 */
	public List<ViviendaRiesgo> RiesgosPorVivienda(String pVivienda);

	/**
	 * Busca un objeto {@link ViviendaRiesgo} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pViviendaRiesgoId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pViviendaRiesgoId);
	
	/**
	 * Guarda un objeto {@link ViviendaRiesgo} existente en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operación UPDATE en la base de datos
	 * @param pViviendaRiesgo Objeto {@link ViviendaRiesgo} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Guardar(ViviendaRiesgo pViviendaRiesgo);
	/**
	 * Agrega un objeto {@link ViviendaRiesgo} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pViviendaRiesgo Objeto {@link ViviendaRiesgo} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(ViviendaRiesgo pViviendaRiesgo);
	/**
	 * Elimina el objeto {@link ViviendaRiesgo} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param pViviendaRiesgoId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pViviendaRiesgoId);

}
