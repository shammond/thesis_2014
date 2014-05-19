package thesis;

class GretchenBrain2Classifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = GretchenBrain2Classifier.N1d66a29b0(i);
    return p;
  }
  static double N1d66a29b0(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.546842) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() > 0.546842) {
    p = GretchenBrain2Classifier.N3d3d7d311(i);
    } 
    return p;
  }
  static double N3d3d7d311(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.3) {
    p = GretchenBrain2Classifier.Nfe63b602(i);
    } else if (((Double) i[4]).doubleValue() > 0.3) {
      p = 1;
    } 
    return p;
  }
  static double Nfe63b602(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 0;
    } else if (((Double) i[4]).doubleValue() <= 0.1) {
    p = GretchenBrain2Classifier.N544423c73(i);
    } else if (((Double) i[4]).doubleValue() > 0.1) {
      p = 1;
    } 
    return p;
  }
  static double N544423c73(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() <= 0.814441) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() > 0.814441) {
      p = 1;
    } 
    return p;
  }
}
