# JSON Operator

## Overview
JSON Operator is a Spring Boot application to handle JSON dataset records. This application provides endpoints to store structured JSON data and perform operations such as sorting and grouping.

## Prerequisites
Before running the application, ensure you have the following installed:

- **Java 17** or later
- **Maven** (if using `mvn` instead of `./mvnw`)
- **Docker & Docker Compose** (for database setup)

## Installation and Setup
1. **Clone the repository:**
   ```sh
   git clone https://github.com/priyanshu29470/json_record_op.git
   cd json-operator
   ```

2. **Give execution permissions to scripts:**
   ```sh
   chmod +x run.sh test.sh
   ```

3. **Run the application:**
   ```sh
   ./run.sh
   ```
   This will start the database and run the application.

## Running Tests and Generating Code Coverage Report
To execute test cases and generate a JaCoCo coverage report:

```sh
./test.sh
```

After execution, the code coverage report can be found at:

```
target/site/jacoco/index.html
```


## API Endpoints
The API allows inserting and querying JSON records for a specified dataset.

### 1. Insert Record
- **Endpoint:** `POST /api/dataset/{datasetName}/record`
- **Description:** Inserts a record into the dataset.
- **Request Body:**
  ```json
    {
        "id": 1,
        "name": "John Doe",
        "age": 30,
        "department": "Engineering"
    }
  ```
- **Response Example:**
  ```json
    {
        "message": "Record added successfully",
        "dataset": "employee_dataset",
        "recordId": 1
    }
  ```

- **Postman Usage:**
  - Select **POST** method.
  - Enter URL: `http://localhost:8080/api/dataset/Engineering/record`
  - Set `Content-Type: application/json` in headers.
  - Provide the JSON body.

### 2. Query Dataset
- **Endpoint:** `GET /api/dataset/{datasetName}/query`
- **Description:** Retrieves records from a dataset, with optional sorting and grouping.
- **Query Parameters:**
  - `groupBy` (optional) - Attribute to group records by.
  - `sortBy` (optional) - Attribute to sort records by.
  - `order` (default: `asc`) - Sorting order (`asc` or `desc`).
  - **Note:** `groupBy` and `sortBy` cannot be used together; only one can be specified per request.

#### Query API with Group-By
- **Example Request:**
  ```sh
  GET /api/dataset/employees/query?groupBy=department
  ```
- **Response Example:**
  ```json
  {
    "groupedRecords": {
      "Engineering": [
        { "id": 1, "name": "John Doe", "age": 30, "department": "Engineering" },
        { "id": 2, "name": "Jane Smith", "age": 25, "department": "Engineering" }
      ],
      "Marketing": [
        { "id": 3, "name": "Alice Brown", "age": 28, "department": "Marketing" }
      ]
    }
  }
  ```

#### Query API with Sort-By
- **Example Request:**
  ```sh
  GET /api/dataset/employees/query?sortBy=age&order=asc
  ```
- **Response Example:**
  ```json
  {
    "sortedRecords": [
      { "id": 2, "name": "Jane Smith", "age": 25, "department": "Engineering" },
      { "id": 3, "name": "Alice Brown", "age": 28, "department": "Marketing" },
      { "id": 1, "name": "John Doe", "age": 30, "department": "Engineering" }
    ]
  }
  ```


