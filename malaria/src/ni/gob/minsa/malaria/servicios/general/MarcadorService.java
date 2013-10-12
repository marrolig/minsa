package ni.gob.minsa.malaria.servicios.general;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.general.Marcador;

public interface MarcadorService {
	
	/**
	 * Retorna una lista de objetos {@link Marcador}
	 */
	public List<Marcador> listarMarcadores();
	
    /**
     * Busca una instancia del objeto {@link Marcador} en el contexto de la persistencia
     * utilizando su identificador
     * 
     * @param pMarcadorId Identificador del objeto marcador
     * @return InfoResultado Objeto con el resultado de la operación
     */
	public InfoResultado Encontrar(long pMarcadorId);
	
    /**
     * Busca una instancia del objeto {@link Marcador} en el contexto de la persistencia
     * utilizando el código
     * 
     * @param pCodigo Código asignado al marcador
     * @return InfoResultado Objeto con el resultado de la operación
     */
	public InfoResultado Encontrar(String pCodigo);
	
	
}
