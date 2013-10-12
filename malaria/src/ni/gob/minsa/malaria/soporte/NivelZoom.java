// -----------------------------------------------
// NivelZoom.java
// -----------------------------------------------
package ni.gob.minsa.malaria.soporte;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.general.ParametroDA;
import ni.gob.minsa.malaria.modelo.general.Parametro;
import ni.gob.minsa.malaria.servicios.general.ParametroService;

/**
* Clase que implementa la lista de valores para los niveles
* de acercamiento o zoom a ser utilizados en la visualización
* del mapa
*
* <p>
* @author Marlon Arróliga
* @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
* @version 1.0, &nbsp; 18/06/2012
* @since jdk1.6.0_21
*/
public class NivelZoom {

	public int PAIS;
	public int DEPARTAMENTO;
	public int MUNICIPIO;
	public int UNIDAD;
	public int COMUNIDAD;
	public static final int ESTANDAR=7;

	private static ParametroService parametroService = new ParametroDA();

	public NivelZoom() {
		
		InfoResultado oResultadoParametro = new InfoResultado();
		
		Integer iValor=null;
			
		oResultadoParametro=parametroService.Encontrar("ZOOM_MAPA_PAIS");
		if (oResultadoParametro!=null && oResultadoParametro.isOk()) {
				Parametro oParametro=(Parametro)oResultadoParametro.getObjeto();
				iValor=Utilidades.stringAEntero(oParametro.getValor());
				PAIS=iValor!=null?iValor.intValue():ESTANDAR;
		} else {
				PAIS=ESTANDAR;
		}
			
		oResultadoParametro=parametroService.Encontrar("ZOOM_MAPA_DEPTO");
		if (oResultadoParametro!=null && oResultadoParametro.isOk()) {
				Parametro oParametro=(Parametro)oResultadoParametro.getObjeto();
				iValor=Utilidades.stringAEntero(oParametro.getValor());
				DEPARTAMENTO=iValor!=null?iValor.intValue():ESTANDAR;
		} else {
				DEPARTAMENTO=ESTANDAR;
		}
			
		oResultadoParametro=parametroService.Encontrar("ZOOM_MAPA_MUNICIPIO");
		if (oResultadoParametro!=null && oResultadoParametro.isOk()) {
				Parametro oParametro=(Parametro)oResultadoParametro.getObjeto();
				iValor=Utilidades.stringAEntero(oParametro.getValor());
				MUNICIPIO=iValor!=null?iValor.intValue():ESTANDAR;
		} else {
				MUNICIPIO=ESTANDAR;
		}
			
		oResultadoParametro=parametroService.Encontrar("ZOOM_MAPA_UNIDAD");
		if (oResultadoParametro!=null && oResultadoParametro.isOk()) {
				Parametro oParametro=(Parametro)oResultadoParametro.getObjeto();
				iValor=Utilidades.stringAEntero(oParametro.getValor());
				UNIDAD=iValor!=null?iValor.intValue():ESTANDAR;
		} else {
				UNIDAD=ESTANDAR;
		}

		oResultadoParametro=parametroService.Encontrar("ZOOM_MAPA_COMUNIDAD");
		if (oResultadoParametro!=null && oResultadoParametro.isOk()) {
				Parametro oParametro=(Parametro)oResultadoParametro.getObjeto();
				iValor=Utilidades.stringAEntero(oParametro.getValor());
				COMUNIDAD=iValor!=null?iValor.intValue():ESTANDAR;
		} else {
				COMUNIDAD=ESTANDAR;
		}

	}
}
