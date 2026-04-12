using Microsoft.EntityFrameworkCore;

namespace api_exchange_rate
{
    internal class CurrencyContext : DbContext
    {
        public DbSet<Currency> Currencies { get; set; }
        public DbSet<Rate> Rates { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder options)
        {
            options.UseSqlite("Data Source=C:\\Users\\huber\\Desktop\\Platformy_programistyczne_Net_i_Java-master\\Platformy_programistyczne_Net_i_Java-master\\Laboratorium 3\\api_exchange_rate\\ExchangeRate.db");
        }
    }
}