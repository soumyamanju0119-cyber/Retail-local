Retail Rewards Application
Overview
This Spring Boot application calculates retailer reward points for customer transactions over a rolling transaction dataset. It exposes REST endpoints that return monthly reward totals per customer and overall totals for the selected period.

The reward rules are:

2 points for every whole dollar spent above $100 in a transaction.
1 point for every whole dollar spent between $50 and $100 in a transaction.
Purchases at or below $50 earn 0 points.
Example: a $120 transaction earns 90 points (2 x 20 + 1 x 50).

Technology Stack
Java 17
Spring Boot 3
Spring Web
JUnit 5 / Spring Boot Test
Maven
Project Structure
src/main/java/com/retail/rewards
├── controller   # REST endpoint definitions
├── exception    # Custom exceptions and global exception handling
├── model        # API and domain records
├── repository   # Data access abstraction and in-memory demo dataset
├── service      # Reward calculation and aggregation logic
└── RetailApplication.java
Implementation Notes
The application does not hard-code month names. It derives the month dynamically from each transaction date using YearMonth.
A seeded in-memory repository demonstrates multiple customers with multiple transactions across three months.
Optional startDate and endDate query parameters allow the same endpoints to support full-range and filtered summaries.
The service determines the default date range from the minimum and maximum transaction dates in the dataset.
Validation and exception handling cover invalid date ranges, invalid request parameter formats, missing customers, and malformed transactions.
Demo Dataset
The seeded dataset includes three customers and twelve transactions spread across January, February, and March 2026:

C001 / Alice Johnson
C002 / Brian Smith
C003 / Carla Gomez
This dataset demonstrates:

transactions below $50,
transactions between $50 and $100,
transactions above $100,
multiple transactions per month,
multiple customers across multiple months.
REST Endpoints
Get rewards for all customers
GET /api/rewards
GET /api/rewards?startDate=2026-02-01&endDate=2026-03-31
Get rewards for a single customer
GET /api/rewards/{customerId}
GET /api/rewards/C002?startDate=2026-02-01&endDate=2026-03-31
Sample Response
{
  "startDate": "2026-01-08",
  "endDate": "2026-03-17",
  "customers": [
    {
      "customerId": "C001",
      "customerName": "Alice Johnson",
      "monthlyRewards": [
        { "month": "2026-01", "points": 94 },
        { "month": "2026-02", "points": 110 },
        { "month": "2026-03", "points": 39 }
      ],
      "totalPoints": 243
    }
  ]
}
Running the Application
mvn spring-boot:run
The API will start on http://localhost:8080.

Running Tests
mvn test
Quality Considerations
JavaDocs are included at class and method level for the primary production classes and tests.
target/ and bin/ are excluded through .gitignore.
Unit tests cover reward thresholds, dynamic monthly aggregation, date filtering, invalid repository data, invalid date ranges, and unknown customers.
Integration tests cover successful endpoint responses plus 400 and 404 error scenarios.
