package ni.gob.minsa.malaria.servicios.encuestas;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.encuesta.Criadero;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;

/**
 * @author mdelgado
 * Interfaz que define las operaciones a ser implementadas en la clase CriaderoDA
 * Su implementacion permite el acceso a la capa de persistencia, permitiendo de
 * este modo persistir los datos al modelo de datos
 */
/**
 * @author dev
 *
 */
public interface CriaderoServices {

	/**
	 * Metodo que permite obtener un objeto de tipo {@link Criadero}} a partir
	 * de su identificador unico retornando el resultado en un objeto de tipo
	 * {@link InfoResultado}
	 * 
	 * @param pCriaderoId Entero Largo con el identificador del Criadero
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerCriaderoPorId(long pCriaderoId);
	
	/**
	 * Metodo que permite obtener una lista de criaderos en dependencia del tipo de filtro 
	 * {@link DivisionPolitica} {@link Comunidad} {@link EntidadAdtva} pasado como parametro
	 * en el objeto {@link Object} retornando el resultado en un objeto de tipo
	 * {@link InfoResultado}
	 * 
	 * @param pPaginaActual Entero que identifica la pagina actual Paginacion
	 * @param pRegistroPorPagina Entero que identifica cantidad de registros por pagina Paginacion
	 * @param pFieldSort String que identifica el nombre del campo por el que se realizara un order de los registros encontrados 
	 * @param pSortOrder Boolean que identifica el orden de tipo Ascendente / Descendente
	 * @param pComunidad Objeto de tipo {@link Comunidad}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerCriaderos(int pPaginaActual, int pRegistroPorPagina, String pFieldSort, Boolean pSortOrder, Comunidad pComunidad);
	
	/**
	 * Metodo que permite obtener una lista de criaderos en dependencia del tipo de filtro 
	 * {@link DivisionPolitica} {@link Comunidad} {@link EntidadAdtva} pasado como parametro
	 * en el objeto {@link Object} retornando el resultado en un objeto de tipo
	 * {@link InfoResultado}
	 * 
	 * @param pPaginaActual Entero que identifica la pagina actual Paginacion
	 * @param pRegistroPorPagina Entero que identifica cantidad de registros por pagina Paginacion
	 * @param pFieldSort String que identifica el nombre del campo por el que se realizara un order de los registros encontrados 
	 * @param pSortOrder Boolean que identifica el orden de tipo Ascendente / Descendente
	 * @param pSilais Objeto de tipo {@link EntidadAdtva}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerCriaderos(int pPaginaActual, int pRegistroPorPagina, String pFieldSort, Boolean pSortOrder, EntidadAdtva pSilais);	
	
	/**
	 * Metodo que permite obtener una lista de criaderos en dependencia del tipo de filtro 
	 * {@link DivisionPolitica} {@link Comunidad} {@link EntidadAdtva} pasado como parametro
	 * en el objeto {@link Object} retornando el resultado en un objeto de tipo
	 * {@link InfoResultado}
	 * 
	 * @param pPaginaActual Entero que identifica la pagina actual Paginacion
	 * @param pRegistroPorPagina Entero que identifica cantidad de registros por pagina Paginacion
	 * @param pFieldSort String que identifica el nombre del campo por el que se realizara un order de los registros encontrados 
	 * @param pSortOrder Boolean que identifica el orden de tipo Ascendente / Descendente
	 * @param pMunicipio Objeto de tipo {@link DivisionPolitica}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerCriaderos(int pPaginaActual, int pRegistroPorPagina, String pFieldSort, Boolean pSortOrder, DivisionPolitica pMunicipio);	
	
	/**
	 * Metodo que permite acceder a la capa de persistencia y guardar un objeto de tipo
	 * {@link Criadero} a la capa del modelo de datos del sistema
	 * 
	 * @param pCriadero Objeto de tipo {@link Criadero}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado guardarCriadero(Criadero pCriadero);
	
	/**
	 * Metodo que elimina un objeto de tipo {@link Criadero} a partir
	 * de su identificador unico, accediendo a la capa de persistencia y eliminando
	 * el objeto del modelo de datos del sistema
	 * 
	 * @param pCriaderoId Entero Largo con el identificador del Criadero
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado eliminarCriadero(long pCriaderoId); 
}
