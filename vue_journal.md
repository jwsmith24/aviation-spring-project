# Frontend Jeopardy Application with Vue and The Open Trivia Database API

Created a Vue application that implements a multiplayer Jeopardy game. Given a list of 
requirements and the Trivia API documentation, designed the game logic, scoring, and a responsive 
game board which scales for 2-6 players and 2-6 question categories. 

The biggest challenge faced working with this API was that it limits requests to once per 5 
seconds. This had two major implications: 

1) Fetching all questions upfront wasn't practical
2) Fetching questions during the game needed to respect the same limit so that multiple players 
   couldn't spam requests.

For the solution, I went with a recursive retry/backoff pattern. If the API returned a 429 
error, the function would wait and then retry with an even larger delay if another 429 is 
returned. Based on user testing (me), I tuned the delay and backoff scaling values so that 
there's a good balance between not spamming the API and the app still feeling responsive.

In addition to working around API limitations, key features include:

- Turn-based gameplay with automatic player cycling
- Scoring and visual feedback: correct answers turn the board cell green, incorrect ones turn it 
  red, player balances update in real time.
- Double Jeopardy! - 10% chance for a double jeopardy to trigger allowing the player to make a 
  wager (validated input to make sure the wager meets the game rules)

Got lots of practice with using async requests and handling assoicated errors, processing data 
from an external API (leveraging JavaScript array functions for filtering/mapping), and managing 
state across multiple interactive components.

Github: https://github.com/jwsmith24/ser-421/tree/main/lab4/activity1

