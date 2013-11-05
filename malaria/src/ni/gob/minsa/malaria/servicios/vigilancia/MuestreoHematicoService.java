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
	 * @param fechaToma Fecha a partir de la cual se generará el listado, comparando con
	 * 					la fecha de toma de gota gruesa.
	 * @return Lista de {@link MuestreoHematico}
	 */
	public List<MuestreoHematico> ListarPorPersona(long pPersonaId,Date fechaToma);
	
	/**
	 * Busca una lista de objetos {@link MuestreoHematico} que correspondan a aquellos 
	 * muestreos hemáticos con resultado positivo para el método por gota gruesa.
	 * Solo se retornarán aquellos muestredos que correspondan a pacientes en a la unidad 
	 * de salud seleccionada.
	 * Retorna <code>null</code> si no encuentra ningún registro de muestreo hemático que
	 * cumpla las condiciones indicadas.
	 * 
	 * @param pUnidadId Identificador de la unidad
	 * @param pAnioEpi Anio epidemiológico en el cual se tomó la muestra.
	 * @param pMesEpi Mes epidemiológico en el cual se tomó la muetra.
	 * @param pActivos <code>true</code> incluye únicamente las muestras hemáticas sin una M10 asociada,
	 * o bien con una M10 sin cerrar.
	 * @return Lista de {@link MuestreoHematico}
	 */
	public List<MuestreoHematico> ListarPositivosPorUnidad(
			long pEntidadAdtvaId,
			long pUnidadId,
			int pAnioEpi,
			boolean pActivos,
			int pPaginaActual, 
			int pTotalPorPagina, 
			int pNumRegistros);
	/**
	 * 
	 * @param pUnidadId Identificador de la unidad
	 * @param pAnioEpi Anio epidemiológico en el cual se tomó la muestra.
	 * @param pMesEpi Mes epidemiológico en el cual se tomó la muetra.
	 * @param pActivos <code>true</code> incluye únicamente las muestras hemáticas sin una M10 asociada,
	 * o bien con una M10 sin cerrar.
	 * @return Entero con el número de objetos {@link MuestreoHematico}.
	 */
	public int ContarPositivosPorUnidad(
			long pEntidadAdtvaId,
			long pUnidadId,
			int pAnioEpi,
			boolean pActivos);
	
	/**
	 * @param pUnidadId Identificador de la unidad
	 * @param pActivos <code>true</code> incluye únicamente los años con muestras hemáticas sin una M10 asociada,
	 * o bien con una M10 sin cerrar.
	 * @return Lista de años con muestreos hemáticos
	 */
	public List<Integer> ListarAñosConPositivosPorUnidad(long pEntidadAdtvaId,long pUnidadId,boolean pActivos);
	
	/**
	 * Busca un objeto {@link MuestreoHematico} en la base de datos mediante
	 * la clave y número de lámina.  Retorna <code>null</code>
	 * si no encuentra ningún registro de muestreo hemático asociado.
	 * 
	 * @param pClave Clave
	 * @param pLamina Número de lámina
	 * @return Objeto {@link MuestreoHematico} o <code>null</code> si no encuentra
	 */
	public MuestreoHematico EncontrarPorLamina(String pClave, BigDecimal pLamina);

	/**
	 * Retorna una lista de objetos {@link MuestreoHematico} que correspondan
	 * a una clave específica a partir de la fecha de toma de muestra especificada.
	 * La clave puede estar asignada en el tiempo a diferentes puestos de notificación.  
	 * 
	 * @param pClave Clave bajo la cual se realizó el muestreo hemático
	 * @param fechaToma Fecha a partir de la cual se generará el listado, comparando con
	 * 					la fecha de toma de gota gruesa.
	 * @return Lista de {@link MuestreoHematico}
	 */
	public List<MuestreoHematico> ListarPorClave(String pClave,Date fechaToma);

}
