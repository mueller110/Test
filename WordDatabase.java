import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WordDatabase {
	static double idf[] = new double[(int) Math.pow(10, 6)]; // per 10 M
	static int total = 0;

	public static void initDatabase() {
		BufferedReader br = null;

		try {
			for (int i = 0; i < idf.length; i++) {
				idf[i] = -1;
			}
			String cLine;

			br = new BufferedReader(new FileReader("written.num"));

			while ((cLine = br.readLine()) != null) {
				String words[] = cLine.split("(\\s+)");
				if (words.length == 4) {
					int hash = (int) ((words[1].hashCode() & 0x7fffffff) % idf.length);
					int value = StringToInt(words[3]);

					if (value == 3209) {
						idf[hash] = 0;
					} else {
						double res = Math.log10(3209 / value);
						if (idf[hash] != -1) {			
							if (res < idf[hash])
								idf[hash] = res;
						} else {
							idf[hash] = res;
						}
					}					

				}
			}
			for (int i = 0; i < idf.length; i++) {
				if (idf[i]==-1){
					idf[i] = 0; //log10(3029/1) =3.48
				}
			}
			
			

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	static int StringToInt(String number) {
		int i = 0;
		int res = 0;
		int to = number.length() - 1;
		while (i <= to) {
			int cpos = number.charAt(i) - 48;
			res = (int) (res + cpos * Math.pow(10, to - i));
			i = i + 1;
		}
		return res;
	}

	static int power(int base, int exp) {
		if (exp == 0)
			return 1;
		int res = base;
		while (exp > 1) {
			res = res * base;
			exp = exp - 1;
		}
		return res;
	}

}