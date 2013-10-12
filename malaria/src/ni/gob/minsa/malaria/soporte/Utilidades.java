package ni.gob.minsa.malaria.soporte;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.faces.context.FacesContext;

import ni.gob.minsa.ciportal.dto.InfoSesion;

public class Utilidades {

	public static final String CODIGO_SISTEMA="malaria";
	public static final String SEXO_MASCULINO="SEXO|M";
	public static final String SEXO_FEMENINO="SEXO|F";
	public static final String PAIS_CODIGO="NI";
	
	public static final String PUESTO_NOTIFICACION_UNIDAD="U";
	public static final String PUESTO_NOTIFICACION_COLVOL="C";
	
	public static final int TIPO_BUSQUEDA_ACTIVA=1;
	public static final int TIPO_BUSQUEDA_PASIVA=0;
	
	public static final BigDecimal MANEJO_CLINICO_AMBULATORIO=new BigDecimal(1);
	public static final BigDecimal MANEJO_CLINICO_HOSPITALARIO=new BigDecimal(0);
	
	public static final BigDecimal POSITIVO=new BigDecimal(1);
	public static final BigDecimal NEGATIVO=new BigDecimal(0);
	
	public static final BigDecimal CERO=new BigDecimal(0);
	
	public static final String ES_PUESTO_NOTIFICACION="ePN";
	public static final String DECLARA_MUESTREO_HEMATICO="dMxHem";
	
	// ----------------------------------------- Constructor
	
	public Utilidades() {    // clase no instanciable
	}
	
	// ------------------------------------------- Funciones
	
	/**
	 * Convierte una cadena con valores 1 o 0 a su valor
	 * booleano (verdadero o falso respectivamente).
	 * <p>
	 * Si la cadena contiene un valor diferenet a 1 o 0
	 * retornará un valor false
	 * 
	 * @param pString Cadena a ser convertida
	 * @return Valor booleando de la cadena
	 */
	public static boolean CadenaABooleano(String pString) {
		
		Boolean result=false;
		
		if (pString.equals("1")) {
			result=true;
		}
		return result;
	}
	
	/**
	 * Convierte el valor booleano a una cadena con valores
	 * 1 o 0 (verdadero o falso, respectivamente)
	 * 
	 * @param pBooleano Valor booleando a convertir a cadena
	 * @return Cadena con el valor 0 o 1 (falso o verdadero)
	 */
	public static String BooleanoACadena(boolean pBooleano) {
		String result="0";
		if (pBooleano) {
			result="1";
		}
		return result;
	}
	/**
	 * Retorna el período actual, el cual se representa por el
	 * año concatenado con el mes, es decir AAAAMM donde AAAA 
	 * es el año y MM es el mes.
	 * 
	 * @return Número entero que representa el período actual en formato AAAAMM
	 */
	public static int PeriodoActual() {
		Calendar iCalendar = Calendar.getInstance();
		return Integer.valueOf(Integer.toString(iCalendar.get(Calendar.YEAR))+String.format("%02d", iCalendar.get(Calendar.MONTH)+1));
	}
	/**
	 * Retorna el objeto {@link Mes} con el mes actual del sistema
	 * 
	 * @return Objeto {@link Mes}
	 */
	public static Mes MesActual() {
		Calendar iCalendar = Calendar.getInstance();
		
		for (Mes pMes : Mes.VALORES) {
			if (pMes.getCodigo().equals(String.format("%02d", iCalendar.get(Calendar.MONTH)+1))) {
				return pMes;
			}
		}
		return null;
	}
	/**
	 * Retorna el año actual del sistema, con el formato AAAA.
	 * 
	 * @return Entero con el año del sistema
	 */
	public static Integer AñoActual() {
		Calendar iCalendar = Calendar.getInstance();
		
		return Integer.valueOf(iCalendar.get(Calendar.YEAR));
	}
	
