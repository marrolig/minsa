// -----------------------------------------------
// ControlEntidadPoblacionService.java
// -----------------------------------------------
package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.poblacion.ControlEntidadPoblacion;

/**
 * Esta clase define el interface para ControlEntidadPoblacionService.
 * La implementaci�n de este servicio accede a la capa de persistencia y 
 * proporciona el llamado a la base de datos.  Este interface tambi�n 
 * proporciona las operaciones de mantenimiento para los datos de la 
 * tabla CONTROL_ENTIDAD_POBLACION.
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 09/05/2012
 * @since jdk1.6.0_21 
 */
public interface ControlEntidadPoblacionService {
	
	/**
	 * Busca un objeto {@link ControlEntidadPoblacion} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * 
	 * @param  pControlEntidadPoblacionId Entero largo con el identificador del control de la poblaci�n a ser
	 * 									  atendida por las unidades de salud vinculadas a la entidad administrativa
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Encontrar(long pControlEntidadPoblacionId);
	
	/**
	 * Agrega un objeto {@link ControlEntidadPoblacion} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}.  La existencia
	 * del registro implica una confirmaci�n o bloqueo de los datos de la poblaci�n de
	 * las comunidades bajo la cobertura de las unidades de salud que dependen de una
	 * entidad administrativa.
	 * <p>
	 * Realiza una operaci�n INSERT en la base de datos
	 *  
	 * @param pControlEntidadPoblacion Objeto {@link ControlEntidadPoblacion} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Agregar(ControlEntidadPoblacion pControlEntidadPoblacion);

	/**
	 * Elimina el objeto {@link ControlEntidadPoblacion} de la base de datos utilizando
	 * su identificador o clave primaria.  La eliminaci�n implica un desbloqueo
	 * de los datos de poblaci�n de las comunidades bajo la cobertura de las unidades
	 * de salud asociadas a la entidad administrativa
	 * <p>
	 * Realiza una operaci�n DELETE en la base de datos
	 * 
	 * @param  pControlEntidadPoblacionId Entero largo con el identificador del control de la poblaci�n de cobertura de la entidad administrativa
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Eliminar(long pControlEntidadPoblacionId);

	/**
	 * Retorna un objeto {@link ControlEntidadPoblacion} que corresponden a un
	 * a�o y una entidad administrativa espec�fica.  Retorna <code>null</code>
	 * si la entidad a�n no ha confirmado los datos de poblaci�n.
	 * 
	 * @param pA�o              Entero que representa el a�o para el cual se solicita el objeto
	 * @param pEntidad          Entero largo con el c�digo de la Entidad Administrativa
	 * @return Objeto {@link ControlEntidadPoblacion}
	 */
	public ControlEntidadPoblacion ControlPorEntidad(Integer pA�o, long pEntidad);

	/**
	 * Retorna una lista de objetos {@link ControlEntidadPoblacion} que responde
	 * a una a�o espec�fico, i.e. retorna la lista de entidades con poblacion de comunidades
	 * confirmada.
	 * 
	 * @param pA�o Entero que representa el a�o para el cual se solicita la lista
	 * @return Lista de Objetos {@link ControlEntidadPoblacion}
	 */
	public List<ControlEntidadPoblacion> EntidadesConPoblacionConfirmada(Integer pA�o);
}

