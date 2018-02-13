A Demo Project for using WildFly, JPA and JSF together.
==========================================================

The task itself is nothing serious: there are some users, who can make requests,
and Admin users may confirm them or reject.
The goal is to experiment with putting together the components.

Database: MySQL, created and filled with test data from scripts. 

Authentication and authorization should be Database; the first one is working but I cannot authorize (probably my fault, not being an expert in administering WildFly); so the program checks itself whether the user has the necessary role in the tables.  

TODO
=========

* Test concurrent usage with multiple client
* Correct Database authorization
* Store hashed passwords (now they are plain)
* Add tests (unit, end to end)
* Add pagination
