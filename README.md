# Quiz Leaderboard System

📌 Overview

This Java application solves the Quiz Leaderboard assignment by polling an external API, removing duplicate data, aggregating scores, and submitting a leaderboard.

🧩 Problem Statement

The application must:

- Poll a validator API 10 times with a 5-second delay
- Handle duplicate data using `(roundId + participant)`
- Aggregate scores per participant
- Generate a leaderboard sorted by total score
- Compute total score across all participants
- Submit the leaderboard only once

⚙️ Solution Approach

1. **Polling**
   - Calls API: `GET /quiz/messages?regNo=XXXX&poll=0–9`
2. **Deduplication**
   - Uses a `HashSet` to track:
     - `roundId + "_" + participant`
   - Ensures duplicate entries are ignored
3. **Aggregation**
   - Uses a `HashMap<String, Integer>`
   - Adds scores per participant
4. **Leaderboard**
   - Converts `Map` → `List`
   - Sorts in descending order
5. **Submission**
   - Sends final result: `POST /quiz/submit`

🛠️ Technologies Used

- Java 11+
- Maven
- Jackson (JSON parsing)
- Java HttpClient (API calls)

▶️ How to Run

```bash
mvn clean compile exec:java
```

🌐 API Details

Base URL:
https://devapigw.vidalhealthtpa.com/srm-quiz-task

Endpoints:

- `GET /quiz/messages?regNo=XXXX&poll=0–9`
- `POST /quiz/submit`

📊 Sample Output

Leaderboard:

```
Alice : 100
Bob   : 120
```

Total Score: 220

Submission Response:

```json
{
  "isCorrect": true,
  "submittedTotal": 220,
  "expectedTotal": 220,
  "message": "Correct!"
}
```

✅ Key Highlights

- Handles duplicate API responses correctly
- Ensures idempotent processing
- Maintains required delay between API calls
- Produces accurate leaderboard and total score
