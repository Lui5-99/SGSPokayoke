package dbOperacion;/*
//////////////////////////////////////////////////////////////////////
KMF Tecnología - Saint Gobain Sekurit
Desarrolló:  Joaquín Andrés García Gutierrez
Fecha:       08/01/2018
Descripción: Clase para manejo de variables globales
//////////////////////////////////////////////////////////////////////
*/

public class cGlobales {
    private static cGlobales instance = null;

    private String vUsuario = "";
    private int vActivityOrigen = 0;
    private Boolean vAutorizacion = false;
    private int vIdTarima = 0;
    private String vTarima = "";
    private int vEmbarque = 0;
    private Boolean bEditar = false;
    private Boolean bParcial = false;
    private Boolean vIsSuper = false;
    private Boolean vbEmpAlt = false;
    private String vIdNumeroParte = "";
    private String vEditIdNumeroParte = "";
    private int vCharolas = 0;
    private String LastPrint = "";
    private int vLastTarima = 0;
    private String vLastNP = "";
    private String vSupervisor = "";
    private String vLastNombreTarima = "";

    //Constructor
    protected cGlobales(){

    }
    //Devuelve una instancia
    public static cGlobales getInstance() {
        if (instance == null)
            instance = new cGlobales();
        return instance;
    }
    public void setUsuario(String Usuario){
        vUsuario = Usuario;
    }
    public String getUsuario(){
        return vUsuario;
    }

    //Se utiliza para saber que Activity llama la autorización del Supervisor
    public void setActivityOrigen(int ActividadOrigen) { vActivityOrigen = ActividadOrigen;}
    public int getActivityOrigen() {return vActivityOrigen;}

    //Devuelve si autoriza o rechaza el supervisor
    public void setAutorizacion(boolean Autorizacion) { vAutorizacion = Autorizacion;}
    public boolean getAutorizacion(){return vAutorizacion;}

    public void setIdTarima (int ITarima){vIdTarima = ITarima;}
    public int getIdTarima(){return vIdTarima;}

    public void setTarima (String Tarima) {vTarima = Tarima;}
    public String getTarima() {return vTarima;}

    public void setEmbarque(int IEmbarque) {vEmbarque = IEmbarque;}
    public int getEmbarque() {return vEmbarque;}

    public void setEditar(boolean Bandera) {bEditar = Bandera;}
    public Boolean getEditar() {return bEditar;}

    public void setIdNumeroParte (String NumeroParte){vIdNumeroParte = NumeroParte;}
    public String getIdNumeroParte(){return vIdNumeroParte;}

    public void setEditIdNumeroParte (String NumeroParte){vEditIdNumeroParte = NumeroParte;}
    public String getEditIdNumeroParte(){return vEditIdNumeroParte;}

    public void setCharolas(int iCantidad) {vCharolas = iCantidad;}
    public int getCharolas() {return vCharolas;}

    public void setLastPrint (String SLabel){LastPrint = SLabel;}
    public String getLastPrint(){return LastPrint;}

    public void setLastTarima (int Tarima) {vLastTarima = Tarima;}
    public int getLastTarima() {return vLastTarima;}

    public void setLastNP (String NP) {vLastNP = NP;}
    public String getLastNP() {return vLastNP;}

    public void setLastNombreTarima (String Tarima) {vLastNombreTarima = Tarima;}
    public String getLastNombreTarima() {return vLastNombreTarima;}

    public void setCierreParcial (boolean Cierre) {bParcial = Cierre;}
    public boolean getCierreParcial() {return bParcial;}

    public void setSuper (String Supervisor) {vSupervisor = Supervisor;}
    public String getSuper() {return vSupervisor;}

    public void setIsSuper (boolean Bandera) {vIsSuper = Bandera;}
    public boolean getIsSuper() {return vIsSuper;}

    public void setIsEmpAlt (boolean Bandera) {vbEmpAlt = Bandera;}
    public boolean getIsEmpAlt() {return vbEmpAlt;}
}
