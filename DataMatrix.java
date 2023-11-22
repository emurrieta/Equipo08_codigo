public class DataMatrix {
	public static void main (String[] args) {
		Object[][] matrix;

		matrix = new Object[2][3];

		matrix[0][0] = Integer.valueOf(10);
		matrix[1][0] = Double.valueOf(3.14);
		matrix[0][1] = Double.valueOf(3.14);
		matrix[1][1] = Integer.valueOf(10);

		for(int i=0; i<2; i++)
			for (int j=0; j<2; j++){
				if (matrix[i][j] instanceof Integer) 
					matrix[i][j]=(int)matrix[i][j]/2;
				else if (matrix[i][j] instanceof Double) 
					matrix[i][j]=2*(double)matrix[i][j];
			}

		System.out.println(matrix[0][0]+"\t"+matrix[0][1]);
		System.out.println(matrix[1][0]+"\t"+matrix[1][1]);

	}
}
