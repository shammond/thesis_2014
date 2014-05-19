package thesis;

class ElliotGBrainStateClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = ElliotGBrainStateClassifier.N53d672440(i);
    return p;
  }
  static double N53d672440(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 3.7532748E-4) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 3.7532748E-4) {
    p = ElliotGBrainStateClassifier.N23e7f95c1(i);
    } 
    return p;
  }
  static double N23e7f95c1(Object []i) {
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
