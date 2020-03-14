/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package termostat;

import java.util.*;

/**
 * Senzorul fiecarei camere, cu id si bucket-uri pentru temperatura si umiditate
 * @author Emilian
 */
public class Senzor {
    private String id;
    ArrayList<SortedSet<Double>> buckets;
    ArrayList<SortedSet<Double>> bucketsH;
    
    /**
     * Metoda de obtinere a id-ului senzorului
     * @return Id-ul senzorului
     */
    public String getId() {
        return id;
    }
    
    /**
     * Constructor de creare a unui nou senzor
     * @param id Id-ul ce va fi setat unui senzor, care nu poate fi modificat
     */
    public Senzor(String id) {
        this.id = id;
        
        this.buckets = new ArrayList<>();
        // se creeaza 24 de set-uri, pentru temperatura
        for(int i = 0; i < 24; i++) {
            SortedSet<Double> a = new TreeSet<>();
            buckets.add(a);
        }
        
        this.bucketsH = new ArrayList<>();
        // se creeaza 24 de set-uri, pentru umiditate
        for(int i = 0; i < 24; i++) {
            SortedSet<Double> a = new TreeSet<>();
            bucketsH.add(a);
        }
    }
}
