package com.company;

public class Matrix {
    Double[][] mat;

    int NBrsEff;
    int NKolEff;

    Matrix() {
    }

    Matrix(Double[][] mtrxHasil, int NBrsEff, int NKolEff) {
        mat = new Double[NBrsEff][NKolEff];
        mat = mtrxHasil;
    }

    private static int[] obtainFreeVarCol(float[][] mat, int matLen)
    // I.S augmented matrix, ukuran matrix
    // F.S mengeluarkan array menunjukkan kolom mana saja yang merupakan free variabel (freeVarCol)
    {
        int i, j;

        boolean[] notFreeVarColBol = new boolean[matLen];
        // init not free var col
        for (j=0; j<matLen; j++) {
            notFreeVarColBol[j] = true;
        }
        // mark not free var col
        int notFreeVarColCount = 0;
        for (i=0; i<matLen; i++) {
            for (j=0; j<matLen; j++) {
                if (mat[i][j] == 1) {
                    notFreeVarColBol[j] = false;
                    notFreeVarColCount++;
                    break;
                }
            }
        }
        // get free var col array
        int freeVarColCount = 0;
        int[] freeVarCol = new int[matLen - notFreeVarColCount];
        for (j=0; j<matLen; j++) {
            if (notFreeVarColBol[j]) {
                freeVarCol[freeVarColCount] = j;
                freeVarColCount++;
            }
        }

        return freeVarCol;
    }

    private static boolean isColFree(int[] arr, int arrLen, int x)
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

    private static int searchFreeVarIdx(int[] arr, int arrLen, int x)
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

