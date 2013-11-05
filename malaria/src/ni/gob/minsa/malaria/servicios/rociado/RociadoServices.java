package ni.gob.minsa.malaria.servicios.rociado;

import java.util.Date;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.estructura.EntidadAdtva;
import ni.gob.minsa.malaria.modelo.poblacion.Comunidad;
import ni.gob.minsa.malaria.modelo.poblacion.DivisionPolitica;
import ni.gob.minsa.malaria.modelo.rociado.RociadosMalaria;

import org.primefaces.model.SortOrder;

public interface RociadoServices {
	
	/**
	 * Metodo que permite obtener un objeto de tipo {@link RociadosMalaria}} a partir
	 * de su identificador unico retornando el resultado en un objeto de tipo
	 * {@link InfoResultado}
	 * 
	 * @param pRociadoId	Entero Largo con el identificador del Criadero
	 * @return Objeto		De tipo {@link InfoResultado} 			
	 */
	public InfoResultado obtenerCriaderoPorId(long pRociadoId);
	
	/**
	 * Metodo que permite obtener una lista de Rociados en dependencia del tipo de filtro 
	 * {@link Comunidad} como parametro, retornando el resultado en un objeto de tipo
	 * {@link InfoResultado}
	 * 
	 * @param pPaginaActual Entero que identifica la pagina actual Paginacion
	 * @param pRegistroPorPagina Entero que identifica cantidad de registros por pagina Paginacion
	 * @param pFieldSort String que identifica el nombre del campo por el que se realizara un order de los registros encontrados 
	 * @param pSortOrder Boolean que identifica el orden de tipo Ascendente / Descendente
	 * @param pComunidad Objeto de tipo {@link Comunidad}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerListaRociados(int pPaginaActual, int pRegistroPorPagina, String pFieldSort, SortOrder pSortOrder, Comunidad pComunidad);
	
	/**
	 * Metodo que permite obtener una lista de criaderos en dependencia del tipo de filtro 
	 * {@link DivisionPolitica} como parametro, retornando el resultado en un objeto de tipo
	 * {@link InfoResultado}
	 * 
	 * @param pPaginaActual Entero que identifica la pagina actual Paginacion
	 * @param pRegistroPorPagina Entero que identifica cantidad de registros por pagina Paginacion
	 * @param pFieldSort String que identifica el nombre del campo por el que se realizara un order de los registros encontrados 
	 * @param pSortOrder Boolean que identifica el orden de tipo Ascendente / Descendente
	 * @param pCodMunicipio Objeto de tipo {@link String}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerListaRociados(int pPaginaActual, int pRegistroPorPagina, String pFieldSort, SortOrder pSortOrder, String pCodMunicipio);
	
	/**
	 * Metodo que permite obtener una lista de criaderos en dependencia del tipo de filtro 
	 * {@link EntidadAdtva} pasado como parametro, retornando el resultado en un objeto de tipo
	 * {@link InfoResultado}
	 * 
	 * @param pPaginaActual Entero que identifica la pagina actual Paginacion
	 * @param pRegistroPorPagina Entero que identifica cantidad de registros por pagina Paginacion
	 * @param pFieldSort String que identifica el nombre del campo por el que se realizara un order de los registros encontrados 
	 * @param pSortOrder Boolean que identifica el orden de tipo Ascendente / Descendente
	 * @param pCodSilais Objeto de tipo {@link Long}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado obtenerListaRociados(int pPaginaActual, int pRegistroPorPagina, String pFieldSort, SortOrder pSortOrder, long pCodSilais);
	
	/**
	 * Metodo que permite acceder a la capa de persistencia y guardar un objeto de tipo
	 * {@link RociadosMalaria} a la capa del modelo de datos del sistema
	 * 
	 * @param pRociado Objeto de tipo {@link RociadosMalaria}
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado guardarRociado(RociadosMalaria pRociado);
	
	/**
	 * Metodo que valida que un numero de control de rociado sea unico por
	 * mes, siendo el valor devuelto true para numero de control valido y false
	 * para numero de control ya registrado
	 * 
	 * @param pNumControl
	 * @return boolean
	 */
	public boolean validarNumeroControlRociado(int pNumControl, long pCodSilais, String pCodMunicipio, String pCodComunidad, String pCodSector, Date pFecha);
	
}
