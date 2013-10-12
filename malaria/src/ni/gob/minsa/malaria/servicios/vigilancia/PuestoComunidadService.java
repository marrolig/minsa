package ni.gob.minsa.malaria.servicios.vigilancia;

import java.util.*;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.vigilancia.PuestoComunidad;


public interface PuestoComunidadService {
	
    /** 
     * Obtiene todas los objetos {@link PuestoComunidad} asociados a un puesto
     * de notificaci�n identificado por <b>pPuestoNotificacionId</b>
     * 
     * @param pPuestoNotificacionId Identificador del puesto de notificaci�n
     * @return Una lista de objetos {@link PuestoComunidad}
     */
	public List<PuestoComunidad> ComunidadesPorPuesto(long pPuestoNotificacionId);

    /**
     * Elimina la asociaci�n entre un puesto de notificaci�n y una comunidad utilizando 
     * el identificador <b>pPuestoComunidadId</b>
     * 
     * @param pPuestoComunidadId Identificador de la asociaci�n entre el puesto y la comunidad
     * @return Un objeto del tipo InfoResultado con el resultado de la operaci�n
     */
	public InfoResultado Eliminar(long pPuestoComunidadId);
	
    /**
     * Busca el objeto {@link PuestoComunidad} utilizando su identificador
     * 
     * @param pPuestoComunidadId Identificador de la asociaci�n entre el puesto de notificaci�n y la comunidad
     * @return InfoResultado Objeto con el resultado de la operaci�n
     */
	public InfoResultado Encontrar(long pPuestoComunidadId);
	
    /**
     * Agrega una asociaci�n entre un puesto de notificaci�n y una comunidad
     * 
     * @param pPuestoComunidad Objeto en el cual se encuentra la asociaci�n entre el puesto y la comunidad
     */
	public InfoResultado Agregar(PuestoComunidad pPuestoComunidad);
	
}
