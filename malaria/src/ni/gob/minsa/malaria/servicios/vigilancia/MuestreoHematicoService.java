// -----------------------------------------------
// MuestreoHematicoService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.vigilancia;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.ejbPersona.dto.Persona;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoHematico;
import ni.gob.minsa.malaria.modelo.vigilancia.MuestreoPruebaRapida;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en MuestreoHematicoDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 07/11/2012
 * @since jdk1.6.0_21
 */
public interface MuestreoHematicoService {

	/**
	 * Busca un objeto {@link MuestreoHematico} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * 
	 * @param pMuestreoHematicoId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Encontrar(long pMuestreoHematicoId);
	
	/**
	 * Guarda un objeto {@link MuestreoHematico} existente en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}.  Adicionalmente
	 * guarda, crea o elimina el objeto {@link MuestreoPruebaRapida} vinculado al Muestreo
	 * Hemático así como guarda, crea o elimina el objeto (@link MuestreoDiagnostico}.  
	 * <p>
	 * Realiza una operación UPDATE en la base de datos
	 * @param pMuestreoHemático Objeto {@link MuestreoHematico} a ser almacenado en la base de datos
	 * @param pPersona Objeto {@link Persona} que será actualizado y que está asociada al muestreo hemático
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Guardar(MuestreoHematico pMuestreoHematico, 
								 Persona pPersona);
	/**
	 * Agrega un objeto {@link MuestreoHematico} en la base de datos y retorna el
	 * resultado de la operación en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operación INSERT en la base de datos
	 *  
	 * @param pMuestreoHemático Objeto {@link MuestreoHematico} a ser almacenado en la base de datos
	 * @param pPersona Objeto {@link Persona} que será actualizado y que está asociada al muestreo hemático
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Agregar(MuestreoHematico pMuestreoHematico,
								 Persona pPersona);
	/**
	 * Elimina el objeto {@link MuestreoHematico} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operación DELETE en la base de datos
	 * 
	 * @param pMuestreoHematicoId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operación
	 */
	public InfoResultado Eliminar(long pMuestreoHematicoId);
	
	/**
	 * Busca una lista de objetos {@link MuestreoHematico} que correspondan
	 * al identificador de la persona.  Se entiende que la persona puede
	 * tener ninguno o más de un muestreo hemático.
	 * 
	 * @param pPersonaId Identificador de la persona
	 * @return Lista de {@link MuestreoHematico}
	 */
	public List<MuestreoHematico> ListarPorPersona(long pPersonaId);

	/**
	 * Busca un objeto {@link MuestreoHematico} en la base de datos mediante
	 * la clave y número de lámina.  Retorna <code>null</code>
	 * si no encuentra ningún registro de muestreo hemático asociado.
	 * 
	 * @param pEntidadId Identificador de la Entidad Administrativa
	 * @param pClave Clave
	 * @param pLamina Número de lámina
	 * @return Objeto {@link MuestreoHematico} o <code>null</code> si no encuentra
	 */
	public MuestreoHematico EncontrarPorLamina(long pEntidadIdId, String pClave, BigDecimal pLamina);

	/**
	 * Retorna una lista de objetos {@link MuestreoHematico} que correspondan
	 * a un puesto de notificación específico, para un año epidemiológico específico y situación.  
	 * 
	 * @param pPuestoNotificacionId Identificador del puesto de notificación
	 * @param pAnioEpi Año epidemiológico al cual corresponde el muestreo hemático; 0 = sin filtro de año
	 * @param pSituacion Número entero de 1 a 4 que indica el estado o situación del muestreo hemático
	 * <p>
	 * <code>0</code>: Todas las situaciones.<br>
	 * <code>1</code>: Ficha existente sin investigación epidemiológica asociada.<br>  
	 * <code>2</code>: Ficha existente con investigación epidemiológica abierta sin confirmación.<br>
	 * <code>3</code>: Ficha existente con investigación epidemiológica abierta con confirmación de diagnóstico. <br>
	 * <code>4</code>: Ficha existente con investigación epidemiológica cerrada.  
	 *
	 * @return Lista de {@link MuestreoHematico}
	 */
	public List<MuestreoHematico> ListarPorPuesto(long pPuestoNotificacionId, Integer pAnioEpi, Integer pSituacion, int pPaginaActual, int pTotalPorPagina, int pNumRegistros);

