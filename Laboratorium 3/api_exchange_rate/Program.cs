using System;
using System.Threading.Tasks;

namespace api_exchange_rate
{
    internal class Program
    {
        static async Task Main(string[] args)
        {
            ApiService service = new ApiService();

            Console.WriteLine("--- System Pobierania Kursów Walut ---");
            Console.Write("Podaj kod waluty (np. PLN, EUR, GBP): ");

            string input = Console.ReadLine();

            if (!string.IsNullOrWhiteSpace(input))
            {
                await service.Get_data(input);
            }
            else
            {
                await service.Get_data("PLN");
            }
        }
    }
}