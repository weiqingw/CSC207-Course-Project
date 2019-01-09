# README

This is my course project for CSC207H1 F - Software Design course at the University of Toronto. With the instructors' permission,
I am sharing this project on my personal GitHub. 

Note: Running this program requires `Android Studio` which can be downloaded from <https://developer.android.com/studio/>. Before running this program, you need to do the following:
* Open Tools –> AVD Manager. Create a Virtual Device. Choose Pixel 2.
* On the System Image screen, choose Oreo with API Level 27. Click the Download link. This will download an Google APIs Intel x86 Atom System Image.
* Once the download finishes, select Oreo and then Next.
* On the Android Virtual Device (AVD) screen, click Finish, then close the next window.
* Open Tools –> SDK Manager. On the SDK Platforms tab, Select Android 8.1 (Oreo) and click OK.

Now to make sure the setup is done, make a new project.
* Select File –> New Project. Leave the defaults. Click Next.
* Under Phone and Tablet, select API 27: Android 81. (Oreo). Click Next.
* On the Add an Activity to Mobile screen, click Next.
* On the Configure Activity screen, click Finish.


This project can be tested on Android Virtual Machine through Android Studio, or on an Android device in Developer Mode.
## Set Up

* Choose a new directory for this project
* Clone the project from Github.
* `Open an existing Android Studio project`from the directory you chose above (/Phase2/GameCentre) select `GameCentre`
* Build and run the project with Android Virtual Machine.


## Games

1. Sliding Tiles
   * User can select and import their preferred images to be the background of the game, or he/she could play with default setting of the game. You can set up your own background as follows:
     * Download and save a image to your device (if your gallery is empty);
     * Click on `New Game` -> `With image` -> click `Import your image` -> select your image from gallery;
     * Select the level of difficulty (3x3, 4x4, 5x5);
     * (optional) Choose the undo limit. By default, undo limit is 3. However, you may not choose a limit greater than 20.
   * Click on `NEW GAME`.
   * Score is calculated according to level of difficulty you chose, time you used and steps you took to solve the game.

2. Sudoku
   * There are three level of difficulty for Sudoku (Easy, Normal, Hard). The higher the level is, the more cells you need to figure out yourself. 
   * Click on `New Game` -> choose the level of difficulty;
   * Undo Button: undo the last movement user made before reaching the current state;
   * Clear Button: erase the entire board (This action is __not__ undoable);
   * Hint Button: Click on a cell and click the hint button, the solution for that single cell will be displayed. It should be noted that 
     * Easy mode has 1 hint available to users, 
     * Normal mode has 5 hints available to users, and
     * Hard mode has 3 hints available.
   * Erase Button: Click on a cell and then click the erase button to erase that single cell.
   * Your score is calculated according to difficulty and the length of time you took to solve the game.
   * After your solve the Sudoku board and get your score, user can no longer play with the same board (the board will be displayed if loaded, but you may not play with it).

3. Picture Match
   * All cards are flipped over (hidden), when you click one of them, they will flip over and show the picture on it. 
     Once two cards are selected, they will flip over again. You have to remember the card you have selected and try to match the same picture. Once Two cards with the same picture are selected, a tick will be displayed at their position.

   * How to play: click `New Game` -> `select difficulty(4 X 4, 6 X 6, 8 X 8)` -> `select theme (number, animal, emoji)` -> `NEW GAME` 

   * Score is calculated according to time taken to solve the game. Level of difficulty is counted.


## Game Centre

### Login and Sign-up

* Sign In: 

  * User `admin` is preloaded (username: admin, password: admin), user could sign in with this account or sign up another account. Note: `admin` user cannot change password

* Sign Up:

  * Click `Sign Up` button on the login interface, enter the `nickname`, `username` and `password` you wish to use, then press `REGISTER`. After completion, click on the back button to sign in with the new `username` and `password`. 

    ​

### Game Centre Interface:

* This is the interface user gets to after signed in, where user could choose the game he/she would like to play

