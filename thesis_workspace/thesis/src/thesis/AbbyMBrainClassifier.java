package thesis;

class AbbyMBrainClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = AbbyMBrainClassifier.N5ccab23b0(i);
    return p;
  }
  static double N5ccab23b0(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.534134) {
    p = AbbyMBrainClassifier.N7f7b6f141(i);
    } else if (((Double) i[6]).doubleValue() > 0.534134) {
    p = AbbyMBrainClassifier.N7f1168455(i);
    } 
    return p;
  }
  static double N7f7b6f141(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.3) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() > 0.3) {
    p = AbbyMBrainClassifier.Nb9cb6f2(i);
    } 
    return p;
  }
  static double Nb9cb6f2(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 0.013456760906) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 0.013456760906) {
    p = AbbyMBrainClassifier.N25dae4c83(i);
    } 
    return p;
  }
  static double N25dae4c83(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() <= 0.554634) {
    p = AbbyMBrainClassifier.N45e219e24(i);
    } else if (((Double) i[7]).doubleValue() > 0.554634) {
      p = 0;
    } 
    return p;
  }
  static double N45e219e24(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.330085) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() > 0.330085) {
      p = 1;
    } 
    return p;
  }
  static double N7f1168455(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 3.45499866E-4) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 3.45499866E-4) {
      p = 1;
    } 
    return p;
  }
}
