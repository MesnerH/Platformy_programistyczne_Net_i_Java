using Microsoft.VisualStudio.TestTools.UnitTesting;
using Knapsack;
using System.Collections.Generic;
using System.Linq;

namespace Unit_Tests
{
    [TestClass]
    public class KnapsackTests
    {
        // 1 Sprawdzenie, czy jeśli co najmniej jeden przedmiot spełnia ograniczenia, to zwrócono co najmniej jeden element.
        [TestMethod]
        public void Test_One_Item_Returned()
        {
            int number_of_items = 10;
            int seed = 1;
            int capacity = 100;
            Problem problem = new Problem(number_of_items, seed);

            Result result = problem.Solver(capacity);

            Assert.IsTrue(result.Id_Items.Count > 0);
        }

        // 2 Sprawdzenie, czy jeśli żaden przedmiot nie spełnia ograniczeń, to zwrócono puste rozwiązanie.
        [TestMethod]
        public void Test_No_Items_Returned()
        {
            int number_of_items = 10;
            int seed = 1;
            int capacity = 0;
            Problem problem = new Problem(number_of_items, seed);

            Result result = problem.Solver(capacity);

            Assert.AreEqual(0, result.Id_Items.Count);
        }

        // 3 Sprawdzenie poprawności wyniku dla konkretnej instancji.
        [TestMethod]
        public void Test_Specific_Instance()
        {
            Problem problem = new Problem(1, 235);

            Result result = problem.Solver(10);

            int Expected_Value = problem.Items[0].Value;
            Assert.AreEqual(Expected_Value, result.Total_Value);
        }

        // 4  Sprawdzenie, czy suma wag nie przekracza pojemności
        [TestMethod]
        public void Test_Total_Weight_Dont_Exceeds_Capacity()
        {
            int capacity = 20;
            Problem problem = new Problem(250, 532);

            Result result = problem.Solver(capacity);

            Assert.IsTrue(result.Total_Weight <= capacity);
        }

        // 5 Sprawdzenie, czy jeśli pojemność jest duża, to wszystkie przedmioty zostaną zabrane.
        [TestMethod]
        public void Test_All_Items_Taken_In_Knapsack()
        {
            int number_of_items = 5;
            int seed = 1;
            int capacity = 100;
            Problem problem = new Problem(number_of_items, seed);

            Result result = problem.Solver(capacity);

            Assert.AreEqual(number_of_items, result.Id_Items.Count);
        }
    }
}