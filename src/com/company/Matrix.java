package com.company;

public class Matrix {
  Double[][] matOri;

  int NBrsEff;
  int NKolEff;
  int NKolEffAug;

  Matrix(Double[][] mtrxHasil, int NBrsEff, int NKolEff) {
      this.matOri = new Double[NBrsEff+1][NKolEff+1];
      this.matOri = mtrxHasil;
      this.NBrsEff = NBrsEff;
      this.NKolEffAug = NKolEff;
      this.NKolEff = NKolEff - 1;
  }

  private int[] obtainFreeVarCol(Double[][] mat)
  // I.S augmented matrix
  // F.S mengeluarkan array yang menunjukkan kolom mana saja yang merupakan free variabel (freeVarCol)
  {
      int i, j;

      boolean[] notFreeVarColBol = new boolean[this.NKolEff+1];
      // init not free var col
      for (j=1; j<=this.NKolEff; j++) {
          notFreeVarColBol[j] = true;
      }
      // mark not free var col
      int notFreeVarColCount = 0;
      for (i=1; i<=this.NBrsEff; i++) {
          for (j=1; j<=this.NKolEff; j++) {
              if (mat[i][j] == 1) {
                  notFreeVarColBol[j] = false;
                  notFreeVarColCount++;
                  break;
              }
          }
      }
      // get free var col array
      int freeVarColCount = 0;
      int[] freeVarCol = new int[this.NKolEff - notFreeVarColCount];
      for (j=1; j<=this.NKolEff; j++) {
          if (notFreeVarColBol[j]) {
              freeVarCol[freeVarColCount] = j;
              freeVarColCount++;
          }
      }

      return freeVarCol;
  }

  private boolean isColFree(int[] arr, int arrLen, int x)
  // I.S freeVarCol, neff freeVarCol, indeks kolom
  // F.S mengeluarkan true apabila kolom pada matriks tersebut merupakan variabel bebas
  {
      for (int i=0; i<arrLen; i++) {
          if (arr[i] == x) {
              return true;
          }
      }
      return false;
  }

  private int searchFreeVarIdx(int[] arr, int arrLen, int x)
  // I.S freeVarCol, neff freeVarCol, indeks kolom (x)
  // F.S mengeluarkan indeks nilai x pada array freeVarCol
  {
      int i;
      for (i=0; i<arrLen; i++) {
          if (arr[i] == x) {
              return i;
          }
      }
      return i;
  }

