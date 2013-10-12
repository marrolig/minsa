package ni.gob.minsa.malaria.servicios.general;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;

public interface CatalogoElementoService<C,K> {
	
	/**
	 * Retorna una lista de objetos C activos y que pertenecen a una coleccion 
	 * o nodo padre del catalogo colectivo 
	 *  
	 * @param pEntidadId Identificador de la entidad administrativa
	 */
	public List<C> ListarActivos();
	
	/**
	 * Retorna una lista de objetos C activos y que pertencen a una coleccion
	 * o nodo padre del catalogo colectivo e incluye el elemento 
	 * <code>pElemento</code> en la lista, el cual puede o no ser un elemento
	 * activo en la colección establecida
	 *  
	 * @param pElemento Código del elemento a ser incluido en la lista 
	 */
	public List<C> ListarActivos(String pElemento);

    /**
     * Busca una instancia del objeto C en el contexto de la persistencia
     * utilizando su identificador
     * 
     * @param pElementoId Identificador del objeto C
     * @return InfoResultado Objeto con el resultado de la operación
     */
	public InfoResultado Encontrar(long pElementoId);
	
}
