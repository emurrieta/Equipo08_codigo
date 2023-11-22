abstract public class Data {

	void setValue (int val) {}
	void setValue (float val) {}
	void setValue (double val) {}
	void setValue (String val) {}

	void printValue() {}
}

class DataInt extends Data {
	int value;

	DataInt (int val) {
		this.value = val;
	}

	@Override
	void setValue (int val) {
		this.value = val;
	}

	@Override
	void printValue() {
		System.out.println("Valor = "+value);
	}

	int getValue() {
		return(value);
	}
}

class DataDouble extends Data {
	double value;

	DataDouble (double val) {
		this.value = val;
	}

	@Override
	void setValue (double val) {
		this.value = val;
	}

	@Override
	void printValue() {
		System.out.println("Valor = "+value);
	}
	
	double getValue() {
		return(value);
	}
}
