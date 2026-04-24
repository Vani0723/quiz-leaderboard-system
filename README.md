 # Quiz Leaderboard System

---

## Overview

This Java application implements a solution for the Quiz Leaderboard assignment. It interacts with an external API to collect quiz data, removes duplicate entries, aggregates participant scores, generates a sorted leaderboard, and submits the final result for validation.

---

## Problem Statement

The application is required to:

* Poll a validator API 10 times with a 5-second delay between each request
* Handle duplicate data using a composite key (roundId + participant)
* Aggregate scores for each participant
* Generate a leaderboard sorted in descending order of total scores
* Compute the total score across all participants
* Submit the leaderboard exactly once

---

## Solution Approach

### Polling

The application sends HTTP GET requests to retrieve quiz data:

```
GET /quiz/messages?regNo=XXXX&poll=0–9
```

---

### Deduplication

Duplicate entries are handled using a HashSet. Each record is uniquely identified using:

```
roundId + "_" + participant
```

This ensures that repeated API responses do not affect score calculations.

---

### Aggregation

A HashMap is used to store and update total scores per participant:

* Key: participant
* Value: total score

---

### Leaderboard Generation

The aggregated data is converted into a list and sorted in descending order based on total scores.

---

### Submission

The final leaderboard is sent using a POST request:

```
POST /quiz/submit
```

---

## Technologies Used

* Java 11 or higher
* Maven for build management
* Jackson for JSON parsing
* Java HttpClient for API communication

---

## How to Run

1. Ensure Java 11 or higher is installed
2. Clone the repository
3. Navigate to the project directory
4. Execute the following command:

```
mvn clean compile exec:java
```

---

## API Details

Base URL:

```
https://devapigw.vidalhealthtpa.com/srm-quiz-task
```

Endpoints:

```
GET  /quiz/messages?regNo=XXXX&poll=0–9  
POST /quiz/submit
```

---

## Sample Output

```
Leaderboard:
Alice : 100
Bob   : 120

Total Score: 220

Submission Response:
{
  "isCorrect": true,
  "submittedTotal": 220,
  "expectedTotal": 220,
  "message": "Correct!"
}
```

---

## Key Highlights

* Ensures accurate handling of duplicate API responses
* Maintains strict polling constraints with delay enforcement
* Produces a correctly sorted leaderboard
* Submits validated results in the required format

---

