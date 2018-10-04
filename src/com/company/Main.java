
import java.io.*;
import java.util.*;
import java.lang.*;

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
    static String namaFileOutput; //Variabel unruk menyimpan nama file eksternal yang menampung output

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
          selesai = true;
        }

        if (!selesai) {
          System.out.println("Metode Input");
          System.out.println("1. File txt eksternal");
          System.out.println("2. Matriks masukkan");
          System.out.print("Pilihan : ");
          pilihanInpt = keyboard.nextInt(); //Memasukkan pilihan metode input

          if (pilihanInpt == 1) {
            BacaFile();
          } else {
            BacaMatriks();
          }

          System.out.println("Metode Penyelesaian");
          System.out.println("1. Eleminasi Gauss");
          System.out.println("2. Eleminasi Gauss-Jordan");
          System.out.print("Pilihan : ");
          pilihanMetode = keyboard.nextInt(); //Memasukkan pilihan metode penyelesaian

          if (pilihanProblem == 2) {
            solusi = "";
            Double[] solInterpol =  new Double[100];
            interpolasi();
            if (pilihanMetode == 1) {
              solInterpol = matriks.interpolateG();
            } else {
              solInterpol = matriks.interpolateGJ();
            }
            for (int i = 0; i <= solInterpol.length-1; i++) {
              solusi += String.valueOf(solInterpol[i]) + "x^" + String.valueOf(i);
              if (i != solInterpol.length-1) {
                solusi += " + ";
              }
            }
            System.out.println(solusi);
            Double x = 0.0;
            while (x != -999) {
              System.out.print("Masukkan X, masukkan -999 untuk berhenti : ");
              x = keyboard.nextDouble();
              if (x != -999) {
                System.out.println(hasilFungsi(solInterpol, x));
              }
            }
          } else if (pilihanProblem == 1) {
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
          System.out.println("1. Masukkan solusi ke file eksternal");
          System.out.println("2. Tidak perlu, makasih");
          System.out.print("Pilihan : ");
          pilihanInpt = keyboard.nextInt();
          if (pilihanInpt == 1){
            System.out.print("Masukkan nama file :");
            namaFileOutput = keyboard.next();
            TulisKeFile(solusi, namaFileOutput);
          }
        }
      }
    }

    public static void BacaMatriks ()
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

   public static void BacaFile () throws IOException
   //I.S mtrxHasil, NBrsEff, dan NBrsKol sembarang
   //F.S mtrxHasil, NBrsEff, dan NBrsKol terdefinisi sesuai isi file
   {
     FileReader in = null;
     String[][] isiFile = new String[100][100]; //Variabel untuk menampung isi file
     int Kol = 1; //Inisiasi jumlah kolom minimal. Asumsi file tidak pernah kosong
     int Brs = 1; //Inisiasi jumlah baris minimal. Asumsi file tidak pernah kosong
     String NamaFile = ""; //Variabel untuk menampung nama file
     NKolEff = 1;
     NBrsEff = 1;
     System.out.print("Masukkan NamaFile : ");
     NamaFile = keyboard.next(); //Memasukkan nama file
     while (!(new File(NamaFile)).exists()) {
      System.out.println("File tidak ditemukan, periksa kembali nama file!");
      System.out.print("Masukkan NamaFile : ");
      NamaFile = keyboard.next(); //Memasukkan nama file
     }
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
        mtrxHasil[i][j] = Double.valueOf(isiFile[i][j]); //Mengisi matriks double dengan matriks string yang sudah dikonversi
       }
     }

     //matriks = new Matrix(mtrxHasil, NBrsEff, NKolEff);
   }

   public static void TulisKeFile (String solusi, String namaFileOutput)
   //I.S solusi terdefinsi. file eksternal sembarang
   //F.S mengisi file eksternal dengan solusi
   {
     try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new FileWriter(namaFileOutput, true)));
            out.println(solusi);
            out.close();
        } catch (IOException e) {
            System.out.println("Gagal menulis ke file " + namaFileOutput);
            e.printStackTrace();
        }
   }

   public static void interpolasi()
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
   }

   public static Double hasilFungsi (Double[] solInterpol,Double x)
   {
     Double hasil = 0.0;
     for (int i = 0; i <= solInterpol.length-1; i++){
       hasil += Math.pow(x,i) * solInterpol[i];
     }
     return hasil;
   }
}
