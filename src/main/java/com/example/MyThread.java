package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class MyThread extends Thread {
    Socket s;
    
    public MyThread (Socket s) {
        this.s = s;
    }

    public void run () {
        try {
            ArrayList<String> listaNote = new ArrayList<>();
            ArrayList<String> listaUsename = new ArrayList<>();
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            String username = in.readLine();
            if (listaUsename.contains(username)) {
                out.writeBytes("Errore. L'Username inserito e' già presente." + "\n");
            } else {
                listaUsename.add(username);
                out.writeBytes("L'username e' stato inserito con successo" + "\n");
            }
            do {
                String stringaRicevuta = in.readLine();
                if (stringaRicevuta.equals("!")) {
                    break;
                } else if (stringaRicevuta.equals("?")) {
                    for (int i = 0; i < listaNote.size(); i++) {
                        out.writeBytes(listaNote.get(i) + "\n");
                    }
                    out.writeBytes("@" + "\n");
                } else if (stringaRicevuta.equals("#")) {
                    String indice = in.readLine();
                    int indiceIntero = Integer.parseInt(indice);
                    String esitoRimozione;
                    if (indiceIntero > 0 && indiceIntero <= listaNote.size()) {
                        esitoRimozione = "La nota " + listaNote.get(indiceIntero - 1) + " e' stata rimossa";
                        listaNote.remove(indiceIntero - 1);
                    } else {
                        esitoRimozione = "Impossibile eliminare la nota numero " + indice + " non esiste";
                    }
                    out.writeBytes(esitoRimozione + "\n");
                } else {
                    listaNote.add(stringaRicevuta);
                    out.writeBytes("OK" + "\n");
                }
            } while (true);
            in.close();
            out.close();
            s.close();
        } catch (Exception e) {

        }
    }
}
