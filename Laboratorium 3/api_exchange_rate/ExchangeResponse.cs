using System.Text.Json.Serialization;
using System.Collections.Generic;

namespace api_exchange_rate
{
    public class ExchangeResponse
    {
        [JsonPropertyName("base")]
        public required string Base { get; set; }

        [JsonPropertyName("rates")]
        public required Dictionary<string, double> Rates { get; set; }
    }
}