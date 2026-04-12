using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Linq;

namespace ConsoleApp1
{
    internal class FizzBuzz
    {
        int gorny;
        public FizzBuzz(int zakres_Gorny) 
        {
            gorny = zakres_Gorny;
        }   

        public void wyswietlanie()
        {
            for (int i = 1; i <= gorny; i++)
            {
                if (i % 3 == 0 && i % 5 == 0) Console.WriteLine("FizzBuzz");
                else if (i % 3 == 0) Console.WriteLine("Fizz");
                else if (i % 5 == 0) Console.WriteLine("Buzz");
                else Console.WriteLine(i);
            }
        }
    }
}
