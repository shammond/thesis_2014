package thesis;

class SubjectA1v2moreClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = SubjectA1v2moreClassifier.N52b12fef0(i);
    return p;
  }
  static double N52b12fef0(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.505518) {
    p = SubjectA1v2moreClassifier.N6ec21e521(i);
    } else if (((Double) i[6]).doubleValue() > 0.505518) {
      p = 1;
    } 
    return p;
  }
  static double N6ec21e521(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 9.87787498E-4) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 9.87787498E-4) {
    p = SubjectA1v2moreClassifier.Na54d24d2(i);
    } 
    return p;
  }
  static double Na54d24d2(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() <= 0.420191) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() > 0.420191) {
    p = SubjectA1v2moreClassifier.N46bac2873(i);
    } 
    return p;
  }
  static double N46bac2873(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.651868) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 0.651868) {
      p = 0;
    } 
    return p;
  }
}