	/**
	 * Obtiene un vector de años.  Si el año establecido es declarado
	 * nulo, el vector iniciará a partir del año actual y se agregará
	 * el siguiente año.  Si el año establecido no es nulo e inferior
	 * al año actual, el vector iniciará a partir del año establecido
	 * y finalizará en un año posterior al actual.
	 * 
	 * @param pAñoEstablecido Objeto Entero con el año establecido
	 * @param pAñoSiguiente   Valor booleano que indica si se agregará o no el año siguiente
	 * @return Arreglo de Enteros contiendo los años
	 */
	public static Map<Integer,Integer> ObtenerAños(Integer pAñoEstablecido,boolean pAñoSiguiente) {
		SortedMap<Integer,Integer> iAños=new TreeMap<Integer,Integer>();
		int iAñoActual=Integer.valueOf(Utilidades.AñoActual());

		if (pAñoEstablecido!=null) {
			int iAño=Integer.valueOf(pAñoEstablecido);
			if (iAño<iAñoActual) {
				for(int i=iAño;i<iAñoActual;i++) {
					iAños.put(Integer.valueOf(i),Integer.valueOf(i));
				}
			}
		}
		iAños.put(Integer.valueOf(iAñoActual),Integer.valueOf(iAñoActual));
		if (pAñoSiguiente) {
			iAños.put(Integer.valueOf(iAñoActual+1),Integer.valueOf(iAñoActual+1));
		}
		
		/*Comparator<Integer> iComparador = new Comparator<Integer>() { 
            @Override 
            public int compare(Integer o1, Integer o2) { 
                    return o1.compareTo(o2); 
            }}; */

        //Map<Integer, Integer> iOrdenado = new TreeMap<Integer, Integer>(iComparador);
        // iOrdenado.putAll(iAños);
		return iAños;
	}

	/**
	 * Elimina una variable de sesion que inicia con 
	 * el nombre con el cual se identificada
	 * 
	 * @param pNombreSesion Cadena de caracteres con el nombre de la variable de sesión
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void EliminarSesion(String pNombreSesion) {
		
		FacesContext oFacesContext = FacesContext.getCurrentInstance();
		Map oSessionMap= oFacesContext.getExternalContext().getSessionMap();
		for (Iterator iter = oSessionMap.keySet().iterator(); iter.hasNext();) {
			   Object key = (Object) iter.next();
			   if (key.toString().startsWith(pNombreSesion)) {
				   oSessionMap.put(key, null);
				   // oSessionMap.remove(key);
			   }
		}
	}

	/**
	 * Retorna el objeto {@link InfoSesion} con los datos del usuario
	 * y sistema a la cual pertenece la sesión y que ha sido autorizada.
	 * La información de la sesión se encuentra almacenada en una
	 * variable de sesión inicializada al establecer el marco de trabajo
	 * en el MainBean.
	 * 
	 * @return Objeto InfoSesion
	 */
	public static InfoSesion obtenerInfoSesion() {
		
		FacesContext oFacesContext = FacesContext.getCurrentInstance();
        return (InfoSesion)oFacesContext.getExternalContext().getSessionMap().get("usuarioActual");

	}
	
	/**
	 * Convierte una cadena de texto bajo un formato especíco
	 * a un objeto fecha.  Retorna <code>null</code> si se ha producido
	 * un error en la conversión
	 * 
	 * @param pFechaTexto
	 * @param pFormato
	 */
	public static Date textoAFecha(String pFechaTexto, String pFormato) {
		
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat(pFormato);
		Date iFecha = null;
		try {
		    iFecha = formatoDelTexto.parse(pFechaTexto);
		    return iFecha;
		} catch (ParseException ex) {
			return null;
		}
	}
	
	public static Integer stringAEntero(String pTexto) {

		Integer iValor=null;
		try {
			iValor=Integer.parseInt(pTexto);
			return iValor;
		} catch (NumberFormatException nfe){
			return iValor;
		}
	}
	
	public static Integer calcularEdadEnAnios(Date pFechaNac){
		  try{
			  Calendar hoy = Calendar.getInstance();
			  Calendar fdn = Calendar.getInstance();
			  fdn.setTime(pFechaNac);
			  
			  int age = hoy.get(Calendar.YEAR) - fdn.get(Calendar.YEAR);
			  if (hoy.get(Calendar.MONTH) < fdn.get(Calendar.MONTH)) {
			        age--;
			  } else if(hoy.get(Calendar.MONTH) == fdn.get(Calendar.MONTH)) {
				  if (hoy.get(Calendar.DAY_OF_MONTH) < fdn.get(Calendar.DAY_OF_MONTH)) {
					  age--;
			      }
			  }

			  return new Integer(age);
		  }catch (Exception e) {
			System.out.println("---------------------------- EXCEPCION");
	        System.out.println("Error no controlado: " + e.toString());
			return null;
		}
	  }
	
	public static boolean compararCadenas(String pCadena1, String pCadena2) {
		
		if (pCadena1!=null && pCadena2!=null) return pCadena1.trim().equals(pCadena2.trim());
		if (pCadena1==null && pCadena2!=null && !pCadena2.trim().isEmpty()) return false;
		if (pCadena2==null && pCadena1!=null && !pCadena1.trim().isEmpty()) return false;
		return true;
	}
	
	public static boolean esEntero(String pCadena) {
		
		   try {  
		      Integer.parseInt(pCadena);  
		      return true;  
		   }
		   catch(NumberFormatException e) {
			   return false;
		   }
		   catch(Exception e) {  
		      return false;  
		   }  
	}
}

