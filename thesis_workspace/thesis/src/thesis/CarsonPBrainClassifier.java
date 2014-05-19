package thesis;

class CarsonPBrainClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = CarsonPBrainClassifier.N374d2f770(i);
    return p;
  }
  static double N374d2f770(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 0;
    } else if (((Double) i[3]).doubleValue() <= 0.001099579502) {
      p = 0;
    } else if (((Double) i[3]).doubleValue() > 0.001099579502) {
    p = CarsonPBrainClassifier.N2c6749fe1(i);
    } 
    return p;
  }
  static double N2c6749fe1(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() <= 0.775018) {
    p = CarsonPBrainClassifier.N312ad9782(i);
    } else if (((Double) i[5]).doubleValue() > 0.775018) {
      p = 1;
    } 
    return p;
  }
  static double N312ad9782(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 0.787702143192) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() > 0.787702143192) {
      p = 0;
    } 
    return p;
  }
}
