using System;
using System.Collections.Generic;
using System.Text;

namespace Knapsack
{
    internal class Result
    {
        // properties of class Result
        public List<int> Id_Items { get; set; }
        public int Total_Value { get; set; }
        public int Total_Weight { get; set; }

        // constructor
        public Result()
        {
            Id_Items = new List<int>();
            Total_Value = 0;
            Total_Weight = 0;
        }

        //methods
        public override string ToString()
        {
            string ids = string.Join(", ", Id_Items);

            return $"items: {ids}\n total value: {Total_Value}\n total weight: {Total_Weight}";
        }
    }
}