  public String solutionG()
  // I.S
  // F.S mengeluarkan string solusi persamaan, tulisan "Tidak mempunyai solusi", ataupun solusi parametrik
  {
      int i, j;   // iterator var
      int ct; // counter variabel
      String solution;

      // ubah matriks original menjadi bentuk eselon dengan menggunakan metode gauss
      Double[][] mat = gauss(this.matOri);

      /* Parametrik Check */
      boolean parametrik = false; // parametrik solution indicator
      ct = 0;
      for (i=this.NBrsEff; i>=1; i--) {
          ct = 0;
          for (j=1; j<=this.NKolEffAug; j++) {
              if (mat[i][j] == 0) {
                  ct++;
              }
          }
          if (ct == this.NKolEffAug) {
              parametrik = true;
              break;
          }
      }
      if (parametrik) {
          int k; double mult;
          for (i=this.NBrsEff; i>=1; i--) {
              for (j=1; j<=this.NKolEff; j++) {
                  if (mat[i][j] == 1) {
                      for (k=i-1; k>=1; k--) {
                          mult = mat[k][j];
                          mat[k][j] = 0.0;
                          mat[k][this.NKolEffAug] -= mult * mat[i][this.NKolEffAug];
                      }
                      break;
                  }
              }
          }

          // Obtain free var col
          int[] freeVarCol = obtainFreeVarCol(mat);
          int freeVarColCount = freeVarCol.length;

          // Produce solution
          solution = "";
          char[] constChar = { 's', 't', 'u', 'v', 'w', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l' };
          int constCharIdx;
          for (j=1; j<=this.NKolEff; j++) {
              if (isColFree(freeVarCol, freeVarColCount, j)) {
                  constCharIdx = searchFreeVarIdx(freeVarCol, freeVarColCount, j);
                  solution += "x" + Integer.toString(j) + " = " + constChar[constCharIdx];
              } else {
                  for (i=1; i<=this.NBrsEff; i++) {
                      if (mat[i][j] == 1) {
                          solution += "x" + Integer.toString(j) + " =";
                          if (mat[i][this.NKolEffAug] != 0) {
                              solution += " " + mat[i][this.NKolEffAug];
                          } else if (mat[i][this.NKolEffAug] == 0 && !isColFree(freeVarCol, freeVarColCount, this.NKolEffAug)) {
                              solution += " " + mat[i][this.NKolEffAug];
                          }
                          for (k=0; k<freeVarColCount; k++) {
                              if (mat[i][freeVarCol[k]] > 0) {
                                  solution += " - " + mat[i][freeVarCol[k]] + constChar[k];
                              } else if (mat[i][freeVarCol[k]] < 0) {
                                  solution += " + " + (-1 * mat[i][freeVarCol[k]]) + constChar[k];
                                  if (k != freeVarColCount) {
                                      solution += " ";
                                  }
                              }
                          }
                          break;
                      }
                  }
              }
              if (j != this.NBrsEff) {
                  solution += "\n";
              }
          }
          return (solution);
      }

      /* No Solution Check */
      boolean noSolution = false; // no solution indicator
      ct = 0;
      for (i=this.NBrsEff; i>=1; i--) {
          ct = 0;
          for (j=1; j<=this.NKolEff; j++) {
              if (mat[i][j] == 0) {
                  ct++;
              }
          }
          if (ct == this.NKolEff) {
              noSolution = true;
              break;
          }
      }
      if (noSolution) {
          return ("Solusi tidak ada");
      }

      /* Solution Check */
      Double mult;
      for (i=this.NBrsEff; i>=1; i--) {
          for (j=i-1; j>=1; j--) {
              mult = mat[j][i];
              mat[j][i] = 0.0;
              mat[j][this.NKolEffAug] -= mult * mat[i][this.NKolEffAug];
          }
      }
      solution = "";
      for (i=1; i<=this.NBrsEff; i++) {
          solution += ("x" + Integer.toString(i) + " = " + Double.toString(mat[i][this.NKolEffAug]));
          if (i != this.NBrsEff) {
              solution += (", ");
          }
      }
      return (solution);
  }

  public String solutionGJ()
  // I.S
  // F.S mengeluarkan string solusi persamaan, tulisan "Tidak mempunyai solusi", ataupun solusi parametrik
  {
      int i, j;   // iterator var
      int ct; // counter variabel
      String solution;

      Double[][] mat = gaussJordan(this.matOri);

      /* Parametrik Check */
      boolean parametrik = false; // parametrik solution indicator
      ct = 0;
      for (i=this.NBrsEff; i>=1; i--) {
          ct = 0;
          for (j=1; j<=this.NKolEffAug; j++) {
              if (mat[i][j] == 0) {
                  ct++;
              }
          }
          if (ct == this.NKolEffAug) {
              parametrik = true;
              break;
          }
      }
      if (parametrik) {
          int k; double mult;
          for (i=this.NBrsEff; i>=1; i--) {
              for (j=1; j<=this.NKolEff; j++) {
                  if (mat[i][j] == 1) {
                      for (k=i-1; k>=1; k--) {
                          mult = mat[k][j];
                          mat[k][j] = 0.0;
                          mat[k][this.NKolEffAug] -= mult * mat[i][this.NKolEffAug];
                      }
                      break;
                  }
              }
          }

          // Obtain free var col
          int[] freeVarCol = obtainFreeVarCol(mat);
          int freeVarColCount = freeVarCol.length;

          // Produce solution
          solution = "";
          char[] constChar = { 's', 't', 'u', 'v', 'w', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l' };
          int constCharIdx;
          for (j=1; j<=this.NKolEff; j++) {
              if (isColFree(freeVarCol, freeVarColCount, j)) {
                  constCharIdx = searchFreeVarIdx(freeVarCol, freeVarColCount, j);
                  solution += "x" + Integer.toString(j) + " = " + constChar[constCharIdx];
              } else {
                  for (i=1; i<=this.NBrsEff; i++) {
                      if (mat[i][j] == 1) {
                          solution += "x" + Integer.toString(j) + " =";
                          if (mat[i][this.NKolEffAug] != 0) {
                              solution += " " + mat[i][this.NKolEffAug];
                          } else if (mat[i][this.NKolEffAug] == 0 && !isColFree(freeVarCol, freeVarColCount, this.NKolEffAug)) {
                              solution += " " + mat[i][this.NKolEffAug];
                          }
                          for (k=0; k<freeVarColCount; k++) {
                              if (mat[i][freeVarCol[k]] > 0) {
                                  solution += " - " + mat[i][freeVarCol[k]] + constChar[k];
                              } else if (mat[i][freeVarCol[k]] < 0) {
                                  solution += " + " + (-1 * mat[i][freeVarCol[k]]) + constChar[k];
                                  if (k != freeVarColCount) {
                                      solution += " ";
                                  }
                              }
                          }
                          break;
                      }
                  }
              }
              if (j != this.NBrsEff) {
                  solution += "\n";
              }
          }
          return (solution);
      }

      /* No Solution Check */
      boolean noSolution = false; // no solution indicator
      ct = 0;
      for (i=this.NBrsEff; i>=1; i--) {
          ct = 0;
          for (j=1; j<=this.NKolEff; j++) {
              if (mat[i][j] == 0) {
                  ct++;
              }
          }
          if (ct == this.NKolEff) {
              noSolution = true;
              break;
          }
      }
      if (noSolution) {
          return ("Solusi tidak ada");
      }

      /* Solution Check */
      solution = "";
      for (i=1; i<=this.NBrsEff; i++) {
          solution += ("x" + Integer.toString(i) + " = " + Double.toString(mat[i][this.NKolEffAug]));
          if (i != this.NBrsEff) {
              solution += (", ");
          }
      }
      return (solution);
  }

  public void printmat(Double[][] mat) {
      int i, j;

      for (i=1; i<=this.NBrsEff; i++) {
          for (j=1; j<=this.NKolEffAug; j++) {
              System.out.print(Double.toString(mat[i][j]) + " ");
          }
          System.out.println("");
      }
      System.out.println("");
  }

  // Gauss Elimination Method
  // Procedur Swap Baris
  private void SwapBaris(Double[][] Matriks, int Brs1, int Brs2){
          //Brs1 dan Brs2 merupakan matriks yang mau di swap
          int j;
          double Temp;
          for (j=1; j<=this.NKolEffAug; j++){
              Temp=Matriks[Brs1][j];
              Matriks[Brs1][j]=Matriks[Brs2][j];
              Matriks[Brs2][j]=Temp;
          }
  }

  //Procedur Gauss
  private Double[][] gauss(Double[][] mtrxInp){
      /*KAMUS*/
      int i, j, k;
      int pass;
      double Max, Temp;
      int BrsMax;


      /*ALGORITMA*/
      //Mencari maksimum dari kolom pertama
      Max=mtrxInp[1][1];
      BrsMax=1;
      for (i=2;i<=NBrsEff;i++){
          if (mtrxInp[i][1]>Max){
              Max=mtrxInp[i][1];
              BrsMax=i;
          }
      }

      //Baris di swap
      SwapBaris(mtrxInp, 1, BrsMax);

      //Membentuk segitiga 0 di kiri bawah
      for (pass=1;pass<=this.NKolEffAug-2;pass++){
          for (i=pass+1;i<=NBrsEff;i++){
              Temp=mtrxInp[i][pass]/mtrxInp[pass][pass];
              for (j=1;j<=this.NKolEffAug;j++){
                  mtrxInp[i][j]=mtrxInp[i][j]-(Temp*mtrxInp[pass][j]);
              }
          }
      }

      //Membentuk leading coefficients dari tiap baris menjadi 1
      for (i=1;i<=NBrsEff;i++){
          for (j=1;j<=this.NKolEffAug-1;j++){
              if (mtrxInp[i][j]!=0.0){
                  Temp=mtrxInp[i][j];
                  for (k=1;k<=this.NKolEffAug;k++){
                      if (mtrxInp[i][k]!=0.0){
                          mtrxInp[i][k]=mtrxInp[i][k]/Temp;
                      }
                  }
                  break;
              }
          }
      }

      return mtrxInp;
  }

  // Gauss-Jordan Elimination Method
  private Double[][] gaussJordan(Double[][] mtrxInp){
      /*KAMUS*/
      int i, j, k;
      double Temp;
      /*ALGORITMA*/
      //Mengubah matriks jadi bentuk row echelon
      mtrxInp = gauss(mtrxInp);
      printmat(mtrxInp);
      //Reverse engineering Gauss
      for (k=this.NBrsEff;k>=1;k--){
          for (i=k-1; i>=1; i--){
              Temp = mtrxInp[i][k]/mtrxInp[k][k];
              for (j=this.NKolEffAug;j>=1;j--){
                  mtrxInp[i][j] -= Temp*mtrxInp[k][j];
              }
          }
      }

      return mtrxInp;
  }

  // Interpolate Gauss
  public Double[] interpolateG()
  // I.S
  // F.S mengembalikan array yang berisi solusi dari permasalahan interpolasi yang diselesaikan dengan metode Gauss
  {
    int i, j;   // iterator var
    Double[] solutionArr = new Double[this.NKolEff];

    // ubah matriks original menjadi bentuk eselon dengan menggunakan metode gauss
    Double[][] mat = gauss(this.matOri);

    /* Solution Check */
    Double mult;
    for (i=this.NBrsEff; i>=1; i--) {
      for (j=i-1; j>=1; j--) {
        mult = mat[j][i];
        mat[j][i] = 0.0;
        mat[j][this.NKolEffAug] -= mult * mat[i][this.NKolEffAug];
      }
    }
    for (i=1; i<=this.NBrsEff; i++) {
      solutionArr[i-1] = mat[i][this.NKolEffAug];
    }

    return (solutionArr);
  }

  // Interpolate Gauss-Jordan
  public Double[] interpolateGJ()
  // I.S
  // F.S mengembalikan array yang berisi solusi dari permasalahan interpolasi yang diselesaikan dengan metode Gauss-Jordan
  {
    int i, j;   // iterator var
    Double[] solutionArr = new Double[this.NKolEff];

    Double[][] mat = gaussJordan(this.matOri);

    /* Solution Check */
    for (i=1; i<=this.NBrsEff; i++) {
      solutionArr[i-1] = mat[i][this.NKolEffAug];
    }

    return (solutionArr);
  }

}
