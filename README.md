# Projet d'application d'entreprises | Groupe 1

## Endpoints

### 1) Auth
| URI                 | Méthode HTTP | Auths? | Opération                                                                |
|---------------------|--------------|--------|--------------------------------------------------------------------------|
| **/auths/login**    | POST         | No     | READ ONE : Logs a user in and returns the token with the user.           |
| **/auths/register** | POST         | No     | CREATE ONE : Registers a user and returns the token with the user.       |
| **/auths/user**     | GET          | JWT    | READ ONE : Returns the user associated with the token stored in browser. | 

### 2) Users
| URI                               | Méthode HTTP | Auths? | Opération                                                                             |
|-----------------------------------|--------------|--------|---------------------------------------------------------------------------------------|
| **/users**                        | GET          | JWT    | READ ALL : Returns all users.                                                         |
| **/users/{idUser}/promote**       | PATCH        | JWT    | PATCH ONE : Update the user role to helper or manager.                                |  
| **/users/{idUser}/objects**       | GET          | JWT    | READ ALL : Returns all the objects of a given user.                                   |
| **/users/{idUser}**               | GET          | JWT    | READ ONE : Returns the user associated with the given id.                             |
| **/users/{idUser}/notifications** | GET          | JWT    | READ ALL : Returns a list of all the user notifications associated with the given id. |
| **/users/{idUser}/edit**          | PATCH        | JWT    | PATCH ONE : Modifies the user associated with the given id.                           |

### 3) Objects
| URI                        | Méthode HTTP | Auths? | Opération                                                                              |
|----------------------------|--------------|--------|----------------------------------------------------------------------------------------|
| **/objects**               | GET          | JWT    | READ ALL : Returns all the objects.                                                    |
| **/objects/home**          | GET          | No     | READ ALL : Returns all the home page objects.                                          |
| **/objects/{id}**          | GET          | No     | READ ONE : Returns the object associated with the given id.                            |
| **/objects/proposed**      | GET          | JWT    | READ ALL : Returns all the objects with the "PROPOSED" status.                         |
| **/objects**               | POST         | No     | CREATE ONE : Creates an object with the "PROPOSED" status.                             |
| **/objects/{id}/edit**     | PATCH        | JWT    | PATCH ONE : Modifies the object associated with the given id.                          |
| **/objects/{id}/accept**   | PATCH        | JWT    | PATCH ONE : Modifies the object associated with the given id to the "ACCEPTED" status. |
| **/objects/{id}/refuse**   | PATCH        | JWT    | PATCH ONE : Modifies the object associated with the given id to the "REFUSED" status.  |
| **/objects/{id}/workshop** | PATCH        | JWT    | PATCH ONE : Modifies the object associated with the given id to the "WORKSHOP" status. |
| **/objects/{id}/shop**     | PATCH        | JWT    | PATCH ONE : Modifies the object associated with the given id to the "SHOP" status.     |
| **/objects/{id}/onsale**   | PATCH        | JWT    | PATCH ONE : Modifies the object associated with the given id to the "ON_SALE" status.  |
| **/objects/{id}/sell**     | PATCH        | JWT    | PATCH ONE : Modifies the object associated with the given id to the "SOLD" status.     |
| **/objects/{id}/remove**   | PATCH        | JWT    | PATCH ONE : Modifies the object associated with the given id to the "REMOVED" status.  |

### 4) Object Types
| URI                          | Méthode HTTP | Auths? | Opération                                     |
|------------------------------|--------------|--------|-----------------------------------------------|
| **/objectTypes**             | GET          | No     | READ ALL : Returns all the object types.      |


### 5) Dashboard
| URI                          | Méthode HTTP | Auths? | Opération                                     |
|------------------------------|--------------|--------|-----------------------------------------------|
| **/dashboard**               | GET          | JWT    | READ ALL : Returns all the dashboard data.    |

### 6) Availabilities

| URI                 | Méthode HTTP | Auths? | Opération                                  |
|---------------------|--------------|--------|--------------------------------------------|
| **/availabilities** | GET          | No     | READ ALL : Returns all the availabilities. |
