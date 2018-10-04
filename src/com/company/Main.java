package com.company;
import java.io.*;
import java.util.Scanner;

public class Main {
    static Double[][] mtrxHasil = new Double[100][100]; //Matriks untuk menyimpan data SPL. Asumsi memori matriks maksimal 100x100
    static int NBrsEff = 1; //Jumlah baris mtrxHasil. Asumsi input tidak pernah kosong
    static int NKolEff = 1; //Jumlah kolom mtrxHasil. Asumsi input tidak pernah kosong
    static Scanner keyboard = new Scanner(System.in); //Variabel untuk menampung inputan dari keyboard
    static boolean selesai = false; //Variavel untuk menghentikan/melanjutkan program
    static int pilihanProblem; //Variabel untuk menampung pilihan user
    static int pilihanMetode; //Variabel untuk menampung pilihan user
    static int pilihanInpt; //Variabel untuk menampung pilihan user
    static String solusi; //Variabel untuk menampung hasil solusi agar bisa dimasukkan ke file eksternal

    static Matrix matriks;

    public static void main(String[] args) throws IOException {
      while (!selesai) {
        System.out.println("MENU");
        System.out.println("1. Sistem Persamaan Linear");
        System.out.println("2. Interpolasi Problem");
        System.out.println("3. Selesai");
        System.out.print("Pilihan : ");
        pilihanProblem = keyboard.nextInt(); //Memasukkan pilihan problem

        if (pilihanProblem == 3){
          selesai = true; //Menghentikan program
        }

        if (!selesai){
          System.out.println("Metode Input");
          System.out.println("1. File txt eksternal");
          System.out.println("2. Matriks masukkan");
          System.out.print("Pilihan : ");
          pilihanInpt = keyboard.nextInt(); //Memasukkan pilihan metode input

          if (pilihanInpt == 1){
            BacaFile(mtrxHasil,NBrsEff,NKolEff);
          } else{
            BacaMatriks(mtrxHasil,NBrsEff,NKolEff);
          }

          System.out.println("Metode Penyelesaian");
          System.out.println("1. Eleminasi Gauss");
          System.out.println("2. Eleminasi Gauss-Jordan");
          System.out.print("Pilihan : ");
          pilihanMetode = keyboard.nextInt(); //Memasukkan pilihan metode penyelesaian

      	  if (pilihanProblem == 2){
            solusi ="";
            Double[] solInterpol;
      	  	interpolasi(mtrxHasil,NBrsEff,NKolEff);
            if (pilihanMetode == 1){
              solInterpol =  matriks.interpolateG();
            }
            else{
              solInterpol =  matriks.interpolateGJ();
            }
            for (int i = 0; i <= solInterpol.length; i++){
              solusi += Double.toString(solInterpol[i]) + "x^" + Integer.toString(i);
              if (i != solInterpol.length){
                solusi += " + ";
              }
            }
            System.out.println(solusi);
            Double x = new Double;
            while (!cukup){
              System.out.print("Masukkan X, masukkan -999 untuk berhenti : ")
              x = keyboard.nextDouble();
              if (x != -999){
                System.out.println(hasilFungsi(solInterpol,x));
                x = keyboard.nextDouble();
              }
            }
      	  }
          else if (pilihanProblem == 1){
             matriks = new Matrix(mtrxHasil, NBrsEff, NKolEff);
             if (pilihanMetode == 1) {
              System.out.println("Metode eliminasi Gauss dipilih");
              solusi = matriks.solutionG();
             } else if (pilihanMetode == 2) {
              System.out.println("Metode eliminasi Gauss-Jordan dipilih");
              solusi = matriks.solutionGJ();
             }
             System.out.println("Sedang menghitung...");
             System.out.println("Jawaban :");
             System.out.println(solusi);
             System.out.println();

        }
      }
    }

    static void BacaMatriks (Double[][] mtrxHasil, int NBrsEff, int NKolEff)
    //I.S mtrxHasil, NBrsEff, dan NBrsKol sembarang.
    //F.S mtrxHasil, NBrsEff, dan NBrsKol terdefinisi sesuai inputan user
    {
     System.out.print("Masukkan jumlah baris : ");
     NBrsEff = keyboard.nextInt(); //Memasukkan jumlah baris
     System.out.print("Masukkan jumlah kolom : ");
     NKolEff = keyboard.nextInt(); //Memasukkan jumlah kolom
     for (int i =1; i <= NBrsEff; i++){
       for (int j =1; j <= NKolEff; j++){
         mtrxHasil[i][j] = keyboard.nextDouble(); //Memasukkan data matriks
       }
     }
     //matriks = new Matrix(mtrxHasil, NBrsEff, NKolEff);
    }

   static void BacaFile (Double[][] mtrxHasil, int NBrsEff, int NKolEff) throws IOException
   //I.S mtrxHasil, NBrsEff, dan NBrsKol sembarang
   //F.S mtrxHasil, NBrsEff, dan NBrsKol terdefinisi sesuai isi file
   {
     FileReader in = null;
     String[][] isiFile = new String[100][100]; //Variabel untuk menampung isi file
     int Kol = 1; //Inisiasi jumlah kolom minimal. Asumsi file tidak pernah kosong
     int Brs = 1; //Inisiasi jumlah baris minimal. Asumsi file tidak pernah kosong
     String NamaFile; //Variabel untuk menampung nama file
     System.out.print("Masukkan NamaFile : ");
     NamaFile = keyboard.next(); //Memasukkan nama file
     try {
        in = new FileReader(NamaFile); //Mempersiapkan variabel untuk pembacaan file
        int cc; //Variabel untuk menampung isi file per karakter
        isiFile[Brs][Kol] = "";
        while ((cc =  in.read()) != -1) { //Looping selama isi file masih ada
           if (cc == 13) { //cc == 13 menandakan karakter terakhir dari baris ke-x dari isi file
             Brs++;
             NKolEff++;
           }
           else if (cc == 32) { //cc == 32 menandakan cc adalah spasi
             Kol++;
             NKolEff++;
             isiFile[Brs][Kol] = "";
           }
           else if (cc == 10){ //cc == 10 berarti pembacaan file berpindah baris
             Kol = 1;
             isiFile[Brs][Kol]= "";
           }
           else {
             isiFile[Brs][Kol] += (char) cc; //Mengisi matriks string dengan cc
           }
        }
     }
     finally {
        if (in != null) { //Menutup file jika file sudah selesai dibaca
           in.close();
        }
     }
     NBrsEff = Brs -1; //Jumlah baris matriks
     NKolEff = NKolEff / NBrsEff; //jumlah kolom matriks
     for (int i = 1; i <= NBrsEff; i += 1){
       for (int j = 1; j <= NKolEff; j += 1){
        mtrxHasil[i][j] = Double.parseDouble(isiFile[i][j]); //Mengisi matriks double dengan matriks string yang sudah dikonversi
       }
     }

     matriks = new Matrix(mtrxHasil, NBrsEff, NKolEff);
   }

   static void interpolasi(Double[][] mtrxHasil, int NBrsEff, int NKolEff)
   //I.S mtrsHasil, NBrsEff, NKolEff terdefinisi
   //F.S mengeluarkan hasil interpolasi
   {
    Double[][] mtrxInter = new Double[100][100]; //Variabel untuk menyimpan matriks augmented
    for (int i =1; i <= NBrsEff; i++){
      for (int j =1; j <= NBrsEff; j++){
        mtrxInter[i][j] = Math.pow(mtrxHasil[i][1],j-1); //Mengkonversi matriks yang masih dalam bentuk x,y menjadi matriks augmented
      }
      mtrxInter[i][NBrsEff+1] = mtrxHasil[i][2];
    }
    NKolEff = NBrsEff +1;
    matriks = new Matrix(mtrxInter, NBrsEff, NKolEff);
    //Tinggal nunggu Gauss GaussJordan sma nunggu nama fungsi bagian ikra
   }

   static Double hasilFungsi (Double[][] solInterpol,Double x)
   {
     Double hasil = 0;
     for (int i = 0; i <= solInterpol.length; i++){
       hasil += Math.pow(x,i);
     }
     return hasil;
   }
}
