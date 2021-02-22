# Gauntlet ORM
Gauntlet ORM is a very simplistic ORM designed by myself (Alex Googe). I was inspired by Hibernate's syntax and attempted to create my own version, albiet with much less functionality, of an ORM. This ORM can implement simple CRUD methods (Inserting, Select all, Updating, and Deleting).

## Usage
To use Gauntlet, you must go through a few steps:

1. Annotate the POJO classes using the annotations defined in Gauntlet.
2. Configure and Build a SessionFactory (which will hold the annotated POJO class models).
3. Use the SessionFactory to create Sessions. 
4. Use the Session class methods to use simplistic CRUD methods on the POJO.

## Annotations

1. @Entity: Tells Gauntlet that the class should be correctly annotated.
2. @Table: Tells Gauntlet that the POJO corresponds to a specific table.
3. @Id: Denotes that the field / column is the serial primary key in the table.
4. @Column: Denotes the field is a column in the table and allows users to provide the name.

## How to Configure a SessionFactory

The Configuration Class has 3 main items to know about. 
1. The User must past a String path to the Configuration class constructor to a properties file with a "url", "admin-usr", and "admin-pw" line to have the connections configured.
2. The .addAnnotatedClass method is a chainable method that annotates the class passed into it.
3. The .buildSessionFactory() method should be after the .addAnnotatedClass methods.

Example: 
SessionFactory factory = new Configuration("src/main/resources/my.properties")
                            .addAnnotatedClass(myAnnotatedClass.class)
                            .buildSessionFactory();

## Creating Sessions and doing CRUD Operations

After creating a SessionFactory, the User should then only use that SessionFactory to create Sessions using the .openSession() method. 

Example: Session session = factory.openSession();

Then with this session, the User can use CRUD methods on any annotated class.

Example: int newClassId = session.save(myAnnotatedObject);

## Improvements to Include

1. Connection Pooling
2. Log4J
3. JUnit / Mockito

