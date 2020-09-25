/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gpsnew1;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Andrey
 */
public class GPS extends JFrame {

    static JButton jButtonFileCoord;
    static JScrollPane jScrollPane;
    static JPanel jPanel;
    static JPanel jPanelButtons;
    static JButton jButtonFileImage;
    static JButton jButtonDistance;
    static ImagePanel imagePanel;
    static FileDialog fd;
    static JLabel jLabel1;
    static JLabel jLabel2;
    static JLabel jLabel3;
    public static JTextField jTFDolLG;
    public static JTextField jTFSzLG;
    public static JTextField jTFDolPD;
    public static JTextField jTFSzPD;
    public static JTextField jTFDistance;
    static JButton jButtonExit;
    static JSlider jSlider;

    public GPS() {
        setTitle("wizualizacja trasy na GPS współrzędnim");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(950, 650));
        setResizable(true);

        initComponents(this.getContentPane());
        pack();
        setVisible(true);
    }

    public void ToOpenTheDataFile() {
        fd = new FileDialog(this, "Czytać dane z pliku", FileDialog.LOAD);
        fd.setVisible(true);
    }

    public void initComponents(Container pane) {
        jScrollPane = new JScrollPane();
        jPanel = new JPanel();
        jPanelButtons = new JPanel();
        jPanelButtons.setLayout(new GridLayout(13, 2, 1, 1));
        imagePanel = new ImagePanel();
        jButtonExit = new JButton("Wyjście");
        jButtonFileImage = new JButton("Wybrać plik z mapą");
        jButtonFileCoord = new JButton("Wybrać plik z współrzędnymi trasy");
        //     jButtonDistance=new JButton ("<html>Znaleźć odległość przebytą przez pojazd</html>"); 
        jButtonDistance = new JButton("Znaleźć odległość, którą przejechał pojazd");

        jLabel1 = new JLabel("Współrzędne lewego górnego rogu mapy (długość i szerokość geograficzna)");
        jLabel2 = new JLabel("Współrzędne prawego dolnego rogu mapy (długość i szerokość geograficzna)");
        jLabel3 = new JLabel("Skala");
        JSlider jSlider = new JSlider(JSlider.HORIZONTAL,
                50, 150, 100);
        jSlider.setMajorTickSpacing(50);
        jSlider.setMinorTickSpacing(25);
        jSlider.setPaintTicks(true);
        jSlider.setPaintLabels(true);
        jTFDolLG = new JTextField("52.247159");
        jTFDolLG.setPreferredSize(jTFDolLG.getMinimumSize());
        jTFSzLG = new JTextField("20.989173");
        jTFDolPD = new JTextField("52.188177");
        jTFSzPD = new JTextField("21.035837");
        jTFDistance = new JTextField("?");
        ///////////////////////////////////////////////
        jPanelButtons.add(jButtonFileImage);
        jPanelButtons.add(jButtonFileCoord);
        jPanelButtons.add(jLabel1);
        jPanelButtons.add(jTFDolLG);
        jPanelButtons.add(jTFSzLG);
        jPanelButtons.add(jLabel2);
        jPanelButtons.add(jTFDolPD);
        jPanelButtons.add(jTFSzPD);
        jPanelButtons.add(jLabel3);
        jPanelButtons.add(jSlider);
        jPanelButtons.add(jButtonDistance);
        jPanelButtons.add(jTFDistance);
        jPanelButtons.add(jButtonExit);
        /////////////////////////////////

        /////////////////////////////////////////////
        jButtonFileCoord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ToOpenTheDataFile();
                imagePanel.setFile(new File(fd.getFile()));
                try {
                    imagePanel.ToReadDataFromTheSelectedFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                repaint();
            }
        });
        /////////////////////////////////////////////
        jButtonDistance.addActionListener(new ActionListener() {
            double odleg = 0;

            @Override
            public void actionPerformed(ActionEvent e2) {
                String ss = new String(" " + imagePanel.getOdleglosz());
                jTFDistance.setText("Odległość, którą przejechał pojazd :" + ss
                );
                repaint();
            }
        });
//////////////////////////////////////////////////
        jButtonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e2) {
                System.exit(0);
            }
        });
///////////////////////////////////////////////////
        jSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                imagePanel.setTheCurrentWidthOfTheImage(imagePanel.getTheOriginalWidthOfTheImage() / 100 * jSlider.getValue());
                imagePanel.setTheCurrentHeightOfTheImage(imagePanel.getTheOriginalHeightOfTheImage() / 100 * jSlider.getValue());
                imagePanel.setPreferredSize(new Dimension(imagePanel.getTheCurrentWidthOfTheImage(), imagePanel.getTheCurrentHeightOfTheImage()));
                jScrollPane.setViewportView(imagePanel);
                jScrollPane.setPreferredSize(new Dimension(460, 400));
                repaint();
            }
        });
///////////////////////////////////////////////////////////
        jButtonFileImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ToOpenTheDataFile();
                try {
                    imagePanel.setImage((ImageIO.read(new File(fd.getFile()))));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                imagePanel.setTheTableWasReadFromAFile(false);
                jSlider.setValue(100);
                repaint();
            }
        });
//////////////////////////////////////////////////

///////////////////////////////////////////////////
///////////////////////////////////////////////////////////
        /////////////////////    
        jSlider.setValue(101);
        jPanel.setLocation(3, 3);
        jPanel.add(jScrollPane, BorderLayout.WEST);
        jPanel.add(jPanelButtons, BorderLayout.EAST);

        pane.add(jPanel);

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GPS().setVisible(true);
            }
        });
    }

}
