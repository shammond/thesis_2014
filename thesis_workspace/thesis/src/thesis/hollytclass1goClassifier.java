package thesis;

class hollytclass1goClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = hollytclass1goClassifier.N6a7fe54c0(i);
    return p;
  }
  static double N6a7fe54c0(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.272799) {
    p = hollytclass1goClassifier.N72a717331(i);
    } else if (((Double) i[6]).doubleValue() > 0.272799) {
    p = hollytclass1goClassifier.N37e450132(i);
    } 
    return p;
  }
  static double N72a717331(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.3) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() > 0.3) {
      p = 0;
    } 
    return p;
  }
  static double N37e450132(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() <= 0.462209) {
    p = hollytclass1goClassifier.N6e452c0f3(i);
    } else if (((Double) i[6]).doubleValue() > 0.462209) {
      p = 1;
    } 
    return p;
  }
  static double N6e452c0f3(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 9.12771677E-4) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 9.12771677E-4) {
      p = 1;
    } 
    return p;
  }
}
