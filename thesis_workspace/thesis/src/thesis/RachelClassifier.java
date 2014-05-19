package thesis;

class RachelClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = RachelClassifier.N423f08e30(i);
    return p;
  }
  static double N423f08e30(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 0;
    } else if (((Double) i[4]).doubleValue() <= 0.6) {
    p = RachelClassifier.N3860910f1(i);
    } else if (((Double) i[4]).doubleValue() > 0.6) {
      p = 1;
    } 
    return p;
  }
  static double N3860910f1(Object []i) {
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
