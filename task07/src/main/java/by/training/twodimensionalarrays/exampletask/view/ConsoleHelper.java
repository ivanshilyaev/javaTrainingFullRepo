package by.training.twodimensionalarrays.exampletask.view;

import by.training.twodimensionalarrays.exampletask.bean.Matrix;
import by.training.twodimensionalarrays.exampletask.bean.exception.MatrixException;
import by.training.twodimensionalarrays.exampletask.parser.Parser;
import by.training.twodimensionalarrays.exampletask.parser.exception.ParserException;
import by.training.twodimensionalarrays.exampletask.view.exception.ViewException;
import by.training.twodimensionalarrays.exampletask.validation.Validator;

import java.util.Scanner;

public class ConsoleHelper {
    private static final ConsoleHelper INSTANCE = new ConsoleHelper();

    private ConsoleHelper() {
    }

    public static ConsoleHelper getInstance() {
        return INSTANCE;
    }

    Scanner scanner = new Scanner(System.in);
    Validator validator = new Validator();

    public void printMatrix(Matrix matrix) {
        System.out.println(matrix);
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public int readInt() {
        System.out.println("Enter int:");
        int i = 0;
        try {
            i = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("An error occurred while reading int. Try again:");
            readInt();
        }
        return i;
    }

    public int readEvenInt() {
        System.out.println("Enter even int:");
        int i = readInt();
        while (i % 2 == 1) {
            System.out.println("Int must be even. Try again:");
            i = readInt();
        }
        return i;
    }

    public int readIndex(int matrixSize) {
        int index = readInt();
        while (!validator.checkIndex(index, matrixSize)) {
            System.out.println("Invalid index. Try again:");
            index = readInt();
        }
        return index;
    }

    public String readString() {
        return scanner.nextLine();
    }

    public Matrix readMatrix(int m, int n) throws ViewException, MatrixException {
        Matrix matrix = new Matrix(m, n);
        Parser parser = new Parser();
        for (int i = 0; i < m; ++i) {
            int[] tmp;
            try {
                tmp = parser.parse(readString(), n);
            } catch (ParserException e) {
                throw new ViewException("Couldn't read matrix");
            }
            for (int j = 0; j < n; ++j) {
                matrix.setElement(i, j, tmp[j]);
            }
        }
        return matrix;
    }
}
