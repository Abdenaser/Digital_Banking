<h2>Projet : Digital Banking</h2>

We want to create a Web application based on Spring and Angular which allows to manage bank accounts. Each account
belongs to a customer. There are two types of accounts: Current and Savings. each Account may undergo Debit or Credit
transactions. The application consists of the following layers:

- DAO Layer (JPA Entities and Repositories)
- Service layer defining the following operations:
    - Add accounts
    - Add customers
    - Make a debit (Withdrawal)
    - Make a credit (Payment)
    - Transfer money
    - Consult an account
- The DTO layer
- Mappers (DTO <=>Entities)
- The web layer (Rest Controllers)
- Security layer (Spring Security with JWT)

<h2> Use case : </h2>

![dg](https://user-images.githubusercontent.com/85079548/168427608-20c6062e-15a1-4659-ab74-03a77b9c107b.png)
