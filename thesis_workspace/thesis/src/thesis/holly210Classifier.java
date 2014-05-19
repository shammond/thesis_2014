package thesis;

class holly210Classifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = holly210Classifier.Nace3c1b0(i);
    return p;
  }
  static double Nace3c1b0(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.272799) {
    p = holly210Classifier.N69cc16271(i);
    } else if (((Double) i[6]).doubleValue() > 0.272799) {
    p = holly210Classifier.N58aa45683(i);
    } 
    return p;
  }
  static double N69cc16271(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.3) {
    p = holly210Classifier.N55c290b42(i);
    } else if (((Double) i[4]).doubleValue() > 0.3) {
      p = 0;
    } 
    return p;
  }
  static double N55c290b42(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.188612) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 0.188612) {
      p = 0;
    } 
    return p;
  }
  static double N58aa45683(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() <= 0.462209) {
    p = holly210Classifier.N12b2dc524(i);
    } else if (((Double) i[6]).doubleValue() > 0.462209) {
      p = 1;
    } 
    return p;
  }
  static double N12b2dc524(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 9.12771677E-4) {
    p = holly210Classifier.N1c9e95155(i);
    } else if (((Double) i[1]).doubleValue() > 9.12771677E-4) {
      p = 1;
    } 
    return p;
  }
  static double N1c9e95155(Object []i) {
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
