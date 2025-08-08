package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateTaskHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DynamoDbClient dynamoDbClient = DynamoDbClient.create();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        try {
            Task task = objectMapper.readValue(event.getBody(), Task.class);
            task.setId(UUID.randomUUID().toString());

            Map<String, AttributeValue> item = new HashMap<>();
            item.put("id", AttributeValue.fromS(task.getId()));
            item.put("title", AttributeValue.fromS(task.getTitle()));
            item.put("description", AttributeValue.fromS(task.getDescription()));

            PutItemRequest request = PutItemRequest.builder()
                    .tableName("Tasks") // Make sure this table exists
                    .item(item)
                    .build();

            dynamoDbClient.putItem(request);

            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody("Task created with ID: " + task.getId());

        } catch (Exception e) {
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("Error: " + e.getMessage());
        }
    }
}
