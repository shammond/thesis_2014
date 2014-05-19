package thesis;

class AmeliaRBrain2Classifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = AmeliaRBrain2Classifier.N3ddc25a90(i);
    return p;
  }
  static double N3ddc25a90(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.413505) {
    p = AmeliaRBrain2Classifier.N402c99f91(i);
    } else if (((Double) i[6]).doubleValue() > 0.413505) {
      p = 1;
    } 
    return p;
  }
  static double N402c99f91(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.3) {
    p = AmeliaRBrain2Classifier.N3030cb592(i);
    } else if (((Double) i[4]).doubleValue() > 0.3) {
    p = AmeliaRBrain2Classifier.N7ebe59f84(i);
    } 
    return p;
  }
  static double N3030cb592(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 0.018882539123) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() > 0.018882539123) {
    p = AmeliaRBrain2Classifier.N537658c63(i);
    } 
    return p;
  }
  static double N537658c63(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() <= 0.198771) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() > 0.198771) {
      p = 1;
    } 
    return p;
  }
  static double N7ebe59f84(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 0;
    } else if (((Double) i[4]).doubleValue() <= 0.6) {
      p = 0;
    } else if (((Double) i[4]).doubleValue() > 0.6) {
    p = AmeliaRBrain2Classifier.N304e9ca5(i);
    } 
    return p;
  }
  static double N304e9ca5(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 0.286396414042) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 0.286396414042) {
      p = 1;
    } 
    return p;
  }
}
