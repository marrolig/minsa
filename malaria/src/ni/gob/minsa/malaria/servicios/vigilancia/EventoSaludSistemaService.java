package ni.gob.minsa.malaria.servicios.vigilancia;

import java.util.*;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.vigilancia.EventoSaludSistema;


public interface EventoSaludSistemaService {
	
    /** 
     * Obtiene todas los objetos {@link EventoSaludSistema} asociados a un evento de salud
     * identificado por <b>pEventoSaludId</b>
     * 
     * @param pEventoSaludId Identificador del evento de salud
     * @return Una lista de objetos {@link EventoSaludSistema}
     */
	public List<EventoSaludSistema> SistemasPorEventoSalud(long pEventoSaludId);


    /**
     * Elimina la asociación entre un sistema y un evento de salud utilizando 
     * el identificador <b>pEventoSaludSistemaId</b>
     * 
     * @param pEventoSaludSistemaId Identificador de la asociación entre un sistema y un evento de salud
     * @return Un objeto del tipo InfoResultado con el resultado de la operación
     */
	public InfoResultado Eliminar(long pEventoSaludSistemaId);
	
    /**
     * Buscar la asociación entre un sistema y un evento de salud utilizando su identificador
     * en el contexto de la persistencia.
     * 
     * @param pEventoSaludSistemaId Identificador de la asociación entre un sistema y un evento de salud
     * @return InfoResultado Objeto con el resultado de la operación
     */
	public InfoResultado Encontrar(long pEventoSaludSistemaId);
	
    /**
     * Agrega una asociación entre un sistema y un evento de salud
     * 
     * @param pEventoSaludSistema Objeto en el cual se encuentra la asociación entre el evento de salud y el sistema que se agregará
     */
	public InfoResultado Agregar(EventoSaludSistema pEventoSaludSistema);
	
}
