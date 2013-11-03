package ni.gob.minsa.malaria.servicios.encuestas;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosIntervencion;
import ni.gob.minsa.malaria.modelo.encuesta.CriaderosPosInspeccion;

/**
 * @author mdelgado
 * Interfaz que define las operaciones a ser implementadas en la clase PosInspeccionDA
 * Su implementacion permite el acceso a la capa de persistencia, permitiendo de
 * este modo persistir los datos al modelo de datos
 *
 */
public interface PosInspeccionServices {

	/**
	 * Metodo que permite obtener a partir de su identificador unico
	 * un objeto de tipo {@link CriaderosPosInspeccion}
	 * 
	 * @param pPosInspeccionId Entero Largo con el identificador de la PosInspeccion
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerPosInspeccionPorId(long pPosInspeccionId);
	
	/**
	 * Metodo que permite obtener una lista de PosInspecciones de tipo
	 * {@link CriaderosPosInspeccion} a partir de una intervencion de tipo
	 * {@link CriaderosIntervencion}
	 * 
	 * @param pPaginaActual Entero que identifica la pagina actual Paginacion
	 * @param pRegistroPorPagina Entero que identifica cantidad de registros por pagina Paginacion
	 * @param pFieldSort String que identifica el nombre del campo por el que se realizara un order de los registros encontrados 
	 * @param pSortOrder Boolean que identifica el orden de tipo Ascendente / Descendente
	 * @param pIntervencion objeto de tipo {@link CriaderosIntervencion}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerPosInspeccionesPorPorIntervencion(int pPaginaActual, int pRegistroPorPagina, String pFieldSort, Boolean pSortOrder, CriaderosIntervencion pIntervencion);

	/**
	 * Metodo que permite obtener una PosInspecciones de tipo
	 * {@link CriaderosPosInspeccion} a partir de una intervencion de tipo
	 * {@link CriaderosIntervencion}
	 * 
	 * @param pIntervencion objeto de tipo {@link CriaderosIntervencion}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerPosInspeccionPorPorIntervencion(CriaderosIntervencion pIntervencion);	
	
	/**
	 * Metodo que permite acceder a la capa de persistencia y guardar un objeto de tipo
	 * {@link CriaderosPosInspeccion} a la capa del modelo de datos del sistema
	 * 
	 * @param pPosInspeccion objeto de tipo {@link CriaderosPosInspeccion}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado guardarPosInspeccion(CriaderosPosInspeccion pPosInspeccion);
	
	/**
	 * Metodo que elimina un objeto de tipo {@link CriaderosPosInspeccion} a partir
	 * de su identificador unico, accediendo a la capa de persistencia y eliminando
	 * el objeto del modelo de datos del sistema
	 * 
	 * @param pPosInspeccionId Entero Largo con el identificador de la PosInspeccion
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado eliminarPosInspeccion(long pPosInspeccionId);
	
	/**
	 * Metodo que elimina una coleccion de PosInspecciones {@link CriaderosPosInspeccion}
	 * que dependen de una intervencion {@link CriaderosIntervencion}
	 * 
	 * @param pIntervencion objeto {@link CriaderosIntervencion} 
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado eliminarPosInspeccionPorIntervencion(CriaderosIntervencion pIntervencion);
	
}
