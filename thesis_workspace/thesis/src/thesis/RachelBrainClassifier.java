package thesis;

class RachelBrainClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = RachelBrainClassifier.N522a533e0(i);
    return p;
  }
  static double N522a533e0(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 0;
    } else if (((Double) i[4]).doubleValue() <= 0.6) {
    p = RachelBrainClassifier.N58bf35961(i);
    } else if (((Double) i[4]).doubleValue() > 0.6) {
      p = 1;
    } 
    return p;
  }
  static double N58bf35961(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.3) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() > 0.3) {
      p = 0;
    } 
    return p;
  }
}
