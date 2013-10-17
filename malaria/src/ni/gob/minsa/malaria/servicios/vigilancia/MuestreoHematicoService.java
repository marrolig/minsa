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
 * La implementaci�n de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 07/11/2012
 * @since jdk1.6.0_21
 */
public interface MuestreoHematicoService {

	/**
	 * Busca un objeto {@link MuestreoHematico} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * 
	 * @param pMuestreoHematicoId Entero largo con el identificador del objeto
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Encontrar(long pMuestreoHematicoId);
	
	/**
	 * Guarda un objeto {@link MuestreoHematico} existente en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}.  Adicionalmente
	 * guarda, crea o elimina el objeto {@link MuestreoPruebaRapida} vinculado al Muestreo
	 * Hem�tico as� como guarda, crea o elimina el objeto (@link MuestreoDiagnostico}.  
	 * <p>
	 * Realiza una operaci�n UPDATE en la base de datos
	 * @param pMuestreoHem�tico Objeto {@link MuestreoHematico} a ser almacenado en la base de datos
	 * @param pPersona Objeto {@link Persona} que ser� actualizado y que est� asociada al muestreo hem�tico
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Guardar(MuestreoHematico pMuestreoHematico, 
								 Persona pPersona);
	/**
	 * Agrega un objeto {@link MuestreoHematico} en la base de datos y retorna el
	 * resultado de la operaci�n en un objeto {@link InfoResultado}
	 * <p>
	 * Realiza una operaci�n INSERT en la base de datos
	 *  
	 * @param pMuestreoHem�tico Objeto {@link MuestreoHematico} a ser almacenado en la base de datos
	 * @param pPersona Objeto {@link Persona} que ser� actualizado y que est� asociada al muestreo hem�tico
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Agregar(MuestreoHematico pMuestreoHematico,
								 Persona pPersona);
	/**
	 * Elimina el objeto {@link MuestreoHematico} de la base de datos utilizando
	 * su identificador o clave primaria.
	 * <p>
	 * Realiza una operaci�n DELETE en la base de datos
	 * 
	 * @param pMuestreoHematicoId Entero largo con el identificador de los datos 
	 * @return Objeto {@link InfoResultado} con el resultado de la operaci�n
	 */
	public InfoResultado Eliminar(long pMuestreoHematicoId);
	
	/**
	 * Busca una lista de objetos {@link MuestreoHematico} que correspondan
	 * al identificador de la persona.  Se entiende que la persona puede
	 * tener ninguno o m�s de un muestreo hem�tico.
	 * 
	 * @param pPersonaId Identificador de la persona
	 * @param fechaToma Fecha a partir de la cual se generar� el listado, comparando con
	 * 					la fecha de toma de gota gruesa.
	 * @return Lista de {@link MuestreoHematico}
	 */
	public List<MuestreoHematico> ListarPorPersona(long pPersonaId,Date fechaToma);
	
	/**
	 * Busca una lista de objetos {@link MuestreoHematico} que correspondan a aquellos 
	 * muestreos hem�ticos con resultado positivo para el m�todo por gota gruesa y 
	 * que no tienen un registro de M10 vinculado. Solo se mostrar�n aquellos muestredos
	 * que correspondan a pacientes en a la unidad de salud seleccionada.
	 * Retorna <code>null</code> si no encuentra ning�n registro de muestreo hem�tico que
	 * cumpla las condiciones indicadas.
	 * 
	 * @param pUnidadId Identificador de la unidad
	 * @return Lista de {@link MuestreoHematico}
	 */
	public List<MuestreoHematico> ListarPostivosPorUnidad(long pUnidadId);

	/**
	 * Busca un objeto {@link MuestreoHematico} en la base de datos mediante
	 * la clave y n�mero de l�mina.  Retorna <code>null</code>
	 * si no encuentra ning�n registro de muestreo hem�tico asociado.
	 * 
	 * @param pClave Clave
	 * @param pLamina N�mero de l�mina
	 * @return Objeto {@link MuestreoHematico} o <code>null</code> si no encuentra
	 */
	public MuestreoHematico EncontrarPorLamina(String pClave, BigDecimal pLamina);

	/**
	 * Retorna una lista de objetos {@link MuestreoHematico} que correspondan
	 * a una clave espec�fica a partir de la fecha de toma de muestra especificada.
	 * La clave puede estar asignada en el tiempo a diferentes puestos de notificaci�n.  
	 * 
	 * @param pClave Clave bajo la cual se realiz� el muestreo hem�tico
	 * @param fechaToma Fecha a partir de la cual se generar� el listado, comparando con
	 * 					la fecha de toma de gota gruesa.
	 * @return Lista de {@link MuestreoHematico}
	 */
	public List<MuestreoHematico> ListarPorClave(String pClave,Date fechaToma);

}
