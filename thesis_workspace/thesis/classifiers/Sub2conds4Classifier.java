package thesis;

class Sub2conds4Classifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = Sub2conds4Classifier.N156f6adb0(i);
    return p;
  }
  static double N156f6adb0(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 2;
    } else if (((Double) i[3]).doubleValue() <= 0.78553467989) {
    p = Sub2conds4Classifier.N45d05adb1(i);
    } else if (((Double) i[3]).doubleValue() > 0.78553467989) {
    p = Sub2conds4Classifier.N56157d7f166(i);
    } 
    return p;
  }
  static double N45d05adb1(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 2;
    } else if (((Double) i[3]).doubleValue() <= 0.001099579502) {
    p = Sub2conds4Classifier.N71fd03ab2(i);
    } else if (((Double) i[3]).doubleValue() > 0.001099579502) {
    p = Sub2conds4Classifier.N334d545c5(i);
    } 
    return p;
  }
  static double N71fd03ab2(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 2;
    } else if (((Double) i[1]).doubleValue() <= 5.4126409E-5) {
    p = Sub2conds4Classifier.N19fc0a043(i);
    } else if (((Double) i[1]).doubleValue() > 5.4126409E-5) {
    p = Sub2conds4Classifier.N71f599ef4(i);
    } 
    return p;
  }
  static double N19fc0a043(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() <= 0.29675) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() > 0.29675) {
      p = 2;
    } 
    return p;
  }
  static double N71f599ef4(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 2;
    } else if (((Double) i[5]).doubleValue() <= 0.562679) {
      p = 2;
    } else if (((Double) i[5]).doubleValue() > 0.562679) {
      p = 0;
    } 
    return p;
  }
  static double N334d545c5(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 2;
    } else if (((Double) i[5]).doubleValue() <= 0.775018) {
    p = Sub2conds4Classifier.N19dd7e546(i);
    } else if (((Double) i[5]).doubleValue() > 0.775018) {
    p = Sub2conds4Classifier.N42f7a5c143(i);
    } 
    return p;
  }
  static double N19dd7e546(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 2;
    } else if (((Double) i[1]).doubleValue() <= 0.223783120513) {
    p = Sub2conds4Classifier.N1c90ca107(i);
    } else if (((Double) i[1]).doubleValue() > 0.223783120513) {
    p = Sub2conds4Classifier.N6ebc808109(i);
    } 
    return p;
  }
  static double N1c90ca107(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 0;
    } else if (((Double) i[4]).doubleValue() <= 0.3) {
    p = Sub2conds4Classifier.N65c1ef168(i);
    } else if (((Double) i[4]).doubleValue() > 0.3) {
    p = Sub2conds4Classifier.N4f955f5e16(i);
    } 
    return p;
  }
  static double N65c1ef168(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 0;
    } else if (((Double) i[2]).doubleValue() <= 0.227864772081) {
    p = Sub2conds4Classifier.N1f37c18b9(i);
    } else if (((Double) i[2]).doubleValue() > 0.227864772081) {
    p = Sub2conds4Classifier.N3de7648114(i);
    } 
    return p;
  }
  static double N1f37c18b9(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 0;
    } else if (((Double) i[3]).doubleValue() <= 0.003798964433) {
    p = Sub2conds4Classifier.N5839cb010(i);
    } else if (((Double) i[3]).doubleValue() > 0.003798964433) {
    p = Sub2conds4Classifier.N3a3e9a411(i);
    } 
    return p;
  }
  static double N5839cb010(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 7.75956141E-4) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() > 7.75956141E-4) {
      p = 0;
    } 
    return p;
  }
  static double N3a3e9a411(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() <= 0.015883024782) {
    p = Sub2conds4Classifier.N7e400fd912(i);
    } else if (((Double) i[3]).doubleValue() > 0.015883024782) {
      p = 0;
    } 
    return p;
  }
  static double N7e400fd912(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() <= 0.011576230638) {
    p = Sub2conds4Classifier.N1fd0b05013(i);
    } else if (((Double) i[3]).doubleValue() > 0.011576230638) {
      p = 2;
    } 
    return p;
  }
  static double N1fd0b05013(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() <= 1.23304E-5) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() > 1.23304E-5) {
      p = 1;
    } 
    return p;
  }
  static double N3de7648114(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 3;
    } else if (((Double) i[5]).doubleValue() <= 0.456851) {
    p = Sub2conds4Classifier.N227770e715(i);
    } else if (((Double) i[5]).doubleValue() > 0.456851) {
      p = 2;
    } 
    return p;
  }
  static double N227770e715(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 2;
    } else if (((Double) i[6]).doubleValue() <= 0.56085) {
      p = 2;
    } else if (((Double) i[6]).doubleValue() > 0.56085) {
      p = 3;
    } 
    return p;
  }
  static double N4f955f5e16(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 2;
    } else if (((Double) i[4]).doubleValue() <= 0.6) {
    p = Sub2conds4Classifier.N57a83d2317(i);
    } else if (((Double) i[4]).doubleValue() > 0.6) {
    p = Sub2conds4Classifier.Nf39515867(i);
    } 
    return p;
  }
  static double N57a83d2317(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() <= 0.015413998626) {
    p = Sub2conds4Classifier.N6645f1ca18(i);
    } else if (((Double) i[0]).doubleValue() > 0.015413998626) {
    p = Sub2conds4Classifier.N119e5e6b51(i);
    } 
    return p;
  }
  static double N6645f1ca18(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 3;
    } else if (((Double) i[5]).doubleValue() <= 0.290143) {
    p = Sub2conds4Classifier.N7a05393a19(i);
    } else if (((Double) i[5]).doubleValue() > 0.290143) {
    p = Sub2conds4Classifier.N146ae3ad21(i);
    } 
    return p;
  }
  static double N7a05393a19(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 2;
    } else if (((Double) i[7]).doubleValue() <= 0.31329) {
    p = Sub2conds4Classifier.N6cf04d6b20(i);
    } else if (((Double) i[7]).doubleValue() > 0.31329) {
      p = 3;
    } 
    return p;
  }
  static double N6cf04d6b20(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 3;
    } else if (((Double) i[7]).doubleValue() <= 0.230463) {
      p = 3;
    } else if (((Double) i[7]).doubleValue() > 0.230463) {
      p = 2;
    } 
    return p;
  }
  static double N146ae3ad21(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 2;
    } else if (((Double) i[3]).doubleValue() <= 0.015244918875) {
    p = Sub2conds4Classifier.N75897d5322(i);
    } else if (((Double) i[3]).doubleValue() > 0.015244918875) {
    p = Sub2conds4Classifier.N59ac547129(i);
    } 
    return p;
  }
  static double N75897d5322(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.465392) {
    p = Sub2conds4Classifier.N1583106e23(i);
    } else if (((Double) i[6]).doubleValue() > 0.465392) {
    p = Sub2conds4Classifier.N4fabb0ca24(i);
    } 
    return p;
  }
  static double N1583106e23(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 0;
    } else if (((Double) i[3]).doubleValue() <= 0.003529428272) {
      p = 0;
    } else if (((Double) i[3]).doubleValue() > 0.003529428272) {
      p = 3;
    } 
    return p;
  }
  static double N4fabb0ca24(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.52095) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 0.52095) {
    p = Sub2conds4Classifier.N10e3c6a325(i);
    } 
    return p;
  }
  static double N10e3c6a325(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 2;
    } else if (((Double) i[0]).doubleValue() <= 0.009241454303) {
    p = Sub2conds4Classifier.N5a05fffe26(i);
    } else if (((Double) i[0]).doubleValue() > 0.009241454303) {
      p = 1;
    } 
    return p;
  }
  static double N5a05fffe26(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 2;
    } else if (((Double) i[5]).doubleValue() <= 0.692669) {
      p = 2;
    } else if (((Double) i[5]).doubleValue() > 0.692669) {
    p = Sub2conds4Classifier.N3fe9aade27(i);
    } 
    return p;
  }
  static double N3fe9aade27(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 0;
    } else if (((Double) i[2]).doubleValue() <= 0.989334464073) {
    p = Sub2conds4Classifier.N5d0f59a28(i);
    } else if (((Double) i[2]).doubleValue() > 0.989334464073) {
      p = 2;
    } 
    return p;
  }
  static double N5d0f59a28(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 2;
    } else if (((Double) i[0]).doubleValue() <= 4.28390107E-4) {
      p = 2;
    } else if (((Double) i[0]).doubleValue() > 4.28390107E-4) {
      p = 0;
    } 
    return p;
  }
  static double N59ac547129(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 5.91111078E-4) {
    p = Sub2conds4Classifier.N40dbf4b530(i);
    } else if (((Double) i[1]).doubleValue() > 5.91111078E-4) {
    p = Sub2conds4Classifier.N158f07f140(i);
    } 
    return p;
  }
  static double N40dbf4b530(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() <= 0.73893) {
    p = Sub2conds4Classifier.N289eb85731(i);
    } else if (((Double) i[5]).doubleValue() > 0.73893) {
    p = Sub2conds4Classifier.N1d5c484e39(i);
    } 
    return p;
  }
  static double N289eb85731(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 0;
    } else if (((Double) i[2]).doubleValue() <= 0.763814210892) {
    p = Sub2conds4Classifier.N4c586c9632(i);
    } else if (((Double) i[2]).doubleValue() > 0.763814210892) {
    p = Sub2conds4Classifier.N174709c634(i);
    } 
    return p;
  }
  static double N4c586c9632(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() <= 7.62525655E-4) {
    p = Sub2conds4Classifier.N4141130233(i);
    } else if (((Double) i[0]).doubleValue() > 7.62525655E-4) {
      p = 0;
    } 
    return p;
  }
  static double N4141130233(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() <= 9.5942531E-5) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() > 9.5942531E-5) {
      p = 1;
    } 
    return p;
  }
  static double N174709c634(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 2;
    } else if (((Double) i[2]).doubleValue() <= 0.826941311359) {
      p = 2;
    } else if (((Double) i[2]).doubleValue() > 0.826941311359) {
    p = Sub2conds4Classifier.N38d2b21a35(i);
    } 
    return p;
  }
  static double N38d2b21a35(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() <= 0.002159640193) {
    p = Sub2conds4Classifier.N10aae61b36(i);
    } else if (((Double) i[0]).doubleValue() > 0.002159640193) {
      p = 0;
    } 
    return p;
  }
  static double N10aae61b36(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() <= 0.499992) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() > 0.499992) {
    p = Sub2conds4Classifier.N43e8ff2937(i);
    } 
    return p;
  }
  static double N43e8ff2937(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 3;
    } else if (((Double) i[5]).doubleValue() <= 0.593321) {
    p = Sub2conds4Classifier.N75c7ff9338(i);
    } else if (((Double) i[5]).doubleValue() > 0.593321) {
      p = 0;
    } 
    return p;
  }
  static double N75c7ff9338(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 3;
    } else if (((Double) i[0]).doubleValue() <= 1.52591092E-4) {
      p = 3;
    } else if (((Double) i[0]).doubleValue() > 1.52591092E-4) {
      p = 2;
    } 
    return p;
  }
  static double N1d5c484e39(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() <= 2.99918145E-4) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() > 2.99918145E-4) {
      p = 3;
    } 
    return p;
  }
  static double N158f07f140(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 2;
    } else if (((Double) i[6]).doubleValue() <= 0.638946) {
      p = 2;
    } else if (((Double) i[6]).doubleValue() > 0.638946) {
    p = Sub2conds4Classifier.N61526a4541(i);
    } 
    return p;
  }
  static double N61526a4541(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 1;
    } else if (((Double) i[7]).doubleValue() <= 0.748947) {
    p = Sub2conds4Classifier.N69bf09ed42(i);
    } else if (((Double) i[7]).doubleValue() > 0.748947) {
      p = 0;
    } 
    return p;
  }
  static double N69bf09ed42(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 0.900373876095) {
    p = Sub2conds4Classifier.N7d24dcd743(i);
    } else if (((Double) i[2]).doubleValue() > 0.900373876095) {
    p = Sub2conds4Classifier.N754c61e749(i);
    } 
    return p;
  }
  static double N7d24dcd743(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() <= 0.750191) {
    p = Sub2conds4Classifier.N7f1ee77044(i);
    } else if (((Double) i[6]).doubleValue() > 0.750191) {
      p = 2;
    } 
    return p;
  }
  static double N7f1ee77044(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() <= 0.064022265375) {
    p = Sub2conds4Classifier.N45ecab4345(i);
    } else if (((Double) i[1]).doubleValue() > 0.064022265375) {
    p = Sub2conds4Classifier.Na69070748(i);
    } 
    return p;
  }
  static double N45ecab4345(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 0.693071186543) {
    p = Sub2conds4Classifier.N34dfdf9246(i);
    } else if (((Double) i[2]).doubleValue() > 0.693071186543) {
      p = 1;
    } 
    return p;
  }
  static double N34dfdf9246(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() <= 0.011975681409) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() > 0.011975681409) {
    p = Sub2conds4Classifier.N55cf055c47(i);
    } 
    return p;
  }
  static double N55cf055c47(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 0.02429716289) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 0.02429716289) {
      p = 2;
    } 
    return p;
  }
  static double Na69070748(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 4.14310751E-4) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() > 4.14310751E-4) {
      p = 0;
    } 
    return p;
  }
  static double N754c61e749(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 3;
    } else if (((Double) i[0]).doubleValue() <= 0.001406107796) {
    p = Sub2conds4Classifier.N69afc0da50(i);
    } else if (((Double) i[0]).doubleValue() > 0.001406107796) {
      p = 1;
    } 
    return p;
  }
  static double N69afc0da50(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() <= 0.01575477235) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() > 0.01575477235) {
      p = 2;
    } 
    return p;
  }
  static double N119e5e6b51(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 2;
    } else if (((Double) i[7]).doubleValue() <= 0.683586) {
    p = Sub2conds4Classifier.N3448cbd652(i);
    } else if (((Double) i[7]).doubleValue() > 0.683586) {
    p = Sub2conds4Classifier.N6ec21e5264(i);
    } 
    return p;
  }
  static double N3448cbd652(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 3;
    } else if (((Double) i[0]).doubleValue() <= 0.017847346142) {
    p = Sub2conds4Classifier.N173e696b53(i);
    } else if (((Double) i[0]).doubleValue() > 0.017847346142) {
    p = Sub2conds4Classifier.N27afbb954(i);
    } 
    return p;
  }
  static double N173e696b53(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 2;
    } else if (((Double) i[1]).doubleValue() <= 0.001096792053) {
      p = 2;
    } else if (((Double) i[1]).doubleValue() > 0.001096792053) {
      p = 3;
    } 
    return p;
  }
  static double N27afbb954(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 2;
    } else if (((Double) i[3]).doubleValue() <= 0.134117916226) {
    p = Sub2conds4Classifier.N58242ff455(i);
    } else if (((Double) i[3]).doubleValue() > 0.134117916226) {
    p = Sub2conds4Classifier.N205f4f59(i);
    } 
    return p;
  }
  static double N58242ff455(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 2;
    } else if (((Double) i[7]).doubleValue() <= 0.626725) {
    p = Sub2conds4Classifier.N2fc8696156(i);
    } else if (((Double) i[7]).doubleValue() > 0.626725) {
    p = Sub2conds4Classifier.N10ea348e58(i);
    } 
    return p;
  }
  static double N2fc8696156(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 2;
    } else if (((Double) i[6]).doubleValue() <= 0.486873) {
      p = 2;
    } else if (((Double) i[6]).doubleValue() > 0.486873) {
    p = Sub2conds4Classifier.Ne7e77c957(i);
    } 
    return p;
  }
  static double Ne7e77c957(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 2;
    } else if (((Double) i[2]).doubleValue() <= 0.906609356403) {
      p = 2;
    } else if (((Double) i[2]).doubleValue() > 0.906609356403) {
      p = 1;
    } 
    return p;
  }
  static double N10ea348e58(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 3;
    } else if (((Double) i[6]).doubleValue() <= 0.668094) {
      p = 3;
    } else if (((Double) i[6]).doubleValue() > 0.668094) {
      p = 2;
    } 
    return p;
  }
  static double N205f4f59(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 2.11706531E-4) {
    p = Sub2conds4Classifier.N4d513b9960(i);
    } else if (((Double) i[1]).doubleValue() > 2.11706531E-4) {
    p = Sub2conds4Classifier.N101fe17761(i);
    } 
    return p;
  }
  static double N4d513b9960(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 3;
    } else if (((Double) i[3]).doubleValue() <= 0.158542022109) {
      p = 3;
    } else if (((Double) i[3]).doubleValue() > 0.158542022109) {
      p = 0;
    } 
    return p;
  }
  static double N101fe17761(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 2;
    } else if (((Double) i[3]).doubleValue() <= 0.320539861917) {
    p = Sub2conds4Classifier.N1d0b53e662(i);
    } else if (((Double) i[3]).doubleValue() > 0.320539861917) {
    p = Sub2conds4Classifier.N52b12fef63(i);
    } 
    return p;
  }
  static double N1d0b53e662(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() <= 0.175512656569) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() > 0.175512656569) {
      p = 2;
    } 
    return p;
  }
  static double N52b12fef63(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() <= 0.010904861614) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() > 0.010904861614) {
      p = 3;
    } 
    return p;
  }
  static double N6ec21e5264(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 3;
    } else if (((Double) i[6]).doubleValue() <= 0.786277) {
    p = Sub2conds4Classifier.Na54d24d65(i);
    } else if (((Double) i[6]).doubleValue() > 0.786277) {
    p = Sub2conds4Classifier.N46bac28766(i);
    } 
    return p;
  }
  static double Na54d24d65(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 3;
    } else if (((Double) i[5]).doubleValue() <= 0.74303) {
      p = 3;
    } else if (((Double) i[5]).doubleValue() > 0.74303) {
      p = 2;
    } 
    return p;
  }
  static double N46bac28766(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 0.807448506355) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() > 0.807448506355) {
      p = 3;
    } 
    return p;
  }
  static double Nf39515867(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() <= 0.682987) {
    p = Sub2conds4Classifier.N7c0b703668(i);
    } else if (((Double) i[5]).doubleValue() > 0.682987) {
    p = Sub2conds4Classifier.N22480241106(i);
    } 
    return p;
  }
  static double N7c0b703668(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 2;
    } else if (((Double) i[5]).doubleValue() <= 0.600545) {
    p = Sub2conds4Classifier.N52f428d969(i);
    } else if (((Double) i[5]).doubleValue() > 0.600545) {
    p = Sub2conds4Classifier.N4c21e59a100(i);
    } 
    return p;
  }
  static double N52f428d969(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 2;
    } else if (((Double) i[2]).doubleValue() <= 0.394368618727) {
    p = Sub2conds4Classifier.N1ba5e91b70(i);
    } else if (((Double) i[2]).doubleValue() > 0.394368618727) {
    p = Sub2conds4Classifier.N7e4066db96(i);
    } 
    return p;
  }
  static double N1ba5e91b70(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 2;
    } else if (((Double) i[1]).doubleValue() <= 0.20316812396) {
    p = Sub2conds4Classifier.N296af9cb71(i);
    } else if (((Double) i[1]).doubleValue() > 0.20316812396) {
    p = Sub2conds4Classifier.N53497f3c95(i);
    } 
    return p;
  }
  static double N296af9cb71(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 0.186054319143) {
    p = Sub2conds4Classifier.N2e3593ab72(i);
    } else if (((Double) i[2]).doubleValue() > 0.186054319143) {
    p = Sub2conds4Classifier.N4aeacb4a74(i);
    } 
    return p;
  }
  static double N2e3593ab72(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() <= 0.294211) {
    p = Sub2conds4Classifier.N3f71d74073(i);
    } else if (((Double) i[5]).doubleValue() > 0.294211) {
      p = 2;
    } 
    return p;
  }
  static double N3f71d74073(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.218558) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 0.218558) {
      p = 0;
    } 
    return p;
  }
  static double N4aeacb4a74(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 2;
    } else if (((Double) i[5]).doubleValue() <= 0.346383) {
    p = Sub2conds4Classifier.N7cc88db275(i);
    } else if (((Double) i[5]).doubleValue() > 0.346383) {
    p = Sub2conds4Classifier.N2ad6d4be81(i);
    } 
    return p;
  }
  static double N7cc88db275(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() <= 0.217229) {
    p = Sub2conds4Classifier.N52cee11e76(i);
    } else if (((Double) i[5]).doubleValue() > 0.217229) {
    p = Sub2conds4Classifier.Nc1da30b77(i);
    } 
    return p;
  }
  static double N52cee11e76(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 3;
    } else if (((Double) i[0]).doubleValue() <= 0.011385956779) {
      p = 3;
    } else if (((Double) i[0]).doubleValue() > 0.011385956779) {
      p = 0;
    } 
    return p;
  }
  static double Nc1da30b77(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 2;
    } else if (((Double) i[5]).doubleValue() <= 0.310956) {
    p = Sub2conds4Classifier.N6dbb2d6378(i);
    } else if (((Double) i[5]).doubleValue() > 0.310956) {
    p = Sub2conds4Classifier.N64af355680(i);
    } 
    return p;
  }
  static double N6dbb2d6378(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 0;
    } else if (((Double) i[2]).doubleValue() <= 0.249384149909) {
    p = Sub2conds4Classifier.N1ba0f6dd79(i);
    } else if (((Double) i[2]).doubleValue() > 0.249384149909) {
      p = 2;
    } 
    return p;
  }
  static double N1ba0f6dd79(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 2;
    } else if (((Double) i[2]).doubleValue() <= 0.221115574241) {
      p = 2;
    } else if (((Double) i[2]).doubleValue() > 0.221115574241) {
      p = 0;
    } 
    return p;
  }
  static double N64af355680(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 0.02429716289) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() > 0.02429716289) {
      p = 3;
    } 
    return p;
  }
  static double N2ad6d4be81(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 3;
    } else if (((Double) i[0]).doubleValue() <= 6.5234432E-5) {
    p = Sub2conds4Classifier.N7e291bea82(i);
    } else if (((Double) i[0]).doubleValue() > 6.5234432E-5) {
    p = Sub2conds4Classifier.N1a5c9f2986(i);
    } 
    return p;
  }
  static double N7e291bea82(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() <= 2.527291E-5) {
    p = Sub2conds4Classifier.N3ce7e05b83(i);
    } else if (((Double) i[0]).doubleValue() > 2.527291E-5) {
      p = 3;
    } 
    return p;
  }
  static double N3ce7e05b83(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() <= 0.711278915405) {
    p = Sub2conds4Classifier.N1b3a959a84(i);
    } else if (((Double) i[3]).doubleValue() > 0.711278915405) {
      p = 3;
    } 
    return p;
  }
  static double N1b3a959a84(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.526004) {
    p = Sub2conds4Classifier.N2333bf6d85(i);
    } else if (((Double) i[5]).doubleValue() > 0.526004) {
      p = 0;
    } 
    return p;
  }
  static double N2333bf6d85(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() <= 0.032840568572) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() > 0.032840568572) {
      p = 0;
    } 
    return p;
  }
  static double N1a5c9f2986(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 0.208885371685) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() > 0.208885371685) {
    p = Sub2conds4Classifier.N36d54a4487(i);
    } 
    return p;
  }
  static double N36d54a4487(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 2;
    } else if (((Double) i[6]).doubleValue() <= 0.512242) {
    p = Sub2conds4Classifier.N6cfed27b88(i);
    } else if (((Double) i[6]).doubleValue() > 0.512242) {
    p = Sub2conds4Classifier.N1b393a091(i);
    } 
    return p;
  }
  static double N6cfed27b88(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 2;
    } else if (((Double) i[6]).doubleValue() <= 0.473887) {
    p = Sub2conds4Classifier.N4dacc12489(i);
    } else if (((Double) i[6]).doubleValue() > 0.473887) {
      p = 0;
    } 
    return p;
  }
  static double N4dacc12489(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() <= 0.581705152988) {
    p = Sub2conds4Classifier.N8bc4a5390(i);
    } else if (((Double) i[3]).doubleValue() > 0.581705152988) {
      p = 2;
    } 
    return p;
  }
  static double N8bc4a5390(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() <= 0.527915298939) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() > 0.527915298939) {
      p = 3;
    } 
    return p;
  }
  static double N1b393a091(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 2;
    } else if (((Double) i[7]).doubleValue() <= 0.555925) {
    p = Sub2conds4Classifier.N34a0ee3f92(i);
    } else if (((Double) i[7]).doubleValue() > 0.555925) {
    p = Sub2conds4Classifier.N2110c26194(i);
    } 
    return p;
  }
  static double N34a0ee3f92(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 2;
    } else if (((Double) i[1]).doubleValue() <= 0.028122402728) {
    p = Sub2conds4Classifier.N3179851793(i);
    } else if (((Double) i[1]).doubleValue() > 0.028122402728) {
      p = 3;
    } 
    return p;
  }
  static double N3179851793(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 2;
    } else if (((Double) i[6]).doubleValue() <= 0.553685) {
      p = 2;
    } else if (((Double) i[6]).doubleValue() > 0.553685) {
      p = 3;
    } 
    return p;
  }
  static double N2110c26194(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 2;
    } else if (((Double) i[0]).doubleValue() <= 4.46252321E-4) {
      p = 2;
    } else if (((Double) i[0]).doubleValue() > 4.46252321E-4) {
      p = 1;
    } 
    return p;
  }
  static double N53497f3c95(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() <= 0.001352681895) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() > 0.001352681895) {
      p = 3;
    } 
    return p;
  }
  static double N7e4066db96(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() <= 0.003168125637) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() > 0.003168125637) {
    p = Sub2conds4Classifier.N3620f49e97(i);
    } 
    return p;
  }
  static double N3620f49e97(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.507313) {
    p = Sub2conds4Classifier.N2d9bccd598(i);
    } else if (((Double) i[6]).doubleValue() > 0.507313) {
      p = 1;
    } 
    return p;
  }
  static double N2d9bccd598(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() <= 0.024201994762) {
    p = Sub2conds4Classifier.N4fa4cb5799(i);
    } else if (((Double) i[0]).doubleValue() > 0.024201994762) {
      p = 1;
    } 
    return p;
  }
  static double N4fa4cb5799(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 3;
    } else if (((Double) i[7]).doubleValue() <= 0.315498) {
      p = 3;
    } else if (((Double) i[7]).doubleValue() > 0.315498) {
      p = 0;
    } 
    return p;
  }
  static double N4c21e59a100(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 7.29848922E-4) {
    p = Sub2conds4Classifier.N45690882101(i);
    } else if (((Double) i[0]).doubleValue() > 7.29848922E-4) {
      p = 0;
    } 
    return p;
  }
  static double N45690882101(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.477582) {
    p = Sub2conds4Classifier.N72adb267102(i);
    } else if (((Double) i[6]).doubleValue() > 0.477582) {
    p = Sub2conds4Classifier.N711fee78104(i);
    } 
    return p;
  }
  static double N72adb267102(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() <= 0.020848419517) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() > 0.020848419517) {
    p = Sub2conds4Classifier.N699bc302103(i);
    } 
    return p;
  }
  static double N699bc302103(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() <= 5.7225461E-5) {
      p = 0;
    } else if (((Double) i[0]).doubleValue() > 5.7225461E-5) {
      p = 3;
    } 
    return p;
  }
  static double N711fee78104(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 3;
    } else if (((Double) i[0]).doubleValue() <= 2.95893551E-4) {
    p = Sub2conds4Classifier.N6761424d105(i);
    } else if (((Double) i[0]).doubleValue() > 2.95893551E-4) {
      p = 2;
    } 
    return p;
  }
  static double N6761424d105(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 3;
    } else if (((Double) i[5]).doubleValue() <= 0.645616) {
      p = 3;
    } else if (((Double) i[5]).doubleValue() > 0.645616) {
      p = 1;
    } 
    return p;
  }
  static double N22480241106(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.4829) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() > 0.4829) {
    p = Sub2conds4Classifier.N258c0afc107(i);
    } 
    return p;
  }
  static double N258c0afc107(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 2;
    } else if (((Double) i[5]).doubleValue() <= 0.698891) {
      p = 2;
    } else if (((Double) i[5]).doubleValue() > 0.698891) {
    p = Sub2conds4Classifier.Nd2539a6108(i);
    } 
    return p;
  }
  static double Nd2539a6108(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 3;
    } else if (((Double) i[5]).doubleValue() <= 0.729078) {
      p = 3;
    } else if (((Double) i[5]).doubleValue() > 0.729078) {
      p = 2;
    } 
    return p;
  }
  static double N6ebc808109(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() <= 0.57509) {
    p = Sub2conds4Classifier.N619988c4110(i);
    } else if (((Double) i[6]).doubleValue() > 0.57509) {
    p = Sub2conds4Classifier.Ne718bc5134(i);
    } 
    return p;
  }
  static double N619988c4110(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 3;
    } else if (((Double) i[2]).doubleValue() <= 0.0106882127) {
    p = Sub2conds4Classifier.N26e22deb111(i);
    } else if (((Double) i[2]).doubleValue() > 0.0106882127) {
    p = Sub2conds4Classifier.N4b34b33e112(i);
    } 
    return p;
  }
  static double N26e22deb111(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() <= 0.969329476357) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() > 0.969329476357) {
      p = 1;
    } 
    return p;
  }
  static double N4b34b33e112(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 0.019671585411) {
    p = Sub2conds4Classifier.N70e3d204113(i);
    } else if (((Double) i[0]).doubleValue() > 0.019671585411) {
    p = Sub2conds4Classifier.N2767c7d9133(i);
    } 
    return p;
  }
  static double N70e3d204113(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 2.77061481E-4) {
    p = Sub2conds4Classifier.N7cf13e82114(i);
    } else if (((Double) i[0]).doubleValue() > 2.77061481E-4) {
    p = Sub2conds4Classifier.N364e58a9129(i);
    } 
    return p;
  }
  static double N7cf13e82114(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 0;
    } else if (((Double) i[4]).doubleValue() <= 0.3) {
    p = Sub2conds4Classifier.N423f08e3115(i);
    } else if (((Double) i[4]).doubleValue() > 0.3) {
    p = Sub2conds4Classifier.N561b0019120(i);
    } 
    return p;
  }
  static double N423f08e3115(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 1;
    } else if (((Double) i[7]).doubleValue() <= 0.462875) {
    p = Sub2conds4Classifier.N3860910f116(i);
    } else if (((Double) i[7]).doubleValue() > 0.462875) {
    p = Sub2conds4Classifier.N27a35cb3119(i);
    } 
    return p;
  }
  static double N3860910f116(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() <= 0.246242) {
      p = 0;
    } else if (((Double) i[5]).doubleValue() > 0.246242) {
    p = Sub2conds4Classifier.N4bd38cb3117(i);
    } 
    return p;
  }
  static double N4bd38cb3117(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.401696) {
    p = Sub2conds4Classifier.N2dba62a9118(i);
    } else if (((Double) i[5]).doubleValue() > 0.401696) {
      p = 0;
    } 
    return p;
  }
  static double N2dba62a9118(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 2;
    } else if (((Double) i[7]).doubleValue() <= 0.375139) {
      p = 2;
    } else if (((Double) i[7]).doubleValue() > 0.375139) {
      p = 1;
    } 
    return p;
  }
  static double N27a35cb3119(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 0;
    } else if (((Double) i[2]).doubleValue() <= 0.036062590778) {
      p = 0;
    } else if (((Double) i[2]).doubleValue() > 0.036062590778) {
      p = 3;
    } 
    return p;
  }
  static double N561b0019120(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.314066) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() > 0.314066) {
    p = Sub2conds4Classifier.N6a39579121(i);
    } 
    return p;
  }
  static double N6a39579121(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 3;
    } else if (((Double) i[4]).doubleValue() <= 0.6) {
    p = Sub2conds4Classifier.N5daa3e56122(i);
    } else if (((Double) i[4]).doubleValue() > 0.6) {
    p = Sub2conds4Classifier.N3b5ff43e127(i);
    } 
    return p;
  }
  static double N5daa3e56122(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.590792) {
    p = Sub2conds4Classifier.N53e2b024123(i);
    } else if (((Double) i[5]).doubleValue() > 0.590792) {
    p = Sub2conds4Classifier.N129de1fe126(i);
    } 
    return p;
  }
  static double N53e2b024123(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 3;
    } else if (((Double) i[0]).doubleValue() <= 2.5989524E-5) {
    p = Sub2conds4Classifier.N479a3682124(i);
    } else if (((Double) i[0]).doubleValue() > 2.5989524E-5) {
      p = 1;
    } 
    return p;
  }
  static double N479a3682124(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 2;
    } else if (((Double) i[1]).doubleValue() <= 0.327450960875) {
    p = Sub2conds4Classifier.N5d70b587125(i);
    } else if (((Double) i[1]).doubleValue() > 0.327450960875) {
      p = 1;
    } 
    return p;
  }
  static double N5d70b587125(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 2;
    } else if (((Double) i[6]).doubleValue() <= 0.417393) {
      p = 2;
    } else if (((Double) i[6]).doubleValue() > 0.417393) {
      p = 3;
    } 
    return p;
  }
  static double N129de1fe126(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 3;
    } else if (((Double) i[6]).doubleValue() <= 0.505244) {
      p = 3;
    } else if (((Double) i[6]).doubleValue() > 0.505244) {
      p = 0;
    } 
    return p;
  }
  static double N3b5ff43e127(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() <= 0.304745554924) {
    p = Sub2conds4Classifier.N189c30e6128(i);
    } else if (((Double) i[1]).doubleValue() > 0.304745554924) {
      p = 2;
    } 
    return p;
  }
  static double N189c30e6128(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 3;
    } else if (((Double) i[2]).doubleValue() <= 0.114147089422) {
      p = 3;
    } else if (((Double) i[2]).doubleValue() > 0.114147089422) {
      p = 1;
    } 
    return p;
  }
  static double N364e58a9129(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 0;
    } else if (((Double) i[6]).doubleValue() <= 0.35451) {
    p = Sub2conds4Classifier.N519edb19130(i);
    } else if (((Double) i[6]).doubleValue() > 0.35451) {
    p = Sub2conds4Classifier.N15d26318132(i);
    } 
    return p;
  }
  static double N519edb19130(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 0;
    } else if (((Double) i[1]).doubleValue() <= 0.672954857349) {
    p = Sub2conds4Classifier.N1442702c131(i);
    } else if (((Double) i[1]).doubleValue() > 0.672954857349) {
      p = 2;
    } 
    return p;
  }
  static double N1442702c131(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 2;
    } else if (((Double) i[2]).doubleValue() <= 0.020808074623) {
      p = 2;
    } else if (((Double) i[2]).doubleValue() > 0.020808074623) {
      p = 0;
    } 
    return p;
  }
  static double N15d26318132(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 2;
    } else if (((Double) i[4]).doubleValue() <= 0.6) {
      p = 2;
    } else if (((Double) i[4]).doubleValue() > 0.6) {
      p = 1;
    } 
    return p;
  }
  static double N2767c7d9133(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 0;
    } else if (((Double) i[2]).doubleValue() <= 0.043284345418) {
      p = 0;
    } else if (((Double) i[2]).doubleValue() > 0.043284345418) {
      p = 1;
    } 
    return p;
  }
  static double Ne718bc5134(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 2;
    } else if (((Double) i[5]).doubleValue() <= 0.522842) {
    p = Sub2conds4Classifier.N408f39eb135(i);
    } else if (((Double) i[5]).doubleValue() > 0.522842) {
    p = Sub2conds4Classifier.N2c170b96138(i);
    } 
    return p;
  }
  static double N408f39eb135(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.3) {
    p = Sub2conds4Classifier.N7b239469136(i);
    } else if (((Double) i[4]).doubleValue() > 0.3) {
      p = 2;
    } 
    return p;
  }
  static double N7b239469136(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 2;
    } else if (((Double) i[6]).doubleValue() <= 0.636871) {
    p = Sub2conds4Classifier.N5ce4b8a7137(i);
    } else if (((Double) i[6]).doubleValue() > 0.636871) {
      p = 0;
    } 
    return p;
  }
  static double N5ce4b8a7137(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 0.002750906628) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() > 0.002750906628) {
      p = 2;
    } 
    return p;
  }
  static double N2c170b96138(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 1;
    } else if (((Double) i[7]).doubleValue() <= 0.610745) {
      p = 1;
    } else if (((Double) i[7]).doubleValue() > 0.610745) {
    p = Sub2conds4Classifier.N1cf9bb77139(i);
    } 
    return p;
  }
  static double N1cf9bb77139(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() <= 0.228496596217) {
    p = Sub2conds4Classifier.N4f82907d140(i);
    } else if (((Double) i[3]).doubleValue() > 0.228496596217) {
    p = Sub2conds4Classifier.N4dc2753141(i);
    } 
    return p;
  }
  static double N4f82907d140(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.723122) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 0.723122) {
      p = 0;
    } 
    return p;
  }
  static double N4dc2753141(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 3;
    } else if (((Double) i[7]).doubleValue() <= 0.633007) {
      p = 3;
    } else if (((Double) i[7]).doubleValue() > 0.633007) {
    p = Sub2conds4Classifier.N1199bca3142(i);
    } 
    return p;
  }
  static double N1199bca3142(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() <= 0.733655) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() > 0.733655) {
      p = 2;
    } 
    return p;
  }
  static double N42f7a5c143(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 0.6) {
    p = Sub2conds4Classifier.N490a3029144(i);
    } else if (((Double) i[4]).doubleValue() > 0.6) {
    p = Sub2conds4Classifier.N28c800e7163(i);
    } 
    return p;
  }
  static double N490a3029144(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 3;
    } else if (((Double) i[4]).doubleValue() <= 0.3) {
      p = 3;
    } else if (((Double) i[4]).doubleValue() > 0.3) {
    p = Sub2conds4Classifier.N3bdff935145(i);
    } 
    return p;
  }
  static double N3bdff935145(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() <= 0.78756) {
    p = Sub2conds4Classifier.N6d622548146(i);
    } else if (((Double) i[6]).doubleValue() > 0.78756) {
    p = Sub2conds4Classifier.N64b19970160(i);
    } 
    return p;
  }
  static double N6d622548146(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 0.787702143192) {
    p = Sub2conds4Classifier.N467dd212147(i);
    } else if (((Double) i[2]).doubleValue() > 0.787702143192) {
    p = Sub2conds4Classifier.N69ecade2150(i);
    } 
    return p;
  }
  static double N467dd212147(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 2;
    } else if (((Double) i[1]).doubleValue() <= 4.22224053E-4) {
      p = 2;
    } else if (((Double) i[1]).doubleValue() > 4.22224053E-4) {
    p = Sub2conds4Classifier.N6e6abfe5148(i);
    } 
    return p;
  }
  static double N6e6abfe5148(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.801513) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 0.801513) {
    p = Sub2conds4Classifier.N225c8c05149(i);
    } 
    return p;
  }
  static double N225c8c05149(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() <= 0.005307896994) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() > 0.005307896994) {
      p = 2;
    } 
    return p;
  }
  static double N69ecade2150(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() <= 0.022687343881) {
    p = Sub2conds4Classifier.N318c06c2151(i);
    } else if (((Double) i[1]).doubleValue() > 0.022687343881) {
    p = Sub2conds4Classifier.N2a0406c4158(i);
    } 
    return p;
  }
  static double N318c06c2151(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 3;
    } else if (((Double) i[2]).doubleValue() <= 0.992158830166) {
    p = Sub2conds4Classifier.N600fc3f7152(i);
    } else if (((Double) i[2]).doubleValue() > 0.992158830166) {
    p = Sub2conds4Classifier.N5e8bc44a157(i);
    } 
    return p;
  }
  static double N600fc3f7152(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 3;
    } else if (((Double) i[0]).doubleValue() <= 2.4341443E-5) {
    p = Sub2conds4Classifier.N2b0abe66153(i);
    } else if (((Double) i[0]).doubleValue() > 2.4341443E-5) {
    p = Sub2conds4Classifier.N4e5a309d154(i);
    } 
    return p;
  }
  static double N2b0abe66153(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() <= 0.777621) {
      p = 0;
    } else if (((Double) i[7]).doubleValue() > 0.777621) {
      p = 3;
    } 
    return p;
  }
  static double N4e5a309d154(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 3;
    } else if (((Double) i[6]).doubleValue() <= 0.645582) {
      p = 3;
    } else if (((Double) i[6]).doubleValue() > 0.645582) {
    p = Sub2conds4Classifier.N32dbb9b155(i);
    } 
    return p;
  }
  static double N32dbb9b155(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() <= 0.74406) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() > 0.74406) {
    p = Sub2conds4Classifier.N2f77bebe156(i);
    } 
    return p;
  }
  static double N2f77bebe156(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() <= 0.004260466434) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() > 0.004260466434) {
      p = 1;
    } 
    return p;
  }
  static double N5e8bc44a157(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 2;
    } else if (((Double) i[2]).doubleValue() <= 0.996263444424) {
      p = 2;
    } else if (((Double) i[2]).doubleValue() > 0.996263444424) {
      p = 1;
    } 
    return p;
  }
  static double N2a0406c4158(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 6.2897518E-5) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() > 6.2897518E-5) {
    p = Sub2conds4Classifier.N6e584368159(i);
    } 
    return p;
  }
  static double N6e584368159(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 2;
    } else if (((Double) i[2]).doubleValue() <= 0.928806960583) {
      p = 2;
    } else if (((Double) i[2]).doubleValue() > 0.928806960583) {
      p = 1;
    } 
    return p;
  }
  static double N64b19970160(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() <= 0.004463285208) {
    p = Sub2conds4Classifier.N47d0bbb5161(i);
    } else if (((Double) i[1]).doubleValue() > 0.004463285208) {
    p = Sub2conds4Classifier.N58d38ce8162(i);
    } 
    return p;
  }
  static double N47d0bbb5161(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 3;
    } else if (((Double) i[6]).doubleValue() <= 0.827851) {
      p = 3;
    } else if (((Double) i[6]).doubleValue() > 0.827851) {
      p = 1;
    } 
    return p;
  }
  static double N58d38ce8162(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() <= 0.101324543357) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() > 0.101324543357) {
      p = 0;
    } 
    return p;
  }
  static double N28c800e7163(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.804342) {
    p = Sub2conds4Classifier.N62b3529b164(i);
    } else if (((Double) i[5]).doubleValue() > 0.804342) {
      p = 0;
    } 
    return p;
  }
  static double N62b3529b164(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() <= 0.003268839559) {
    p = Sub2conds4Classifier.N6af470bc165(i);
    } else if (((Double) i[1]).doubleValue() > 0.003268839559) {
      p = 1;
    } 
    return p;
  }
  static double N6af470bc165(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() <= 1.71455205E-4) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() > 1.71455205E-4) {
      p = 3;
    } 
    return p;
  }
  static double N56157d7f166(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 3;
    } else if (((Double) i[6]).doubleValue() <= 0.57509) {
    p = Sub2conds4Classifier.N1ce24900167(i);
    } else if (((Double) i[6]).doubleValue() > 0.57509) {
    p = Sub2conds4Classifier.N52c71145178(i);
    } 
    return p;
  }
  static double N1ce24900167(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 3;
    } else if (((Double) i[0]).doubleValue() <= 0.023220101371) {
    p = Sub2conds4Classifier.N4c26add0168(i);
    } else if (((Double) i[0]).doubleValue() > 0.023220101371) {
    p = Sub2conds4Classifier.N7f4613f1173(i);
    } 
    return p;
  }
  static double N4c26add0168(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() <= 0.073530964553) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() > 0.073530964553) {
    p = Sub2conds4Classifier.N7f5d59be169(i);
    } 
    return p;
  }
  static double N7f5d59be169(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() <= 0.137626379728) {
    p = Sub2conds4Classifier.N49af0a45170(i);
    } else if (((Double) i[1]).doubleValue() > 0.137626379728) {
      p = 0;
    } 
    return p;
  }
  static double N49af0a45170(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 3.32500349E-4) {
    p = Sub2conds4Classifier.N2cb5dce171(i);
    } else if (((Double) i[0]).doubleValue() > 3.32500349E-4) {
    p = Sub2conds4Classifier.N757b80d0172(i);
    } 
    return p;
  }
  static double N2cb5dce171(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() <= 0.47784) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() > 0.47784) {
      p = 0;
    } 
    return p;
  }
  static double N757b80d0172(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 3;
    } else if (((Double) i[0]).doubleValue() <= 0.002447374864) {
      p = 3;
    } else if (((Double) i[0]).doubleValue() > 0.002447374864) {
      p = 0;
    } 
    return p;
  }
  static double N7f4613f1173(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 3;
    } else if (((Double) i[5]).doubleValue() <= 0.439529) {
    p = Sub2conds4Classifier.N51c7747e174(i);
    } else if (((Double) i[5]).doubleValue() > 0.439529) {
    p = Sub2conds4Classifier.N793f044c176(i);
    } 
    return p;
  }
  static double N51c7747e174(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() <= 0.032840568572) {
    p = Sub2conds4Classifier.N7db12623175(i);
    } else if (((Double) i[1]).doubleValue() > 0.032840568572) {
      p = 0;
    } 
    return p;
  }
  static double N7db12623175(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 0.056944500655) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() > 0.056944500655) {
      p = 3;
    } 
    return p;
  }
  static double N793f044c176(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() <= 0.834778785706) {
    p = Sub2conds4Classifier.N183357c4177(i);
    } else if (((Double) i[3]).doubleValue() > 0.834778785706) {
      p = 0;
    } 
    return p;
  }
  static double N183357c4177(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() <= 0.004235999659) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() > 0.004235999659) {
      p = 1;
    } 
    return p;
  }
  static double N52c71145178(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 1.96674489E-4) {
    p = Sub2conds4Classifier.Nb3ef378179(i);
    } else if (((Double) i[0]).doubleValue() > 1.96674489E-4) {
    p = Sub2conds4Classifier.N24be0018183(i);
    } 
    return p;
  }
  static double Nb3ef378179(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() <= 0.858742415905) {
      p = 1;
    } else if (((Double) i[3]).doubleValue() > 0.858742415905) {
    p = Sub2conds4Classifier.N51e2510c180(i);
    } 
    return p;
  }
  static double N51e2510c180(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 1;
    } else if (((Double) i[1]).doubleValue() <= 0.007823851891) {
    p = Sub2conds4Classifier.N613714d3181(i);
    } else if (((Double) i[1]).doubleValue() > 0.007823851891) {
      p = 3;
    } 
    return p;
  }
  static double N613714d3181(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 0.101580768824) {
    p = Sub2conds4Classifier.N67385a81182(i);
    } else if (((Double) i[2]).doubleValue() > 0.101580768824) {
      p = 2;
    } 
    return p;
  }
  static double N67385a81182(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 0.788735) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 0.788735) {
      p = 3;
    } 
    return p;
  }
  static double N24be0018183(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() <= 1.5879114E-5) {
      p = 3;
    } else if (((Double) i[1]).doubleValue() > 1.5879114E-5) {
    p = Sub2conds4Classifier.N35f83a80184(i);
    } 
    return p;
  }
  static double N35f83a80184(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 2;
    } else if (((Double) i[7]).doubleValue() <= 0.632807) {
      p = 2;
    } else if (((Double) i[7]).doubleValue() > 0.632807) {
    p = Sub2conds4Classifier.N3bc8c52e185(i);
    } 
    return p;
  }
  static double N3bc8c52e185(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 1;
    } else if (((Double) i[7]).doubleValue() <= 0.695702) {
      p = 1;
    } else if (((Double) i[7]).doubleValue() > 0.695702) {
    p = Sub2conds4Classifier.N7a096dab186(i);
    } 
    return p;
  }
  static double N7a096dab186(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 0.054715473205) {
    p = Sub2conds4Classifier.Nff3425187(i);
    } else if (((Double) i[2]).doubleValue() > 0.054715473205) {
    p = Sub2conds4Classifier.N72b869a5188(i);
    } 
    return p;
  }
  static double Nff3425187(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 0.00627862243) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() > 0.00627862243) {
      p = 2;
    } 
    return p;
  }
  static double N72b869a5188(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 2;
    } else if (((Double) i[0]).doubleValue() <= 6.60358812E-4) {
      p = 2;
    } else if (((Double) i[0]).doubleValue() > 6.60358812E-4) {
      p = 0;
    } 
    return p;
  }
}
