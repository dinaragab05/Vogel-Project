/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vog1;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author Teba
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    Label r;

    @FXML
    TextField n1;
    @FXML
    TextField n2;
    @FXML
    TextField n3;
    @FXML
    TextField n4;
    @FXML
    TextField s1;
    @FXML
    TextField s2;
    @FXML
    TextField s3;
    @FXML
    TextField a1;
    @FXML
    TextField a2;
    @FXML
    TextField a3;
    @FXML
    TextField a4;
    @FXML
    TextField a5;
    @FXML
    TextField a6;
    @FXML
    TextField a7;
    @FXML
    TextField a8;
    @FXML
    TextField a9;
    @FXML
    TextField a10;
    @FXML
    TextField a11;
    @FXML
    TextField a12;

    @FXML
    public void solve() {

        int Rows = 3;
        int Cols = 4;

        int[] needings = new int[Cols];
        int[] supplies = new int[Rows];
        int[][] tableCost = new int[Rows][Cols];
        boolean[] rowDone = new boolean[Rows];
        boolean[] colDone = new boolean[Cols];
        int[][] Results = new int[Rows][Cols];
        int finalCost = 0;

        needings[0] = Integer.parseInt(n1.getText());
        needings[1] = Integer.parseInt(n2.getText());
        needings[2] = Integer.parseInt(n3.getText());
        needings[3] = Integer.parseInt(n4.getText());

        supplies[0] = Integer.parseInt(s1.getText());
        supplies[1] = Integer.parseInt(s2.getText());
        supplies[2] = Integer.parseInt(s3.getText());

        tableCost[0][0] = Integer.parseInt(a1.getText());
        tableCost[0][1] = Integer.parseInt(a2.getText());
        tableCost[0][2] = Integer.parseInt(a3.getText());
        tableCost[0][3] = Integer.parseInt(a4.getText());
        tableCost[1][0] = Integer.parseInt(a5.getText());
        tableCost[1][1] = Integer.parseInt(a6.getText());
        tableCost[1][2] = Integer.parseInt(a7.getText());
        tableCost[1][3] = Integer.parseInt(a8.getText());
        tableCost[2][0] = Integer.parseInt(a9.getText());
        tableCost[2][1] = Integer.parseInt(a10.getText());
        tableCost[2][2] = Integer.parseInt(a11.getText());
        tableCost[2][3] = Integer.parseInt(a12.getText());

        VAMAlgorithm vam = new VAMAlgorithm(Rows, Cols);

        vam.getArrays(needings, supplies, tableCost);
        vam.Run();
        Results = vam.getResult();
        finalCost = vam.getTotalCost();

        //åÊÊãÓÍ ÏáæÞÊ-------------------
        Arrays.stream(Results).forEach(a -> System.out.println(Arrays.toString(a))); //ÈÍæá Þíã ÇáÇÑÑÇì Çááì åì ÇÑÞÇã á ÓÊÑäÌ áÇÊãßä ãä ØÈÇÚÊåÇ 
        r.setText(finalCost + "");

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public class VAMAlgorithm {

        private int[] needs;
        private int[] supply;
        private int[][] costs;
        private int nRows;
        private int nCols;
        private boolean[] rowDone;
        private boolean[] colDone;
        private int[][] result;
        private int totalCost;
        private ExecutorService es;

        //initial values and constructor-----------------------
        VAMAlgorithm(int rows, int cols) {
            this.totalCost = 0;
            this.nRows = rows;
            this.nCols = cols;
            this.needs = new int[this.nCols];
            this.supply = new int[this.nRows];
            this.costs = new int[this.nRows][this.nCols];
            this.result = new int[this.nRows][this.nCols];
            this.rowDone = new boolean[this.nRows];
            this.colDone = new boolean[this.nCols];
            this.es = Executors.newFixedThreadPool(2);

        }

        //Set values of variables
        void getArrays(int[] n, int[] s, int[][] c) {
            int i = 0, j = 0;
            for (i = 0; i < this.nCols; i++) {
                this.needs[i] = n[i];
            }

            for (j = 0; j < this.nRows; j++) {
                this.supply[j] = s[j];
            }

            for (i = 0; i < this.nRows; i++) {
                for (j = 0; j < this.nCols; j++) {
                    this.costs[i][j] = c[i][j];
                }

            }

        }

        //Get the final values
        int getTotalCost() {
            return this.totalCost;
        }

        int[][] getResult() {
            return this.result;
        }

        //
        void Run() {
            int supplyLeft = Arrays.stream(this.supply).sum();

            while (supplyLeft > 0) { // supply left means total supply "sum of all supply"
                try {
                    int[] cell = this.nextCell();
                    int r = cell[0];
                    int c = cell[1];

                    //chek which is min to select 
                    int quantity = Math.min(this.needs[c], this.supply[r]);//"check wich is min potential or needs "
                    this.needs[c] -= quantity; // the min will be subtracted from the needs
                    if (this.needs[c] == 0) { // needs become zero ="0"  ???? ??????             
                        this.colDone[c] = true;
                    }

                    this.supply[r] -= quantity;  // ??? ???? ?? ???? ????  " ????? ????"
                    if (this.supply[r] == 0) {
                        this.rowDone[r] = true;
                    }

                    this.result[r][c] = quantity; // ?? ????? ???? ???? ??????? ???? ?? ?????? ?? ?????
                    supplyLeft -= quantity; // ??? ?? ???? ??????? ??????? 

                    totalCost += quantity * this.costs[r][c]; //?????? ???? ???? ???? ?????? * ??????? ??????
                } catch (Exception e) {
                    System.out.print(e);
                }
            }

            this.es.shutdown();
        }

        //add pointers to move in each cell in rows or col 
        int[] nextCell() throws Exception {

            Future<int[]> f1 = es.submit(() -> maxPenalty(nRows, nCols, true));
            Future<int[]> f2 = es.submit(() -> maxPenalty(nCols, nRows, false));

            int[] res1 = f1.get();
            int[] res2 = f2.get();

            if (res1[3] == res2[3]) {
                return res1[2] < res2[2] ? res1 : res2;
            }
            return (res1[3] > res2[3]) ? res2 : res1;
        }

        //sort row "or" col  to find min in cost ....then pointer on the 2 min cost to subtracted 
        int[] diff(int j, int len, boolean isRow) {
            int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
            int minP = -1;
            for (int i = 0; i < len; i++) {
                if (isRow ? colDone[i] : rowDone[i]) {
                    continue;
                }
                int c = isRow ? costs[j][i] : costs[i][j];
                if (c < min1) {
                    min2 = min1;
                    min1 = c;
                    minP = i;
                } else if (c < min2) {
                    min2 = c;
                }
            }
            return new int[]{min2 - min1, min1, minP};
        }

        //????? ??? ????  ??? ???? ??? ?? ?? ???? ? ???? ??? ?? ????? ? ???? ? ??? ????? ? ??????
        int[] maxPenalty(int len1, int len2, boolean isRow) {
            int md = Integer.MIN_VALUE;
            int pc = -1, pm = -1, mc = -1;
            for (int i = 0; i < len1; i++) {
                if (isRow ? rowDone[i] : colDone[i]) {
                    continue;
                }
                int[] res = diff(i, len2, isRow);
                if (res[0] > md) {
                    md = res[0];  // max diff
                    pm = i;       // pos of max diff
                    mc = res[1];  // min cost
                    pc = res[2];  // pos of min cost
                }
            }
            return isRow ? new int[]{pm, pc, mc, md} : new int[]{pc, pm, mc, md};
        }

    }

}
