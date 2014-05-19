package thesis;

class Rachel1v2Classifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = Rachel1v2Classifier.Nc1da30b0(i);
    return p;
  }
  static double Nc1da30b0(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 0;
    } else if (((Double) i[4]).doubleValue() <= 0.6) {
    p = Rachel1v2Classifier.N6dbb2d631(i);
    } else if (((Double) i[4]).doubleValue() > 0.6) {
      p = 1;
    } 
    return p;
  }
  static double N6dbb2d631(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 0.973980903625) {
    p = Rachel1v2Classifier.N1ba0f6dd2(i);
    } else if (((Double) i[2]).doubleValue() > 0.973980903625) {
      p = 0;
    } 
    return p;
  }
  static double N1ba0f6dd2(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.3) {
    p = Rachel1v2Classifier.N64af35563(i);
    } else if (((Double) i[4]).doubleValue() > 0.3) {
    p = Rachel1v2Classifier.N4dacc12412(i);
    } 
    return p;
  }
  static double N64af35563(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 3.791619E-5) {
    p = Rachel1v2Classifier.N2ad6d4be4(i);
    } else if (((Double) i[0]).doubleValue() > 3.791619E-5) {
      p = 1;
    } 
    return p;
  }
  static double N2ad6d4be4(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.280012) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() > 0.280012) {
    p = Rachel1v2Classifier.N7e291bea5(i);
    } 
    return p;
  }
  static double N7e291bea5(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.639803) {
    p = Rachel1v2Classifier.N3ce7e05b6(i);
    } else if (((Double) i[5]).doubleValue() > 0.639803) {
    p = Rachel1v2Classifier.N6cfed27b11(i);
    } 
    return p;
  }
  static double N3ce7e05b6(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() <= 0.487425) {
    p = Rachel1v2Classifier.N1b3a959a7(i);
    } else if (((Double) i[6]).doubleValue() > 0.487425) {
      p = 1;
    } 
    return p;
  }
  static double N1b3a959a7(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.318751) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 0.318751) {
    p = Rachel1v2Classifier.N2333bf6d8(i);
    } 
    return p;
  }
  static double N2333bf6d8(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.368473) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() > 0.368473) {
    p = Rachel1v2Classifier.N1a5c9f299(i);
    } 
    return p;
  }
  static double N1a5c9f299(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 0;
    } else if (((Double) i[2]).doubleValue() <= 0.074344582856) {
    p = Rachel1v2Classifier.N36d54a4410(i);
    } else if (((Double) i[2]).doubleValue() > 0.074344582856) {
      p = 1;
    } 
    return p;
  }
  static double N36d54a4410(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.434448) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 0.434448) {
      p = 0;
    } 
    return p;
  }
  static double N6cfed27b11(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() <= 0.829631) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() > 0.829631) {
      p = 1;
    } 
    return p;
  }
  static double N4dacc12412(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.410122) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 0.410122) {
    p = Rachel1v2Classifier.N8bc4a5313(i);
    } 
    return p;
  }
  static double N8bc4a5313(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.619984) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() > 0.619984) {
    p = Rachel1v2Classifier.N1b393a014(i);
    } 
    return p;
  }
  static double N1b393a014(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.78166) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 0.78166) {
    p = Rachel1v2Classifier.N34a0ee3f15(i);
    } 
    return p;
  }
  static double N34a0ee3f15(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() <= 3.791619E-5) {
    p = Rachel1v2Classifier.N3179851716(i);
    } else if (((Double) i[0]).doubleValue() > 3.791619E-5) {
      p = 0;
    } 
    return p;
  }
  static double N3179851716(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() <= 0.759511) {
    p = Rachel1v2Classifier.N2110c26117(i);
    } else if (((Double) i[7]).doubleValue() > 0.759511) {
      p = 1;
    } 
    return p;
  }
  static double N2110c26117(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() <= 0.7290776666666667) {
    p = Rachel1v2Classifier.N53497f3c18(i);
    } else if (((Double) i[7]).doubleValue() > 0.7290776666666667) {
      p = 0;
    } 
    return p;
  }
  static double N53497f3c18(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() <= 0.720363) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() > 0.720363) {
      p = 1;
    } 
    return p;
  }
}
