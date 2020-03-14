/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package termostat;
import java.util.*;
import java.io.*;

/**
 * Sistemul care implementeaza comenzile dorite
 * @author Emilian
 */
public class Sistem {
    private int timestampRef;
    private int numarCamere;
    private double tempGlobala;
    private double umidGlobala = -1;
    ArrayList<Camera> camere;

    /**
     * Metoda de setare a timestamp-ului de referinta
     * @param timestampRef Timestamp-ul de referinta
     */
    public void setTimestampRef(int timestampRef) {
        this.timestampRef = timestampRef;
    }

    /**
     * Metoda de setare a numarului de camere
     * @param numarCamere Numarul de camere
     */
    public void setNumarCamere(int numarCamere) {
        this.numarCamere = numarCamere;
    }

    /**
     * Metoda de setare a temperaturii globale
     * @param tempGlobala Temperatura globala
     */
    public void setTempGlobala(double tempGlobala) {
        this.tempGlobala = tempGlobala;
    }

    /**
     * Metoda de setare a umiditatii globale
     * @param umidGlobala Umiditatea globala
     */
    public void setUmidGlobala(double umidGlobala) {
        this.umidGlobala = umidGlobala;
    }

    /**
     * Metoda de obtinere a timestamp-ului de referinta
     * @return Timestamp-ul de referinta
     */
    public int getTimestampRef() {
        return timestampRef;
    }

    /**
     * Metoda de obtinere a numarului de camere
     * @return Numarul de camere
     */
    public int getNumarCamere() {
        return numarCamere;
    }

    /**
     * Metoda de obtinere a temperaturii globale
     * @return Temperatura globala
     */
    public double getTempGlobala() {
        return tempGlobala;
    }

    /**
     * Metoda de obtinere a umiditatii globale
     * @return Umiditatea globala
     */
    public double getUmidGlobala() {
        return umidGlobala;
    }
   
    /**
     * Constructorul implicit, de creare a unui nou sistem
     */
    public Sistem() {
        camere = new ArrayList<>();
    }
    
    /**
     * Metoda de adaugare a unei temperaturi in sistem
     * @param idSenzor ID-ul senzorului
     * @param timestamp Timestamp-ul temperaturii adaugate
     * @param temperatura Valoarea temperaturii adaugate
     */
    public void observe(String idSenzor, int timestamp, double temperatura) {
        // calcularea orei in care trebuie introdusa temperatura respectiva
        int difTimestamp = timestampRef - timestamp;
        int indexList = difTimestamp / 3600;
        double doubleIndex = difTimestamp / 3600.0;
        
        // cautam camera cu senzorul dat ca parametru
        Camera cameraSenzor = null;
        for(Camera c: camere) {
            if(idSenzor.equals(c.senzor.getId())) {
                cameraSenzor = c;
            }
        }
        
        // se verifica daca index-ul obtinut este valid (intre 0 si 23)
        if(indexList < 24 && doubleIndex >= 0 && cameraSenzor != null) {
            Senzor senzor = cameraSenzor.senzor;
            SortedSet<Double> list = senzor.buckets.get(indexList);
            list.add(temperatura);     
        }
    }
    
    /**
     * Metoda de introducere a unei noi umiditati in sistem
     * @param idSenzor ID-ul senzorului
     * @param timestamp Timestamp-ul observarii umiditatii
     * @param umiditate Valoarea umiditatii
     */
    public void observeH(String idSenzor, int timestamp, double umiditate) {
        int difTimestamp = timestampRef - timestamp;
        int indexList = difTimestamp / 3600;
        double doubleIndex = difTimestamp / 3600.0;
        
        // cautam camera cu senzorul dat ca parametru
        Camera cameraSenzor = null;
        for(Camera c: camere) {
            if(idSenzor.equals(c.senzor.getId())) {
                cameraSenzor = c;
            }
        }
        
        // se verifica daca index-ul obtinut este valid (intre 0 si 23)
        if(indexList < 24 && doubleIndex >= 0 && cameraSenzor != null) {
            Senzor senzor = cameraSenzor.senzor;
            SortedSet<Double> list = senzor.bucketsH.get(indexList);
            list.add(umiditate);     
        }
    }
    
