package thesis;

class AbbyM22Classifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = AbbyM22Classifier.N6be774480(i);
    return p;
  }
  static double N6be774480(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.534134) {
    p = AbbyM22Classifier.N28835a501(i);
    } else if (((Double) i[6]).doubleValue() > 0.534134) {
    p = AbbyM22Classifier.N3092c97913(i);
    } 
    return p;
  }
  static double N28835a501(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 0.013456760906) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 0.013456760906) {
    p = AbbyM22Classifier.N479e4ef72(i);
    } 
    return p;
  }
  static double N479e4ef72(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.6) {
    p = AbbyM22Classifier.N6a525edc3(i);
    } else if (((Double) i[4]).doubleValue() > 0.6) {
    p = AbbyM22Classifier.N6d6a8bf010(i);
    } 
    return p;
  }
  static double N6a525edc3(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 0.274499773979) {
    p = AbbyM22Classifier.N45d1f40c4(i);
    } else if (((Double) i[1]).doubleValue() > 0.274499773979) {
      p = 1;
    } 
    return p;
  }
  static double N45d1f40c4(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.548018) {
    p = AbbyM22Classifier.N5aed63a35(i);
    } else if (((Double) i[5]).doubleValue() > 0.548018) {
      p = 0;
    } 
    return p;
  }
  static double N5aed63a35(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() <= 0.397218) {
    p = AbbyM22Classifier.N172890f86(i);
    } else if (((Double) i[7]).doubleValue() > 0.397218) {
    p = AbbyM22Classifier.N30647e138(i);
    } 
    return p;
  }
  static double N172890f86(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() <= 2.06002907E-4) {
    p = AbbyM22Classifier.N684595a87(i);
    } else if (((Double) i[0]).doubleValue() > 2.06002907E-4) {
      p = 0;
    } 
    return p;
  }
  static double N684595a87(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 0.094855234027) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 0.094855234027) {
      p = 1;
    } 
    return p;
  }
  static double N30647e138(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 1;
    } else if (((Double) i[7]).doubleValue() <= 0.422865) {
      p = 1;
    } else if (((Double) i[7]).doubleValue() > 0.422865) {
    p = AbbyM22Classifier.N15912a379(i);
    } 
    return p;
  }
  static double N15912a379(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() <= 0.427787) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() > 0.427787) {
      p = 1;
    } 
    return p;
  }
  static double N6d6a8bf010(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.330085) {
    p = AbbyM22Classifier.N6e0971ae11(i);
    } else if (((Double) i[6]).doubleValue() > 0.330085) {
    p = AbbyM22Classifier.N2e068ef212(i);
    } 
    return p;
  }
  static double N6e0971ae11(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 0;
    } else if (((Double) i[3]).doubleValue() <= 0.911263048649) {
      p = 0;
    } else if (((Double) i[3]).doubleValue() > 0.911263048649) {
      p = 1;
    } 
    return p;
  }
  static double N2e068ef212(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.421786) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 0.421786) {
      p = 0;
    } 
    return p;
  }
  static double N3092c97913(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() <= 0.758814) {
    p = AbbyM22Classifier.N74e93fd814(i);
    } else if (((Double) i[6]).doubleValue() > 0.758814) {
    p = AbbyM22Classifier.N55fcde1f16(i);
    } 
    return p;
  }
  static double N74e93fd814(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 3.45499866E-4) {
    p = AbbyM22Classifier.N7d5eb9de15(i);
    } else if (((Double) i[1]).doubleValue() > 3.45499866E-4) {
      p = 1;
    } 
    return p;
  }
  static double N7d5eb9de15(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 0;
    } else if (((Double) i[4]).doubleValue() <= 0.9) {
      p = 0;
    } else if (((Double) i[4]).doubleValue() > 0.9) {
      p = 1;
    } 
    return p;
  }
  static double N55fcde1f16(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.6) {
    p = AbbyM22Classifier.N4c5af15317(i);
    } else if (((Double) i[4]).doubleValue() > 0.6) {
    p = AbbyM22Classifier.N5aefb0d221(i);
    } 
    return p;
  }
  static double N4c5af15317(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() <= 0.07502412796) {
    p = AbbyM22Classifier.N6699a74e18(i);
    } else if (((Double) i[1]).doubleValue() > 0.07502412796) {
      p = 1;
    } 
    return p;
  }
  static double N6699a74e18(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() <= 0.169510185719) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() > 0.169510185719) {
    p = AbbyM22Classifier.N71b7268119(i);
    } 
    return p;
  }
  static double N71b7268119(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() <= 2.69381795E-4) {
    p = AbbyM22Classifier.N2f41197a20(i);
    } else if (((Double) i[0]).doubleValue() > 2.69381795E-4) {
      p = 1;
    } 
    return p;
  }
  static double N2f41197a20(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() <= 0.77001) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() > 0.77001) {
      p = 0;
    } 
    return p;
  }
  static double N5aefb0d221(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 3.0565639E-5) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() > 3.0565639E-5) {
    p = AbbyM22Classifier.N2e41d9a222(i);
    } 
    return p;
  }
  static double N2e41d9a222(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() <= 0.001044512377) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() > 0.001044512377) {
      p = 1;
    } 
    return p;
  }
}
