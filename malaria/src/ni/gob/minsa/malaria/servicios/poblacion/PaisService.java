package ni.gob.minsa.malaria.servicios.poblacion;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.poblacion.Pais;

public interface PaisService {
	
	/**
	 * Retorna una lista de objetos {@link Pais}
	 */
	public List<Pais> listarPaises();
	
    /**
     * Busca una instancia del objeto {@link Pais} en el contexto de la persistencia
     * utilizando su identificador
     * 
     * @param pPaisId Identificador del objeto Pais
     * @return InfoResultado Objeto con el resultado de la operaci�n
     */
	public InfoResultado Encontrar(long pPaisId);
	
    /**
     * Busca una instancia del objeto {@link Pais} en el contexto de la persistencia
     * utilizando el c�digo alfa2, el cual es el c�digo utilizado como columna de referencia
     * en la persistencia y como clave for�nea a nivel del motor de la base de datos.
     * 
     * @param pCodigo C�digo asignado al pais
     * @return InfoResultado Objeto con el resultado de la operaci�n
     */
	public InfoResultado Encontrar(String pCodigo);
	
	
}
