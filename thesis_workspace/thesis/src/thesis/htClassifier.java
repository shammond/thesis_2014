package thesis;

class htClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = htClassifier.N51e2510c0(i);
    return p;
  }
  static double N51e2510c0(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.272799) {
    p = htClassifier.N613714d31(i);
    } else if (((Double) i[6]).doubleValue() > 0.272799) {
    p = htClassifier.N3bc8c52e5(i);
    } 
    return p;
  }
  static double N613714d31(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.3) {
    p = htClassifier.N67385a812(i);
    } else if (((Double) i[4]).doubleValue() > 0.3) {
      p = 0;
    } 
    return p;
  }
  static double N67385a812(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.188612) {
    p = htClassifier.N24be00183(i);
    } else if (((Double) i[5]).doubleValue() > 0.188612) {
    p = htClassifier.N35f83a804(i);
    } 
    return p;
  }
  static double N24be00183(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.097586) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() > 0.097586) {
      p = 1;
    } 
    return p;
  }
  static double N35f83a804(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() <= 0.451338738203) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() > 0.451338738203) {
      p = 0;
    } 
    return p;
  }
  static double N3bc8c52e5(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() <= 0.462209) {
    p = htClassifier.N7a096dab6(i);
    } else if (((Double) i[6]).doubleValue() > 0.462209) {
      p = 1;
    } 
    return p;
  }
  static double N7a096dab6(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 9.12771677E-4) {
    p = htClassifier.Nff34257(i);
    } else if (((Double) i[1]).doubleValue() > 9.12771677E-4) {
      p = 1;
    } 
    return p;
  }
  static double Nff34257(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 0;
    } else if (((Double) i[4]).doubleValue() <= 0.6) {
      p = 0;
    } else if (((Double) i[4]).doubleValue() > 0.6) {
      p = 1;
    } 
    return p;
  }
}
