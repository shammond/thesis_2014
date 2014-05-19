package thesis;

class MatthewPBrainClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = MatthewPBrainClassifier.N9657d120(i);
    return p;
  }
  static double N9657d120(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.3) {
    p = MatthewPBrainClassifier.N65f62d8f1(i);
    } else if (((Double) i[4]).doubleValue() > 0.3) {
    p = MatthewPBrainClassifier.N5606b5d83(i);
    } 
    return p;
  }
  static double N65f62d8f1(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.1) {
    p = MatthewPBrainClassifier.N5293b952(i);
    } else if (((Double) i[4]).doubleValue() > 0.1) {
      p = 1;
    } 
    return p;
  }
  static double N5293b952(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() <= 0.639475464821) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() > 0.639475464821) {
      p = 1;
    } 
    return p;
  }
  static double N5606b5d83(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() <= 0.917388439178) {
    p = MatthewPBrainClassifier.N528ca4074(i);
    } else if (((Double) i[3]).doubleValue() > 0.917388439178) {
      p = 0;
    } 
    return p;
  }
  static double N528ca4074(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.448428) {
    p = MatthewPBrainClassifier.Nf64f1e85(i);
    } else if (((Double) i[6]).doubleValue() > 0.448428) {
    p = MatthewPBrainClassifier.Nbab753611(i);
    } 
    return p;
  }
  static double Nf64f1e85(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 0.068361312151) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 0.068361312151) {
    p = MatthewPBrainClassifier.N2c45be3d6(i);
    } 
    return p;
  }
  static double N2c45be3d6(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.6) {
    p = MatthewPBrainClassifier.N16ccad807(i);
    } else if (((Double) i[4]).doubleValue() > 0.6) {
    p = MatthewPBrainClassifier.N5792ba318(i);
    } 
    return p;
  }
  static double N16ccad807(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() <= 0.261277318001) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() > 0.261277318001) {
      p = 0;
    } 
    return p;
  }
  static double N5792ba318(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 0.001193737728) {
    p = MatthewPBrainClassifier.N61fe13e19(i);
    } else if (((Double) i[0]).doubleValue() > 0.001193737728) {
      p = 1;
    } 
    return p;
  }
  static double N61fe13e19(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 0;
    } else if (((Double) i[2]).doubleValue() <= 0.085746966302) {
      p = 0;
    } else if (((Double) i[2]).doubleValue() > 0.085746966302) {
    p = MatthewPBrainClassifier.N6fcb4b0910(i);
    } 
    return p;
  }
  static double N6fcb4b0910(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.5302131999999999) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 0.5302131999999999) {
      p = 0;
    } 
    return p;
  }
  static double Nbab753611(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 0.00296270242) {
    p = MatthewPBrainClassifier.N259c323612(i);
    } else if (((Double) i[1]).doubleValue() > 0.00296270242) {
      p = 1;
    } 
    return p;
  }
  static double N259c323612(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.6) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() > 0.6) {
    p = MatthewPBrainClassifier.N31a48a8413(i);
    } 
    return p;
  }
  static double N31a48a8413(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 0;
    } else if (((Double) i[4]).doubleValue() <= 0.9) {
    p = MatthewPBrainClassifier.N2981f99214(i);
    } else if (((Double) i[4]).doubleValue() > 0.9) {
    p = MatthewPBrainClassifier.N4b68be0e16(i);
    } 
    return p;
  }
  static double N2981f99214(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 0;
    } else if (((Double) i[3]).doubleValue() <= 0.699144124985) {
      p = 0;
    } else if (((Double) i[3]).doubleValue() > 0.699144124985) {
    p = MatthewPBrainClassifier.N1427f58815(i);
    } 
    return p;
  }
  static double N1427f58815(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() <= 0.537766) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() > 0.537766) {
      p = 1;
    } 
    return p;
  }
  static double N4b68be0e16(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() <= 0.498769581318) {
    p = MatthewPBrainClassifier.N4995afcf17(i);
    } else if (((Double) i[0]).doubleValue() > 0.498769581318) {
      p = 1;
    } 
    return p;
  }
  static double N4995afcf17(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.755867) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 0.755867) {
      p = 0;
    } 
    return p;
  }
}
