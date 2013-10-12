// -----------------------------------------------
// EventoSaludService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.vigilancia;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.vigilancia.EventoSalud;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en EventoSaludDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 12/06/2012
 * @since jdk1.6.0_21
 */
public interface EventoSaludService {

	/**
	 * Busca un objeto {@link EventoSalud} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pEventoSaludId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pEventoSaludId);
	
	/**
	 * Guarda un objeto {@link EventoSalud} existente en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operación UPDATE en la base de datos
	 * @param pEventoSalud Objeto {@link EventoSalud} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Guardar(EventoSalud pEventoSalud);
	/**
	 * Agrega un objeto {@link EventoSalud} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pEventoSalud Objeto {@link EventoSalud} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(EventoSalud pEventoSalud);
	/**
	 * Elimina el objeto {@link EventoSalud} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param pEventoSaludId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pEventoSaludId);
	
	/**
	 * Retorna una lista de objetos {@link EventoSalud}.
	 * 
	 * @param pPasivo        <code>true</code> incluye únicamente los pasivos;
	 * 						 <code>false</code> incluye únicamente los activos;
	 * 						 <code>null</code> lista todos
	 * @return Lista de objetos {@link EventoSalud}
	 */
	public List<EventoSalud> Listar(Boolean pPasivo); 

	/**
	 * Retorna una lista de objetos {@link EventoSalud} activos, donde se
	 * incluye el EventoSalud con el código <code>pEventoSalud</code>,
	 * el cual puede o no estar activo.
	 * 
	 * @param pEventoSalud  Código del evento de salud que será incluido en la lista
	 * @return Lista de objetos {@link EventoSalud}
	 */
	public List<EventoSalud> Listar(String pEventoSalud);
	
	/**
	 * Busca un objeto {@link EventoSalud} en la base de datos mediante
	 * el código asignado a dicho Evento de Salud.  Retorna <code>null</code>
	 * si no encuentra ningún evento de salud
	 * 
	 * @param pCodigo Código asignado al evento de salud
	 * @return Objeto {@link EventoSalud} o <code>null</code> si no encuentra
	 */
	public EventoSalud EncontrarPorCodigo(String pCodigo);

}
