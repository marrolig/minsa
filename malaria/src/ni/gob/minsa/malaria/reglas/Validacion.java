package ni.gob.minsa.malaria.reglas;

import java.math.BigDecimal;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.modelo.estructura.Unidad;

public class Validacion {

	public static InfoResultado coordenadas(BigDecimal pLatitud, BigDecimal pLongitud) {
		
		InfoResultado oResultado=new InfoResultado();
		if (pLatitud==null && pLongitud==null) {
			oResultado.setOk(true);
			return oResultado;
		}
		
		if (pLatitud!=null && pLongitud!=null) {
			// no se valida el ranto de latitud y longitud para el país,
			// colocar aquí si es requerido
			
			if ((pLatitud.compareTo(new BigDecimal(-90.0))<0) || (pLatitud.compareTo(new BigDecimal(90.0))>0)) {
				oResultado.setOk(false);
				oResultado.setMensaje("La latitud debe estar entre -90 y 90 grados inclusive");
				return oResultado;
			}
			
			if ((pLongitud.compareTo(new BigDecimal(-180.0))<0) || (pLongitud.compareTo(new BigDecimal(180.0))>0)) {
				oResultado.setOk(false);
				oResultado.setMensaje("La longitud debe estar entre -180 y 180 grados inclusive");
				return oResultado;
			}
			
			oResultado.setOk(true);
			return oResultado;
		}
		
		oResultado.setOk(false);
		oResultado.setMensaje("Deben declararse ambas coordenadas geográficas");
		return oResultado;
	}
	
}
