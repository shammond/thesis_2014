package thesis;

class EmmaTBrainClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = EmmaTBrainClassifier.N2e41d9a20(i);
    return p;
  }
  static double N2e41d9a20(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.523561) {
    p = EmmaTBrainClassifier.N653732671(i);
    } else if (((Double) i[6]).doubleValue() > 0.523561) {
    p = EmmaTBrainClassifier.N69cc16273(i);
    } 
    return p;
  }
  static double N653732671(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() <= 0.022643920034) {
    p = EmmaTBrainClassifier.Nace3c1b2(i);
    } else if (((Double) i[0]).doubleValue() > 0.022643920034) {
      p = 0;
    } 
    return p;
  }
  static double Nace3c1b2(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.6) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() > 0.6) {
      p = 0;
    } 
    return p;
  }
  static double N69cc16273(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 0.001201708568) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() > 0.001201708568) {
      p = 0;
    } 
    return p;
  }
}
