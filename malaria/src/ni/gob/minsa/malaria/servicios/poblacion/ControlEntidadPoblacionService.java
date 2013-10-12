// -----------------------------------------------
// ControlEntidadPoblacionService.java
// -----------------------------------------------
package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.poblacion.ControlEntidadPoblacion;

/**
 * Esta clase define el interface para ControlEntidadPoblacionService.
 * La implementación de este servicio accede a la capa de persistencia y 
 * proporciona el llamado a la base de datos.  Este interface también 
 * proporciona las operaciones de mantenimiento para los datos de la 
 * tabla CONTROL_ENTIDAD_POBLACION.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 09/05/2012
 * @since jdk1.6.0_21 
 */
public interface ControlEntidadPoblacionService {
	
	/**
	 * Busca un objeto {@link ControlEntidadPoblacion} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param  pControlEntidadPoblacionId Entero largo con el identificador del control de la población a ser
	 * 									  atendida por las unidades de salud vinculadas a la entidad administrativa
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pControlEntidadPoblacionId);
	
	/**
	 * Agrega un objeto {@link ControlEntidadPoblacion} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.  La existencia
	 * del registro implica una confirmación o bloqueo de los datos de la población de
	 * las comunidades bajo la cobertura de las unidades de salud que dependen de una
	 * entidad administrativa.
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pControlEntidadPoblacion Objeto {@link ControlEntidadPoblacion} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(ControlEntidadPoblacion pControlEntidadPoblacion);

	/**
	 * Elimina el objeto {@link ControlEntidadPoblacion} de la base de datos utilizando
	 * su identificador o clave primaria.  La eliminación implica un desbloqueo
	 * de los datos de población de las comunidades bajo la cobertura de las unidades
	 * de salud asociadas a la entidad administrativa
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param  pControlEntidadPoblacionId Entero largo con el identificador del control de la población de cobertura de la entidad administrativa
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pControlEntidadPoblacionId);

	/**
	 * Retorna un objeto {@link ControlEntidadPoblacion} que corresponden a un
	 * año y una entidad administrativa específica.  Retorna <code>null</code>
	 * si la entidad aún no ha confirmado los datos de población.
	 * 
	 * @param pAño              Entero que representa el año para el cual se solicita el objeto
	 * @param pEntidad          Entero largo con el código de la Entidad Administrativa
	 * @return Objeto {@link ControlEntidadPoblacion}
	 */
	public ControlEntidadPoblacion ControlPorEntidad(Integer pAño, long pEntidad);

	/**
	 * Retorna una lista de objetos {@link ControlEntidadPoblacion} que responde
	 * a una año específico, i.e. retorna la lista de entidades con poblacion de comunidades
	 * confirmada.
	 * 
	 * @param pAño Entero que representa el año para el cual se solicita la lista
	 * @return Lista de Objetos {@link ControlEntidadPoblacion}
	 */
	public List<ControlEntidadPoblacion> EntidadesConPoblacionConfirmada(Integer pAño);
}

