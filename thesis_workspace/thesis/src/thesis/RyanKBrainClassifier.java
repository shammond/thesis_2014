package thesis;

class RyanKBrainClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = RyanKBrainClassifier.N1beaed60(i);
    return p;
  }
  static double N1beaed60(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 3.99934506E-4) {
    p = RyanKBrainClassifier.Ndc8647f1(i);
    } else if (((Double) i[1]).doubleValue() > 3.99934506E-4) {
      p = 1;
    } 
    return p;
  }
  static double Ndc8647f1(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 2.32230552E-4) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 2.32230552E-4) {
    p = RyanKBrainClassifier.N5f3dd4ea2(i);
    } 
    return p;
  }
  static double N5f3dd4ea2(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.391543) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() > 0.391543) {
    p = RyanKBrainClassifier.N54657f7f3(i);
    } 
    return p;
  }
  static double N54657f7f3(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 0.146348506212) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() > 0.146348506212) {
      p = 0;
    } 
    return p;
  }
}
