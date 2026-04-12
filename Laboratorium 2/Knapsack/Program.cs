using Knapsack;


namespace Knapsack
{
    internal class Program
    {
        static void Main(string[] args)
        {
            // seed
            Console.WriteLine("Seed: ");
            int seed = int.Parse(Console.ReadLine());

            // number of items
            Console.WriteLine("How many items: ");
            int n = int.Parse(Console.ReadLine());

            Problem problem = new Problem(n, seed);
            Console.WriteLine(problem.ToString());

            // capacity
            Console.WriteLine("\nCapacity of Knapsack:");
            int capacity = int.Parse(Console.ReadLine());

            Result final_Result = problem.Solver(capacity);
            Console.WriteLine(final_Result.ToString());
        }
    }
}