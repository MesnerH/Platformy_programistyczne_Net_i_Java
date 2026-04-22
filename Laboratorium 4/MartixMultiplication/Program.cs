using System;
using System.Diagnostics;

namespace MatrixMultiplicationParallel
{
    internal class Program
    {
        static void Main(string[] args)
        {
            int size = 400;                                                            // rozmiar macierzy
            int iterations = 5;                                                        // liczba iteracji
            int[] thread_settings = { 1, 2, 4, 8, 16 };                                // liczba watkow
            double time1 = 0;                                                          // czas dla 1 watku

            Console.WriteLine($"Rozmiar macierzy: {size}x{size}");
            Console.WriteLine($"Rdzeni: {Environment.ProcessorCount}");                // informacja o liczbie rdzeni procesora
            Console.WriteLine("| Watki | Sredni Czas Parallel [ms] | Sredni Czas Threads [ms] | Przyspieszenie Parallel | Przyspieszenie Threads | ");
            Console.WriteLine("|_______|___________________________|__________________________|_________________________|________________________|");


            foreach (int threads in thread_settings)
            {
                double total_time_parallel = 0;
                double total_time_threads = 0;

                for (int i = 0; i < iterations; i++)
                {
                    Matrix A = new Matrix(size, size);
                    Matrix B = new Matrix(size, size);
                    A.Fill_random();
                    B.Fill_random();

                    // pomiar czasu dla wersji z parallel
                    Stopwatch sw = Stopwatch.StartNew();                                // start pomiaru czasu
                    Matrix result = Matrix_calculator.Multiply(A, B, threads);          // mnozenie macierzy
                    sw.Stop();                                                          // koniec pomiaru czasu

                    // pomiar czasu dla wersji z watkami
                    Stopwatch sw2 = Stopwatch.StartNew();
                    Matrix_calculator.Multiply_threads(A, B, threads);
                    sw2.Stop();
                    total_time_threads += sw2.Elapsed.TotalMilliseconds;
                    total_time_parallel += sw.Elapsed.TotalMilliseconds;

                    // result.Print();
                }

                double avg_parallel = total_time_parallel / iterations;                 // sredni czas dla wersji z parallel
                double avg_threads = total_time_threads / iterations;                   // sredni czas dla wersji z watkami
                if (threads == 1) time1 = avg_parallel;                                 // zapisanie czasu dla 1 watku do porownania
                double speedup_parallel = time1 / avg_parallel;                         // obliczenie przyspieszenia dla wersji z parallel
                double speedup_threads = time1 / avg_threads;                           // obliczenie przyspieszenia dla wersji z watkami

                Console.WriteLine($"| {threads,5} | {avg_parallel,25:F2} | {avg_threads,24:F2} | {speedup_parallel,23:F2} | {speedup_threads,22:F2} | ");
            }
        }
    }
}