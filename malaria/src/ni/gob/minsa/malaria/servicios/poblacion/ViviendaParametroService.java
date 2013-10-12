// -----------------------------------------------
// ViviendaParametroService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.poblacion.ViviendaParametroEpidemiologico;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en ViviendaParametroDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 06/06/2012
 * @since jdk1.6.0_21
 */
public interface ViviendaParametroService {

	/**
	 * Retorna la lista de objetos {@link ViviendaParametroEpidemiologico} asociados
	 * a una vivienda
	 *
	 * @param pVivienda     Código de la vivienda
	 * @return Lista de objetos {@link ViviendaParametroEpidemiologico}
	 */
	public List<ViviendaParametroEpidemiologico> ParametrosPorVivienda(String pVivienda);

	/**
	 * Busca un objeto {@link ViviendaParametroEpidemiologico} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pViviendaParametroEpidemiologicoId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pViviendaParametroEpidemiologicoId);
	
	/**
	 * Guarda un objeto {@link ViviendaParametroEpidemiologico} existente en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operación UPDATE en la base de datos
	 * @param pViviendaParametroEpidemiologico Objeto {@link ViviendaParametroEpidemiologico} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Guardar(ViviendaParametroEpidemiologico pViviendaParametroEpidemiologico);
	/**
	 * Agrega un objeto {@link ViviendaParametroEpidemiologico} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pViviendaParametroEpidemiologico Objeto {@link ViviendaParametroEpidemiologico} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(ViviendaParametroEpidemiologico pViviendaParametroEpidemiologico);
	/**
	 * Elimina el objeto {@link ViviendaParametroEpidemiologico} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param pViviendaParametroEpidemiologicoId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pViviendaParametroEpidemiologicoId);

}
