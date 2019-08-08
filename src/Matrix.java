public class Matrix {
    private String input;
    private Character[][] matrix;

    public Matrix(String input) {
        this.input = input;
        this.matrix = new Character[4][4];
        stringToMatrix();
    }

    private void stringToMatrix() {
        int row = 0;
        while (row < 4) {
            for (int i = 0; i < 4; i++) {
                Character first = input.charAt(i);
                matrix[row][i] = first;
            }
            row++;
        }
    }

    public String getChar(int i, int j) {
        return matrix[i][j].toString();
    }

}