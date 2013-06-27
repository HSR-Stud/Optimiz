/**Particle Swarm Optimization */

class PSO {
  
  static final int n    = 5;           // number of design variables
  static final int ngen = 150;         // number of generations
  static final int npop = 100;         // Population size
  static final double xmin = -5.0;     // range
  static final double xmax =  5.0;     //
  
  static double w =1.0;                // inertia weight  
  static double c1=2.0;                // cognitive parameter  
  static double c2=2.0;                // social parameter  

// objective function f (Rosenbrock function / Banana function)
  static double Obj_f(double[] x) {
      //return (1.0-x[0])*(1.0-x[0]) + 100.0*(x[1]-x[0]*x[0])*(x[1]-x[0]*x[0]);
      return 2.0*(x[0]*x[0] + x[1]*x[1] - 2.0*x[0] - 2.0*x[1] - x[2] -x[3])
           +  x[2]*x[2] + x[3]*x[3] +(15.0+x[4]*x[4])/2.0 - x[4];
  }
                        
  public static void main(String[] args)  {
    double[][] x = new double[npop][n];    // design variable vector's
    double[][] v = new double[npop][n];    // design velocity vector's  
    double[]   f = new double[npop];       // best "Fitness"  
    double[][] ibest= new double[npop][n]; // individual best position  
    double[] gbest  = new double[n];       // global best position  
    double fbest;                          // global best fitness
    double fnew;
                                     // Genesis     
    for (int p = 0; p<npop; p++) {
      for (int m = 0; m<n; m++) x[p][m] = xmin+(xmax-xmin)*Math.random(); 
        f[p] = 10000.0;
    }
    fbest  = 10000.0;

// hier startet die "Evolution"    
    for (int g=1; g<=ngen; g++)  {         // loop over generations
  
      for (int p = 0; p<npop ; p++) {      // search individual best and global best
        fnew=Obj_f(x[p]);
        if ( fnew < f[p] )  {            // individual best
          f[p] = fnew;
          for (int m = 0; m<n; m++)  ibest[p][m]= x[p][m];
          if ( fnew < fbest )  {       // global best
            fbest = fnew;
            for (int m = 0; m<n; m++)  gbest[m]= x[p][m];
          }
        }
      }   

      System.out.print(" generation "+g);
      for (int m = 0; m<n; m++) InOut.print(gbest[m], 5,6);
      System.out.println("  w "+w+"  f  "+fbest);
      for (int p = 0; p<npop ; p++)        // update velocity & position
        for (int m = 0; m<n; m++) {
          v[p][m] = w*v[p][m] + c1*Math.random()*(ibest[p][m] - x[p][m])
                              + c2*Math.random()*(gbest[m] - x[p][m]);
          x[p][m] = x[p][m] + v[p][m] ;
        }      
      w=w*0.99;                          // inertia weight changes
    }
  }
}