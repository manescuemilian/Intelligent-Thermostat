/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package termostat;

/**
 * Camera, ce contine un senzor
 * @author Emilian
 */
public class Camera {
    private String numeCamera;
    private int suprafata;
    Senzor senzor;

    /**
     * Metoda de setare a numelui camerei
     * @param numeCamera Numele camerei
     */
    public void setNumeCamera(String numeCamera) {
        this.numeCamera = numeCamera;
    }

    /**
     * Metoda de setare a suprafetei camerei
     * @param suprafata Suprafata camerei
     */
    public void setSuprafata(int suprafata) {
        this.suprafata = suprafata;
    }

    /**
     * Metoda de obtinere a numelui camerei
     * @return Numele camerei
     */
    public String getNumeCamera() {
        return numeCamera;
    }

    /**
     * Metoda de obtinere a suprafetei camerei
     * @return Suprafata camerei
     */
    public int getSuprafata() {
        return suprafata;
    }
    
    /** 
     * Constructor de setare a parametrilor camerei
     * @param numeCamera Numele camerei
     * @param idSenzor Id-ul senzorului 
     * @param suprafata Suprafata camerei
     */
    public Camera(String numeCamera, String idSenzor, int suprafata) {
        this.numeCamera= numeCamera;
        this.suprafata = suprafata;
        this.senzor = new Senzor(idSenzor);
    }
}
