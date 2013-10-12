// -----------------------------------------------
// PropiedadUnidadService.java
// -----------------------------------------------

package ni.gob.minsa.malaria.servicios.seguridad;

/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en PropiedadUnidadDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 26/12/2012
 * @since jdk1.6.0_21
 */
public interface PropiedadUnidadService {
	
	/**
	 * Retorna <code>true</code> si la unidad de salud está vinculada
	 * a una propiedad o característica funcional. <code>false</code> caso
	 * contrario. 
	 * 
	 * @return boolean 
	 */
	public boolean TienePropiedad(long pUnidadId, String pPropiedad); 

}
