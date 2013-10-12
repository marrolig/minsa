// -----------------------------------------------
// SisPersonaDA.java
// -----------------------------------------------
package ni.gob.minsa.malaria.datos.sis;

import javax.persistence.EntityManager;

import ni.gob.minsa.ciportal.dto.InfoResultado;
import ni.gob.minsa.malaria.datos.JPAResourceBean;
import ni.gob.minsa.malaria.modelo.sis.SisPersona;
import ni.gob.minsa.malaria.servicios.sis.SisPersonaService;
import ni.gob.minsa.malaria.soporte.Mensajes;



/**
 * Acceso de Datos para la entidad {@link SisPersona}.
 *
 * La persistencia es accedida mediante JPAResourceBean, la cual ser�
 * inyectada con un JSF managed bean.
 * <p>
 * @author Marlon Arr�liga
 * @author <a href=mailto:marrolig@hotmail.com>marrolig@hotmail.com</a>
 * @version 1.0, &nbsp; 09/08/2012
 * @since jdk1.6.0_21
 */
public class SisPersonaDA implements SisPersonaService {
	//almacena una referencia al EMF global para adquirir el EntityManager
    private static JPAResourceBean jpaResourceBean = new JPAResourceBean();

    public SisPersonaDA() {  // clase no instanciable
    }
    
    @Override
    public InfoResultado Encontrar(long pSisPersonaId) {
        
        InfoResultado oResultado=new InfoResultado();
    	EntityManager oEM= jpaResourceBean.getEMF().createEntityManager();
    	try{
    		SisPersona oSisPersona = (SisPersona)oEM.find(SisPersona.class, pSisPersonaId);
    		if (oSisPersona!=null) {
        		oEM.refresh(oSisPersona);
    			oResultado.setFilasAfectadas(1);
    			oResultado.setOk(true);
    			oResultado.setObjeto((Object)oSisPersona);
    			return oResultado;
    		}
    		else {
    			oResultado.setMensaje(Mensajes.ENCONTRAR_REGISTRO_NO_EXISTE);
    			oResultado.setOk(false);
    			oResultado.setFilasAfectadas(0);
    			return oResultado;
    		}
    	}
    	catch (Exception iExcepcion) {
    		oResultado.setExcepcion(true);
    		oResultado.setMensaje(Mensajes.ERROR_NO_CONTROLADO + iExcepcion.getMessage());
    		oResultado.setFuenteError(iExcepcion.toString().split(":",1).toString());
    		oResultado.setOk(false);
    		oResultado.setGravedad(InfoResultado.SEVERITY_FATAL);
    		oResultado.setFilasAfectadas(0);
    		return oResultado;
    	}
    	finally{
    		oEM.close();
    	}
    }
}
