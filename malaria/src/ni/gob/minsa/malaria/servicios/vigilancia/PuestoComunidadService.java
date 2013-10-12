package ni.gob.minsa.malaria.servicios.vigilancia;

import java.util.*;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoComunidad;


public interface PuestoComunidadService {
	
    /** 
     * Obtiene todas los objetos {@link PuestoComunidad} asociados a un puesto
     * de notificación identificado por <b>pPuestoNotificacionId</b>
     * 
     * @param pPuestoNotificacionId Identificador del puesto de notificación
     * @return Una lista de objetos {@link PuestoComunidad}
     */
	public List<PuestoComunidad> ComunidadesPorPuesto(long pPuestoNotificacionId);

    /**
     * Elimina la asociación entre un puesto de notificación y una comunidad utilizando 
     * el identificador <b>pPuestoComunidadId</b>
     * 
     * @param pPuestoComunidadId Identificador de la asociación entre el puesto y la comunidad
     * @return Un objeto del tipo InfoResultado con el resultado de la operación
     */
	public InfoResultado Eliminar(long pPuestoComunidadId);
	
    /**
     * Busca el objeto {@link PuestoComunidad} utilizando su identificador
     * 
     * @param pPuestoComunidadId Identificador de la asociación entre el puesto de notificación y la comunidad
     * @return InfoResultado Objeto con el resultado de la operación
     */
	public InfoResultado Encontrar(long pPuestoComunidadId);
	
    /**
     * Agrega una asociación entre un puesto de notificación y una comunidad
     * 
     * @param pPuestoComunidad Objeto en el cual se encuentra la asociación entre el puesto y la comunidad
     */
	public InfoResultado Agregar(PuestoComunidad pPuestoComunidad);
	
}
