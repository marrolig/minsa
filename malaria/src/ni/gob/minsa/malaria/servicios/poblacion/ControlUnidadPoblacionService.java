/**
 * ControlUnidadPoblacionService.java
 */
package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.poblacion.ControlUnidadPoblacion;

/**
 * Esta clase define el interface para ControlUnidadPoblacionService.
 * La implementaci�n de este servicio accede a la capa de persistencia y 
 * proporciona el llamado a la base de datos.  Este interface tambi�n 
 * proporciona las operaciones de mantenimiento para los datos de la 
 * tabla CONTROL_UNIDAD_POBLACION.
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 23/12/2010
 * @since jdk1.6.0_21
 */
public interface ControlUnidadPoblacionService {

	/**
	 * Busca un objeto {@link ControlUnidadPoblacion} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * 
	 * @param  pControlUnidadPoblacionId Entero largo con el identificador del control de la poblacion de la unidad
	 * @return                           Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Encontrar(long pControlUnidadPoblacionId);
	
	/**
	 * Agrega un objeto ControlUnidadPoblacion en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}.  La existencia
	 * del registro implica una confirmaci�n o bloqueo de los datos de poblaci�n de 
	 * las comunidades que atiende la unidad de salud para un a�o espec�fico.
	 * <p>
	 * Realiza una operaci�n INSERT en la base de datos
	 *  
	 * @param pControlUnidadPoblacion Objeto ControlUnidadPoblacion a ser agregado en la base de datos
	 * @return 						  Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Agregar(ControlUnidadPoblacion pControlUnidadPoblacion);

	/**
	 * Elimina el objeto {@link ControlUnidadPoblacion} de la base de datos utilizando
	 * su identificador o clave primaria.  La eliminaci�n implica un desbloqueo
	 * de los datos de poblaci�n de las comunidades que son atendidas por la unidad de salud
	 * <p>
	 * Realiza una operaci�n DELETE en la base de datos
	 * 
	 * @param  pControlUnidadPoblacionId Entero largo con el identificador del control de la poblaci�n asociada a la unidad
	 * @return                           Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Eliminar(long pControlUnidadPoblacionId);

	/**
	 * Retorna un objeto {@link ControlUnidadPoblacion} que corresponden a un
	 * a�o y una unidad de salud espec�fica
	 * 
	 * @param pA�o              Entero que representa el a�o para el cual se solicita el objeto
	 * @param pUnidad           C�digo institucional de la unidad de salud
	 * @return Objeto {@link ControlUnidadPoblacion}
	 */
	public ControlUnidadPoblacion ControlPoblacionPorUnidad(Integer pA�o, long pUnidad);

	/**
	 * Retorna una lista objetos {@link ControlUnidadPoblacion} que corresponden a un
	 * a�o y entidad administrativa.  La lista contendr� el conjunto de unidades
	 * que ya han confirmado su poblaci�n.
	 * 
	 * @param pA�o              Entero que representa el a�o para el cual se solicita la lista de controles de actividad
	 * @param pEntidadAdtva     C�digo institucional de la entidad administrativa
	 * @return Lista de objetos {@link ControlUnidadPoblacion}
	 */
	public List<ControlUnidadPoblacion> UnidadesConPoblacionConfirmada(Integer pA�o, long pEntidadAdtva);

}
