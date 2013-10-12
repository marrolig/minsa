package ni.gob.minsa.malaria.interfaz.sis;

import javax.el.ELResolver;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class PersonaListener implements ActionListener {
	
	@Override
	public void processAction(ActionEvent event)
	         throws AbortProcessingException {
		
		System.out.println("---------------");
		System.out.println("Dentro del Listener");
		System.out.println("---------------");
	    FacesContext c = FacesContext.getCurrentInstance();
	    ELResolver elResolver = c.getApplication().getELResolver();
	    PersonaBean pb = (PersonaBean)elResolver.getValue(c.getELContext(), null, "personaBean");
	    System.out.println(pb);
	    pb.iniciarPropiedades();
	}
}
