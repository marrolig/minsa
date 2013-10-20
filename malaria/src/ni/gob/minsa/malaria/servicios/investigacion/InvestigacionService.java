package ni.gob.minsa.malaria.servicios.investigacion;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionHospitalario;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionLugar;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMalaria;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionSintoma;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionTransfusion;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarAnte;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarInicio;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarOtro;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoHematico;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en InvestigacionDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Félix Medina
 * @author <a href=mailto:medina.fx@gmail.com>medina.fx@gmail.com</a>
 * @version 1.0, &nbsp; 14/10/2013
 * @since jdk1.6.0_21
 */
public interface InvestigacionService {
	
	/**
	 * Busca un objeto {@link InvestigacionMalaria} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pInvestigacionId Entero largo con el identificador del objeto {@link InvestigacionMalaria}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pInvestigacionId);
	/**
	 * Busca un objeto {@link InvestigacionMalaria} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pMuestreoHematicoId Entero largo con el identificador del objeto {@link MuestreoHematico} al
	 * cual está asociado el objeto {@link InvestigacionMalaria}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado EncontrarPorMuestreoHematico(long pMuestreoHematicoId);
	/**
	 * Elimina el objeto {@link InvestigacionMalaria} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param pInvestigacionId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pInvestigacionId);
	/**
	 * 
	 * Guarda un objeto {@link InvestigacionMalaria} existente en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.   Adicionalmente
	 * guarda, crea o elimina los objetos {@link InvestigacionSintoma},  {@link SintomaLugarInicio},
	 * {@link SintomaLugarAnte}, {@link SintomaLugarOtro}, {@link InvestigacionLugar} y 
	 * {@link InvestigacionTransfusion} .
	 * <p>
	 * Realiza una operación UPDATE en la base de datos
	 * 
	 * @param pInvestigacionMalaria objeto {@link InvestigacionMalaria} a ser almacenado en la base de datos.
	 * @return  Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Guardar(InvestigacionMalaria pInvestigacionMalaria,
			InvestigacionSintoma pInvestigacionSintoma,
			SintomaLugarInicio pSintomaLugarInicio,
			SintomaLugarAnte pSintomaLugarAnte,
			SintomaLugarOtro pSintomaLugarOtro,
			InvestigacionLugar pInvestigacionLugar, 
			InvestigacionTransfusion pInvestigacionTransfusion,
			InvestigacionHospitalario pInvestigacionHospitalario);
	/**
	 * Agrega un objeto {@link InvestigacionMalaria} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pColVol Objeto {@link InvestigacionMalaria} a ser agregado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(InvestigacionMalaria pInvestigacionMalaria,
			InvestigacionSintoma pInvestigacionSintoma,
			SintomaLugarInicio pSintomaLugarInicio,
			SintomaLugarAnte pSintomaLugarAnte,
			SintomaLugarOtro pSintomaLugarOtro,
			InvestigacionLugar pInvestigacionLugar, 
			InvestigacionTransfusion pInvestigacionTransfusion,
			InvestigacionHospitalario pInvestigacionHospitalario);
}
