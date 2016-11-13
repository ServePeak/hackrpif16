# Please Pick A Place
## Inspiration
People can never seem to decide where to go, because of dietary restrictions, distance, or simply just don't want to eat a certain type of food. Our idea is to simplify choosing a place to eat by use of a voting system, where one can sort based 

## What it does
The user joins or creates a group with other people.
User gets a list of nearby restaurants based on geolocation, and can choose a top 3 to vote via the Borda voting system. The person who created the group can stop the vote and parse the data, returning the most popular restaurant nearby.

## How we built it
A flask backend with an android frontend were the essentials of this project. 
MySQL was used as the database to store register/login/groups.
TripAdvisor api was used to get nearby restaurant data.

## Challenges we ran into
The biggest challenge we faced was the communication of the frontend and backend. MySQL with normalization was something we had to learn while doing this project.

## What we learned
How to create an api service in flask-python.
How to create an android app that can communicate with a server.
MySQL normalization.

## What's next
Fully fleshing it out so we ourselves can use it
