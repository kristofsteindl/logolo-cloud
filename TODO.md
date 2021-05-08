# logolo TODO
<h3>Exception handling</h3>
We should rethink the Exception handling. Maybe we can keep the concept, and we should take care not breking the FE

<h3>Task id</h3>
We should rework the whole task id and project mapping concept, or even the backlog concept. 
We should switch from projectKey:String to projectId:Long as a JPA managed ManyToOne. 
We should store the projectSequence as a Long instead of String.
We should rethink the Controller endpoint parameters and the validation as well.

<h3>Repository</h3>
Rewrite every repo call with an Optional<> type
User-Project connection should be ManyToMany
