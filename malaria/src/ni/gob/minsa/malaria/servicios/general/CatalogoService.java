package ni.gob.minsa.malaria.servicios.general;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.general.Catalogo;

public interface CatalogoService {
	
	/**
	 * Retorna una lista de objetos {@link Catalogo} correspondientes
	 * a los nodos padres que est�n asociados al sistema de malaria
	 * que se encuentran activos
	 * 
	 * @return
	 */
	public List<Catalogo> CatalogoColeccionesActivas();
	
	/**
	 * Retorna una lista de objetos {@link Catalogo} que pertenecen a
	 * una colecci�n o nodo padre
	 *  
	 * @param pColeccion C�digo de la colecci�n o nodo padre de la cual dependen
	 * 				     los elementos del catalogo colectivo
	 */
	public List<Catalogo> CatalogoElementosActivos(String pColeccion);

    /**
     * Busca una instancia del objeto {@link Catalogo} en el contexto de la 
     * persistencia utilizando su identificador
     * 
     * @param pColeccionId Identificador del objeto {@link Catalogo}
     * @return InfoResultado Objeto con el resultado de la operaci�n
     */
	public InfoResultado Encontrar(String pColeccionId);
	
}
