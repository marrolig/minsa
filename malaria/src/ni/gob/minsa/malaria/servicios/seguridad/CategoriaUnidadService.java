package ni.gob.minsa.malaria.servicios.seguridad;

import java.math.BigDecimal;


/**
 * Esta clase define el interface para las operaciones a ser
 * implementadas en CategoriaUnidadDA.
 * La implementación de este servicio accede a la capa
 * de persistencia y proporciona el llamado a la base de
 * datos.
 * <p>
 * @author Felix Medina
 * @author <a href=mailto:medina.fx@gmail.com>medina.fx@gmail.com</a>
 * @version 1.0, &nbsp; 03/11/2013
 * @since jdk1.6.0_21
 */
public interface CategoriaUnidadService {
	/**
	 * Retorna <code>true</code> si la unidad de salud está vinculada
	 * a una categoría. <code>false</code> caso
	 * contrario. 
	 * 
	 * @return boolean 
	 */
	public boolean TieneCategoria(long pUnidadId, BigDecimal pCategoria); 
}
