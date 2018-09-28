package com.company;

public class Main {

    Matrix Mat;
    static Double[][] mtrxHasil = new Double[100][100];
    static int NBrsEff = 1;
    static int NKolEff = 1;
    static Scanner keyboard = new Scanner(System.in);
	
    public static void main(String[] args) throws IOException {
	// write your code here
        System.out.println("hello");
    }
	
    static void BacaMatriks (Double[][] mtrxHasil, int NBrsEff, int NKolEff) {
     System.out.print("Masukkan jumlah baris : ");
     NBrsEff = keyboard.nextInt();
     System.out.print("Masukkan jumlah kolom : ");
     NKolEff = keyboard.nextInt();
     for (int i =1; i <= NBrsEff; i++){
       for (int j =1; j <= NKolEff; j++){
         mtrxHasil[i][j] = keyboard.nextDouble();
       }
     }
   }

   static void BacaFile (Double[][] mtrxHasil) throws IOException {
     FileReader in = null;
     String[][] isiFile = new String[100][100];
     int Kol = 1;
     int Brs = 1;
     String NamaFile;
     System.out.print("Masukkan NamaFile : ");
     NamaFile = keyboard.next();
     try {
        in = new FileReader(NamaFile);
        int cc;
        isiFile[Brs][Kol] = "";
        while ((cc =  in.read()) != -1) {
           if (cc == 13) {
             Brs++;
             NKolEff++;
           }
           else if (cc == 32) {
             Kol++;
             NKolEff++;
             isiFile[Brs][Kol] = "";
           }
           else if (cc == 10){
             Kol = 1;
             isiFile[Brs][Kol]= "";
           }
           else {
             isiFile[Brs][Kol] += (char) cc;
           }
        }
     }
     finally {
        if (in != null) {
           in.close();
        }
     }
     NBrsEff = Brs -1;
     NKolEff = NKolEff / NBrsEff;
     for (int i = 1; i <= NBrsEff; i += 1){
       for (int j = 1; j <= NKolEff; j += 1){
        mtrxHasil[i][j] = Double.parseDouble(isiFile[i][j]);
       }
     }
   }

}
