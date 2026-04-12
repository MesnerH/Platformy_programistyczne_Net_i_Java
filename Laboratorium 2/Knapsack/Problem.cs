using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;
using System.Runtime.CompilerServices;


[assembly: InternalsVisibleTo("Unit_Tests")]
namespace Knapsack
{
    internal class Problem
    {
        // properties of class Problem
        public int Number_of_items { get; set; }
        public List<Item> Items { get; set; }

        // constructor
        public Problem(int number_of_items, int seed)
        {
            Number_of_items = number_of_items;
            Items = new List<Item>(number_of_items);
            Random random = new Random(seed);

            for (int i = 0; i < number_of_items; i++)
            {
                int weight = random.Next(1, 11);
                int value = random.Next(1, 11);
                Items.Add(new Item(i, weight, value));
            }
        }

        // methods

        public Result Solver(int capacity)
        {
            Result result = new Result();

            // sortiing items in descending order (based on value to weight)
            var sorted_Items = Items.OrderByDescending(x => (double)x.Value / x.Weight).ToList();

            foreach (var item in sorted_Items)
            {
                if (result.Total_Weight + item.Weight <= capacity)
                {
                    result.Id_Items.Add(item.ID);
                    result.Total_Weight += item.Weight;
                    result.Total_Value += item.Value;
                }
            }
            return result;
        }
        public override string ToString()
        {
            string result = "All items:\n";

            foreach (Item item in Items)
            {
                result += item.ToString() + "\n";
            }

            return result;
        }

    }
}