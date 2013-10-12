package ni.gob.minsa.malaria.servicios.general;

import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.general.TipoUnidad;


public interface TipoUnidadService {
	
	public List<TipoUnidad> TiposUnidadesActivas();
	
	/**
	 * Retorna una lista de objetos {@link TipoUnidad} que tienen
	 * vinculación con unidades que tienen autorizado el registro de actividad
	 * para una entidad administriva específica
	 *  
	 * @param pEntidadId Identificador de la entidad administrativa
	 * @return
	 */
	public List<TipoUnidad> TiposPorEntidadConActividad(long pEntidadId);
	
	/**
	 * Retorna una lista de objetos {@link TipoUnidad} que tienen
	 * vinculación con unidades que tienen autorizado el registro de metas de población
	 * para una entidad administriva específica
	 *  
	 * @param pEntidadId Identificador de la entidad administrativa
	 * @return
	 */
	public List<TipoUnidad> TiposPorEntidadConMeta(long pEntidadId);

	/**
	 * Retorna una lista de objetos {@link TipoUnidad} que tienen
	 * vinculación con unidades que tienen autorizado al control de inventario
	 * para una entidad administriva específica
	 *  
	 * @param pEntidadId Identificador de la entidad administrativa
	 * @return
	 */
	public List<TipoUnidad> TiposPorEntidadConInventario(long pEntidadId);

    /**
     * Busca una instancia del objeto TipoUnidad en el contexto de la persistencia
     * utilizando su identificador
     * 
     * @param pTipoUnidadId Identificador del objeto tipo unidad
     * @return InfoResultado Objeto con el resultado de la operación
     */
	public InfoResultado Encontrar(long pTipoUnidadId);
	
}
