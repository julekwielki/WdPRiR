package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

public class Lab1 {
	
	static int szerokosc = 1024;
	static int wysokosc = 1024;
	static double xmin = -2.1;
	static double xmax = 0.6;
	static double ymin = -1.2;
	static double ymax = 1.2;
	static int maks = 200;
	static int tab_pom[] = {32, 64, 128, 256, 512, 1024, 2048, 4096, 8192};
	
	public static void main(String[] args) throws FileNotFoundException {
		
		rysuj(szerokosc, wysokosc, ymin, ymax, xmin, xmax, maks, "nazwa_pliku.png");
		
		rysuj_wiele(tab_pom, 3);

	}
	
	public static int[][] licz(int s, int w, double ymi, double yma, double xmi, double xma, int l) {
		int[][] wyniki = new int[s][w];
		double dx = (xma - xmi)/s;
		double dy = (yma - ymi)/w;
		
		for (int x = 0; x < s; x++) {
			for (int y = 0; y < w; y++) {
				double a = xmi +x*dx;
				double b = ymi+ y*dy; 
				wyniki[x][y] = piksel(a, b, maks);
			}
		}
		return wyniki;
	}
	
	public static int piksel(double x, double y, int l) {
		double real = 0; 
		double image = 0;
		
		double new_real = 0; 
		double new_image = 0;
		
		for (int i = 0; i < l; i++) {
			new_real = real * real - image * image + x;
			new_image = 2 * real * image + y;
			real = new_real;
			image = new_image;
			if(Math.abs(new_real) == Double.POSITIVE_INFINITY ||  Math.abs(new_image) == Double.POSITIVE_INFINITY) {
				return 255-i;
			} 
		}
		return 0;
	}
	
	
	public static void rysuj(int xLenght, int yLength, double ymi, double yma, double xmi, double xma, int l, String nazwa) {
		
		int[][] rys = licz(xLenght, yLength, ymin, ymax, xmin, xmax, maks);
		
		BufferedImage b = new BufferedImage(xLenght, yLength, BufferedImage.TYPE_INT_RGB);
		
		for(int x = 0; x < xLenght; x++) {
		    for(int y = 0; y < yLength; y++) {
		        int rgb = (int)rys[x][y]<<16 | (int)rys[x][y] << 8 | (int)rys[x][y];
		        b.setRGB(x, y, rgb);
		    }
		}
		
		try {
			ImageIO.write(b, "png", new File(nazwa));
		} catch (IOException e) {
			System.out.println("Błąd");
			e.printStackTrace();
		}
		System.out.println("end");
	}
	
	public static void rysuj_wiele(int tab[], int n) throws FileNotFoundException {

		double xmin = -2.1;
		double xmax = 0.6;
		double ymin = -1.2;
		double ymax = 1.2;
		int maks = 200;

		double[] czas = new double[tab.length];
		
		for (int j = 0; j < tab.length; j++) {
			double sum = 0;
			for (int i = 0; i < n; i++) {
				long pocz = System.nanoTime();
				String nazwa = "rys "+tab[j]+"x"+tab[j]+" nr "+i+1+".png"; 
				rysuj(tab[j], tab[j], ymin, ymax, xmin, xmax, maks, nazwa);
				long koniec = System.nanoTime();
				sum += (koniec-pocz)/1E9;
			}
			czas[j] = sum/n;
		}
		
		PrintWriter zapis = new PrintWriter("dane.txt");

		for (int j = 0; j < tab.length; j++) {	
			zapis.print(tab[j] + "\t"+ czas[j]+"\n");
		}
	    zapis.close();
	}
}
