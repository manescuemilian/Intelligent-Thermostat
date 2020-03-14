/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package termostat;
import java.io.*;

/**
 * Clasa generala, care deschide fisierele de intrare si iesire si ruleaza comenzile  
 * @author Emilian
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        String[] info;
        String linie, comanda;
        Sistem sistem = new Sistem();
        PrintWriter output = new PrintWriter("therm.out");
        try (BufferedReader br = new BufferedReader(new FileReader("therm.in"))) {
            linie = br.readLine();
            info = linie.split(" ");
            // primii doi parametri sunt cunoscuti:
            // numarul de camere si temperatura globala
            sistem.setNumarCamere(Integer.parseInt(info[0]));
            sistem.setTempGlobala(Double.parseDouble(info[1]));
            if(info.length == 3) {
                // daca nu se dau informatii despre umiditate
                sistem.setTimestampRef(Integer.parseInt(info[2]));
            } else {
                // daca se dau si informatii despre umiditate
                sistem.setUmidGlobala(Double.parseDouble(info[2]));
                sistem.setTimestampRef(Integer.parseInt(info[3]));
            }
            
            // se salveaza informatiile despre fiecare camera
            for(int i = 0; i < sistem.getNumarCamere(); i++) {
                linie = br.readLine();
                info = linie.split(" ");
                String numeCamera = info[0];
                String idSenzor = info[1];
                int suprafata = Integer.parseInt(info[2]);
                
                // se creeaza camera si se adauga in lista de camere a sistemului
                Camera camera = new Camera(numeCamera,idSenzor, suprafata);
                sistem.camere.add(camera);                
            }
            
            // rularea comenzilor
            while(( comanda = br.readLine() ) != null) {
                if(comanda.equals("TRIGGER HEAT") == true) {
                    sistem.triggerHeat(output);
                } else {
                    info = comanda.split(" ");
                    switch(info[0]) {
                        case "OBSERVE":
                            int timestamp = Integer.parseInt(info[2]);
                            double tempCamera = Double.parseDouble(info[3]);
                            sistem.observe(info[1], timestamp, tempCamera);
                            break;
                        case "TEMPERATURE":
                            sistem.temperature(Double.parseDouble(info[1]));
                            break;
                        case "LIST":
                            int timestampStart = Integer.parseInt(info[2]);
                            int timestampStop = Integer.parseInt(info[3]);
                            sistem.list(info[1], timestampStart, timestampStop, output);
                            break;
                        case "OBSERVEH":
                            timestamp = Integer.parseInt(info[2]);
                            double umidCamera = Double.parseDouble(info[3]);
                            sistem.observeH(info[1], timestamp, umidCamera);
                            break;
                    }
                }
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("Fisierul nu exista");
        } finally {
            // inchiderea fisierului
            output.close();
        }
    }
    
}
