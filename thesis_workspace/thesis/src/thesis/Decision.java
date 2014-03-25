package thesis;



class Decision {

		public static double classify(Object[] i)
				throws Exception {

			double p = Double.NaN;
			p = Decision.N7178820c0(i);
			return p;
		}
		static double N7178820c0(Object []i) {
			double p = Double.NaN;
			if (i[0] == null) {
				p = 1;
			} else if (i[0].equals("sunny")) {
				p = Decision.N54aad8f91(i);
			} else if (i[0].equals("overcast")) {
				p = 0;
			} else if (i[0].equals("rainy")) {
				p = Decision.N1c8aeedc2(i);
			} 
			return p;
		}
		static double N54aad8f91(Object []i) {
			double p = Double.NaN;
			if (i[2] == null) {
				p = 0;
			} else if (((Double) i[2]).doubleValue() <= 75.0) {
				p = 0;
			} else if (((Double) i[2]).doubleValue() > 75.0) {
				p = 1;
			} 
			return p;
		}
		static double N1c8aeedc2(Object []i) {
			double p = Double.NaN;
			if (i[3] == null) {
				p = 1;
			} else if (i[3].equals("true")) {
				p = 1;
			} else if (i[3].equals("false")) {
				p = 0;
			} 
			return p;
		}
		
		public static void main(String args []) {
			Object [] i = { "sunny", 85.0, 85.0, false };
			Object [] j = { "overcast", 83.0, 86.0, false };
			Object [] k = { "sunny", 80.0, 90.0, true };
			Object [] l = { "rainy", 80.0, 90.0, true };
			try {
				System.out.println(Decision.classify(i));
				System.out.println(Decision.classify(j));
				System.out.println(Decision.classify(k));
				System.out.println(Decision.classify(l));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
