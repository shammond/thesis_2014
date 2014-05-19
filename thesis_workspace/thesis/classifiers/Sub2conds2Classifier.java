package thesis;

class Sub2conds2Classifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = Sub2conds2Classifier.N147136580(i);
    return p;
  }
  static double N147136580(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 0;
    } else if (((Double) i[3]).doubleValue() <= 0.001099579502) {
      p = 0;
    } else if (((Double) i[3]).doubleValue() > 0.001099579502) {
    p = Sub2conds2Classifier.N14a6d5e41(i);
    } 
    return p;
  }
  static double N14a6d5e41(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() <= 0.775018) {
    p = Sub2conds2Classifier.N552476532(i);
    } else if (((Double) i[5]).doubleValue() > 0.775018) {
    p = Sub2conds2Classifier.N4cd46ed03(i);
    } 
    return p;
  }
  static double N552476532(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 0.787702143192) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() > 0.787702143192) {
      p = 0;
    } 
    return p;
  }
  static double N4cd46ed03(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.801562) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 0.801562) {
    p = Sub2conds2Classifier.Neb741184(i);
    } 
    return p;
  }
  static double Neb741184(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() <= 0.101324543357) {
    p = Sub2conds2Classifier.N211296345(i);
    } else if (((Double) i[1]).doubleValue() > 0.101324543357) {
      p = 0;
    } 
    return p;
  }
  static double N211296345(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.6) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() > 0.6) {
    p = Sub2conds2Classifier.N4b4340e26(i);
    } 
    return p;
  }
  static double N4b4340e26(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 1.79907671E-4) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() > 1.79907671E-4) {
      p = 0;
    } 
    return p;
  }
}
