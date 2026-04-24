# Quiz Leaderboard System

This Java application solves the Quiz Leaderboard assignment by polling an external API, deduplicating data, aggregating scores, and submitting the leaderboard.

## Problem Overview

The task involves building an application that:
- Polls a validator API 10 times with 5-second delays
- Handles duplicate data using (roundId + participant) as key
- Aggregates scores per participant
- Generates a sorted leaderboard
- Computes total score
- Submits the leaderboard once

## Solution Approach

1. **Polling**: Uses Java HttpClient to make GET requests to `/quiz/messages` with poll=0 to 9
2. **Deduplication**: Maintains a Set of seen keys (roundId_participant)
3. **Aggregation**: Uses a Map to accumulate scores per participant
4. **Leaderboard**: Sorts participants by total score descending
5. **Submission**: POSTs the leaderboard to `/quiz/submit`

## Technologies Used

- Java 11+
- Maven for build management
- Jackson for JSON processing
- Java HttpClient for API calls

## How to Run

1. Ensure Java 11+ is installed
2. Clone the repository
3. Navigate to the project directory
4. Run `mvn clean compile exec:java`

## API Details

- Base URL: https://devapigw.vidalhealthtpa.com/srm-quiz-task
- GET /quiz/messages?regNo=2024CS101&poll={0-9}
- POST /quiz/submit with JSON body containing regNo and leaderboard

## Output

The application prints the submit response, which includes correctness validation.

## Sample Output

Leaderboard:
Alice: 100
Bob: 120

Total Score: 220

Submission Response:
{
  "isCorrect": true,
  "message": "Correct!"
}