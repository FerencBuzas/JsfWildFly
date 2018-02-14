A Demo Project for using WildFly, MySQL, JPA and JSF together.
================================================================

The task itself is nothing serious: there are some users, who can make requests,
and Admin users may confirm them or reject.
The goal is to experiment with putting together the components.

As a starting point, I used 

Database: MySQL, created and filled with test data from scripts. 

Authentication and authorization should be Database; the first one is working but I cannot authorize (probably my fault, not being an expert in administering WildFly); so the program checks itself whether the user has the necessary role in the tables.  

TODO
=========

* The installer script is not quite complete
* Test concurrent usage with multiple clients
* Correct Database authorization
* Store hashed passwords (now they are plain)
* Add tests (unit, end to end)
* Add pagination
