/**Ant Colony Optimization*/
class ACO {
  static final int n    = 2;           // Anzahl Design Variables
  static final int nbit = 20;          // Anzahl Bits fuer Codierung
  static final int ngen = 150;         // Anzahl Iterationen
  static final int npop = 200;         // Populationsgroesse
  static final double xmin = -5.0;     // x-range
  static final double xmax =  5.0;     //
  static double Delta = (xmax-xmin)/Math.pow(2.0, nbit/2);           
  static double Offset = 0.0;          // Offset for function weighting 
  static double evap   = 0.2;          // evaporation factor  
  static double Q      = 0.05;         // Schwellwert / Elite-Bypass
  static double phi    = 3.0;          // Pheromon-Ausgleichs-Parameter (verhindert zu schnelle Konvergenz)  
  // objective function f (Rosenbrock function / Banana function)
  static double Obj_f(double[] x) {
    return (1.0-x[0])*(1.0-x[0]) + 100.0*(x[1]-x[0]*x[0])*(x[1]-x[0]*x[0]);
  }
  // Decoding bits --> double
  static double[] getVal(int[] bit) {
    double[] x    = new double[n];        // Values
    double fac = 1.0;

    for (int m = 1; m<=nbit/n; m++)  {   // Decoding bits --> double 
      for (int j = 0; j<n; j++)
      x[j] = x[j] + fac*bit[(j+1)*nbit/n-m];
      fac= fac*2.0;
    }
    for (int j = 0; j<n; j++)   x[j] = xmin+x[j]*Delta;   // Scaling to grid
    return x;
  }                     

  public static void main(String[] args)  {
    int[][] ant  = new int[npop][nbit];    // Ants -- coded design variables
    int[] bestAnt= new int[nbit];          // best Ant
    double[] x   = new double[n];          // Design Variablen
    double[][] tau = new double[nbit][n];  // Pheromone Matrix
    double[][] Dtau= new double[nbit][n];  //
    double[] tauTot= new double[nbit];     // Pheromone total pro moeglicher Wert
    double fbest=1000.0;                   // global best fitness
    double fnew;
    int pbest=0;                           // index best ant  
                                  // Genesis
    for (int p = 0; p<npop; p++) {  
      for (int b = 0; b<nbit; b++) ant[p][b] = (int) (2*Math.random());  
      fnew = Obj_f(getVal(ant[p]));
      if (fnew < fbest) {
        pbest=p;                  // Index best ant
        fbest=fnew;
      }
      for (int b = 0; b<nbit; b++)
        tau[b][ant[p][b]] = tau[b][ant[p][b]] + evap /(Offset+fnew);
    }      
    for (int b = 0; b<nbit; b++)  bestAnt[b]= ant[pbest][b];
// hier starten die Ameisen     
    for (int g=1; g<=ngen; g++)  {         // Loop "Generationen" / Iterationen    
      for (int p = 0; p<npop ; p++) {      // Loop ueber alle Ameisen   
        for (int b = 0; b<nbit; b++) {
          tauTot[b]=tau[b][0];
          for (int m = 1; m<n; m++) tauTot[b] += tau[b][m];
        }   
        if (Math.random() < Q )            // Schwellwert / Elite-Bypass
           for (int b = 0; b<nbit; b++)  ant[p][b]= bestAnt[b];
          else {
           for (int b = 0; b<nbit; b++)
             if( Math.random() < tau[b][1]/tauTot[b]) ant[p][b]= 1;
               else ant[p][b]= 0;
          }
      }                                        // Pheromone update
      for (int b = 0; b<nbit; b++)
        for (int m = 0; m<n; m++) Dtau[b][m] = 0.0;
      fbest=1000.0;
      for (int p = 0; p<npop ; p++) {          // und finden beste Loesung
        fnew = Obj_f(getVal(ant[p]));
        if (fnew < fbest) {
          pbest=p;                            // Index best ant
          fbest=fnew;
        }
        for (int b = 0; b<nbit; b++)           // Pheromone Matrix abfuellen
          Dtau[b][ant[p][b]] = Dtau[b][ant[p][b]] + 1.0 /(Offset+fnew);
      } 
      for (int b = 0; b<nbit; b++)             // Pheromone update und Verwitterung
        for (int m = 0; m<n; m++) tau[b][m] = (1.0-evap)*tau[b][m] + evap * Dtau[b][m];
      for (int b = 0; b<nbit; b++)  bestAnt[b]= ant[pbest][b];   // neue beste Loesung
      for (int b = 0; b<nbit; b++)  {          // Pheromon-Ausgleich
        tau[b][0] = (phi*tau[b][0] +     tau[b][1])/(1.0+phi);
        tau[b][1] = (    tau[b][0] + phi*tau[b][1])/(1.0+phi);
      }
      System.out.print(" iteration "+g);
      x= getVal(bestAnt);
      for (int m = 0; m<n; m++) InOut.print(x[m], 5,6);
      System.out.println("  f  "+Obj_f(x));
    }
  }
}