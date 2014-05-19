package thesis;

class SubjectA1v2Classifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = SubjectA1v2Classifier.N27a35cb30(i);
    return p;
  }
  static double N27a35cb30(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.497424) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() > 0.497424) {
      p = 1;
    } 
    return p;
  }
}
