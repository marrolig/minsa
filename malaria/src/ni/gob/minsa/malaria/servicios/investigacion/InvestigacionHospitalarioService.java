package ni.gob.minsa.malaria.servicios.investigacion;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionHospitalario;



/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en InvestigacionHospitalarioDA.
 * La implementaci�n de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author F�lix Medina
 * @author <a href=mailto:medina.fx@gmail.com>medina.fx@gmail.com</a>
 * @version 1.0, &nbsp; 18/10/2013
 * @since jdk1.6.0_21
 */
public interface InvestigacionHospitalarioService {
	/**
	 * Busca un objeto {@link InvestigacionHospitalario} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * 
	 * @param pInvestigacionHospitalarioId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Encontrar(long pInvestigacionHospitalarioId);
	
	/** 
	 * Busca un objeto {@link InvestigacionHospitalario} asociado a la investigaci�n de malaria
     * identificado por <b>pInvestigacionHospitalarioId</b> y retorna el resultado de la operaci�n 
     * en un objeto {@link InfoResultado}
     * 
     * @param pInvestigacionHospitalarioId Identificador de la investigaci�n de malaria
     * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
     */
	public InfoResultado EncontrarPorInvestigacionMalaria(long pInvestigacionHospitalarioId);
	/**
	 * Guarda un objeto {@link InvestigacionHospitalario} existente en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operaci�n UPDATE en la base de datos
	 * @param pInvestigacionHospitalario Objeto {@link InvestigacionHospitalario} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Guardar(InvestigacionHospitalario pInvestigacionHospitalario);
	/**
	 * Agrega un objeto {@link InvestigacionHospitalario} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operaci�n INSERT en la base de datos
	 *  
	 * @param pInvestigacionHospitalario Objeto {@link InvestigacionHospitalario} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Agregar(InvestigacionHospitalario pInvestigacionHospitalario);
	/**
	 * Elimina el objeto {@link InvestigacionHospitalario} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operaci�n DELETE en la base de datos
	 * 
	 * @param pInvestigacionHospitalarioId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Eliminar(long pInvestigacionHospitalarioId);
	
}
