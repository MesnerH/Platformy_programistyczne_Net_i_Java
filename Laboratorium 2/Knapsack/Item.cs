namespace Knapsack
{
    internal class Item
    {
        // properties of class Item
        public int ID { get; set; }
        public int Weight { get; set; }
        public int Value { get; set; }

        // constructor
        public Item(int id, int weight, int value)
        {
            ID = id;
            Weight = weight;
            Value = value;
        }

        // methods
        public override string ToString()
        {
            return $"ID: {ID}, Weight: {Weight}, Value: {Value}";
        }
    }
}