    public static String solutionG(float[][] mat)
    // I.S augmented matrix yang telah berbentuk matriks eselon
    // F.S mengeluarkan string solusi persamaan, tulisan "Tidak mempunyai solusi", ataupun solusi parametrik
    {
        int i, j;   // iterator var
        int ct; // counter variabel
        String solution;

        int l = mat.length; // matrix length

        /* Parametrik Check */
        boolean parametrik = false; // parametrik solution indicator
        ct = 0;
        for (i=l-1; i>=0; i--) {
            ct = 0;
            for (j=0; j<l+1; j++) {
                if (mat[i][j] == 0) {
                    ct++;
                }
            }
            if (ct == l + 1) {
                parametrik = true;
                break;
            }
        }
        if (parametrik) {
            int k; float mult;
            for (i=l-1; i>0; i--) {
                for (j=0; j<l; j++) {
                    if (mat[i][j] == 1) {
                        for (k=i-1; k>=0; k--) {
                            mult = mat[k][j];
                            mat[k][j] = 0;
                            mat[k][l] -= mult * mat[i][l];
                        }
                        break;
                    }
                }
            }

            // Obtain free var col
            int[] freeVarCol = obtainFreeVarCol(mat, l);
            int freeVarColCount = freeVarCol.length;

            // Produce solution
            solution = "";
            char[] constChar = { 's', 't', 'u', 'v', 'w', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l' };
            int constCharIdx;
            for (j=0; j<l; j++) {
                if (isColFree(freeVarCol, freeVarColCount, j)) {
                    constCharIdx = searchFreeVarIdx(freeVarCol, freeVarColCount, j);
                    solution += "x" + Integer.toString(j + 1) + " = " + constChar[constCharIdx];
                } else {
                    for (i=0; i<l; i++) {
                        if (mat[i][j] == 1) {
                            solution += "x" + Integer.toString(j+1) + " =";
                            if (mat[i][l] != 0) {
                                solution += " " + mat[i][l];
                            } else if (mat[i][l] == 0 && !isColFree(freeVarCol, freeVarColCount, l)) {
                                solution += " " + mat[i][l];
                            }
                            for (k=0; k<freeVarColCount; k++) {
                                if (mat[i][freeVarCol[k]] > 0) {
                                    solution += " - " + mat[i][freeVarCol[k]] + constChar[k];
                                } else if (mat[i][freeVarCol[k]] < 0) {
                                    solution += " " + mat[i][freeVarCol[k]] + constChar[k];
                                    if (k != freeVarColCount - 1) {
                                        solution += " ";
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
                if (j != l - 1) {
                    solution += "\n";
                }
            }
            return (solution);
        }

        /* No Solution Check */
        boolean noSolution = false; // no solution indicator
        ct = 0;
        for (i=l-1; i>=0; i--) {
            ct = 0;
            for (j=0; j<l; j++) {
                if (mat[i][j] == 0) {
                    ct++;
                }
            }
            if (ct == l && mat[i][j] != 0) {
                noSolution = true;
                break;
            }
        }
        if (noSolution) {
            return ("Solusi tidak ada");
        }

        /* Solution Check */
        float mult;
        for (i=l-1; i>0; i--) {
            for (j=i-1; j>=0; j--) {
                mult = mat[j][i];
                mat[j][i] = 0;
                mat[j][l] -= mult * mat[j+1][l];
            }
        }
        solution = "";
        for (i=0; i<l; i++) {
            solution += ("x" + Integer.toString(i+1) + " = " + Float.toString(mat[i][l]));
            if (i != l - 1) {
                solution += (", ");
            }
        }
        return (solution);
    }

    public static String solutionGJ(float[][] mat)
    // I.S augmented matrix yang telah berbentuk reduced row eselon form
    // F.S mengeluarkan string solusi persamaan, tulisan "Tidak mempunyai solusi", ataupun solusi parametrik
    {
        int i, j;   // iterator var
        int ct; // counter variabel
        String solution;

        int l = mat.length; // matrix length

        /* Parametrik Check */
        boolean parametrik = false; // parametrik solution indicator
        ct = 0;
        for (i=l-1; i>=0; i--) {
            ct = 0;
            for (j=0; j<l+1; j++) {
                if (mat[i][j] == 0) {
                    ct++;
                }
            }
            if (ct == l + 1) {
                parametrik = true;
                break;
            }
        }
        if (parametrik) {
            // Obtain free var col
            int[] freeVarCol = obtainFreeVarCol(mat, l);
            int freeVarColCount = freeVarCol.length;

            // Produce solution
            int k;
            solution = "";
            char[] constChar = { 's', 't', 'u', 'v', 'w', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l' };
            int constCharIdx;
            for (j=0; j<l; j++) {
                if (isColFree(freeVarCol, freeVarColCount, j)) {
                    constCharIdx = searchFreeVarIdx(freeVarCol, freeVarColCount, j);
                    solution += "x" + Integer.toString(j + 1) + " = " + constChar[constCharIdx];
                } else {
                    for (i=0; i<l; i++) {
                        if (mat[i][j] == 1) {
                            solution += "x" + Integer.toString(j+1) + " =";
                            if (mat[i][l] != 0) {
                                solution += " " + mat[i][l];
                            }
                            for (k=0; k<freeVarColCount; k++) {
                                if (mat[i][freeVarCol[k]] > 0) {
                                    solution += " - " + mat[i][freeVarCol[k]] + constChar[k];
                                } else if (mat[i][freeVarCol[k]] < 0) {
                                    solution += " " + mat[i][freeVarCol[k]] + constChar[k];
                                    if (k != freeVarColCount - 1) {
                                        solution += " ";
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
                if (j != l - 1) {
                    solution += "\n";
                }
            }
            return (solution);
        }

        /* No Solution Check */
        boolean noSolution = false; // no solution indicator
        ct = 0;
        for (i=l-1; i>=0; i--) {
            ct = 0;
            for (j=0; j<l; j++) {
                if (mat[i][j] == 0) {
                    ct++;
                }
            }
            if (ct == l && mat[i][j] != 0) {
                noSolution = true;
                break;
            }
        }
        if (noSolution) {
            return ("Solusi tidak ada");
        }

        /* Solution Check */
        solution = "";
        for (i=0; i<l; i++) {
            solution += ("x" + Integer.toString(i+1) + " = " + Float.toString(mat[i][l]));
            if (i != l - 1) {
                solution += ("\n");
            }
        }
        return (solution);
    }

    public static void printmat(float[][] mat) {
        int i, j;
        for (i=0; i<mat.length; i++) {
            for (j=0; j<mat[0].length; j++) {
                System.out.print(Float.toString(mat[i][j]) + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }


    // Gauss Elimination Method

    // Gauss-Jordan Elimination Method

    // Interpolate Matrix

}
