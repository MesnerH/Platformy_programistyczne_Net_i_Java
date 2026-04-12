using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace api_exchange_rate
{
    public class Currency
    {
        [Key]
        public required string Code { get; set; }
        // relacja - jedna waluta ma wiele rekordow kursow
        public List<Rate> Rates { get; set; } = new List<Rate>();
    }

    public class Rate
    {
        public int Id { get; set; }
        public double Value { get; set; }
        public DateTime Date { get; set; }
        public required string CurrencyCode { get; set; }
    }
}