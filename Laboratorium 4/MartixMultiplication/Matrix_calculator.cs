using MatrixMultiplicationParallel;
using System;
using System.Threading.Tasks;

namespace MatrixMultiplicationParallel
{
    public class Matrix_calculator
    {
        public static Matrix Multiply(Matrix A, Matrix B, int max_threads)                  // metoda do mnozenia macierzy
        {
            if (A.Cols != B.Rows)
                throw new InvalidOperationException("Zly wymiar");                          // sprawdzenie zgodnosci rozmiarow macierzy

            Matrix result = new Matrix(A.Rows, B.Cols);

            ParallelOptions options = new ParallelOptions
            {
                MaxDegreeOfParallelism = max_threads                                        // ustawienie maksymalnej liczby watkow do wykorzystania
            };

            Parallel.For(0, A.Rows, options, i =>                                           // rownolegla petla do mnozenia macierzy, gdzie kazdy wiersz jest przetwarzany przez inny watek
            {
                for (int j = 0; j < B.Cols; j++)
                {
                    double sum = 0;
                    for (int k = 0; k < A.Cols; k++)
                    {
                        sum += A.Data[i, k] * B.Data[k, j];
                    }
                    result.Data[i, j] = sum;
                }
            });

            return result;
        }
        public static Matrix Multiply_threads(Matrix A, Matrix B, int thread_count)
        {
            if (A.Cols != B.Rows)
                throw new InvalidOperationException("Zly wymiar");                          // sprawdzenie zgodnosci rozmiarow macierzy
            Matrix result = new Matrix(A.Rows, B.Cols);
            Thread[] threads = new Thread[thread_count];

            // dzielimy wiersze macierzy A na liczbe dostepnych watkow
            int rows_per_thread = A.Rows / thread_count;

            for (int t = 0; t < thread_count; t++)
            {
                int start_row = t * rows_per_thread;                                        // poczatek zakresu wierszy dla danego watku
                int end_row = (t + 1) * rows_per_thread;                                    // koniec zakresu wierszy dla danego watku
                    
                if (t == thread_count - 1)                                                  // ostatni watek bierze pozostale wiersze
                {
                    end_row = A.Rows;
                }
    
                threads[t] = new Thread(() =>
                {
                    for (int i = start_row; i < end_row; i++)
                    {
                        for (int j = 0; j < B.Cols; j++)
                        {
                            double sum = 0;
                            for (int k = 0; k < A.Cols; k++)
                                sum += A.Data[i, k] * B.Data[k, j];
                            result.Data[i, j] = sum;
                        }
                    }
                });
                threads[t].Start();                                                         // uruchamiamy watek
            }

            // czekamy na zakonczenie wszystkich watkow
            foreach (var thread in threads)
                thread.Join();

            return result;
        }
    }
}