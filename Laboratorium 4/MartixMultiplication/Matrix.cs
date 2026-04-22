using System;

namespace MatrixMultiplicationParallel
{
    public class Matrix
    {
        public int Rows { get; }
        public int Cols { get; }
        public double[,] Data { get; }

        public Matrix(int rows, int cols)                                   // konstruktor
        {
            Rows = rows;
            Cols = cols;
            Data = new double[rows, cols];
        }

        public void Fill_random()                                           // generowanie losowych wartosci do macierzy
        {
            Random random = new Random();
            for (int i = 0; i < Rows; i++)
            {
                for (int j = 0; j < Cols; j++)
                {
                    Data[i, j] = Math.Round(random.NextDouble() * 10, 2);   // wartosci z zakresu 0-10
                }
            }
        }

        public void Print()                                                 // metoda pomocniczna do wyswietlania macierzy
        {
            for (int i = 0; i < Rows; i++)
            {
                for (int j = 0; j < Cols; j++)
                {
                    Console.Write($"{Data[i, j]:F2}\t");
                }
                Console.WriteLine();
            }
        }
    }
}