package ni.gob.minsa.malaria.servicios.vigilancia;

import java.util.*;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.vigilancia.FactorRiesgoEvento;


public interface FactorRiesgoEventoService {
	
    /** 
     * Obtiene todas los objetos {@link FactorRiesgoEvento} asociados a un evento de salud
     * identificado por <b>pFactorRiesgoId</b>
     * 
     * @param pFactorRiesgoId Identificador del factor de riesgo
     * @return Una lista de objetos {@link FactorRiesgoEvento}
     */
	public List<FactorRiesgoEvento> EventosPorFactorRiesgo(long pFactorRiesgoId);


    /**
     * Elimina la asociaci�n entre un factor de riesgo y un evento de salud utilizando 
     * el identificador <b>pFactorRiesgoEventoId</b>
     * 
     * @param pFactorRiesgoEventoId Identificador de la asociaci�n entre un factor de riesgo y un evento de salud
     * @return Un objeto del tipo InfoResultado con el resultado de la operaci�n
     */
	public InfoResultado Eliminar(long pFactorRiesgoEventoId);
	
    /**
     * Buscar la asociaci�n entre un factor de riesgo y un evento de salud utilizando su identificador
     * en el contexto de la persistencia.
     * 
     * @param pFactorRiesgoEventoId Identificador de la asociaci�n entre un factor de riesgo y un evento de salud
     * @return InfoResultado Objeto con el resultado de la operaci�n
     */
	public InfoResultado Encontrar(long pFactorRiesgoEventoId);
	
    /**
     * Agrega una asociaci�n entre un factor de riesgo y un evento de salud
     * 
     * @param pFactorRiesgoEvento Objeto en el cual se encuentra la asociaci�n entre el evento de salud y el factor de riesgo que se agregar�
     */
	public InfoResultado Agregar(FactorRiesgoEvento pFactorRiesgoEvento);
	
}
