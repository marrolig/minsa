package ni.gob.minsa.malaria.servicios.estructura;

import java.math.BigDecimal;
import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;

public interface UnidadService {
	/**
	 * Retorna la lista total de unidades de salud 
	 * o establecimientos activos
	 * 
	 * @return Lista de Unidades de Salud
	 */
	public List<Unidad> UnidadesActivas();
	/**
	 * Busca una unidad de salud o establecimiento mediante su
	 * identificador único de fila (no el código institucional) y
	 * el resultado es retornado mediante un objeto {@link InfoResultado}
	 *  
	 * @param pUnidadId Identificador de la Unidad de Salud
	 * @return Objeto {@link InfoResultado}
	 */
	public InfoResultado Encontrar(long pUnidadId);
	/**
	 * Retorna todas la unidades de salud o establecimientos activos
	 * que responden administrativamente a una Entidad Administrativa
	 * (SILAIS)
	 * 
	 * @param pEntidadId Identificador de la Entidad Administrativa
	 * @return Lista de Unidades de Salud
	 */
	public List<Unidad> UnidadesActivasPorEntidadAdtva(long pEntidadId);
	/**
	 * Retorna todas las unidades de salud o establecimientos activos
	 * filtrados por Entidad Administrativa y Tipo de Unidad.<p>
	 * Si <code>pTipoUnidadId</code> es 0 (cero), retornará todas las unidades
	 * de salud independientemente del tipo de unidad que posea.  Si
	 * <code>pTipoUnidadId</code> es diferente de cero, retornará únicamente
	 * aquellas unidades de salud del tipo correspondiente. 
	 * 
	 * @param pEntidadId Identificador de la Entidad Administrativa
	 * @param pTipoUnidadId Identificador del Tipo de Unidad o Perfil
	 * @return Lista de Unidades
	 */
	public List<Unidad> UnidadesActivasPorEntidadYTipo(long pEntidadId,long pTipoUnidadId);
	
	/**
	 * 
	 * @param pEntidadId Identificador de la Entidad Administrativa
	 * @param pTipoUnidadI
	 * @return Lista de Unidades
	 */
	public List<Unidad>UnidadesActivasPorEntidadYCategoria(long pEntidadId,BigDecimal pCategoriaUnidad);
	/**
	 * 
	 * @param pMunicipioId
	 * @param pTipoUnidadId
	 * @param pNombre
	 * @return
	 */
	public int ContarUnidadesPorMunicipio(long pMunicipioId,long pTipoUnidadId, String pNombre);
	/**
	 * 
	 * @param pMunicipioId
	 * @param pTipoUnidadId
	 * @param pNombre
	 * @param pPaginaActual
	 * @param pTotalPorPagina
	 * @param pNumRegistros
	 * @return
	 */
	public List<Unidad> UnidadesPorMunicipio(long pMunicipioId, long pTipoUnidadId, String pNombre, int pPaginaActual, int pTotalPorPagina, int pNumRegistros);
	
	/**
	 * 
	 * @param pCategoria
	 * @return
	 */
	public List<Unidad> UnidadesActivasPorPropiedad(String pPropiedad);
	/**
	 * Retorna una lista de unidades de salud que contienen la literal especificada
	 * en <code>pNombre</code>.  La búsqueda se puede tamizar en base a la entidad
	 * administrativa, el municipio y su situación.<p>
	 * 
	 * Si <code>pEntidadAdtvaId</code> y <code>pMunicipioId</code> son cero, 
	 * la búsqueda se realizará sobre todo el conjunto de unidades de salud definidas.<p>
	 * 
	 * Si <code>pEntidadAdtvaId</code> es cero y <code>pMunicipioId</code> diferente 
	 * de cero, la búsqueda se realizará sobre todas las unidades de salud que se 
	 * encuentran ubicada en el municipio especificado sin importar a que entidad 
	 * administrativa pertenece.<p>
	 * 
	 * Si <code>pEntidadAdtvaId</code> es diferente de cero y <code>pMunicipioId</code> 
	 * es cero, la búsqueda se realizará sobre todas las unidades de salud que dependen 
	 * administrativamente de la entidad, sin importar el municipio en el cual se encuentra
	 * ubicada geográficamente.
	 * 
	 * Si el valor de <code>pPasivo</code> es nulo, la lista de unidades de salud incluirán
	 * tanto activas como pasivas.  
	 * 
	 * @param pNombre          Literal del nombre
	 * @param pEntidadAdtvaId  Identificador de la entidad administrativa
	 * @param pMunicipioId     Identificador del municipio
	 * @param pPasivo          nulo: Cualquier situación, 1: Solo pasivas, 0: Solo activas
	 * @return
	 */
	public List<Unidad> UnidadesPorNombre(String pNombre, long pEntidadAdtvaId, long pMunicipioId, String pPasivo);
	

}