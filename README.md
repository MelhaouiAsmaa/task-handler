# Task Handler - Serverless Task Management Backend

This project is a simple serverless backend for managing tasks using AWS Lambda (Java) and DynamoDB. It provides two Lambda functions: one to create tasks and one to retrieve tasks from a DynamoDB table.

## Features

- Create tasks with a unique UUID and store them in DynamoDB  
- Retrieve all tasks from DynamoDB  
- Uses AWS SDK v2 for DynamoDB operations  
- Built with Maven and packaged as a fat JAR  

## Prerequisites

- AWS Account  
- AWS CLI or access to AWS Management Console  
- Java 17 and Maven installed locally (for building the project)  

## Setup and Deployment Steps

### 1. Build the Project

Clone the repo and build the fat JAR to deploy to AWS Lambda:

git clone https://github.com/MelhaouiAsmaa/task-handler.git
cd task-handler
mvn clean package


This will generate a shaded JAR file under `target/lambda-tasks-1.0-SNAPSHOT.jar`.

### 2. Create the DynamoDB Table

1. Log in to the AWS Management Console.  
2. Navigate to **DynamoDB > Tables**.  
3. Click **Create table**.  
4. Enter `Tasks` as the **Table name**.  
5. For **Partition key**, enter `id` and select type **String**.  
6. Leave other settings as default and click **Create table**.  

### 3. Create Lambda Functions

#### CreateTaskHandler Lambda

1. Go to **AWS Lambda** in the console.  
2. Click **Create function**.  
3. Choose **Author from scratch**.  
4. Enter function name: `CreateTaskHandler`.  
5. Choose **Java 17** as runtime.  
6. Under **Permissions**, create or select an existing role with DynamoDB full access or at least permissions to put items into the `Tasks` table.  
7. Click **Create function**.  
8. On the function page, upload the `lambda-tasks-1.0-SNAPSHOT.jar` file under **Code > Upload from > .zip or jar file**.  
9. Set the **Handler** field to:  
   `com.example.CreateTaskHandler::handleRequest`  
10. Save.

#### GetTasksHandler Lambda

Repeat the above steps to create a second Lambda function:

- Function name: `GetTasksHandler`  
- Runtime: Java 17  
- Handler: `com.example.GetTasksHandler::handleRequest`  
- Upload the same fat JAR `lambda-tasks-1.0-SNAPSHOT.jar`  
- Use the same or appropriate IAM role with DynamoDB read permissions  

### 4. Configure API Gateway

To invoke the Lambdas over HTTP:

1. Go to **API Gateway** in the AWS Console.  
2. Click **Create API** and select **HTTP API**.  
3. Click **Build**.  
4. Under **Integrations**, add the `CreateTaskHandler` Lambda as a **POST** method (e.g., path `/tasks`).  
5. Add another integration with the `GetTasksHandler` Lambda as a **GET** method on the same path `/tasks`.  
6. Review and create the API.  
7. Note the **Invoke URL** shown after creation.

### 5. Test Your API

#### Create a Task (POST)

Use curl or Postman to send a POST request:

curl -X POST https://<your-api-id>.execute-api.<region>.amazonaws.com/tasks
-H "Content-Type: application/json"
-d '{"title": "Test Task", "description": "This is a test task"}'


**Response:**

Task created with ID: <uuid>

#### Retrieve Tasks (GET)

Send a GET request:

curl https://<your-api-id>.execute-api.<region>.amazonaws.com/tasks


**Response:** JSON array of tasks.

## Notes

- Make sure the Lambda execution role has necessary permissions for DynamoDB.  
- Update the API Gateway integration if you change function names or handlers.  
- This setup uses simple Scan operations; consider paging or query optimizations for large datasets.  

Happy Coding!

