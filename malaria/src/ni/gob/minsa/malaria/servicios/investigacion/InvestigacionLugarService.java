package ni.gob.minsa.malaria.servicios.investigacion;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionLugar;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en InvestigacionLugarDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Félix Medina
 * @author <a href=mailto:medina.fx@gmail.com>medina.fx@gmail.com</a>
 * @version 1.0, &nbsp; 18/10/2013
 * @since jdk1.6.0_21
 */
public interface InvestigacionLugarService {
	/**
	 * Busca un objeto {@link InvestigacionLugar} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pInvsmalariaLugarId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pInvsmalariaLugarId);
	
	/** 
	 * Busca un objeto {@link InvestigacionLugar} asociado a la investigación de malaria
     * identificado por <b>pInvestigacionMalariaId</b> y retorna el resultado de la operación 
     * en un objeto {@link InfoResultado}
     * 
     * @param pInvestigacionMalariaId Identificador de la investigación de malaria
     * @return Objeto {@link InfoResultado} con el resultado de la operación
     */
	public InfoResultado EncontrarPorInvestigacionMalaria(long pInvestigacionMalariaId);
	/**
	 * Guarda un objeto {@link InvestigacionLugar} existente en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operación UPDATE en la base de datos
	 * @param pInvestigacionLugar Objeto {@link InvestigacionLugar} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Guardar(InvestigacionLugar pInvestigacionLugar);
	/**
	 * Agrega un objeto {@link InvestigacionLugar} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pInvestigacionLugar Objeto {@link InvestigacionLugar} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(InvestigacionLugar pInvestigacionLugar);
	/**
	 * Elimina el objeto {@link InvestigacionLugar} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param pInvsmalariaLugarId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pInvsmalariaLugarId);
}
