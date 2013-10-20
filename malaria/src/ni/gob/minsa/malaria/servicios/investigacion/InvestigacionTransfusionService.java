package ni.gob.minsa.malaria.servicios.investigacion;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionTransfusion;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en InvestigacionTransfusionDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Félix Medina
 * @author <a href=mailto:medina.fx@gmail.com>medina.fx@gmail.com</a>
 * @version 1.0, &nbsp; 18/10/2013
 * @since jdk1.6.0_21
 */
public interface InvestigacionTransfusionService {
	/**
	 * Busca un objeto {@link InvestigacionTransfusion} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pInvestigacionTransfusionId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pInvestigacionTransfusionId);
	
	/** 
	 * Busca un objeto {@link InvestigacionTransfusion} asociado a la investigación de malaria
     * identificado por <b>pInvestigacionMalariaId</b> y retorna el resultado de la operación 
     * en un objeto {@link InfoResultado}
     * 
     * @param pInvestigacionMalariaId Identificador de la investigación de malaria
     * @return Objeto {@link InfoResultado} con el resultado de la operación
     */
	public InfoResultado EncontrarPorInvestigacionMalaria(long pInvestigacionMalariaId);
	/**
	 * Guarda un objeto {@link InvestigacionTransfusion} existente en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operación UPDATE en la base de datos
	 * @param pInvestigacionTransfusion Objeto {@link InvestigacionTransfusion} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Guardar(InvestigacionTransfusion pInvestigacionTransfusion);
	/**
	 * Agrega un objeto {@link InvestigacionTransfusion} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pInvestigacionTransfusion Objeto {@link InvestigacionTransfusion} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(InvestigacionTransfusion pInvestigacionTransfusion);
	/**
	 * Elimina el objeto {@link InvestigacionTransfusion} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param pInvestigacionTransfusionId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pInvestigacionTransfusionId);
}
