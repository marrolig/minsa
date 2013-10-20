package ni.gob.minsa.malaria.servicios.investigacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.investigacion.SintomaLugarOtro;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en SintomaLugarOtroDA.
 * La implementaci�n de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author F�lix Medina
 * @author <a href=mailto:medina.fx@gmail.com>medina.fx@gmail.com</a>
 * @version 1.0, &nbsp; 18/10/2013
 * @since jdk1.6.0_21
 */
public interface SintomaLugarOtroService {
	/** 
     * Obtiene todas los objetos {@link SintomaLugarOtro} asociados a la investigaci�n de sintomas
     * identificado por <b>pInvestigacionSintomaId</b>
     * 
     * @param pInvestigacionSintomaId Identificador de la investigaci�n de sintomas
     * @return Una lista de objetos {@link SintomaLugarOtro}
     */
	public List<SintomaLugarOtro> SintomasLugarOtroPorInvestigacionSintomas(long pInvestigacionSintomaId);


    /**
     * Elimina la asociaci�n entre una investigaci�n de sintomas y el lugar donde se presentaron, 
	 * utilizando el identificador <b>pSintomaLugarOtroId</b>
     * 
     * @param pSintomaLugarOtroId Identificador del lugar donde se presentaron s�ntomas.
     * @return Un objeto del tipo InfoResultado con el resultado de la operaci�n
     */
	public InfoResultado Eliminar(long pSintomaLugarOtroId);
	
    /**
     * Busca la asociaci�n entre una investigaci�n de sintomas y el lugar donde se presentaron 
	 * en el contexto de la persistencia.
     * 
     * @param pSintomaLugarOtroId Identificador de la asociaci�n entre un medicamento y una investigaci�n de sintomas
     * @return InfoResultado Objeto con el resultado de la operaci�n
     */
	public InfoResultado Encontrar(long pSintomaLugarOtroId);
	/**
	 * 
	 * @param pSintomaLugarOtro
	 * @return
	 */
	public InfoResultado Guardar(SintomaLugarOtro pSintomaLugarOtro);
    /**
     * Agrega una asociaci�n entre una Investigaci�n de sintomas y el lugar donde se presenta. 
     * 
     * @param pSintomaLugarOtro Objeto en el cual se encuentra la asociaci�n entre la investigaci�n del sintoma y el lugar
     */
	public InfoResultado Agregar(SintomaLugarOtro pSintomaLugarOtro);
}