	/**
	 * Contabiliza el número de muestras notificadas por un puesto de notificación
	 * para un año epidemiológico específico y situación.
	 * 
	 * @param pPuestoNotificacionId
	 * @param pAnioEpi   Año epidemiológico; 0 = sin filtro de año epidemiológico
	 * @param pSituacion Número entero de 1 a 4 que indica el estado o situación del muestreo hemático, 0 incluye a todas las situaciones
	 * <p>
	 * <code>0</code>: Todas las situaciones.<br>
	 * <code>1</code>: Ficha existente sin investigación epidemiológica asociada.<br>  
	 * <code>2</code>: Ficha existente con investigación epidemiológica abierta sin confirmación.<br>
	 * <code>3</code>: Ficha existente con investigación epidemiológica abierta con confirmación de diagnóstico. <br>
	 * <code>4</code>: Ficha existente con investigación epidemiológica cerrada.  
	 * @return
	 */
	public int ContarPorPuesto(long pPuestoNotificacionId, Integer pAnioEpi, Integer pSituacion);

	/**
	 * Retorna una lista de objetos {@link MuestreoHematico} que notificados por los puestos
	 * de notificación coordinados por la unidad, y aquellos objetos notificados directamente
	 * por dicha únidad, en caso de que dicha unidad sea puesto de notificación.  Para un año 
	 * epidemiológico específico y situación.  
	 * 
	 * @param pUnidad Identificador de la unidad de salud coordinadora/puesto
	 * @param pAnioEpi Año epidemiológico al cual corresponde el muestreo hemático
	 * @param pSituacion Número entero de 1 a 4 que indica el estado o situación del muestreo hemático
	 * <p>
	 * <code>1</code>: Ficha existente sin investigación epidemiológica asociada.<br>  
	 * <code>2</code>: Ficha existente con investigación epidemiológica abierta sin confirmación.<br>
	 * <code>3</code>: Ficha existente con investigación epidemiológica abierta con confirmación de diagnóstico. <br>
	 * <code>4</code>: Ficha existente con investigación epidemiológica cerrada.  
	 *
	 * @return Lista de {@link MuestreoHematico}
	 */
	public List<MuestreoHematico> ListarPorUnidad(long pUnidadId, Integer pAnioEpi, Integer pSituacion, int pPaginaActual, int pTotalPorPagina, int pNumRegistros);

	/**
	 * Contabiliza el número de muestras notificadas por los puestos de notificación coordinados
	 * por una unidad específica y aquellas notificadas por dicha unidad directamente, en caso que la 
	 * unidad además sea un puesto de notificación, para un año epidemiológico específico y situación.
	 *
	 * @param pUnidad   Identificador de la unidad de salud
	 * @param pAnioEpi  Año epidemiológico o 0 para todos los años
	 * @param pSituacion
	 * <p>
	 * <code>0</code>: Todas las situaciones.<br>
	 * <code>1</code>: Ficha existente sin investigación epidemiológica asociada.<br>  
	 * <code>2</code>: Ficha existente con investigación epidemiológica abierta sin confirmación.<br>
	 * <code>3</code>: Ficha existente con investigación epidemiológica abierta con confirmación de diagnóstico. <br>
	 * <code>4</code>: Ficha existente con investigación epidemiológica cerrada.  
	 * @return
	 */
	public int ContarPorUnidad(long pUnidadId, Integer pAnioEpi, Integer pSituacion);
	
	/**
	 * Retorna una lista de los años epidemiológicos en los cuales existen muestreos hemáticos
	 * notificados por una unidad de salud, sea esta puesto de notificación o solo coordinadora
	 * de puestos de notificación (colaboradores voluntarios) a partir de un año
	 * epidemiológico especifico
	 * 
	 * @param pUnidad  Identificador de la unidad de salud
	 * @param pAnioEpi
	 * @return
	 */
	public List<Integer> ListarAñosConMuestreoUnidad(long pUnidadId, Integer pAnioEpi);
	
	/**
	 * Retorna una lista de los años epidemiológicos en los cuales existen muestreos hemáticos vinculados
	 * a un puesto de notificación específico a partir de un año epidemiológico específico
	 * 
	 * @param pPuestoNotificacionId
	 * @param pAnioEpi
	 * @return
	 */
	public List<Integer> ListarAñosConMuestreoPuesto(long pPuestoNotificacionId, Integer pAnioEpi);
	
	/**
	 * Retorna el número de muestreos hemáticos vinculados a un puesto de notificación
	 * posteriores a la fecha indicada
	 * @param pPuestoNotificacionId  Identificador del puesto de notificación
	 * @param pFecha  Fecha posterior a la cual se contabilizan los muestreos hemáticos
	 * @param pModo   0=Antes de la Fecha, 1=Después de la Fecha
	 * @return
	 */
	public int ContarPorPuestoFecha(long pPuestoNotificacionId, Date pFecha, int pModo);
}
