package ni.gob.minsa.malaria.servicios.sis;

import ni.gob.minsa.ciportal.dto.InfoResultado;

public interface SisPersonaService {

	/**
	 * Busca una persona mediante su identificador único de fila y 
	 * el resultado es retornado mediante un objeto {@link InfoResultado}
	 *  
	 * @param pSisPersonaId Identificador de la Persona
	 * @return Objeto {@link InfoResultado}
	 */
	public InfoResultado Encontrar(long pSisPersonaId);

}