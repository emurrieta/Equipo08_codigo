public class Main {
	public static void main (String[] args) {
		Data data[];

		data = new Data[2];

		data[0] = new DataInt(10);
		data[1] = new DataDouble(3.14);

		data[0].printValue();
		data[1].printValue();

	}
}
