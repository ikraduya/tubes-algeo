

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
      Double[][] mat = metode(this.matOri,0);

      /* No Solution Check */
      boolean noSolution = false; // no solution indicator
      ct = 0;
      for (i=this.NBrsEff; i>=1; i--) {
          ct = 0;
          for (j=1; j<=this.NKolEff; j++) {
              if (mat[i][j] == 0.0) {
                  ct++;
              }
          }
          if ((ct == this.NKolEff) && (mat[i][NKolEffAug] != 0)) {
              noSolution = true;
              break;
          }
      }
      if (noSolution) {
          return ("Solusi tidak ada");
      }

      /* Parametrik Check */
      boolean parametrik = false; // parametrik solution indicator
      ct = 0;
      for (i=this.NBrsEff; i>=1; i--) {
          ct = 0;
          for (j=1; j<=this.NKolEffAug; j++) {
              if (mat[i][j] == 0.0) {
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
          // freeVarCol = [2, 3]
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
              if (j != this.NKolEff) {
                  solution += "\n";
              }
          }
          return (solution);
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

      Double[][] mat = metode(this.matOri,1);

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
              if (j != this.NKolEff) {
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


  public Double[][] metode(Double[][] mtrxInp, int x){
    int posisiBar = 1;
    int searchBar;
    int posisiKol = 1;
    while (posisiKol <= NKolEff){
      searchBar = posisiBar;
      while ((mtrxInp[searchBar][posisiKol] != 1) && (searchBar < NBrsEff)){
        searchBar++;
      }
      if (mtrxInp[searchBar][posisiKol] != 1){
        searchBar = posisiBar;
        while ((mtrxInp[searchBar][posisiKol] == 0) && (searchBar < NBrsEff)){
          searchBar++;
        }
      }
      if (mtrxInp[searchBar][posisiKol] != 0){
        swapBaris(mtrxInp, posisiBar, searchBar);
      /*  TulisMatrix();
        System.out.println("swapBaris");*/
        Jadikan1(mtrxInp, mtrxInp[posisiBar][posisiKol], posisiBar);
      /*  TulisMatrix();
        System.out.println("Jadikan1");*/
        EleminasiKolom(mtrxInp, x, posisiBar, posisiKol);
      /*  TulisMatrix();
        System.out.println("EleminasiKolom");*/
        posisiBar++;
      }
      /*TulisMatrix(mtrxInp);
      System.out.println("-");*/
      posisiKol++;

    }
    normalisasi(mtrxInp);
    TulisMatrix(mtrxInp);
    return mtrxInp;
  }

  public void swapBaris(Double[][] mtrxInp, int posisiBar, int searchBar){
    Double temp;
    for (int i = 1; i <= NKolEffAug; i++){
      temp = mtrxInp[posisiBar][i];
      mtrxInp[posisiBar][i] = mtrxInp[searchBar][i];
      mtrxInp[searchBar][i] = temp;
    }
  }

  public void Jadikan1(Double[][] mtrxInp, Double pembagi, int posisiBar){
    for (int i = 1; i <= NKolEffAug; i++){
      mtrxInp[posisiBar][i] = mtrxInp[posisiBar][i] / pembagi;
    }
  }

  public void EleminasiKolom(Double[][] mtrxInp, int x, int posisiBar, int posisiKol){
    int mulaiBrs;
    if (x == 0) //Gauss
      mulaiBrs = posisiBar;
    else //GaussJordan
      mulaiBrs = 1;
    for (int i = mulaiBrs; i <= NBrsEff; i++){
      if (i != posisiBar){
        EleminasiBaris(mtrxInp, i, mtrxInp[i][posisiKol] / mtrxInp[posisiBar][posisiKol], posisiBar, posisiKol);
      }
    }
  }

  public void EleminasiBaris(Double[][] mtrxInp, int brs, Double pengali, int posisiBar, int posisiKol){
    for (int i =1; i <= NKolEffAug; i++){
      mtrxInp[brs][i] -= mtrxInp[posisiBar][i]*pengali;
    }
  }

  public void TulisMatrix(Double[][] mtrxInp){
    for (int i = 1; i <= NBrsEff; i++){
      for (int j = 1; j <= NKolEffAug; j++){
        System.out.print(mtrxInp[i][j] + " ");
      }
      System.out.println();
    }
  }

  public void normalisasi(Double[][] mtrxInp){
    for (int i = 1; i <= NBrsEff; i++){
      for (int j = 1; j <= NKolEffAug; j++){
        if (mtrxInp[i][j]-1 == -1){
          mtrxInp[i][j] = 0.0;
        }
      }
    }
  }







  // Interpolate Gauss
  public Double[] interpolateG()
  // I.S
  // F.S mengembalikan array yang berisi solusi dari permasalahan interpolasi yang diselesaikan dengan metode Gauss
  {
    int i, j;   // iterator var
    Double[] solutionArr = new Double[this.NKolEff];

    // ubah matriks original menjadi bentuk eselon dengan menggunakan metode gauss
    Double[][] mat = metode(this.matOri,0);

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

    Double[][] mat = metode(this.matOri,1);

    /* Solution Check */
    for (i=1; i<=this.NBrsEff; i++) {
      solutionArr[i-1] = mat[i][this.NKolEffAug];
    }

    return (solutionArr);
  }

}
