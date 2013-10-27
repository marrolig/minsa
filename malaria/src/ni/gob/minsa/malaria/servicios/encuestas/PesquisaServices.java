package ni.gob.minsa.malaria.servicios.encuestas;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.encuesta.Criadero;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosPesquisa;

/**
 * @author mdelgado
 * 
 * Interfaz que define las operaciones a ser implementadas en la clase PesquisaDA
 * Su implementacion permite el acceso a la capa de persistencia, permitiendo de
 * este modo persistir los datos al modelo de datos
 *
 */
public interface PesquisaServices {

	
	/**
	 * 
	 * Metodo que permite obtener una pesquisa de criadero por su identificador unico,
	 * retornando el resultado en un objeto {@link InfoResultado}
	 * 
	 * @param pPesquisaId Entero Largo con el identificador de la Pesquisa
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerPesquisaPorId(long pPesquisaId);
	
	/**
	 * Metodo que permite obtener una lista de pesquisas a partir de un 
	 * criadero especifico, retornando el resultado del metodo en un
	 * objeto {@link InfoResultado}
	 * 
	 * @param pPaginaActual Entero que identifica la pagina actual Paginacion
	 * @param pRegistroPorPagina Entero que identifica cantidad de registros por pagina Paginacion
	 * @param pFieldSort String que identifica el nombre del campo por el que se realizara un order de los registros encontrados 
	 * @param pSortOrder Boolean que identifica el orden de tipo Ascendente / Descendente
	 * @param pCriadero Objeto Entity de tipo Criadero {@link Criadero}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerPesquisasPorCriadero(int pPaginaActual, int pRegistroPorPagina, String pFieldSort, Boolean pSortOrder, Criadero pCriadero);

	/**
	 * Metodo que permite obtener una lista de pesquisas a partir de un 
	 * criadero especifico, retornando el resultado del metodo en un
	 * objeto {@link InfoResultado}
	 * 
	 * @param pCriadero Objeto Entity de tipo Criadero {@link Criadero}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerPesquisasPorCriadero(Criadero pCriadero);	
	
	/**
	 * Metodo que permite acceder a la capa de persistencia y guardar un objeto de tipo
	 * {@link CriaderosPesquisa} a la capa del modelo de datos del sistema
	 * 
	 * @param pPesquisa Objeto Entity de tipo CriaderoPesquisa {@link CriaderosPesquisa}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado guardarPesquisa(CriaderosPesquisa pPesquisa);
	
	/**
	 * Metodo que elimina un objeto de tipo {@link CriaderosPesquisa} a partir
	 * de su identificador unico, accediendo a la capa de persistencia y eliminando
	 * el objeto del modelo de datos del sistema
	 * 
	 * @param pPesquisaId Entero Largo con el identificador de la Pesquisa
	 * @return  Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado eliminarPesquisa(long pPesquisaId);
	
	
}
