package ni.gob.minsa.malaria.servicios.investigacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.investigacion.InvestigacionMedicamento;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en InvestigacionMedicamentoDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Félix Medina
 * @author <a href=mailto:medina.fx@gmail.com>medina.fx@gmail.com</a>
 * @version 1.0, &nbsp; 18/10/2013
 * @since jdk1.6.0_21
 */
public interface InvestigacionMedicamentoService {

	/** 
     * Obtiene todas los objetos {@link InvestigacionMedicamento} asociados a la investigación de malaria
     * identificado por <b>pInvestigacionMalariaId</b>
     * 
     * @param pInvestigacionMalariaId Identificador de la investigación de malaria
     * @return Una lista de objetos {@link InvestigacionMedicamento}
     */
	public List<InvestigacionMedicamento> EncontrarPorInvestigacionMalaria(long pInvestigacionMalariaId);


    /**
     * Elimina la asociación entre un factor de riesgo y un evento de salud utilizando 
     * el identificador <b>pFactorRiesgoEventoId</b>
     * 
     * @param pIvestigacionMedicamentoId Identificador de la asociación entre un medicamento y una investigación de malaria
     * @return Un objeto del tipo InfoResultado con el resultado de la operación
     */
	public InfoResultado Eliminar(long pIvestigacionMedicamentoId);
	
    /**
     * Buscar la asociación entre un medicamento y una investigación de malaria
     * en el contexto de la persistencia.
     * 
     * @param pIvestigacionMedicamentoId Identificador de la asociación entre un medicamento y una investigación de malaria
     * @return InfoResultado Objeto con el resultado de la operación
     */
	public InfoResultado Encontrar(long pIvestigacionMedicamentoId);
	
    /**
     * Agrega una asociación entre un factor de riesgo y un evento de salud
     * 
     * @param pInvestigacionMedicamento Objeto en el cual se encuentra la asociación entre el medicamento y la investigación de malaria que se agregará
     */
	public InfoResultado Agregar(InvestigacionMedicamento pInvestigacionMedicamento);
}
