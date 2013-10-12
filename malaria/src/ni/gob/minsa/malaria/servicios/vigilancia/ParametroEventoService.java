package ni.gob.minsa.malaria.servicios.vigilancia;

import java.util.*;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.vigilancia.ParametroEvento;


public interface ParametroEventoService {
	
    /** 
     * Obtiene todas los objetos {@link ParametroEvento} asociados a un evento de salud
     * identificado por <b>pParametroEpidemiologicoId</b>
     * 
     * @param pParametroEpidemiologicoId Identificador del par�metro epidemiol�gico
     * @return Una lista de objetos {@link ParametroEvento}
     */
	public List<ParametroEvento> EventosPorParametro(long pParametroEpidemiologicoId);


    /**
     * Elimina la asociaci�n entre un par�metro epidemiol�gico y un evento de salud utilizando 
     * el identificador <b>pParametroEventoId</b>
     * 
     * @param pParametroEventoId Identificador de la asociaci�n entre un par�metro epidemiol�gico y un evento de salud
     * @return Un objeto del tipo InfoResultado con el resultado de la operaci�n
     */
	public InfoResultado Eliminar(long pParametroEventoId);
	
    /**
     * Buscar la asociaci�n entre un par�metro epidemiol�gico y un evento de salud utilizando su identificador
     * en el contexto de la persistencia.
     * 
     * @param pParametroEventoId Identificador de la asociaci�n entre un par�metro epidemiol�gico y un evento de salud
     * @return InfoResultado Objeto con el resultado de la operaci�n
     */
	public InfoResultado Encontrar(long pParametroEventoId);
	
    /**
     * Agrega una asociaci�n entre un par�metro epidemiol�gico y un evento de salud
     * 
     * @param pParametroEvento Objeto en el cual se encuentra la asociaci�n entre el evento de salud y el par�metro epidemiol�gico que se agregar�
     */
	public InfoResultado Agregar(ParametroEvento pParametroEvento);
	
}
