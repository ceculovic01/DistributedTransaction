# DistributedTransaction

In the context of a university's student services, let's consider a distributed transaction scenario.
The Student Service is responsible for adding a new student with their personal information, the Student Management Service handles student enrollment in the appropriate academic year and assigns relevant courses, while the Group Service assigns the student to the relevant student group.
This distributed transaction is implemented using the Two-Phase Commit protocol, where the Transaction Service acts as the coordinator.In the context of a university's student services, let's consider a distributed transaction scenario.
The Student Service is responsible for adding a new student with their personal information, the Student Management Service handles student enrollment in the appropriate academic year and assigns relevant courses, while the Group Service assigns the student to the relevant student group.
This distributed transaction is implemented using the Two-Phase Commit protocol, where the Transaction Service acts as the coordinator.

## Architecture

This application adopts a microservices architecture, where each service engages in synchronous communication with the coordinator to provide updates on their transaction status.
On the other hand, the transaction service, acting as a coordinator, communicates synchronously with participants to ascertain their readiness for the commit phase, resembling the prepare phase in 2PC.
It then leverages asynchronous communication through RabbitMQ to inform them whether to proceed with committing or rolling back their transactions, akin to the commit phase in 2PC.

![Architecture](https://github.com/ceculovic01/DistributedTransaction/assets/104586407/7182fd76-d7b3-41ee-b992-97ccb31c57dd)
