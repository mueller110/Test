public class List {
	Element first;
	Element last;
	int size = 0;
	double frequency;

	public void add(String p) {
		Element c = new Element(p);
		if (first == null) {
			first = c;
			last = c;
		} else {
			last.next = c;
			last = c;
		}
		size++;
	}

	public boolean isEmpty() {
		if (first == null)
			return true;
		return false;
	}

	public void print() {
		Element c = first;
		while (c != null) {
			System.out.print(c.name + " ");
			c = c.next;
		}
	}

	public class Element {
		String name;
		Element next;

		Element(String name) {
			this.name = name;
		}
	}
}