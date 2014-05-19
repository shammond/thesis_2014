package thesis;

class JulianABrainClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = JulianABrainClassifier.N70ae18dd0(i);
    return p;
  }
  static double N70ae18dd0(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 3.59081896E-4) {
    p = JulianABrainClassifier.N35e28ef61(i);
    } else if (((Double) i[1]).doubleValue() > 3.59081896E-4) {
    p = JulianABrainClassifier.N2d17d41d2(i);
    } 
    return p;
  }
  static double N35e28ef61(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() <= 0.654351) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() > 0.654351) {
      p = 1;
    } 
    return p;
  }
  static double N2d17d41d2(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.341928) {
    p = JulianABrainClassifier.N7766d30b3(i);
    } else if (((Double) i[6]).doubleValue() > 0.341928) {
      p = 1;
    } 
    return p;
  }
  static double N7766d30b3(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 0.624339520931) {
    p = JulianABrainClassifier.N7fa9b46a4(i);
    } else if (((Double) i[2]).doubleValue() > 0.624339520931) {
      p = 0;
    } 
    return p;
  }
  static double N7fa9b46a4(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() <= 0.768765389919) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() > 0.768765389919) {
      p = 0;
    } 
    return p;
  }
}
