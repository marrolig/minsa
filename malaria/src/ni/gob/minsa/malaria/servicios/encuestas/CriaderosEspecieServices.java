package ni.gob.minsa.malaria.servicios.encuestas;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.encuesta.Criadero;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosEspecie;

/**
 * @author mdelgado
 * Interfaz que define las operaciones a ser implementadas en la clase PosInspeccionDA
 * Su implementacion permite el acceso a la capa de persistencia, permitiendo de
 * este modo persistir los datos al modelo de datos
 *
 */
public interface CriaderosEspecieServices {

	/**
	 * Metodo que permite obtener a partir de su identificador unico
	 * un objeto de tipo {@link CriaderosEspecie}
	 * 
	 * @param pEspecieId Entero largo con el identificador de la Especie
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerEspeciePorId(long pEspecieId);
	
	/**
	 * Metodo que permite obtener una lista Especies de tipo {@link CriaderosEspecie}
	 * a partir de un criadero de tipo {@link Criadero}
	 * 
	 * @param pPaginaActual Entero que identifica la pagina actual Paginacion
	 * @param pRegistroPorPagina Entero que identifica cantidad de registros por pagina Paginacion
	 * @param pFieldSort String que identifica el nombre del campo por el que se realizara un order de los registros encontrados 
	 * @param pSortOrder Boolean que identifica el orden de tipo Ascendente / Descendente
	 * @param pCriadero Objeto de tipo {@link Criadero}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerEspeciesPorCriadero(int pPaginaActual, int pRegistroPorPagina, String pFieldSort, Boolean pSortOrder, Criadero pCriadero);
	
	/**
	 * Metodo que permite obtener una lista Especies de tipo {@link CriaderosEspecie}
	 * a partir de un criadero de tipo {@link Criadero} 
	 * 
	 * @param pCriadero Objeto de tipo {@link Criadero}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerEspeciesPorCriadero(Criadero pCriadero);
	
	/**
	 * Metodo que permite acceder a la capa de persistencia y guardar un objeto de tipo
	 * {@link CriaderosEspecie} a la capa del modelo de datos del sistema
	 * 
	 * @param pEspecie Objeto de tipo {@link CriaderosEspecie}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado guardarEspecie(CriaderosEspecie pEspecie);
	
	/**
	 * Metodo que elimina un objeto de tipo {@link CriaderosEspecie} a partir
	 * de su identificador unico, accediendo a la capa de persistencia y eliminando
	 * el objeto del modelo de datos del sistema
	 * 
	 * @param pEspecieId Entero largo con el identificador de la Especie
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado eliminarEspecie(long pEspecieId);
	
}
