package thesis;

class SubjectCPSDClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = SubjectCPSDClassifier.N7cc88db20(i);
    return p;
  }
  static double N7cc88db20(Object []i) {
    double p = Double.NaN;
    if (i[59] == null) {
      p = 0;
    } else if (((Double) i[59]).doubleValue() <= 1.52671) {
    p = SubjectCPSDClassifier.N52cee11e1(i);
    } else if (((Double) i[59]).doubleValue() > 1.52671) {
    p = SubjectCPSDClassifier.N7e291bea7(i);
    } 
    return p;
  }
  static double N52cee11e1(Object []i) {
    double p = Double.NaN;
    if (i[39] == null) {
      p = 0;
    } else if (((Double) i[39]).doubleValue() <= 1.930294) {
    p = SubjectCPSDClassifier.Nc1da30b2(i);
    } else if (((Double) i[39]).doubleValue() > 1.930294) {
    p = SubjectCPSDClassifier.N2ad6d4be6(i);
    } 
    return p;
  }
  static double Nc1da30b2(Object []i) {
    double p = Double.NaN;
    if (i[49] == null) {
      p = 0;
    } else if (((Double) i[49]).doubleValue() <= 1.566103) {
    p = SubjectCPSDClassifier.N6dbb2d633(i);
    } else if (((Double) i[49]).doubleValue() > 1.566103) {
    p = SubjectCPSDClassifier.N64af35565(i);
    } 
    return p;
  }
  static double N6dbb2d633(Object []i) {
    double p = Double.NaN;
    if (i[78] == null) {
      p = 0;
    } else if (((Double) i[78]).doubleValue() <= 1.602919) {
    p = SubjectCPSDClassifier.N1ba0f6dd4(i);
    } else if (((Double) i[78]).doubleValue() > 1.602919) {
      p = 0;
    } 
    return p;
  }
  static double N1ba0f6dd4(Object []i) {
    double p = Double.NaN;
    if (i[8] == null) {
      p = 1;
    } else if (((Double) i[8]).doubleValue() <= 1.59069) {
      p = 1;
    } else if (((Double) i[8]).doubleValue() > 1.59069) {
      p = 0;
    } 
    return p;
  }
  static double N64af35565(Object []i) {
    double p = Double.NaN;
    if (i[39] == null) {
      p = 0;
    } else if (((Double) i[39]).doubleValue() <= 1.694017) {
      p = 0;
    } else if (((Double) i[39]).doubleValue() > 1.694017) {
      p = 1;
    } 
    return p;
  }
  static double N2ad6d4be6(Object []i) {
    double p = Double.NaN;
    if (i[49] == null) {
      p = 0;
    } else if (((Double) i[49]).doubleValue() <= 1.411726) {
      p = 0;
    } else if (((Double) i[49]).doubleValue() > 1.411726) {
      p = 1;
    } 
    return p;
  }
  static double N7e291bea7(Object []i) {
    double p = Double.NaN;
    if (i[39] == null) {
      p = 0;
    } else if (((Double) i[39]).doubleValue() <= 1.876394) {
    p = SubjectCPSDClassifier.N3ce7e05b8(i);
    } else if (((Double) i[39]).doubleValue() > 1.876394) {
    p = SubjectCPSDClassifier.N4b34b33e37(i);
    } 
    return p;
  }
  static double N3ce7e05b8(Object []i) {
    double p = Double.NaN;
    if (i[89] == null) {
      p = 0;
    } else if (((Double) i[89]).doubleValue() <= 1.597779) {
    p = SubjectCPSDClassifier.N1b3a959a9(i);
    } else if (((Double) i[89]).doubleValue() > 1.597779) {
    p = SubjectCPSDClassifier.N6ebc80834(i);
    } 
    return p;
  }
  static double N1b3a959a9(Object []i) {
    double p = Double.NaN;
    if (i[75] == null) {
      p = 0;
    } else if (((Double) i[75]).doubleValue() <= 2.26344) {
    p = SubjectCPSDClassifier.N2333bf6d10(i);
    } else if (((Double) i[75]).doubleValue() > 2.26344) {
    p = SubjectCPSDClassifier.N53497f3c20(i);
    } 
    return p;
  }
  static double N2333bf6d10(Object []i) {
    double p = Double.NaN;
    if (i[49] == null) {
      p = 0;
    } else if (((Double) i[49]).doubleValue() <= 1.576927) {
    p = SubjectCPSDClassifier.N1a5c9f2911(i);
    } else if (((Double) i[49]).doubleValue() > 1.576927) {
    p = SubjectCPSDClassifier.N1b393a016(i);
    } 
    return p;
  }
  static double N1a5c9f2911(Object []i) {
    double p = Double.NaN;
    if (i[39] == null) {
      p = 0;
    } else if (((Double) i[39]).doubleValue() <= 1.477556) {
    p = SubjectCPSDClassifier.N36d54a4412(i);
    } else if (((Double) i[39]).doubleValue() > 1.477556) {
    p = SubjectCPSDClassifier.N4dacc12414(i);
    } 
    return p;
  }
  static double N36d54a4412(Object []i) {
    double p = Double.NaN;
    if (i[24] == null) {
      p = 0;
    } else if (((Double) i[24]).doubleValue() <= 2.435111) {
    p = SubjectCPSDClassifier.N6cfed27b13(i);
    } else if (((Double) i[24]).doubleValue() > 2.435111) {
      p = 1;
    } 
    return p;
  }
  static double N6cfed27b13(Object []i) {
    double p = Double.NaN;
    if (i[57] == null) {
      p = 1;
    } else if (((Double) i[57]).doubleValue() <= 1.882102) {
      p = 1;
    } else if (((Double) i[57]).doubleValue() > 1.882102) {
      p = 0;
    } 
    return p;
  }
  static double N4dacc12414(Object []i) {
    double p = Double.NaN;
    if (i[48] == null) {
      p = 0;
    } else if (((Double) i[48]).doubleValue() <= 1.92825) {
      p = 0;
    } else if (((Double) i[48]).doubleValue() > 1.92825) {
    p = SubjectCPSDClassifier.N8bc4a5315(i);
    } 
    return p;
  }
  static double N8bc4a5315(Object []i) {
    double p = Double.NaN;
    if (i[44] == null) {
      p = 1;
    } else if (((Double) i[44]).doubleValue() <= 2.098716) {
      p = 1;
    } else if (((Double) i[44]).doubleValue() > 2.098716) {
      p = 0;
    } 
    return p;
  }
  static double N1b393a016(Object []i) {
    double p = Double.NaN;
    if (i[59] == null) {
      p = 0;
    } else if (((Double) i[59]).doubleValue() <= 1.852241) {
    p = SubjectCPSDClassifier.N34a0ee3f17(i);
    } else if (((Double) i[59]).doubleValue() > 1.852241) {
      p = 1;
    } 
    return p;
  }
  static double N34a0ee3f17(Object []i) {
    double p = Double.NaN;
    if (i[89] == null) {
      p = 1;
    } else if (((Double) i[89]).doubleValue() <= 1.223547) {
      p = 1;
    } else if (((Double) i[89]).doubleValue() > 1.223547) {
    p = SubjectCPSDClassifier.N3179851718(i);
    } 
    return p;
  }
  static double N3179851718(Object []i) {
    double p = Double.NaN;
    if (i[79] == null) {
      p = 0;
    } else if (((Double) i[79]).doubleValue() <= 1.59958) {
    p = SubjectCPSDClassifier.N2110c26119(i);
    } else if (((Double) i[79]).doubleValue() > 1.59958) {
      p = 1;
    } 
    return p;
  }
  static double N2110c26119(Object []i) {
    double p = Double.NaN;
    if (i[67] == null) {
      p = 1;
    } else if (((Double) i[67]).doubleValue() <= 1.56478) {
      p = 1;
    } else if (((Double) i[67]).doubleValue() > 1.56478) {
      p = 0;
    } 
    return p;
  }
  static double N53497f3c20(Object []i) {
    double p = Double.NaN;
    if (i[53] == null) {
      p = 1;
    } else if (((Double) i[53]).doubleValue() <= 2.783658) {
    p = SubjectCPSDClassifier.N7e4066db21(i);
    } else if (((Double) i[53]).doubleValue() > 2.783658) {
      p = 0;
    } 
    return p;
  }
  static double N7e4066db21(Object []i) {
    double p = Double.NaN;
    if (i[49] == null) {
      p = 0;
    } else if (((Double) i[49]).doubleValue() <= 1.440401) {
    p = SubjectCPSDClassifier.N3620f49e22(i);
    } else if (((Double) i[49]).doubleValue() > 1.440401) {
    p = SubjectCPSDClassifier.N72adb26727(i);
    } 
    return p;
  }
  static double N3620f49e22(Object []i) {
    double p = Double.NaN;
    if (i[39] == null) {
      p = 1;
    } else if (((Double) i[39]).doubleValue() <= 1.434553) {
    p = SubjectCPSDClassifier.N2d9bccd523(i);
    } else if (((Double) i[39]).doubleValue() > 1.434553) {
    p = SubjectCPSDClassifier.N4c21e59a25(i);
    } 
    return p;
  }
  static double N2d9bccd523(Object []i) {
    double p = Double.NaN;
    if (i[89] == null) {
      p = 1;
    } else if (((Double) i[89]).doubleValue() <= 1.248834) {
      p = 1;
    } else if (((Double) i[89]).doubleValue() > 1.248834) {
    p = SubjectCPSDClassifier.N4fa4cb5724(i);
    } 
    return p;
  }
  static double N4fa4cb5724(Object []i) {
    double p = Double.NaN;
    if (i[35] == null) {
      p = 0;
    } else if (((Double) i[35]).doubleValue() <= 2.389963) {
      p = 0;
    } else if (((Double) i[35]).doubleValue() > 2.389963) {
      p = 1;
    } 
    return p;
  }
  static double N4c21e59a25(Object []i) {
    double p = Double.NaN;
    if (i[89] == null) {
      p = 0;
    } else if (((Double) i[89]).doubleValue() <= 1.205515) {
      p = 0;
    } else if (((Double) i[89]).doubleValue() > 1.205515) {
    p = SubjectCPSDClassifier.N4569088226(i);
    } 
    return p;
  }
  static double N4569088226(Object []i) {
    double p = Double.NaN;
    if (i[15] == null) {
      p = 1;
    } else if (((Double) i[15]).doubleValue() <= 2.256254) {
      p = 1;
    } else if (((Double) i[15]).doubleValue() > 2.256254) {
      p = 0;
    } 
    return p;
  }
  static double N72adb26727(Object []i) {
    double p = Double.NaN;
    if (i[29] == null) {
      p = 1;
    } else if (((Double) i[29]).doubleValue() <= 1.467374) {
    p = SubjectCPSDClassifier.N699bc30228(i);
    } else if (((Double) i[29]).doubleValue() > 1.467374) {
    p = SubjectCPSDClassifier.N258c0afc32(i);
    } 
    return p;
  }
  static double N699bc30228(Object []i) {
    double p = Double.NaN;
    if (i[27] == null) {
      p = 1;
    } else if (((Double) i[27]).doubleValue() <= 2.136868) {
    p = SubjectCPSDClassifier.N711fee7829(i);
    } else if (((Double) i[27]).doubleValue() > 2.136868) {
      p = 1;
    } 
    return p;
  }
  static double N711fee7829(Object []i) {
    double p = Double.NaN;
    if (i[67] == null) {
      p = 1;
    } else if (((Double) i[67]).doubleValue() <= 2.199999) {
    p = SubjectCPSDClassifier.N6761424d30(i);
    } else if (((Double) i[67]).doubleValue() > 2.199999) {
      p = 0;
    } 
    return p;
  }
  static double N6761424d30(Object []i) {
    double p = Double.NaN;
    if (i[49] == null) {
      p = 1;
    } else if (((Double) i[49]).doubleValue() <= 1.641679) {
    p = SubjectCPSDClassifier.N2248024131(i);
    } else if (((Double) i[49]).doubleValue() > 1.641679) {
      p = 1;
    } 
    return p;
  }
  static double N2248024131(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() <= 2.269009) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() > 2.269009) {
      p = 1;
    } 
    return p;
  }
  static double N258c0afc32(Object []i) {
    double p = Double.NaN;
    if (i[49] == null) {
      p = 1;
    } else if (((Double) i[49]).doubleValue() <= 1.536755) {
    p = SubjectCPSDClassifier.Nd2539a633(i);
    } else if (((Double) i[49]).doubleValue() > 1.536755) {
      p = 0;
    } 
    return p;
  }
  static double Nd2539a633(Object []i) {
    double p = Double.NaN;
    if (i[19] == null) {
      p = 0;
    } else if (((Double) i[19]).doubleValue() <= 1.493064) {
      p = 0;
    } else if (((Double) i[19]).doubleValue() > 1.493064) {
      p = 1;
    } 
    return p;
  }
  static double N6ebc80834(Object []i) {
    double p = Double.NaN;
    if (i[39] == null) {
      p = 0;
    } else if (((Double) i[39]).doubleValue() <= 1.817451) {
      p = 0;
    } else if (((Double) i[39]).doubleValue() > 1.817451) {
    p = SubjectCPSDClassifier.N619988c435(i);
    } 
    return p;
  }
  static double N619988c435(Object []i) {
    double p = Double.NaN;
    if (i[20] == null) {
      p = 1;
    } else if (((Double) i[20]).doubleValue() <= 2.517323) {
    p = SubjectCPSDClassifier.N26e22deb36(i);
    } else if (((Double) i[20]).doubleValue() > 2.517323) {
      p = 0;
    } 
    return p;
  }
  static double N26e22deb36(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 0;
    } else if (((Double) i[2]).doubleValue() <= 2.02964) {
      p = 0;
    } else if (((Double) i[2]).doubleValue() > 2.02964) {
      p = 1;
    } 
    return p;
  }
  static double N4b34b33e37(Object []i) {
    double p = Double.NaN;
    if (i[29] == null) {
      p = 1;
    } else if (((Double) i[29]).doubleValue() <= 1.678735) {
    p = SubjectCPSDClassifier.N70e3d20438(i);
    } else if (((Double) i[29]).doubleValue() > 1.678735) {
    p = SubjectCPSDClassifier.N7b23946961(i);
    } 
    return p;
  }
  static double N70e3d20438(Object []i) {
    double p = Double.NaN;
    if (i[49] == null) {
      p = 1;
    } else if (((Double) i[49]).doubleValue() <= 2.081816) {
    p = SubjectCPSDClassifier.N7cf13e8239(i);
    } else if (((Double) i[49]).doubleValue() > 2.081816) {
    p = SubjectCPSDClassifier.Ne718bc559(i);
    } 
    return p;
  }
  static double N7cf13e8239(Object []i) {
    double p = Double.NaN;
    if (i[60] == null) {
      p = 1;
    } else if (((Double) i[60]).doubleValue() <= 2.599897) {
    p = SubjectCPSDClassifier.N423f08e340(i);
    } else if (((Double) i[60]).doubleValue() > 2.599897) {
    p = SubjectCPSDClassifier.N189c30e653(i);
    } 
    return p;
  }
  static double N423f08e340(Object []i) {
    double p = Double.NaN;
    if (i[38] == null) {
      p = 1;
    } else if (((Double) i[38]).doubleValue() <= 2.327335) {
    p = SubjectCPSDClassifier.N3860910f41(i);
    } else if (((Double) i[38]).doubleValue() > 2.327335) {
    p = SubjectCPSDClassifier.N479a368249(i);
    } 
    return p;
  }
  static double N3860910f41(Object []i) {
    double p = Double.NaN;
    if (i[56] == null) {
      p = 1;
    } else if (((Double) i[56]).doubleValue() <= 1.970491) {
    p = SubjectCPSDClassifier.N4bd38cb342(i);
    } else if (((Double) i[56]).doubleValue() > 1.970491) {
    p = SubjectCPSDClassifier.N27a35cb344(i);
    } 
    return p;
  }
  static double N4bd38cb342(Object []i) {
    double p = Double.NaN;
    if (i[44] == null) {
      p = 1;
    } else if (((Double) i[44]).doubleValue() <= 2.185974) {
      p = 1;
    } else if (((Double) i[44]).doubleValue() > 2.185974) {
    p = SubjectCPSDClassifier.N2dba62a943(i);
    } 
    return p;
  }
  static double N2dba62a943(Object []i) {
    double p = Double.NaN;
    if (i[60] == null) {
      p = 0;
    } else if (((Double) i[60]).doubleValue() <= 2.50197) {
      p = 0;
    } else if (((Double) i[60]).doubleValue() > 2.50197) {
      p = 1;
    } 
    return p;
  }
  static double N27a35cb344(Object []i) {
    double p = Double.NaN;
    if (i[25] == null) {
      p = 1;
    } else if (((Double) i[25]).doubleValue() <= 2.179815) {
    p = SubjectCPSDClassifier.N561b001945(i);
    } else if (((Double) i[25]).doubleValue() > 2.179815) {
      p = 1;
    } 
    return p;
  }
  static double N561b001945(Object []i) {
    double p = Double.NaN;
    if (i[38] == null) {
      p = 1;
    } else if (((Double) i[38]).doubleValue() <= 2.268892) {
    p = SubjectCPSDClassifier.N6a3957946(i);
    } else if (((Double) i[38]).doubleValue() > 2.268892) {
    p = SubjectCPSDClassifier.N53e2b02448(i);
    } 
    return p;
  }
  static double N6a3957946(Object []i) {
    double p = Double.NaN;
    if (i[39] == null) {
      p = 0;
    } else if (((Double) i[39]).doubleValue() <= 1.899501) {
    p = SubjectCPSDClassifier.N5daa3e5647(i);
    } else if (((Double) i[39]).doubleValue() > 1.899501) {
      p = 1;
    } 
    return p;
  }
  static double N5daa3e5647(Object []i) {
    double p = Double.NaN;
    if (i[11] == null) {
      p = 0;
    } else if (((Double) i[11]).doubleValue() <= 2.443454) {
      p = 0;
    } else if (((Double) i[11]).doubleValue() > 2.443454) {
      p = 1;
    } 
    return p;
  }
  static double N53e2b02448(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() <= 2.415699) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() > 2.415699) {
      p = 0;
    } 
    return p;
  }
  static double N479a368249(Object []i) {
    double p = Double.NaN;
    if (i[50] == null) {
      p = 1;
    } else if (((Double) i[50]).doubleValue() <= 2.49025) {
    p = SubjectCPSDClassifier.N5d70b58750(i);
    } else if (((Double) i[50]).doubleValue() > 2.49025) {
    p = SubjectCPSDClassifier.N3b5ff43e52(i);
    } 
    return p;
  }
  static double N5d70b58750(Object []i) {
    double p = Double.NaN;
    if (i[32] == null) {
      p = 1;
    } else if (((Double) i[32]).doubleValue() <= 2.526103) {
    p = SubjectCPSDClassifier.N129de1fe51(i);
    } else if (((Double) i[32]).doubleValue() > 2.526103) {
      p = 0;
    } 
    return p;
  }
  static double N129de1fe51(Object []i) {
    double p = Double.NaN;
    if (i[45] == null) {
      p = 0;
    } else if (((Double) i[45]).doubleValue() <= 1.924302) {
      p = 0;
    } else if (((Double) i[45]).doubleValue() > 1.924302) {
      p = 1;
    } 
    return p;
  }
  static double N3b5ff43e52(Object []i) {
    double p = Double.NaN;
    if (i[55] == null) {
      p = 1;
    } else if (((Double) i[55]).doubleValue() <= 2.20661) {
      p = 1;
    } else if (((Double) i[55]).doubleValue() > 2.20661) {
      p = 0;
    } 
    return p;
  }
  static double N189c30e653(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() <= 2.313059) {
    p = SubjectCPSDClassifier.N364e58a954(i);
    } else if (((Double) i[5]).doubleValue() > 2.313059) {
    p = SubjectCPSDClassifier.N15d2631857(i);
    } 
    return p;
  }
  static double N364e58a954(Object []i) {
    double p = Double.NaN;
    if (i[87] == null) {
      p = 0;
    } else if (((Double) i[87]).doubleValue() <= 2.269194) {
    p = SubjectCPSDClassifier.N519edb1955(i);
    } else if (((Double) i[87]).doubleValue() > 2.269194) {
      p = 1;
    } 
    return p;
  }
  static double N519edb1955(Object []i) {
    double p = Double.NaN;
    if (i[70] == null) {
      p = 1;
    } else if (((Double) i[70]).doubleValue() <= 2.025627) {
      p = 1;
    } else if (((Double) i[70]).doubleValue() > 2.025627) {
    p = SubjectCPSDClassifier.N1442702c56(i);
    } 
    return p;
  }
  static double N1442702c56(Object []i) {
    double p = Double.NaN;
    if (i[27] == null) {
      p = 1;
    } else if (((Double) i[27]).doubleValue() <= 1.620867) {
      p = 1;
    } else if (((Double) i[27]).doubleValue() > 1.620867) {
      p = 0;
    } 
    return p;
  }
  static double N15d2631857(Object []i) {
    double p = Double.NaN;
    if (i[11] == null) {
      p = 0;
    } else if (((Double) i[11]).doubleValue() <= 2.450718) {
      p = 0;
    } else if (((Double) i[11]).doubleValue() > 2.450718) {
    p = SubjectCPSDClassifier.N2767c7d958(i);
    } 
    return p;
  }
  static double N2767c7d958(Object []i) {
    double p = Double.NaN;
    if (i[37] == null) {
      p = 0;
    } else if (((Double) i[37]).doubleValue() <= 1.848426) {
      p = 0;
    } else if (((Double) i[37]).doubleValue() > 1.848426) {
      p = 1;
    } 
    return p;
  }
  static double Ne718bc559(Object []i) {
    double p = Double.NaN;
    if (i[39] == null) {
      p = 1;
    } else if (((Double) i[39]).doubleValue() <= 2.139072) {
      p = 1;
    } else if (((Double) i[39]).doubleValue() > 2.139072) {
    p = SubjectCPSDClassifier.N408f39eb60(i);
    } 
    return p;
  }
  static double N408f39eb60(Object []i) {
    double p = Double.NaN;
    if (i[69] == null) {
      p = 0;
    } else if (((Double) i[69]).doubleValue() <= 1.739859) {
      p = 0;
    } else if (((Double) i[69]).doubleValue() > 1.739859) {
      p = 1;
    } 
    return p;
  }
  static double N7b23946961(Object []i) {
    double p = Double.NaN;
    if (i[50] == null) {
      p = 1;
    } else if (((Double) i[50]).doubleValue() <= 2.149786) {
      p = 1;
    } else if (((Double) i[50]).doubleValue() > 2.149786) {
    p = SubjectCPSDClassifier.N5ce4b8a762(i);
    } 
    return p;
  }
  static double N5ce4b8a762(Object []i) {
    double p = Double.NaN;
    if (i[40] == null) {
      p = 0;
    } else if (((Double) i[40]).doubleValue() <= 1.874606) {
    p = SubjectCPSDClassifier.N2c170b9663(i);
    } else if (((Double) i[40]).doubleValue() > 1.874606) {
      p = 0;
    } 
    return p;
  }
  static double N2c170b9663(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 2.029014) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 2.029014) {
      p = 1;
    } 
    return p;
  }
}
