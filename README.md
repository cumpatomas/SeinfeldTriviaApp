# Seinfun - Seinfeld Trivia App
Working on a Seinfeld fan trivia app to put in practice Flows, Retrofit, JSON, Room.
I also scrap the scripts from a website and take some random lines.

### Compose Animation intro 
A little animation to show the points earned so far by the user and a little quote from the show according to the points number.

https://github.com/cumpatomas/SeinfeldTriviaApp/assets/102058754/50c607e7-23fb-4692-a753-cf4229041393

### Audios Game
Guessing the faces.

Here I implemented MediaPlayer to play random short audios from the Firebase hosting site so the user have to guess the correct face, if he doesn't guess 1 point will be discount, on the contrary he'll have to guess the 12 faces to earn 10 points.
Once the character audio game is won, it will be marked as completed in the Recycler View and the user can't enter to the same character again. 

https://github.com/cumpatomas/SeinfeldTriviaApp/assets/102058754/bd99a8c3-9a2d-4f32-93f4-a2c9b226b0fe

### Script Screen
Can you guess the correct episode?

Here I scrap the script on the fly and take a random fragment to be guess by the user

https://github.com/cumpatomas/SeinfeldTriviaApp/assets/102058754/150d27d9-8bab-4c7e-8155-f6f17c014193

### Quiz Screen
Now implementing a Quiz Screen calling a JSON file to get questions and answers. Lottie Animation applied for the answer result.

https://github.com/cumpatomas/SeinfeldTriviaApp/assets/102058754/0e52fad4-7c0a-4d24-97f4-0fea4620ce36

### Lines Screen:
From the random script uscase I get 3 random lines from the 4 main charachters in an episode so the user have to drag and drop the line to it's author.
Here I applied Jetpack Compose for the drag and drop and a marquee Text View for the episode hint.

https://github.com/cumpatomas/SeinfeldTriviaApp/assets/102058754/b32cf47a-76b7-4131-bd27-0cb9d6fc0211

