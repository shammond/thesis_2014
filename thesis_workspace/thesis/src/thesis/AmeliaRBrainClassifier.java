package thesis;

class AmeliaRBrainClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = AmeliaRBrainClassifier.N5095b8a60(i);
    return p;
  }
  static double N5095b8a60(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.413505) {
    p = AmeliaRBrainClassifier.N158dc39f1(i);
    } else if (((Double) i[6]).doubleValue() > 0.413505) {
      p = 1;
    } 
    return p;
  }
  static double N158dc39f1(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.3) {
    p = AmeliaRBrainClassifier.Ne2606c72(i);
    } else if (((Double) i[4]).doubleValue() > 0.3) {
    p = AmeliaRBrainClassifier.Ndbb453d5(i);
    } 
    return p;
  }
  static double Ne2606c72(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.514887) {
    p = AmeliaRBrainClassifier.N6286fa123(i);
    } else if (((Double) i[5]).doubleValue() > 0.514887) {
      p = 0;
    } 
    return p;
  }
  static double N6286fa123(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() <= 0.991812944412) {
    p = AmeliaRBrainClassifier.Nb8be6474(i);
    } else if (((Double) i[1]).doubleValue() > 0.991812944412) {
      p = 1;
    } 
    return p;
  }
  static double Nb8be6474(Object []i) {
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
  static double Ndbb453d5(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 0.286396414042) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 0.286396414042) {
    p = AmeliaRBrainClassifier.N1beaed66(i);
    } 
    return p;
  }
  static double N1beaed66(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 0;
    } else if (((Double) i[3]).doubleValue() <= 0.500001490116) {
      p = 0;
    } else if (((Double) i[3]).doubleValue() > 0.500001490116) {
      p = 1;
    } 
    return p;
  }
}
