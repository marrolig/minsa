package ni.gob.minsa.malaria.servicios.investigacion;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMalaria;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoHematico;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoPruebaRapida;

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
	 * @param pInvestigacionMalariaId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pInvestigacionMalariaId);
	
	/**
	 * Guarda un objeto {@link InvestigacionMalaria} existente en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.  
	 * <p>
	 * Realiza una operación UPDATE en la base de datos
	 * 
	 * @param pInvestigacionMalaria {@link InvestigacionMalaria} a ser almacenado en la base de datos
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Guardar(InvestigacionMalaria pInvestigacionMalaria);

}
