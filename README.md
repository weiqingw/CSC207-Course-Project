# README

## SetUp

* Choose a new directory for this project
* git clone <https://markus.teach.cs.toronto.edu/git/csc207-2018-09-reg/group_0613> with terminal or git bash
* `Open an existing Android Studio project`from the directory you chose above (group_0613/Phase2/GameCentre) select `GameCentre`
* Build and run the project

## Meeting Notes

* Meeting notes located in group_0613/Note

## Games

1. Sliding Tiles
   * user can select and import their prefered images to be the background of the game, or he/she could play with default setting of the game. You can set up your own background as follows:
     * download and save a image to your device
     * click `new game` $\rightarrow$ click `with image` $\rightarrow$ click `import your image`$\rightarrow$ select your image from gallery (download one from browser or drag one from your computer if your gallery is empty)
     * select game difficulty (3x3, 4x4, 5x5)
     * Set undo limit (optional) By default, undo limit is 3, maximum is 20.
   * click `NEW GAME`
   * Score is calculated according to difficulty, time taken and steps taken to solve the game.

2. Sudoku
   * There are three level of difficulty for Sudoku (Easy, Normal, Hard). The higher the level is, the less cells are given to user.
   * click `new game` $\rightarrow$ select difficulty
   * Undo Button: undo each movement user makes
   * Clear Button: erase the entire board (Except for those cells given to user)
   * Hint Button: choose a cell and select hint, the solution for that single cell will be displayed
     * Easy mode has 1 hint
     * Normal mode has 5 hints
     * Hard mode has 3 hints
   * Erase Button: select a cell and click erase will erase that single cell
   * Score is calculated according to difficulty and time taken to solve the game.
   * After game is done and score is calculated, user can no longer play with the same board (board will be displayed if loaded, but disabled for playing)

3. Picture Match

4. * All cards are flipped over (hidden), when you click one of them, they will flip over and show the picture on it.

     Once two cards are selected, they will flip over again

     You have to remember the card you have selected and try to match the same picture. Once Two cards with the same picture are selected, a tick will be displayed at their position.

   * How to play: click `New Game` $\rightarrow$ `select difficulty`(4$\times$4, 6$\times$6, 8$\times$8) $\rightarrow$ `select theme`(number, animal, emoji) $\rightarrow$ `NEW GAME` 

   * Score is calculated according to time taken to solve the game.


## Game Centre

### Login and Signup

* Sign In: 

  * User `admin` is preloaded (username: admin, password: admin), user could sign in with this account or sign up another account. Note: `admin` user cannot change password

* Sign Up:

  * Click Sign Up on the login interface, enter `nickname`, `username` and `password` twice, then `REGISTER`

    Then click **back** button and sign in with the new `username` and `password` 

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
  	 In addition, for each game the user has played, only the highest score will be displayed, every time the 	 			user scores a new record, the database and scoreboard will be updated.
* **Scoreboard By Games**
  * This scoreboard locates in every game starting interface where user could start a new game.
  * Click the trophy on the top right corner of the interface to view the scoreboard.
  * Only users that has played this game and their score will be displayed with ranks on this scoreboard (no other games i.e. If you are in Sudoku game, then the scoreboard only displays the ranking list of this Sudoku game)

## Undo and Autosave

* Undo: When undo button is clicked, game will revert to previous condition (steps will keep adding in sliding tiles)

* Autosave: Games are autosaved when game is finished or quitted. 

  Autosave is not based on time, but status of game. (Game state is saved within ```onpause()``` method )


* Sliding Tiles has both Undo and Autosave functions
* Sudoku has both Undo and Autosave functions
* Picture Match has Autosave function




## Source

Souces are recorded in a file called ```source.md``` in Note folder. 

We never copy code from anywhere. We only learn the ideas and copied some pictures from the website.

## Note

In git log,

- ```Huakun``` and ```Huakun Shen``` are the same person
- ```Jato Xiang``` is actually ```Jiatao Xiang```
- ```Xu Wang``` and ```xu wang``` are the same person

## 