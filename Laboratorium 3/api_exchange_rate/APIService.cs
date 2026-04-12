using System;
using System.Linq;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;

namespace api_exchange_rate
{
    public class ApiService
    {
        private static readonly HttpClient client = new HttpClient();

        public async Task Get_data(string code)
        {
            using (var db = new CurrencyContext())
            {
                code = code.ToUpper();

                // szukanie kursu w bazie danych
                var existing_rate = db.Rates.FirstOrDefault(r => r.CurrencyCode == code && r.Date.Date == DateTime.Today);

                if (existing_rate != null)
                {
                    Console.WriteLine($"[BAZA] Kurs {code} z dnia {existing_rate.Date:d}: {existing_rate.Value}");
                    return;
                }
                // pobieranie danych z sieci
                Console.WriteLine("[API] Pobieranie danych z sieci...");
                string url = $"https://openexchangerates.org/api/latest.json?app_id=399e59ea86b14ea296174672d61a4275";

            {
                string response = await client.GetStringAsync(url);
                var apiData = JsonSerializer.Deserialize<ExchangeResponse>(response);

                if (apiData?.Rates != null && apiData.Rates.ContainsKey(code))
                {
                    double val = apiData.Rates[code];

                    var currency = db.Currencies.Find(code);
                    if (currency == null)
                    {
                        currency = new Currency { Code = code };
                        db.Currencies.Add(currency);
                    }

                    db.Rates.Add(new Rate
                    {
                        Value = val,
                        Date = DateTime.Now,
                        CurrencyCode = code
                    });
                        db.SaveChanges();
                        Console.WriteLine($"[API] Nowy kurs {code}: {val} (Zapisano do bazy)");
                    }
                }
            }
        }
    }
}