package ni.gob.minsa.malaria.servicios.investigacion;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionSintoma;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarAnte;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarInicio;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarOtro;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en InvestigacionSintomaDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Félix Medina
 * @author <a href=mailto:medina.fx@gmail.com>medina.fx@gmail.com</a>
 * @version 1.0, &nbsp; 18/10/2013
 * @since jdk1.6.0_21
 */
public interface InvestigacionSintomaService {
	/**
	 * Busca un objeto {@link InvestigacionSintoma} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pInvestigacionSintomaId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pInvestigacionSintomaId);
	
	/** 
	 * Busca un objeto {@link InvestigacionSintoma} asociado a la investigación de malaria
     * identificado por <b>pInvestigacionMalariaId</b> y retorna el resultado de la operación 
     * en un objeto {@link InfoResultado}
     * 
     * @param pInvestigacionMalariaId Identificador de la investigación de malaria
     * @return Objeto {@link InfoResultado} con el resultado de la operación
     */
	public InfoResultado EncontrarPorInvestigacionMalaria(long pInvestigacionMalariaId);
	/**
	 * Guarda un objeto {@link InvestigacionSintoma} existente en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.
	 * <p>
	 * Realiza una operación UPDATE en la base de datos
	 * 
	 * @param pInvestigacionSintoma Objeto {@link InvestigacionSintoma} a ser almacenado en la base de datos
	 * @param pSintomaLugarInicio Objeto {@link SintomaLugarInicio} a ser almacenado en la base de datos
	 * @param pSintomaLugarAnte Objeto {@link SintomaLugarAnte} a ser almacenado en la base de datos
	 * @param pSintomaLugarOtro  Objeto {@link SintomaLugarOtro} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Guardar(InvestigacionSintoma pInvestigacionSintoma, SintomaLugarInicio pSintomaLugarInicio, 
			SintomaLugarAnte pSintomaLugarAnte, SintomaLugarOtro pSintomaLugarOtro);
	/**
	 * Agrega un objeto {@link InvestigacionSintoma} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pInvestigacionSintoma Objeto {@link InvestigacionSintoma} a ser almacenado en la base de datos
	 * @param pSintomaLugarInicio Objeto {@link SintomaLugarInicio} a ser almacenado en la base de datos
	 * @param pSintomaLugarAnte Objeto {@link SintomaLugarAnte} a ser almacenado en la base de datos
	 * @param pSintomaLugarOtro  Objeto {@link SintomaLugarOtro} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(InvestigacionSintoma pInvestigacionSintoma,SintomaLugarInicio pSintomaLugarInicio, 
			SintomaLugarAnte pSintomaLugarAnte, SintomaLugarOtro pSintomaLugarOtro);
	/**
	 * Elimina el objeto {@link InvestigacionSintoma} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param pInvestigacionSintomaId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pInvestigacionSintomaId);
}
