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
	 * @return Lista de {@link MuestreoHematico}
	 */
	public List<MuestreoHematico> ListarPorPersona(long pPersonaId);

	/**
	 * Busca un objeto {@link MuestreoHematico} en la base de datos mediante
	 * la clave y n�mero de l�mina.  Retorna <code>null</code>
	 * si no encuentra ning�n registro de muestreo hem�tico asociado.
	 * 
	 * @param pEntidadId Identificador de la Entidad Administrativa
	 * @param pClave Clave
	 * @param pLamina N�mero de l�mina
	 * @return Objeto {@link MuestreoHematico} o <code>null</code> si no encuentra
	 */
	public MuestreoHematico EncontrarPorLamina(long pEntidadIdId, String pClave, BigDecimal pLamina);

	/**
	 * Retorna una lista de objetos {@link MuestreoHematico} que correspondan
	 * a un puesto de notificaci�n espec�fico, para un a�o epidemiol�gico espec�fico y situaci�n.  
	 * 
	 * @param pPuestoNotificacionId Identificador del puesto de notificaci�n
	 * @param pAnioEpi A�o epidemiol�gico al cual corresponde el muestreo hem�tico; 0 = sin filtro de a�o
	 * @param pSituacion N�mero entero de 1 a 4 que indica el estado o situaci�n del muestreo hem�tico
	 * <p>
	 * <code>0</code>: Todas las situaciones.<br>
	 * <code>1</code>: Ficha existente sin investigaci�n epidemiol�gica asociada.<br>  
	 * <code>2</code>: Ficha existente con investigaci�n epidemiol�gica abierta sin confirmaci�n.<br>
	 * <code>3</code>: Ficha existente con investigaci�n epidemiol�gica abierta con confirmaci�n de diagn�stico. <br>
	 * <code>4</code>: Ficha existente con investigaci�n epidemiol�gica cerrada.  
	 *
	 * @return Lista de {@link MuestreoHematico}
	 */
	public List<MuestreoHematico> ListarPorPuesto(long pPuestoNotificacionId, Integer pAnioEpi, Integer pSituacion, int pPaginaActual, int pTotalPorPagina, int pNumRegistros);

	/**
	 * Contabiliza el n�mero de muestras notificadas por un puesto de notificaci�n
	 * para un a�o epidemiol�gico espec�fico y situaci�n.
	 * 
	 * @param pPuestoNotificacionId
	 * @param pAnioEpi   A�o epidemiol�gico; 0 = sin filtro de a�o epidemiol�gico
	 * @param pSituacion N�mero entero de 1 a 4 que indica el estado o situaci�n del muestreo hem�tico, 0 incluye a todas las situaciones
	 * <p>
	 * <code>0</code>: Todas las situaciones.<br>
	 * <code>1</code>: Ficha existente sin investigaci�n epidemiol�gica asociada.<br>  
	 * <code>2</code>: Ficha existente con investigaci�n epidemiol�gica abierta sin confirmaci�n.<br>
	 * <code>3</code>: Ficha existente con investigaci�n epidemiol�gica abierta con confirmaci�n de diagn�stico. <br>
	 * <code>4</code>: Ficha existente con investigaci�n epidemiol�gica cerrada.  
	 * @return
	 */
	public int ContarPorPuesto(long pPuestoNotificacionId, Integer pAnioEpi, Integer pSituacion);

	/**
	 * Retorna una lista de objetos {@link MuestreoHematico} que notificados por los puestos
	 * de notificaci�n coordinados por la unidad, y aquellos objetos notificados directamente
	 * por dicha �nidad, en caso de que dicha unidad sea puesto de notificaci�n.  Para un a�o 
	 * epidemiol�gico espec�fico y situaci�n.  
	 * 
	 * @param pUnidad Identificador de la unidad de salud coordinadora/puesto
	 * @param pAnioEpi A�o epidemiol�gico al cual corresponde el muestreo hem�tico
	 * @param pSituacion N�mero entero de 1 a 4 que indica el estado o situaci�n del muestreo hem�tico
	 * <p>
	 * <code>1</code>: Ficha existente sin investigaci�n epidemiol�gica asociada.<br>  
	 * <code>2</code>: Ficha existente con investigaci�n epidemiol�gica abierta sin confirmaci�n.<br>
	 * <code>3</code>: Ficha existente con investigaci�n epidemiol�gica abierta con confirmaci�n de diagn�stico. <br>
	 * <code>4</code>: Ficha existente con investigaci�n epidemiol�gica cerrada.  
	 *
	 * @return Lista de {@link MuestreoHematico}
	 */
	public List<MuestreoHematico> ListarPorUnidad(long pUnidadId, Integer pAnioEpi, Integer pSituacion, int pPaginaActual, int pTotalPorPagina, int pNumRegistros);

	/**
	 * Contabiliza el n�mero de muestras notificadas por los puestos de notificaci�n coordinados
	 * por una unidad espec�fica y aquellas notificadas por dicha unidad directamente, en caso que la 
	 * unidad adem�s sea un puesto de notificaci�n, para un a�o epidemiol�gico espec�fico y situaci�n.
	 *
	 * @param pUnidad   Identificador de la unidad de salud
	 * @param pAnioEpi  A�o epidemiol�gico o 0 para todos los a�os
	 * @param pSituacion
	 * <p>
	 * <code>0</code>: Todas las situaciones.<br>
	 * <code>1</code>: Ficha existente sin investigaci�n epidemiol�gica asociada.<br>  
	 * <code>2</code>: Ficha existente con investigaci�n epidemiol�gica abierta sin confirmaci�n.<br>
	 * <code>3</code>: Ficha existente con investigaci�n epidemiol�gica abierta con confirmaci�n de diagn�stico. <br>
	 * <code>4</code>: Ficha existente con investigaci�n epidemiol�gica cerrada.  
	 * @return
	 */
	public int ContarPorUnidad(long pUnidadId, Integer pAnioEpi, Integer pSituacion);
	
	/**
	 * Retorna una lista de los a�os epidemiol�gicos en los cuales existen muestreos hem�ticos
	 * notificados por una unidad de salud, sea esta puesto de notificaci�n o solo coordinadora
	 * de puestos de notificaci�n (colaboradores voluntarios) a partir de un a�o
	 * epidemiol�gico especifico
	 * 
	 * @param pUnidad  Identificador de la unidad de salud
	 * @param pAnioEpi
	 * @return
	 */
	public List<Integer> ListarA�osConMuestreoUnidad(long pUnidadId, Integer pAnioEpi);
	
	/**
	 * Retorna una lista de los a�os epidemiol�gicos en los cuales existen muestreos hem�ticos vinculados
	 * a un puesto de notificaci�n espec�fico a partir de un a�o epidemiol�gico espec�fico
	 * 
	 * @param pPuestoNotificacionId
	 * @param pAnioEpi
	 * @return
	 */
	public List<Integer> ListarA�osConMuestreoPuesto(long pPuestoNotificacionId, Integer pAnioEpi);
	
	/**
	 * Retorna el n�mero de muestreos hem�ticos vinculados a un puesto de notificaci�n
	 * posteriores a la fecha indicada
	 * @param pPuestoNotificacionId  Identificador del puesto de notificaci�n
	 * @param pFecha  Fecha posterior a la cual se contabilizan los muestreos hem�ticos
	 * @param pModo   0=Antes de la Fecha, 1=Despu�s de la Fecha
	 * @return
	 */
	public int ContarPorPuestoFecha(long pPuestoNotificacionId, Date pFecha, int pModo);
}
