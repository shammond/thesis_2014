package thesis;

class DanWBrainClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = DanWBrainClassifier.Nbd995970(i);
    return p;
  }
  static double Nbd995970(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 0;
    } else if (((Double) i[2]).doubleValue() <= 0.225908055902) {
      p = 0;
    } else if (((Double) i[2]).doubleValue() > 0.225908055902) {
    p = DanWBrainClassifier.N79ebf2941(i);
    } 
    return p;
  }
  static double N79ebf2941(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.50696) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() > 0.50696) {
      p = 1;
    } 
    return p;
  }
}
