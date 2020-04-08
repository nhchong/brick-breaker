# CS349 A2
Student: nhchong
Marker: Rebecca


Total: 90 / 100 (90.00%)

Code:
(CO: won’t compile, CR: crashes, FR: UI freezes/unresponsive, NS: not submitted)


Notes:   

## TECHNICAL REQUIREMENTS (30)

1. [5/5] Basic Requirements

2. [10/10] Handling Command-Line Parameters

3. [0/10] Window Behaviour

-2 The user should be able to resize the window, making it larger or smaller than the starting size.
-3 There should be a minimum and maximum size that is supported.
-5 When the window is resized, all of the graphical elements of the screen should be resized as well. It is acceptable to constrain the aspect ratio, or put borders to try and maintain the same aspect ratio.

4. [5/5] Controls.

## GAMEPLAY (40)

5. [5/5] The program opens with a splash screen, displaying the student's name and ID are displayed. Instructions for playing the game (key usage) are also displayed.

6. [10/10] There are at least 5 rows of coloured blocks arranged at the top of the screen, and a paddle at the bottom that can be moved left or right.

7. [5/5] When the game starts, the ball starts to move across the screen.

8. [5/5] The ball bounces when struck by the paddle, or when striking a brick, roughly conforming to the reflection law.

9. [5/5] If the ball hits a block, the block disappears, and the ball bounces.

10. [5/5] The game ends when the ball touches the bottom of the screen.

11. [5/5] There is a score system rewarding bounces of the ball or hits of the block. The score is displayed and updated in real-time when the ball hits the paddle (optionally, it can score for other factors, like hitting bricks).

## MODEL-VIEW-CONTROLLER (10)

12. [5/5] A model class exists which manages the animation timer and updates game elements.

13. [5/5] A view class exists that handles drawing the bricks, paddle and ball.

## ADDITIONAL FEATURE (10)

14. [10/10] An additional feature at the student's choice is described and implemented. The feature must be a significant enhacement.
+10 Extra lives 

## AESTHETICS (10)

15. [5/5] The interface design is aesthetically pleasing.
16. [5/5] The game is enjoyable to play (responsive paddle, ball speed that makes for exciting gameplay, no lag).
