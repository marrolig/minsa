/**
 * ControlUnidadPoblacionService.java
 */
package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.poblacion.ControlUnidadPoblacion;

/**
 * Esta clase define el interface para ControlUnidadPoblacionService.
 * La implementación de este servicio accede a la capa de persistencia y 
 * proporciona el llamado a la base de datos.  Este interface también 
 * proporciona las operaciones de mantenimiento para los datos de la 
 * tabla CONTROL_UNIDAD_POBLACION.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 23/12/2010
 * @since jdk1.6.0_21
 */
public interface ControlUnidadPoblacionService {

	/**
	 * Busca un objeto {@link ControlUnidadPoblacion} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param  pControlUnidadPoblacionId Entero largo con el identificador del control de la poblacion de la unidad
	 * @return                           Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pControlUnidadPoblacionId);
	
	/**
	 * Agrega un objeto ControlUnidadPoblacion en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.  La existencia
	 * del registro implica una confirmación o bloqueo de los datos de población de 
	 * las comunidades que atiende la unidad de salud para un año específico.
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pControlUnidadPoblacion Objeto ControlUnidadPoblacion a ser agregado en la base de datos
	 * @return 						  Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(ControlUnidadPoblacion pControlUnidadPoblacion);

	/**
	 * Elimina el objeto {@link ControlUnidadPoblacion} de la base de datos utilizando
	 * su identificador o clave primaria.  La eliminación implica un desbloqueo
	 * de los datos de población de las comunidades que son atendidas por la unidad de salud
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param  pControlUnidadPoblacionId Entero largo con el identificador del control de la población asociada a la unidad
	 * @return                           Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pControlUnidadPoblacionId);

	/**
	 * Retorna un objeto {@link ControlUnidadPoblacion} que corresponden a un
	 * año y una unidad de salud específica
	 * 
	 * @param pAño              Entero que representa el año para el cual se solicita el objeto
	 * @param pUnidad           Código institucional de la unidad de salud
	 * @return Objeto {@link ControlUnidadPoblacion}
	 */
	public ControlUnidadPoblacion ControlPoblacionPorUnidad(Integer pAño, long pUnidad);

	/**
	 * Retorna una lista objetos {@link ControlUnidadPoblacion} que corresponden a un
	 * año y entidad administrativa.  La lista contendrá el conjunto de unidades
	 * que ya han confirmado su población.
	 * 
	 * @param pAño              Entero que representa el año para el cual se solicita la lista de controles de actividad
	 * @param pEntidadAdtva     Código institucional de la entidad administrativa
	 * @return Lista de objetos {@link ControlUnidadPoblacion}
	 */
	public List<ControlUnidadPoblacion> UnidadesConPoblacionConfirmada(Integer pAño, long pEntidadAdtva);

}
