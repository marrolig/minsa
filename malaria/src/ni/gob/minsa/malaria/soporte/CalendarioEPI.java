package ni.gob.minsa.malaria.soporte;


import java.util.Calendar;
import java.util.Date;

public class CalendarioEPI {

	// ----------------------------------------- Constructor
	
	public CalendarioEPI() {    // clase no instanciable
	}
	
	// ------------------------------------------- Funciones
	

	/**
	 * Retorna la fecha del a�o epidemiol�gico m�s cercana a Enero 01 del a�o ingresado. Primero
	 * encuentra la fecha 01/01/yyyy y luego se mueve hacia adelante o hacia atr�s el n�mero
	 * correcto de d�as para que sea el inicio de la semana epidemiol�gica. 
	 * La semana epidemiol�gica n�mero 1 es siempre la primer semana del a�o que tiene un m�nimo
	 * de 4 d�as en el a�o nuevo.  Si el primero de enero cae en Jueves, Viernes o S�bado, la fecha de inicio del calendario
	 * epidemiol�gico retornada deber� ser mayor que la fecha actual y debe volver a ejecutarse el
	 * m�todo con la nueva fecha.
	 * <p>
	 * Si el primero de enero es un Lunes, Martes o Mi�rcoles, la fecha de inicio del calendario
	 * epidemiol�gico, retrocede hasta el �ltimo Domingo en Diciembre del a�o anterior, el cual
	 * es el inicio de la semana epidemiol�gica No.1 para el a�o ingresado.
	 * <p>
	 * Si el primero de enero es un Jueves, Viernes o S�bado, la fecha de inicio del calendario
	 * epidemiol�gico se adelanta hasta el primer domingo de Enero del a�o ingresado, el cual es
	 * el inicio de la semana epidemiol�gica No.1 para el a�o ingresado.
	 * <p>
	 * Por ejemplo, si se la fecha ingresada es 02/01/1998, un Viernes, la fecha inicial del
	 * calendario epidemiol�gico retornada es 04/01/1998, un Domingo.  Como 04/01/1998 es mayor
	 * que 02/01/1998, se debe sustraer un a�o y pasar al 1 Enero del a�o resultante a este 
	 * m�todo, ie. 01/01/1997.  El m�todo entonces retornar� la fecha que corresponde a la fecha
	 * de inicio de la primer semana epidemiol�gica del a�o anterior.
	 * 
	 */
	private static Date fechaInicio(Date pFechaIn) {
		
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(pFechaIn);
	    calendar.set(calendar.get(Calendar.YEAR), 0, 1);
	    if (calendar.get(Calendar.DAY_OF_WEEK) <= Calendar.WEDNESDAY) {
	    	calendar.add(Calendar.DAY_OF_MONTH, -(calendar.get(Calendar.DAY_OF_WEEK)-1));
	    } else {
	    	calendar.add(Calendar.DAY_OF_MONTH, ((7-calendar.get(Calendar.DAY_OF_WEEK))+1));
	    }

	    return calendar.getTime();
	}
	
	public static int semana(Date pFechaIn) {

    	if (pFechaIn==null) {
    		return 0;
    	}

	    Calendar calendarIn = Calendar.getInstance();
	    calendarIn.setTime(pFechaIn);
	    
	    Calendar calendarTmpIn = Calendar.getInstance();
	    calendarTmpIn.setTime(pFechaIn);
	    
	    int iA�o = calendarIn.get(Calendar.YEAR);
	    
	    Calendar calendarEnd = Calendar.getInstance();
	    calendarEnd.set(iA�o, 11, 31);
	    
	    int iDiaFinA�o=calendarEnd.get(Calendar.DAY_OF_WEEK);
	    	
	    if (iDiaFinA�o < Calendar.WEDNESDAY) { 
	    	if ((calendarEnd.get(Calendar.DAY_OF_YEAR)-calendarTmpIn.get(Calendar.DAY_OF_YEAR)) < iDiaFinA�o) {
	    		calendarTmpIn.set(iA�o+1,0,1);
	    	}
	    }
	    
	    Date iFechaIni = fechaInicio(calendarTmpIn.getTime());
	    
	    if (iFechaIni.after(calendarTmpIn.getTime())) {
	    	Calendar oCalendarTmp = Calendar.getInstance();
	    	oCalendarTmp.set(iA�o-1,0,1);
	    	iFechaIni = fechaInicio(oCalendarTmp.getTime());
	    }
	    
	    Calendar cFechaIni=Calendar.getInstance();
	    cFechaIni.setTime(iFechaIni);
	    
	    long iDifSemanas=(calendarTmpIn.getTimeInMillis()-cFechaIni.getTimeInMillis())/(7*24 * 60 * 60 * 1000);
	    
	    return (1 + (int)iDifSemanas);

	}

	public static int a�o(Date pFechaIn) {

    	if (pFechaIn==null) {
    		return 0;
    	}

	    Calendar calendarIn = Calendar.getInstance();
	    calendarIn.setTime(pFechaIn);

        int iSemana = semana(pFechaIn);
        int iA�o = calendarIn.get(Calendar.YEAR);
        
        if (iSemana>=52 && (calendarIn.get(Calendar.MONTH)==0)) {
        	iA�o=iA�o-1;
        }
        
        if (iSemana==1 && (calendarIn.get(Calendar.MONTH)==11)) {
        	iA�o=iA�o+1;
        }
        
        return iA�o;
	}
	
}

