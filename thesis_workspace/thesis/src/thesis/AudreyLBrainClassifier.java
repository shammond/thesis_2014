package thesis;

class AudreyLBrainClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = AudreyLBrainClassifier.N6a7fe54c0(i);
    return p;
  }
  static double N6a7fe54c0(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.505518) {
    p = AudreyLBrainClassifier.N72a717331(i);
    } else if (((Double) i[6]).doubleValue() > 0.505518) {
      p = 1;
    } 
    return p;
  }
  static double N72a717331(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 9.87787498E-4) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 9.87787498E-4) {
    p = AudreyLBrainClassifier.N37e450132(i);
    } 
    return p;
  }
  static double N37e450132(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() <= 0.420191) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() > 0.420191) {
      p = 1;
    } 
    return p;
  }
}
