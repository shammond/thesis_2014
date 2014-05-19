package thesis;

class Sub1conds2alldataClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = Sub1conds2alldataClassifier.N273ea9d00(i);
    return p;
  }
  static double N273ea9d00(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.505518) {
    p = Sub1conds2alldataClassifier.N2feaad11(i);
    } else if (((Double) i[6]).doubleValue() > 0.505518) {
      p = 1;
    } 
    return p;
  }
  static double N2feaad11(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 9.87787498E-4) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 9.87787498E-4) {
    p = Sub1conds2alldataClassifier.N1d8080e02(i);
    } 
    return p;
  }
  static double N1d8080e02(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() <= 0.420191) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() > 0.420191) {
    p = Sub1conds2alldataClassifier.N5f8d01413(i);
    } 
    return p;
  }
  static double N5f8d01413(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.651868) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 0.651868) {
      p = 0;
    } 
    return p;
  }
}
