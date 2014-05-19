package thesis;

class gretchen2Classifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = gretchen2Classifier.N3c1e1fd30(i);
    return p;
  }
  static double N3c1e1fd30(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.546842) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() > 0.546842) {
      p = 1;
    } 
    return p;
  }
}
