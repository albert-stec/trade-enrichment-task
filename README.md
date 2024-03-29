See the [TASK](./TASK.md) file for instructions.

### Project Overview

This Spring Boot application is designed to efficiently handle and process large file uploads by utilizing Java's parallel streams. The service processes uploaded files in chunks, optimizing resource usage and ensuring the application remains responsive.

### Running the Application

## Using Maven

1. Clone the repository:
2. Navigate to the project directory.
3. Run `mvn clean install`
4. Run `mvn spring-boot:run` 

The application will start on port 8080 by default.


## Using IntelliJ IDEA
1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Run the `FileProcessingApplication` class.

The application will start on port 8080 by default.

## Using Docker
1. Clone the repository.
2. Navigate to the project directory.
3. Run `docker build --tag=enricher:latest .`
4. Run `docker run -p8080:8080 enricher:latest`

## Example Request

You can use curl to execute the API call, just adjust the atribute pathToYourFile accordingly:
'curl --location `http://localhost:8080/api/v1/enrich/trade --form 'file=pathToYourFile'`'

You can also use Postman. See video manual how to send this API call via Postman: https://youtu.be/UnLdw77fTcQ

The example file is located in the project directory: `src/test/resources/trade.csv` 


### Current State

The application provides a RESTful endpoint that allows for the uploading of files. Upon receiving a file, it processes the file in chunks in parallel, enhancing the processing speed and efficiency for large files. The processed data is then stored temporarily, and the response to the upload request includes information about the processing status.

### Architecture

### Pros and Cons

#### Pros

- **Efficiency**: Parallel processing of file chunks significantly reduces the processing time for large files.
- **Simplicity**: Utilizes Java's built-in parallel streams, avoiding external dependencies or complex asynchronous programming models.
- **Resource Optimization**: By processing the file in chunks, the application minimizes memory usage, making it capable of handling large files under constrained resources.

#### Cons

- **CPU Intensity**: Parallel streams can be CPU-intensive, potentially impacting the performance of other parts of the application if not carefully managed.
- **Error Handling Complexity**: Managing errors in parallel stream processing can be more complex, especially when ensuring all parts of a file are processed successfully.
- **Deterministic Processing Order**: While processing chunks in parallel, maintaining a deterministic processing order (if required) can introduce additional complexity.

### Future Improvements

- **Adaptive Chunk Sizing**: Implement logic to adjust chunk sizes dynamically based on file size and system performance metrics.
- **Enhanced Monitoring and Metrics**: Incorporate monitoring to measure and optimize the processing performance and resource utilization.
- **Improved Error Recovery**: Develop mechanisms to better handle errors during processing, including retries for failed chunks.
- **Security Measures**: Enhance security around file uploads, including file validation and secure storage of processed data.

It is worth noting that this implementation is a simplified version of a file processing service for interview task with specific constraints.
In a real world scenario, the application should utilize "real" asynchronous processing. The real world implementation could involve async processing with `CompletableFuture`, `ExecutorService`, or reactive programming with `Project Reactor` or `RxJava`, depending on the requirements and constraints of the application.
Moreover, the REST should consist two endpoints: one for uploading the file with instant response and another for checking the processing status or retrieving the processed data.
Also using REST for handling big loads of data is not the best choice. Kafka, RabbitMQ, or other messaging systems could be used for handling big loads of data. 