package ni.gob.minsa.malaria.soporte;


import java.util.Calendar;
import java.util.Date;

public class CalendarioEPI {

	// ----------------------------------------- Constructor
	
	public CalendarioEPI() {    // clase no instanciable
	}
	
	// ------------------------------------------- Funciones
	

	/**
	 * Retorna la fecha del año epidemiológico más cercana a Enero 01 del año ingresado. Primero
	 * encuentra la fecha 01/01/yyyy y luego se mueve hacia adelante o hacia atrás el número
	 * correcto de días para que sea el inicio de la semana epidemiológica. 
	 * La semana epidemiológica número 1 es siempre la primer semana del año que tiene un mínimo
	 * de 4 días en el año nuevo.  Si el primero de enero cae en Jueves, Viernes o Sábado, la fecha de inicio del calendario
	 * epidemiológico retornada deberá ser mayor que la fecha actual y debe volver a ejecutarse el
	 * método con la nueva fecha.
	 * <p>
	 * Si el primero de enero es un Lunes, Martes o Miércoles, la fecha de inicio del calendario
	 * epidemiológico, retrocede hasta el último Domingo en Diciembre del año anterior, el cual
	 * es el inicio de la semana epidemiológica No.1 para el año ingresado.
	 * <p>
	 * Si el primero de enero es un Jueves, Viernes o Sábado, la fecha de inicio del calendario
	 * epidemiológico se adelanta hasta el primer domingo de Enero del año ingresado, el cual es
	 * el inicio de la semana epidemiológica No.1 para el año ingresado.
	 * <p>
	 * Por ejemplo, si se la fecha ingresada es 02/01/1998, un Viernes, la fecha inicial del
	 * calendario epidemiológico retornada es 04/01/1998, un Domingo.  Como 04/01/1998 es mayor
	 * que 02/01/1998, se debe sustraer un año y pasar al 1 Enero del año resultante a este 
	 * método, ie. 01/01/1997.  El método entonces retornará la fecha que corresponde a la fecha
	 * de inicio de la primer semana epidemiológica del año anterior.
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
	    
	    int iAño = calendarIn.get(Calendar.YEAR);
	    
	    Calendar calendarEnd = Calendar.getInstance();
	    calendarEnd.set(iAño, 11, 31);
	    
	    int iDiaFinAño=calendarEnd.get(Calendar.DAY_OF_WEEK);
	    	
	    if (iDiaFinAño < Calendar.WEDNESDAY) { 
	    	if ((calendarEnd.get(Calendar.DAY_OF_YEAR)-calendarTmpIn.get(Calendar.DAY_OF_YEAR)) < iDiaFinAño) {
	    		calendarTmpIn.set(iAño+1,0,1);
	    	}
	    }
	    
	    Date iFechaIni = fechaInicio(calendarTmpIn.getTime());
	    
	    if (iFechaIni.after(calendarTmpIn.getTime())) {
	    	Calendar oCalendarTmp = Calendar.getInstance();
	    	oCalendarTmp.set(iAño-1,0,1);
	    	iFechaIni = fechaInicio(oCalendarTmp.getTime());
	    }
	    
	    Calendar cFechaIni=Calendar.getInstance();
	    cFechaIni.setTime(iFechaIni);
	    
	    long iDifSemanas=(calendarTmpIn.getTimeInMillis()-cFechaIni.getTimeInMillis())/(7*24 * 60 * 60 * 1000);
	    
	    return (1 + (int)iDifSemanas);

	}

	public static int año(Date pFechaIn) {

    	if (pFechaIn==null) {
    		return 0;
    	}

	    Calendar calendarIn = Calendar.getInstance();
	    calendarIn.setTime(pFechaIn);

        int iSemana = semana(pFechaIn);
        int iAño = calendarIn.get(Calendar.YEAR);
        
        if (iSemana>=52 && (calendarIn.get(Calendar.MONTH)==0)) {
        	iAño=iAño-1;
        }
        
        if (iSemana==1 && (calendarIn.get(Calendar.MONTH)==11)) {
        	iAño=iAño+1;
        }
        
        return iAño;
	}
	
}

