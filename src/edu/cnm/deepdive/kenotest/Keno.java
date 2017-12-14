package edu.cnm.deepdive.kenotest;

import java.io.BufferedReader;
import java.util.Random;
import java.util.Scanner;

public class Keno {

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    double playerMoney = 100; //Start the player off with some money
    int playerNums[] = new int[15];
    int computerNums[] = new int[20];
    int kenoSpot, kenoCatch;
    double bet;
    boolean continueGame = true;
    String userInput;
    // Don't really understand how to implement this?
    // BufferedReader input = null;

    System.out.println("Hello, and welcome to the game Keno!");
    System.out.println("This is a high stakes game. You can win and lose money quickly!");
    while (continueGame) {
      System.out.println("You currently have: $" + playerMoney);
      System.out.println("Let's get some numbers to begin.");
      System.out.println("You may enter up to 20 numbers");
      playerNums = getUserInput();
      bet = getBet(playerMoney);
      computerNums = getComputerNums();
      kenoSpot = getSpot(playerNums);
      kenoCatch = getCatch(playerNums, computerNums);
      System.out.println("Catch: " + (kenoCatch + 1));
      playerMoney += payout(kenoSpot, kenoCatch, bet);
      playerMoney -= bet;
      System.out.println("You now have: $" + playerMoney);
      if (playerMoney <= 0) {
        continueGame = false;
        System.out.println("Sorry, you ran out of money!");
        System.out.println("Better luck next time! :)");
      } else {
        System.out.println("Would you like to continue?");
        userInput = input.nextLine();

        if ((userInput.equals("y")) || userInput.equals("yes")) {
          continueGame = true;
        } else {
          continueGame = false;
        }
      }
    }
    System.out.println("Thanks for playing!");
    System.out.println("Overall, you now have: $" + playerMoney);
  }

	/*
		getUserInput function
		@return array userInput
		the array userInput is all of the numbers that the user entered
		this function initally sets all the numbers in the array to 0.
		It checks to make sure the user has not inputted the number before
		and also that the number entered is greater than 0 and less than 80
	*/

  public static int[] getUserInput() {
    Scanner input = new Scanner(System.in); // for input
    boolean invalidInput = true;
    boolean continuePlayer = true;
    int playerNums[] = new int[15];
    int index;
    int numberEntered;
    String enterString;
    for (index = 0; index < playerNums.length; index++) {
      playerNums[index] = 0;
    }
    index = 0;
    while (continuePlayer && index < 15) {
      do {
        do {
          System.out.println("Enter number " + (index + 1));
          numberEntered = input.nextInt();
          if ((numberEntered > 0) && (numberEntered < 81)) {
            if (isUnique(numberEntered, playerNums)) {
              invalidInput = false;
              playerNums[index] = numberEntered;
            } else {
              System.out.println("Sorry, you already entered that number before!");
              System.out.println("Try again!	");
              invalidInput = true;
            }
          } else {
            invalidInput = true;
            System.out
                .println("Sorry, the number you entered is either less than 0 or greater than 80");
            System.out.println("Try again");
            System.out.println("");
          }
        } while (invalidInput);

        if (index < 14) //makes sure program doesn't ask to continue when on the last number
        {
          System.out.println("Do you wish to continue? (yes/no)");
          input.nextLine();
          enterString = input.nextLine();
          if ((enterString.equals("n")) || (enterString.equals("no"))) {
            invalidInput = false;
            continuePlayer = false;
          } else if ((enterString.equals("y")) || (enterString.equals("yes"))) {
            invalidInput = false;
            continuePlayer = true;
          } else {
            System.out.println("Sorry, I didn't understand that.");
            System.out.println("");
            invalidInput = true;
          }

        }
      } while (invalidInput);

      index++;
    }
    return playerNums;
  }

	/*
		isUnique function
		@return boolean isUnique
		@params number and array
		Returns whether the number is unique to the array or not
		if it is, it will return true, if it is not then it will return false
		Used to ensure that there are no repeats in both playerNums and computerNums
	*/

  public static boolean isUnique(int number, int[] array) {
    int index;
    for (index = 0; index < array.length; index++) {
      if (number == array[index]) {
        return false;
      }
    }
    return true;
  }

	/*
		getComputerNums function
		@return array computerNums

		This function generates the numbers for the computer
		The numbers are generated randomly.
	*/

  public static int[] getComputerNums() {
    int[] computerNums = new int[20];
    Random rand = new Random();
    int index;
    int randomNumber = 0;

    for (index = 0; index < computerNums.length; index++) {
      randomNumber = rand.nextInt(80) + 1;
      if (isUnique(randomNumber, computerNums)) {
        computerNums[index] = randomNumber;
      } else {
        index--;
      }

    }
    return computerNums;
  }

	/*
		getSpot function
		@return spot
		@params playerArray
		The getSpot function returns the spot, which is
		the number of numbers the user initially picked
		it makes spot go up when it doesn't find a 0.
		A 0 shows that the user did not enter a number for that index
	*/

  public static int getSpot(int[] playerArray) {
    int index;
    int spot = 0;
    for (index = 0; index < playerArray.length; index++) {
      if (playerArray[index] != 0) {
        spot++;
      }

    }
    spot -= 1; //Subtract one so it works properly in array
    return spot;
  }

	/*
		getBet function
		@return bet
		@param playerMoney
		This function gets the bet from the user.
		It makes sure that the best is greater than or equal to 0
		It also makes sure that the user has enough money to bet
	*/

  public static double getBet(double playerMoney) {
    Scanner input = new Scanner(System.in);
    double bet = 0;
    boolean invalidInput = true;
    while (invalidInput) {
      System.out.print("Enter the bet amount in whole dollars: $");
      bet = input.nextInt();
      if (bet < 0) {
        System.out.println("Bet amount can't be less than 0!");
        invalidInput = true;
      } else if (bet > playerMoney) {
        System.out.println("You don't have enough money to bet that!");
        invalidInput = true;
      } else {
        invalidInput = false;
      }
    }
    return bet;
  }


	/*
		getCatch function
		@return catch
		@params arrays playerArray and computerArray
		This function returns the catch which is
		how many of the user's numbers matched the computer's.
	*/

  public static int getCatch(int[] playerArray, int[] computerArray) {
    int playerIndex;
    int computerIndex;
    int kenoCatch = 0;
    for (playerIndex = 0; playerIndex < playerArray.length; playerIndex++) {
      for (computerIndex = 0; computerIndex < computerArray.length; computerIndex++) {
        if (playerArray[playerIndex] == computerArray[computerIndex]) {
          kenoCatch++;
        }
      }
    }
    kenoCatch -= 1;//subtact one so it works in array
    return kenoCatch;
  }

	/*
		payout function
		@return payoutAmount
		@param spot and catch
		This function will return the total amount that should be multiplied
		by the bet amount. For the payout array, each line is a spot and
		each number within the array is the catch
	*/

  public static double payout(int kenoSpot, int kenoCatch, double betAmount) {
    double payoutAmount = 0;
    double multiplier;
    double payout[][] =
        {
            {3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //1
            {1, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},  //2
            {1, 2, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},  //3
            {0.5, 2, 6, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //4
            {0.5, 1, 3, 15, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//5
            {0.5, 1, 2, 3, 30, 75, 0, 0, 0, 0, 0, 0, 0, 0, 0},//6
            {0.5, 0.5, 1, 6, 12, 36, 100, 0, 0, 0, 0, 0, 0, 0, 0},//7
            {0.5, 0.5, 1, 3, 6, 19, 90, 720, 0, 0, 0, 0, 0, 0, 0},//8
            {0.5, 0.5, 1, 2, 4, 8, 20, 80, 1200, 0, 0, 0, 0, 0, 0},//9
            {0, 0.5, 1, 2, 3, 5, 10, 30, 600, 1800, 0, 0, 0, 0, 0},//10
            {0, 0.5, 1, 1, 2, 6, 15, 25, 180, 1000, 3000, 0, 0, 0, 0},//11
            {0, 0, 0.5, 1, 2, 4, 24, 72, 250, 500, 2000, 4000, 0, 0, 0},//12
            {0, 0, 0.5, 0.5, 3, 4, 5, 20, 80, 240, 500, 3000, 6000, 0, 0},//13
            {0, 0, 0.5, 0.5, 2, 3, 5, 12, 50, 150, 500, 1000, 2000, 7500, 0},//14
            {0, 0, 0.5, 0.5, 1, 2, 5, 15, 50, 150, 300, 600, 1200, 2500, 10000}//15
        };
    if (kenoCatch < 0) {
      multiplier = 0;
    } else {
      multiplier = payout[kenoSpot][kenoCatch];
      payoutAmount = multiplier * betAmount;
    }
    return payoutAmount;
  }
}