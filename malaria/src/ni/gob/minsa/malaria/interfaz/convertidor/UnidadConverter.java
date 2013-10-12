// -----------------------------------------------
// UnidadConverter.java
// -----------------------------------------------
package ni.gob.minsa.malaria.interfaz.convertidor;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.estructura.UnidadDA;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;
import ni.gob.minsa.malaria.servicios.estructura.UnidadService;


/**
 * Conversor vinculado a la clase {@link Unidad} y que sirve de soporte
 * a la capa de interfaz. 
 *
 * <p>
 * @author Marlon Arróliga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 17/02/2011
 * @since jdk1.6.0_21
 */
@FacesConverter(value="unidadConverter")
public class UnidadConverter implements Converter {

	// ------------------------------------------- Acciones

	/**
	 * Obtiene el actual objeto Unidad a partir de la conversión de la única representación
	 * de Cadena de Texto (String) de dicho objeto almacenado en el valor enviado

	 * @param pContexto  Contexto de la acción
	 * @param pComponente Componente implicado en la acción
	 * @param pValorEnviado Representación en cadena de texto del Objeto GrupoEconomico actual
	 * @return Objeto actual representado por la cadena de texto
	 */	
	public Object getAsObject(FacesContext pContexto, UIComponent pComponente, String pValorEnviado) {
		
		if (pValorEnviado.trim().equals("")) {
			return null;
		} 
		else {
			try {
				UnidadService unidadService=new UnidadDA();
				InfoResultado oResultado=unidadService.Encontrar(Long.parseLong(pValorEnviado));
				if (oResultado.isOk()) {
					return ((Unidad)oResultado.getObjeto());
				}
				else {
					throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Conversión", "No es una unidad de salud válida")); 
				}
			} catch(NumberFormatException exception) {
		    	throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Conversión", "No es una unidad de salud válida"));
			}
		}
	}
	/**
	 * Convierte el objeto {@link Unidad} a su única representación en
	 * cadena de texto (String)
	 * 
	 * @param pContexto  Contexto de la acción
	 * @param pComponente Componente implicado en la acción
	 * @param pValor Objeto a convertir a su representación de cadena de texto
	 * @return Cadena de texto que representa al objeto actual
	 */
	public String getAsString(FacesContext pContexto, UIComponent pComponente, Object pValor) {
		if (pValor == null) {
			return null;
		} 
		else {
			System.out.println(pValor);
			return String.valueOf(((Unidad) pValor).getUnidadId());
		}
	}
}
