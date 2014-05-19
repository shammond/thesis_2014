package thesis;

class AdamDBrainClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = AdamDBrainClassifier.N39f43b130(i);
    return p;
  }
  static double N39f43b130(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.632723) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() > 0.632723) {
    p = AdamDBrainClassifier.N51527a1e1(i);
    } 
    return p;
  }
  static double N51527a1e1(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 0;
    } else if (((Double) i[3]).doubleValue() <= 0.020995490253) {
      p = 0;
    } else if (((Double) i[3]).doubleValue() > 0.020995490253) {
      p = 1;
    } 
    return p;
  }
}
