import java.io.*;
import java.net.*;
import javax.net.ssl.*;

public class Elevation{
    private String prefix = "https://maps.googleapis.com/maps/api/elevation/xml?locations=";
    private String suffix = "&key=";
    private String filename;
    private boolean saved = false;
    private String url;

    public Elevation(float latitude, float longitude, String filename){
        URL server;
        HttpsURLConnection service;
        BufferedReader input;
        BufferedWriter output;
        String line;
        String location;
        int status;
        this.filename = filename;
        
        try {
            //costruzione dell'url di interrogazione del servizio
            location = Float.toString(latitude) + "," + Float.toString(longitude);
            url = prefix + URLEncoder.encode(location, "UTF-8") + suffix;
            server = new URL(url);
            service = (HttpsURLConnection)server.openConnection();
            //impostazione header richiesta
            service.setRequestProperty("Host", "maps.googleapis.com");
            service.setRequestProperty("Accept", "application/json");
            service.setRequestProperty("Accept-Charset", "UTF-8");
            //impostazione metodo get
            service.setRequestMethod("GET");
            //attivazione ricezione
            service.setDoInput(true);
            //connessione al servizio;
            service.connect();
            //verifica codice di stato risposta
            status = service.getResponseCode();
            if(status != 200){
                return; //errore
            }
            //apertura stream di ricezione da risora web
            input = new BufferedReader(new InputStreamReader(service.getInputStream(),"UTF-8");
            //apertura stream per scrittura su file
            output = new BufferedWriter(new FileWriter(filename));
            //ciclo lettura/scrittura
            while((line = input.readLine()) != null){
                output.write(line);
                output.newLine();
            }
            input.close();
            output.close();
            saved = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isSaved(){
        return saved;
    }

    public static void main(String[] args) {
        float latitude = "40°50′49.20″";
        float longitude = "14°15′54.00″";
        String filename = "File.txt";
        
        latitude = Float.parseFloat(latitude);
        longitude = Float.parseFloat(longitude);

        Elevation elevation = new Elevation(latitude, longitude, filename);
        if(elevation.isSaved()){
            System.out.println("Risposta salvata nel file" + filename);
        }
        else{
            System.out.println("error");
        }
    }
}
