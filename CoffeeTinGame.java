package a1_2101040075;

import java.util.Arrays;

public class CoffeeTinGame {

	private static final char GREEN = 'G';
	private static final char BLUE = 'B';
	private static final char REMOVED = '-';
	private static final char NULL = '\u0000';
	private static char[] BeansBag;

	static {
		int MIN_BEANS = 30;
		BeansBag = new char[MIN_BEANS];
		int eachType = MIN_BEANS / 3;
		int restId = MIN_BEANS % 3;

		int index = 0;
		for (int i = 0; i < eachType; i++) {
			BeansBag[index++] = 'B';
		}
		for (int i = 0; i < eachType; i++) {
			BeansBag[index++] = 'G';
		}
		for (int i = 0; i < eachType; i++) {
			BeansBag[index++] = '-';
		}

		// Đảm bảo nếu MIN_BEANS đổi thành số không chia hết
		// cho 3 thì phân bố B và G cho các vị trí thừa
		if (restId == 1) {
			BeansBag[MIN_BEANS - 1] = 'B';
		} else if (restId == 2) {
			BeansBag[MIN_BEANS - 2] = 'B';
			BeansBag[MIN_BEANS - 1] = 'G';
		}
	}

	//MAIN
	public static void main(String[] args) {
		char[][] tins = { { BLUE, BLUE, BLUE, GREEN, GREEN },
				          { BLUE, BLUE, BLUE, GREEN, GREEN, GREEN },
				          { GREEN },
				          { BLUE },
				          { BLUE, GREEN } };

		for (int i = 0; i < tins.length; i++) {
			char[] tin = tins[i];

			int greens = 0;
			for (char bean : tin) {
				if (bean == GREEN)
					greens++;
			}
			// expected last bean
			final char last = (greens % 2 == 1) ? GREEN : BLUE;

			System.out.printf("%nTIN (%d Gs): %s %n", greens, Arrays.toString(tin));

			char lastBean = tinGame(tin);

			System.out.printf("tin after: %s %n", Arrays.toString(tin));

			if (lastBean == last) {
				System.out.printf("last bean: %c%n", lastBean);
			} else {
				System.out.printf("Oops, wrong last bean: %c (expected: %c)%n", lastBean, last);
			}
		}
	}

	//tinGame
	public static char tinGame(char[] tin) {
		while (hasAtLeastTwoBeans(tin)) {
			char[] twoBeans = takeTwo(tin);
			char b1 = twoBeans[0];
			char b2 = twoBeans[1];
			updateTin(tin, b1, b2);
		}
		return anyBean(tin);
	}

	//check con it nhat 2 beans trong hop
	private static boolean hasAtLeastTwoBeans(char[] tin) {
		int count = 0;
		for (char bean : tin) {
			if (bean != REMOVED) {
				count++;
			}
			if (count >= 2) {
				return true;
			}
		}
		return false;
	}

	//LAY 2 HAT
	private static char[] takeTwo(char[] tin) {
		char first = takeOne(tin);
		char second = takeOne(tin);
		return new char[] { first, second };
	}

	//LAY 1 HAT
	// check số lượng beans còn lại (if = 0 => return Null
	// >< randInt lấy 1 bean bất kì chưa removed => mark là removed sau khi đã lấy
	public static char takeOne(char[] tin) {
		int beansLeft = 0;
		for (char bean : tin)
			beansLeft += (bean != REMOVED) ? 1 : 0;

		if (beansLeft == 0)
			return NULL;

		int rndIndex;
		while (tin[(rndIndex = randInt(tin.length))] == REMOVED)
			;

		char bean = tin[rndIndex];
		tin[rndIndex] = REMOVED;
		return bean;
	}

	// method update dùng getBean() => 2 bean giong nhau=> get B, khac => get G =>
	// put lại tin
	public static void updateTin(char[] tin, char fstBean, char sndBean) {
	    char beanTaken = getBean(BeansBag, (fstBean == sndBean) ? 'B' : 'G');
	    
	    // Check if a bean was successfully taken
	    if (beanTaken != NULL) {
	        // Put the taken bean into the tin
	        putIn(tin, beanTaken);
	    } else {
	        // Pronounce no coffee ==beanType in BeansBag
	        System.out.println("No coffee beans available to add to the tin.");
	    }
	}

	private static char anyBean(char[] tin) {
		for (char bean : tin) {
			if (bean != REMOVED) {
				return bean;
			}
		}
		return NULL;
	}

	// put bean in tin (put in position where its index has been removed)
	private static void putIn(char[] tin, char bean) {
		for (int i = 0; i < tin.length; i++) {
			if (tin[i] == REMOVED) {
				tin[i] = bean;
				break;
			}
		}
	}

	// get bean that has same type(input) from other beanbag
	// check số beans còn lại trong beansBag => if = 0 => return Null
	// >< => random lấy 1 bean giống beanType trong beansBag rồi set thành removed
	// => mark đã lấy
	public static char getBean(char[] beansBag, char beanType) {
		int beansLeft = 0;
		for (char bean : beansBag)
			beansLeft += (bean == beanType) ? 1 : 0;
		if (beansLeft == 0)
			return NULL;

		int rndIndex;
		do
			rndIndex = randInt(beansBag.length);
		while (beansBag[rndIndex] != beanType);

		char bean = beansBag[rndIndex];
		beansBag[rndIndex] = REMOVED;
		return bean;
	}

	// *n => return from 0 -> n-1 (index in tin)
	public static int randInt(int n) {
		return (int) (Math.random() * n);
	}
}
