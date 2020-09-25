/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gpsnew1;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.swing.JPanel;
import static gpsnew1.GPS.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ImagePanel extends JPanel {

    public int[][] wspolrzędne;
// czy jest wciśnięty przycisk, aby otworzyć plik
    public boolean TheTableWasReadFromAFile = false;
    private File file;
    private ArrayList<Double> SzGPS = new ArrayList<Double>();
    private ArrayList<Double> DolGPS = new ArrayList<Double>();
    private Image image;
    private int TheCurrentWidthOfTheImage;
    private int TheCurrentHeightOfTheImage;
    private int TheOriginalWidthOfTheImage;
    private int TheOriginalHeightOfTheImage;

    public void setTheTableWasReadFromAFile(boolean TheTableWasReadFromAFile) {
        this.TheTableWasReadFromAFile = TheTableWasReadFromAFile;
    }

    public void setTheCurrentWidthOfTheImage(int TheCurrentWidthOfTheImage) {
        this.TheCurrentWidthOfTheImage = TheCurrentWidthOfTheImage;
    }

    public void setTheCurrentHeightOfTheImage(int TheCurrentHeightOfTheImage) {
        this.TheCurrentHeightOfTheImage = TheCurrentHeightOfTheImage;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getTheOriginalWidthOfTheImage() {
        return TheOriginalWidthOfTheImage;
    }

    public int getTheOriginalHeightOfTheImage() {
        return TheOriginalHeightOfTheImage;
    }

    public int getTheCurrentWidthOfTheImage() {
        return TheCurrentWidthOfTheImage;
    }

    public int getTheCurrentHeightOfTheImage() {
        return TheCurrentHeightOfTheImage;
    }

    public void setImage(Image image) {
        this.image = image;
        TheCurrentWidthOfTheImage = image.getWidth(null);
        TheCurrentHeightOfTheImage = image.getHeight(null);
        TheOriginalHeightOfTheImage = TheCurrentHeightOfTheImage;
        TheOriginalWidthOfTheImage = TheCurrentWidthOfTheImage;
    }
 private void readFile(File file) throws IOException {
//metoda odczytuje dane z pliku do listy i tablicy 
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        // ustawienie separatorów do podziału wiersza
        Pattern pattern = Pattern.compile(":|;| ");
        line = br.readLine();
        //  tworzenie tablicy słów oddzielonych wzorem (":|;| ")       
        String[] WierszDannych = pattern.split(line);
// inicjalizacja listy współrzędnych geograficznych 
//(długość i szerokość geograficzna)
        while ((line = br.readLine()) != null) {
            WierszDannych = pattern.split(line);
            SzGPS.add(Double.parseDouble(WierszDannych[1]));
            DolGPS.add(Double.parseDouble(WierszDannych[2]));
        }
// inicjalizacja tablicy współrzędnych geograficznych
        wspolrzędne = new int[SzGPS.size()][2];
        br.close();
        fr.close();
    }
  
     
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (image != null) {
   //jeśli zmienna został zainicjalizowany wzorem, wyświetlić rysunek z podanymi wymiarami
            g2d.drawImage(image, 0, 0, TheCurrentWidthOfTheImage, TheCurrentHeightOfTheImage, null);
        }
        //ustawić kolor i grubość linii
        g2d.setColor(Color.blue);
        g2d.setStroke(new BasicStroke(5.0f));
        ToWriteTheCoordinatesFromTheListToAnArray();
        if (TheTableWasReadFromAFile == true) {
     //jeśli przycisk jest wciśnięty, to narysować trasę       
            for (int i = 0; i < wspolrzędne.length; i++) {
                g2d.drawLine(wspolrzędne[i][0], wspolrzędne[i][1], wspolrzędne[i][0], wspolrzędne[i][1]);
            }
        }
    }
////////////////////////////////////////////////////////////////////
 
    public void ToReadDataFromTheSelectedFile() throws IOException {
        // dane zostały odczytane z pliku
        TheTableWasReadFromAFile = true;
        readFile(file);
        ToWriteTheCoordinatesFromTheListToAnArray();

    }

    public void ToWriteTheCoordinatesFromTheListToAnArray() {
        for (int i = 0; i < SzGPS.size(); i++) {
            wspolrzędne[i][1] = Kon_Dl_w_X(SzGPS.get(i));
            wspolrzędne[i][0] = Kon_Sz_w_Y(DolGPS.get(i));
        }
    }

   private int Kon_Dl_w_X(double Dol) {
        return (int) (((Double.parseDouble(jTFDolLG.getText()) - Dol) * TheCurrentHeightOfTheImage)
                / (Double.parseDouble(jTFDolLG.getText()) - Double.parseDouble(jTFDolPD.getText())));
    }

    private int Kon_Sz_w_Y(double Sz) {
        return (int) (((Sz - Double.parseDouble(jTFSzLG.getText())) * TheCurrentWidthOfTheImage)
                / (Double.parseDouble(jTFSzPD.getText()) - Double.parseDouble(jTFSzLG.getText())));
    }
///////////////////////////////////////////////////////////////
    public double getOdleglosz() {
        double odleg = 0;
        //  SzGPS   SzGPS.size()   DolGPS 
        for (int i = 1; i < SzGPS.size(); i++) {
            odleg += odleglosz(DolGPS.get(i - 1), DolGPS.get(i - 1),
                    DolGPS.get(i), DolGPS.get(i));
        }
        return (odleg);
    }

    private double odleglosz(double d1, double sz1, double d2, double sz2) {
        /*
         odległość, która jest mierzona w kierunku szerokości stale.
         Zmiana szerokości na jeden stopień równoznaczne dojazdu,
         odległość wzdłuż dowolnej szerokości geograficznej
         w 111,1329 km.
         Odległość mierzoną wzdłuż linii długości geograficznej nie stale.
         Na równiku w jednym stopniu 111,3213.
         Dla dowolnej geograficzna odległość w km można
         obliczyć jak 111,3213*cos (szerokość).
         Trzeba zmiana długości geograficznej razy
         cosinus szerokości (dla wybranej trasy to około 20,995401).

         */
        double rasst_dolg = Math.abs((double) 111.1348611 * (d1 - d2));
        double rasst_szir = (double) 111.3213778 * Math.abs((sz1 - sz2) * Math.cos(0.017453293 * d1));
        return (Math.sqrt(rasst_dolg * rasst_dolg + rasst_szir * rasst_szir));
    }


   
    
    
    
    
    
    
    
    
    
    
    
    
    
}
