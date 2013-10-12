package ni.gob.minsa.malaria.servicios.general;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.general.Ocupacion;

public interface OcupacionService {
	
	/**
	 * Retorna una lista de objetos {@link Ocupacion} que se encuentran activos.
	 * 
	 * @param pOcupacion Lista de ocupaciones activas mas la ocupaci�n con el c�digo especificado, 
	 * @return
	 */
	public List<Ocupacion> ListarActivos(long pOcupacion);

	/**
	 * Retorna una lista de objetos {@link Ocupacion} que se encuentran activos.
	 * 
	 * @return
	 */
	public List<Ocupacion> ListarActivos();

    /**
     * Busca una instancia del objeto {@link Ocupacion} en el contexto de la persistencia
     * utilizando su identificador
     * 
     * @param pOcupacionId Identificador del objeto {@link Ocupacion}
     * @return InfoResultado Objeto con el resultado de la operaci�n
     */
	public InfoResultado Encontrar(long pOcupacionId);
	
	/**
	 * Retorna una lista de objetos {@link Ocupacion} que contiene como parte del nombre
	 * la literal especificada en el par�metro pNombre.
	 * 
	 * @param pNombre  Literal de b�squeda de ocupaciones por nombre
	 * @return
	 */
	public List<Ocupacion> ListarPorNombre(String pNombre);
	
}
