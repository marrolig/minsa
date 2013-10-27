package ni.gob.minsa.malaria.servicios.encuestas;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosIntervencion;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosPesquisa;

/**
 * @author mdelgado
 * Interfaz que define las operaciones a ser implementadas en la clase IntervencionDA
 * Su implementacion permite el acceso a la capa de persistencia, permitiendo de
 * este modo persistir los datos al modelo de datos
 *
 */
public interface IntervencionServices {
	
	/**
	 * Metodo que permite obtener a partir de su identificador unico un objeto
	 * de tipo {@link CriaderosIntervencion}
	 * 
	 * @param pIntervencionId Entero Largo con el identificador de la Intervencion
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerIntervencionPorId(long pIntervencionId);
	
	/**
	 * Metodo que permite obtener la lista de intervenciones de tipo 
	 * {@link CriaderosIntervencion} a partir de un objeto de tipo 
	 * {@link CriaderosPesquisa}
	 * 
	 * @param pPaginaActual Entero que identifica la pagina actual Paginacion
	 * @param pRegistroPorPagina Entero que identifica cantidad de registros por pagina Paginacion
	 * @param pFieldSort String que identifica el nombre del campo por el que se realizara un order de los registros encontrados 
	 * @param pSortOrder Boolean que identifica el orden de tipo Ascendente / Descendente
	 * @param pPesquisa objeto tipo {@link CriaderosPesquisa}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerIntervencionesPorPesquisa(int pPaginaActual, int pRegistroPorPagina, String pFieldSort, Boolean pSortOrder, CriaderosPesquisa pPesquisa);

	/**
	 * Metodo que permite obtener la lista de intervenciones de tipo 
	 * {@link CriaderosIntervencion} a partir de un objeto de tipo 
	 * {@link CriaderosPesquisa}
	 * 
	 * @param pPesquisa objeto tipo {@link CriaderosPesquisa}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerIntervencionesPorPesquisa(CriaderosPesquisa pPesquisa);	
	
	/**
	 * Metodo que permite acceder a la capa de persistencia y guardar un objeto de tipo
	 * {@link CriaderosIntervencion} a la capa del modelo de datos del sistema
	 * 
	 * @param pIntervencion objeto de tipo {@link CriaderosIntervencion}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado guardarIntervencion(CriaderosIntervencion pIntervencion);
	
	/**
	 * Metodo que elimina un objeto de tipo {@link CriaderosIntervencion} a partir
	 * de su identificador unico, accediendo a la capa de persistencia y eliminando
	 * el objeto del modelo de datos del sistema
	 * 
	 * @param pIntervencionId Entero Largo con el identificador de la Intervencion
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado eliminarIntervencion(long pIntervencionId);

	/**
	 * Metodo que elimina una coleccion de Intervenciones {@link CriaderosIntervencion}
	 * que dependen de una pesquisa {@link CriaderosPesquisa}
	 * 
	 * @param pPesquisa objeto {@link CriaderosPesquisa}
	 * @return
	 */
	public InfoResultado eliminarIntervencionPorPesquisa(CriaderosPesquisa pPesquisa);
	

}
