package thesis;

class ElliotGBrainClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = ElliotGBrainClassifier.N592c17ce0(i);
    return p;
  }
  static double N592c17ce0(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 3.7532748E-4) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 3.7532748E-4) {
    p = ElliotGBrainClassifier.N5dcf031e1(i);
    } 
    return p;
  }
  static double N5dcf031e1(Object []i) {
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
}
