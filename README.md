**Task Handler - Serverless Task Management Backend**
This project is a simple serverless backend for managing tasks using AWS Lambda (Java) and DynamoDB. It provides two Lambda functions: one to create tasks and one to retrieve tasks from a DynamoDB table.

**Features**
Create tasks with a unique UUID and store them in DynamoDB
Retrieve all tasks from DynamoDB
Uses AWS SDK v2 for DynamoDB operations
Built with Maven and packaged as a fat JAR

**Prerequisites**
AWS Account
AWS CLI or access to AWS Management Console
Java 17 and Maven installed locally (for building the project)

**Setup and Deployment Steps**
**1. Build the Project**
Clone the repo and build the fat JAR to deploy to AWS Lambda:
git clone https://github.com/MelhaouiAsmaa/task-handler.git
cd task-handler
mvn clean package
This will generate a shaded JAR file under target/lambda-tasks-1.0-SNAPSHOT.jar.

**2. Create the DynamoDB Table**
Log in to the AWS Management Console.
Navigate to DynamoDB > Tables.
Click Create table.
Enter Tasks as the Table name.
For Partition key, enter id and select type String.
Leave other settings as default and click Create table.

**3. Create Lambda Functions**
   
**CreateTaskHandler Lambda**
Go to AWS Lambda in the console.
Click Create function.
Choose Author from scratch.
Enter function name: CreateTaskHandler.
Choose Java 17 as runtime.
Under Permissions, create or select an existing role with DynamoDB full access or at least permissions to put items into the Tasks table.
Click Create function.
On the function page, upload the lambda-tasks-1.0-SNAPSHOT.jar file under Code > Upload from > .zip or jar file.
Set the Handler field to:
com.example.CreateTaskHandler::handleRequest
Save.

**GetTasksHandler Lambda**
Repeat the above steps to create a second Lambda function:
Function name: GetTasksHandler
Runtime: Java 17
Handler: com.example.GetTasksHandler::handleRequest
Upload the same fat JAR lambda-tasks-1.0-SNAPSHOT.jar
Use the same or appropriate IAM role with DynamoDB read permissions

**4. Configure API Gateway**
To invoke the Lambdas over HTTP:
Go to API Gateway in the AWS Console.
Click Create API and select HTTP API.
Click Build.
Under Integrations, add the CreateTaskHandler Lambda as a POST method (e.g., path /tasks).
Add another integration with the GetTasksHandler Lambda as a GET method on the same path /tasks.
Review and create the API.
Note the Invoke URL shown after creation.

**5. Test Your API**
**Create a Task (POST)**
Use curl or Postman to send a POST request:
curl -X POST https://<your-api-id>.execute-api.<region>.amazonaws.com/tasks \
-H "Content-Type: application/json" \
-d '{"title": "Test Task", "description": "This is a test task"}'
**Response:**
Task created with ID: <uuid>
Retrieve Tasks (GET)
Send a GET request:
curl https://<your-api-id>.execute-api.<region>.amazonaws.com/tasks
Response: JSON array of tasks.

**Notes**
Make sure the Lambda execution role has necessary permissions for DynamoDB.
Update the API Gateway integration if you change function names or handlers.
This setup uses simple Scan operations; consider paging or query optimizations for large datasets.

Happy Coding !
