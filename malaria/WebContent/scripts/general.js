// ---------------------------------------------------------
// arSoft
// Marlon Arroliga Téllez
// 
// Funciones Generales
// Fecha: 19/12/2011
// ---------------------------------------------------------

//==========================================================
// Valida la longitud máxima de una cadena de caracteres
//==========================================================
function ValidarCaracteres(control,maxlength)
{
    if (control.value.length > maxlength)
    {
        control.value = control.value.substring(0,maxlength);
        alert("Solo se permite ingresar hasta un maximo de "+maxlength+" caracteres");
    }
}

//===========================================================
// Valida que la tecla ENTER no realice la función habitual
//===========================================================
function ValidarEnter(control, event)
{
    tecla=event.keyCode ? event.keyCode : event.which ? event.which : event.charCode; 
    if(tecla == 13)
        return false;
        
    return true;
}

//===========================================================
// Establece el foco sobre el elemento enviado por parámetro
//===========================================================
function setFocus(str){
	jQuery('[id="'+str+'"]').focus();
}

//===========================================================
//Función a ser llamada en el evento onkeypress para permitir
//únicamente números enteros
//===========================================================
function validateOnlyNumber(evt){
	evt = (evt) ? evt : event;
	var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode : ((evt.which) ? evt.which : 0));
	if(charCode > 31 && (charCode < 48 || charCode > 57)) {
		return false;
	}
	return true;
}

//===========================================================
//Función a ser llamada en el evento onkeypress para permitir
//números con decimales
//===========================================================
function validateNumberWithDecimal(evt){
	evt = (evt) ? evt : event;
	var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode : ((evt.which) ? evt.which : 0));
	if(charCode > 31 && (charCode < 48 || charCode > 57)) {
		if(charCode==46){
	    	var componentValue = jQuery('[id='+jQuery(evt.target).attr('id')+']').attr('value');
	        if(componentValue.indexOf('.') < 0 && componentValue.length > 0){return true;}
	    }
		return false;
	}
	return true;
}

//=================================================================
//Deshabilita el botón derecho
//=================================================================
function right(e) 
{
	return true;
	if (navigator.appName == 'Netscape' && 
	(e.which == 3 || e.which == 2))
	return false;
	else if (navigator.appName == 'Microsoft Internet Explorer' && (event.button == 2 || event.button == 3)) {
	alert("Función Deshabilitada");
	return false;
	}
return true;
}

document.onmousedown=right;
if (document.layers) window.captureEvents(Event.MOUSEDOWN);
window.onmousedown=right;

//==== *** fin de función para deshabilitar botón derecho *** =====

//============================================================
// Realiza una búsqueda binaria en el dropdownlist y retorna
// el índice del elemento que concuerda
//============================================================

function SmartSelect(searchString, oSelect)
{
    var sInput = String(searchString).toUpperCase();
    var iLength = sInput.length;

    if (iLength <= 0)
        return -1;

    var oOptions = oSelect.options;
    var i, diff, bFound, sTemp;

    var iHigh = oOptions.length - 1;
    var iLow = 0;
    var iCurrent = Math.floor((iHigh + 1) / 2);

    bFound = false;
    do
    {
        // Obtiene la opción actual

        sTemp = oOptions(iCurrent).text.toUpperCase();
        var sSubstr = sTemp.substr(0, iLength);

        if (sSubstr < sInput)
        {
            // Busca en la mitad superior de elementos
            iLow = iCurrent + 1;
        }
        else if (sSubstr > sInput)
        {
            // Busca en la mitad inferior de elementos
            iHigh = iCurrent - 1;
        }
        else
        {
            bFound = true;
            break;
        }
        
        // Toma la mitad de elementos nuevamente
        iCurrent = Math.floor(iLow + ((iHigh + 1) - iLow) / 2);

    } while (iHigh >= iLow);

    // Hay una mejor concordancia del texto ingresado?
    if (iLength < sTemp.length)
    {
        // Almacena el valor anterior actual
        var iOld = iCurrent--;

        // Ahora retorna hasta encontra uno que no concuerde con el texto

        while (iCurrent >= 0)
        {
            // No se encontró ninguna concordancia
            if (oOptions(iCurrent).value.toUpperCase().substr(0,iLength) != sInput)
                break;

            iOld = iCurrent--;
        }

        iCurrent = iOld;
    }

    if (bFound)
        return iCurrent;
    else
        return -1;
        
}

// variables globales

var searchString="";
var lastSearch="";
var lastObjID="";

//=====================================================
// Reinicia la búsqueda si el texto no ha sido cambiado
// en los últimos 2 segundos
//=====================================================
function ClearSearchString()
{
    date= new Date();
    var tdiff= date.valueOf() - lastSearch;

    if(tdiff<1000*2)
    {
        setTimeout("ClearSearchString();",1000);
    }
    else
    {
        searchString="";
    }
}

//=========================================================
// Pulsa la selección de un elemento en particular dentro
// del dropdownlist el cual fue afectado por el desplazamiento
//=========================================================
function PulseSelection(oOption)
{
    var bSelected=oOption.selected;

    oOption.selected=false;
    oOption.selected=true;
    oOption.selected=bSelected;
}

//=========================================================
// Llamado siempre que una tecla es presionada para la
// búsqueda de un objeto
//=========================================================
function SmartSearch_ListKeyDown(lb)
{
    if(lastObjID!=lb.id)
    { 
        // busca un diferente control para reiniciar
        // la búsqueda

        lastObjID=lb.id;
        ClearSearchString();
    }

    // verifica si un tecla "especial" y la envía al objeto
    // para manejarla

    switch(event.keyCode)
    {
        case 32:
            if(searchString.length<=0)return true; //tecla space
            break;
        case 38://cursor up - tecla hacia arriba
        case 40://cursor down - tecla hacia abajo
            return true;
    }

    searchString += String.fromCharCode(event.keyCode);

    var i = SmartSelect( searchString, lb);
    if(i>=0)
    {
        PulseSelection(lb.options[i]);
        date= new Date();
        lastSearch = date.valueOf();
    }

    setTimeout("ClearSearchString();",1000);

    return(false); //retorna false para detener la funcionalidad del keypress por omisión
}

//====== *** fin de funcionalidad del dropdown list *** ===========

//=================================================================
// Abre una ventana del navegador (pop-up), centrada
//=================================================================
function abrirVentana(pURL,pNombre,pAnchoVent,pAltoVent) {

	iPosIzq=(window.screen.width-pAnchoVent)/2;
	iPosAlt=(window.screen.height-pAltoVent)/2;
	var W=window.open(pURL,pNombre,"height="+pAltoVent+",width="+pAnchoVent+",status=no,toolbar=no,menubar=no,location=no,left="+iPosIzq+",top="+iPosAlt);
	W.focus();
	
}

//=================================================================
// Verifica si existe el contenedor del aplicativo
//=================================================================
function checkContainer() {
	if (top.document.getElementById('ifrMain')==null) {
		top.document.location.replace('/PAI/redirect.xhtml');
	}
}
