package thesis;

class GretchenWBrainClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = GretchenWBrainClassifier.Nde960250(i);
    return p;
  }
  static double Nde960250(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.546842) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() > 0.546842) {
      p = 1;
    } 
    return p;
  }
}