    /**
     * Metoda de afisare in fisier a temperaturilor intre doua timestamp-uri
     * @param numeCamera Numele camerei
     * @param timestampStart Timestamp-ul de la care se incepe afisarea
     * @param timestampStop Timestamp-ul pana la care se afiseaza
     * @param o Fisierul in care se vor scrie temperaturile
     */
    public void list(String numeCamera, int timestampStart, 
            int timestampStop, PrintWriter o) {
       
        // se obtine referinta la camera cu numele dat ca parametru
        Camera cameraList = null;
        for(Camera camera: camere) {
            if(camera.getNumeCamera().equals(numeCamera)) {
                cameraList = camera;
            }
        }
        
        // se calculeaza index-ul de inceput si de sfarsit pentru afisare
        int indexMax = (timestampRef - timestampStart) / 3600;
        int indexMin = (timestampRef - timestampStop) / 3600;
        
        o.print(cameraList.getNumeCamera() + " ");
        int nrElemente = 0;
        
        // se parcurg doar intervalele de o ora intre indecsii aflati mai devreme
        for(int i = indexMin; i < indexMax; i++) {
            SortedSet<Double> set = cameraList.senzor.buckets.get(i);
            // se parcurg temperaturile din bucket-ul respectiv
            for(Double temp: set) {
                if(nrElemente == 0) {
                    o.print(String.format("%.2f", temp));
                } else {
                    o.print(" " + String.format("%.2f", temp));
                }
                nrElemente++;
            }
        }
        o.println();
        
    }
    
    /**
     * Metoda de decidere a pornirii sistemului
     * @param o Fisierul in care se va scrie raspunsul "YES" sau "NO"
     */
    public void triggerHeat(PrintWriter o) {
        double temperaturaMedie = 0;
        double umiditateMedie = 0;
        double suprafete = 0;
        double suprafeteH = 0;
        
        // se parcurg toate camerele
        for(Camera camera: camere) {
            int index;
            int indexH;
            int i = 0;
            
            // setul pentru temperaturi
            SortedSet<Double> lista = camera.senzor.buckets.get(0);
            while(i < 24 && lista.isEmpty() == true) {
                lista = camera.senzor.buckets.get(i);
                i++;
            }

            // setul pentru umiditati
            SortedSet<Double> listaH = camera.senzor.bucketsH.get(0);
            i = 0;
            while(i < 24 && listaH.isEmpty() == true) {
                listaH = camera.senzor.bucketsH.get(i);
                i++;
            }

            if(lista.isEmpty() == false) {               
                double temp = lista.first(); // temperatura minima
                suprafete += camera.getSuprafata();
                temperaturaMedie += (temp * camera.getSuprafata());
            }
            
            if(listaH.isEmpty() == false) {
                double umid = listaH.last(); // umiditatea maxima
                suprafeteH += camera.getSuprafata();
                umiditateMedie += (umid * camera.getSuprafata());
            }
        }
        
        if(suprafete != 0) {
            temperaturaMedie /= suprafete;
        }
        
        if(suprafeteH != 0) {
            umiditateMedie /= suprafeteH;
        } else {
            // daca nu s-a dat nicio umiditate
            umiditateMedie = -1;
        }
        
        // umiditatea globala e setata la -1 in cazul in care nu exista OBSERVEH
        if(umidGlobala != -1 || umiditateMedie > umidGlobala &&
                umiditateMedie == -1) {
            o.println("NO");
            return;
        }
        

        if(temperaturaMedie < tempGlobala) {
            o.println("YES");
        } else {
            o.println("NO");
        }
    }
    
    /**
     * Metoda de modificare a temperaturii globale
     * @param temperature Noua temperatura globala
     */
    public void temperature(Double temperature) {
        tempGlobala = temperature;
    }
}