* There is a **setting** menu in this interface

  ```txt
  Swipe from the left edge of the screen to the right edge, then the setting menu will appear.
  ```

  There are three options in this menu

  - Change password

    Note: admin account cannot change password

  - Scoreboard

    This is one of the two scoreboard, which shows only current user's scores. Scoreboard will be explained in more details later


  - Avatar

    Click the avatar, then user could change its nickname and avatar by importing a image from the gallery

    Click apply to set changes

### Scoreboard

* There are two types of scoreboard, one is created by user, the other is created by games
* **Scoreboard By User**
  * This scoreboard locates in the setting menu in game centre interface.
  * Only games played by the current signed in user will be displayed on this scoreboard.
  	 In addition, for each game the user has played, only the highest score will be displayed, every time the user scores a new record, the database and scoreboard will be updated.
* **Scoreboard By Games**
  * This scoreboard locates in every game starting interface where user could start a new game.
  * Click the trophy on the top right corner of the interface to view the scoreboard.
  * Only users that has played this game and their score will be displayed with ranks on this scoreboard (no other games i.e. If you are in Sudoku game, then the scoreboard only displays the ranking list of this Sudoku game)

## Undo and Autosave

* Undo: When undo button is clicked, game will revert to previous condition (steps will keep adding in sliding tiles)
* Autosave: Games are autosaved when game is finished or quitted. Autosave is not based on time, but status of game. (Game state is saved within ```onpause()``` method )
* Sliding Tiles has both Undo and Autosave functions
* Sudoku has both Undo and Autosave functions
* Picture Match has Autosave function

## Acknowledgements
It should be noted that this project is a course group project completed collaboratively by X. Wang, Y. Xu, H. Shen, J. Xiang and myself, and contributions
made by everyone is an important part of the success.

## Feedback from the Course Instruction Team
Dear Weiqing,

This is the feedback for Phase 2. You were in group group_0613, and here are
the UTORids of your team members:

	she****k, wan****4, wan****2, xia****5, xuy****1

When you are working on a team, in a professional setting, good design is related to being a good team member. 

1. Is it easy for other team members to read and understand your code?

2. Is it easy for other people to extend your code and add new features? (Consider the Open/Closed Principle and Test Driven Design.)

3. Is it easy for other team members to write code that interacts with yours?

The list continues, and includes almost every topic from CSC207.

Your project makes good use of comments and javadoc, data structures (such as hashmaps to save your scores), SQL, nice and thorough tests, inheritance, interfaces, and the MVC, Observer, and Iterator design patterns. Your style of coding is consistent and your class and methods sizes are reasonable. Nice use of Markdown!

A user playing your games will see memory, sliding tiles, sudoku games in one app with a nice UI! Your project has a nicely designed user dashboard with the ability to change the avatar, reset the password, and (for the Memory game) change the theme. There are some good design decisions and improvements from Phase 1.

A good next step would be to make sure that you are using the Java naming conventions when coding in Java (e.g, popScore should start with a capital letter, packages should be lowercase_with_underscores). There are a couple of places where you might have refactored to clean up your code a bit, but this was not a major problem. For example, In class SQLDatabase, you could have refactored methods updateScore and addData to both call a helper method that manages the ContentValues.

As you continue to practice object-oriented programming, you will see that your past experience informs your decisions. It will be easier to make good design decisions before you code, rather than refactoring at every step. 

We hope that you enjoyed this course and that your experience with the project has been useful to you.

Here is your grade breakdown for the project. The "Design" mark is the letter
grade for your Phase 2 software design converted to a numeric mark, using the
upper end of the appropriate grade range. Here are a few examples: A has a
range of 85-89, so we would use 89%, resulting in a grade of 10.68/12; another
example is B-, which has a range of 70-72, so we would use 72%, or 8.64/12.
You can find the full scale here:

    http://www.artsci.utoronto.ca/newstudents/transition/academic/grading

If the Phase 2 "Total" percentage was higher than your Phase 1 mark, we
replaced your Phase 1 mark with your Phase 2 mark when calculating your
overall course grade.

   utor: wan****4 \
   sec: 0101 \
   testing: 2 / 2 \
   presentation: 3 / 3 \
   design: 12.0 / 12 \
   p2_total: 17.0 / 17 
