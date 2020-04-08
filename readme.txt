This program was created by Nicholas Chong on June 29th, 2019. It was built as part of an assignment for CS 349 at the University of Waterloo

This program is a 2D video game reflective of the original "breakout" game. 

In order to run the game, following these steps:
1) run the gradle build command
2) run the gradle run command (you can add 0, 2, or 4 command line arguments as explained below)
3) press the "Let's Get This Party Started" button
4) press the spacebar to begin the movement of the ball

If at anytime you would like to reset, simply click the ESC key

Command line arguments:
	- if given zero, default values will be used
	- if given 2
		1. repaint FPS
		2. ball speed (1, 2, or 3, 3 being the fastest) 
	- if given 4
		1. repaint FPS
		2. ball speed (1, 2, or 3, 3 being the fastest)
		3. number of rows (greater than 0, preferrebly less than 14)
		4. number of columns (greater than 0) 

The additional feature that I added was three lives instead of only the one. A users score carries over for the three lives. When the user loses all three lives, 
a screen is shown with their score and how many lives they had left (this value won't be zero is the user quits prematurely via ESC key).
Another additional feature I implemented was the ability to customize the number of blocks in the game by allowing the user to specify how many rows and columns
they would like. You can take advatage of this feature by adding two extra command line arguments (ontop of the already two given), the first being
the number of rows and the second being the number of columns. If you do choose to specify these parameters, they must be greater than 0. Moreover, if you want
to preserve a visually appealling game, I wouldn't add more than 13 rows.  

The structure of my program is MVC, the Engine being the model, and , of course, the View being the View. That being said, the Engine class ended up being the main controller for gameplay as everything goes through that. The program is set up and run from the Main class. 
