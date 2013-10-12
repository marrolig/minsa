package ni.gob.minsa.malaria.servicios.vigilancia;

import java.util.*;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.vigilancia.ParametroEvento;


public interface ParametroEventoService {
	
    /** 
     * Obtiene todas los objetos {@link ParametroEvento} asociados a un evento de salud
     * identificado por <b>pParametroEpidemiologicoId</b>
     * 
     * @param pParametroEpidemiologicoId Identificador del parámetro epidemiológico
     * @return Una lista de objetos {@link ParametroEvento}
     */
	public List<ParametroEvento> EventosPorParametro(long pParametroEpidemiologicoId);


    /**
     * Elimina la asociación entre un parámetro epidemiológico y un evento de salud utilizando 
     * el identificador <b>pParametroEventoId</b>
     * 
     * @param pParametroEventoId Identificador de la asociación entre un parámetro epidemiológico y un evento de salud
     * @return Un objeto del tipo InfoResultado con el resultado de la operación
     */
	public InfoResultado Eliminar(long pParametroEventoId);
	
    /**
     * Buscar la asociación entre un parámetro epidemiológico y un evento de salud utilizando su identificador
     * en el contexto de la persistencia.
     * 
     * @param pParametroEventoId Identificador de la asociación entre un parámetro epidemiológico y un evento de salud
     * @return InfoResultado Objeto con el resultado de la operación
     */
	public InfoResultado Encontrar(long pParametroEventoId);
	
    /**
     * Agrega una asociación entre un parámetro epidemiológico y un evento de salud
     * 
     * @param pParametroEvento Objeto en el cual se encuentra la asociación entre el evento de salud y el parámetro epidemiológico que se agregará
     */
	public InfoResultado Agregar(ParametroEvento pParametroEvento);
	
}
