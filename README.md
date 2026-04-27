# InfyMeDigitalBanking

You are required to build a backend application called InfyMeDigitalBanking_Accounts using Spring Boot and Spring REST, with MySQL as the database. The application represents the account-management part of a digital banking platform. Its purpose is to let a UI perform essential banking operations such as fetching all bank accounts linked to a mobile number, linking a bank account to the digital banking app, checking balance, transferring funds, and fetching account statements. The business flow is meant to resemble platforms like Paytm, GPay, and PhonePe, where a user starts with a mobile number, retrieves bank accounts mapped to that number, links an account after verification, and then performs transactions.

Core business objective

The main objective is to create a secure REST backend that acts as a bridge between the UI and the banking data. In simple words, the application should support two broad use cases. First, it must let users discover and link their bank accounts to the digital banking system. Second, once accounts are linked, it must allow operational banking features such as balance inquiry, fund transfer, and statement viewing. The entire implementation is expected to be backend-only, with a strong focus on APIs, database mapping, validation, service logic, and exception handling.

Technology stack and mandatory platform choices

The backend must be implemented using Spring Boot and Spring REST. The database must be MySQL. The repository layer should use Spring Data JPA wherever possible. The application should be configured so that table creation can be auto-generated from entity classes using application properties. Sample data should be inserted either through DML statements or through POST endpoints that you create for populating the database.

Functional scope of the application

The backend is expected to support the following major features:

1.  Creating a bank account record.
2.  Listing all bank accounts associated with a given mobile number.
3.  Linking a bank account to the digital banking application without OTP verification.
4.  Linking a bank account to the digital banking application with OTP verification.
5.  Checking account balance for a linked account.
6.  Performing fund transfer from one account to another.
7.  Fetching account statement based on mobile number.

High-level domain model

The project revolves around three main persistent data structures:

1.  BankAccount, which stores raw bank account information associated with mobile numbers.
2.  DigitalBankAccount, which stores the accounts that have been linked to the digital banking platform.
3.  Transaction, which stores fund transfer records and related transaction details.

Database design expectations

You are expected to create all required tables in a single database so that entity mapping and relationships are easier to manage. The document explicitly says that if you feel more or fewer columns are needed to properly satisfy the requirement, you may adjust the schema. That means the given structure is the baseline, not an absolute prison. However, if you deviate, you should still preserve the business meaning of the original design.

BankAccount table specification

The BankAccount table stores bank account details associated with a mobile number. The account number is the primary key. Required fields are account_number, bank_name, balance, account_type, ifsc_code, opening_date, and mobile_number. All fields except the primary key’s nullability assumption are expected to be non-null. The opening_date uses a date type corresponding to LocalDate in Java. The table represents a unified view of accounts instead of separate tables per bank, which simplifies the scenario compared to real-life banking integrations.

DigitalBankAccount table specification

The DigitalBankAccount table stores only those bank accounts that a user has linked to the digital banking application. It contains digital_banking_id as the primary key, mobile_number as a foreign key-like reference, account_number as a foreign key-like reference, and account_type. The account_number should use appropriate association mapping in the entity. The table is effectively the “linked accounts” registry of the app.

Transaction table specification

The Transaction table stores all transactions performed through the digital banking application. It includes transaction_id as the primary key, mode_of_transaction, paid_to, receiver_account_number, amount, transaction_date_time, remarks, paid_from, and sender_account_number. Although the design mentions multiple possible transaction modes like bill pay or recharge, the task simplifies all transaction modes to “Fund Transfer” so that account statement functionality is easier to implement.

ID generation requirements

There are explicit generation expectations for identifiers. BankAccount.accountNumber must be auto-generated starting from 512345678. DigitalBankAccount.digitalBankingId must be auto-generated in a string format like W_1001 and should continue increasing in the thousand series. Transaction.transactionId must be auto-generated starting from 1234543. This means you need a custom ID-generation strategy or an application-level mechanism, because these formats are not all standard auto-increment use cases.

Sample data expectation

The document states that sample data structures and sample data are given, but the visible content mainly describes the structure and mentions examples such as four accounts mapped to mobile number 9876987431. So, to put it simply, you should seed realistic test data that demonstrates one mobile number having multiple bank accounts, linked and unlinked account states, and enough transactions to test account statement and transfer behavior. The task also explicitly allows you to insert the sample data through SQL or through endpoints.

REST controller expectations

You must create a REST controller class named AccountApi. It must be annotated as a REST controller and also include cross-origin support. Required services and a logger must be autowired. Each method should log its method name. This controller is responsible for exposing all account-related endpoints such as create account, list accounts, link account, OTP-based linking, balance check, fund transfer, and account statement.

Required endpoints

The controller must expose the following API behaviors:

1.  POST /accounts to create a new account.
2.  GET /accounts/{mobileNo} to list accounts for a mobile number.
3.  POST /accounts/{mobileNo} to link an account without OTP.
4.  POST /accounts/{mobileNo} to link an account with OTP.
5.  GET /accounts/balance/{mobileNo} with accountNo as a query parameter to check balance.
6.  PUT or PATCH /accounts/fundtransfer/ to perform fund transfer.
7.  GET /accounts/statement/{mobileNo} to fetch transactions for that mobile number.

Important API design ambiguity you should resolve carefully

The specification lists two POST endpoints with the same URI, /accounts/{mobileNo}, one for linking without OTP and one for linking with OTP. That is ambiguous as written. In practice, you will need to distinguish them by request body shape, request parameters, different sub-paths, or by consolidating them into one endpoint that decides logic based on whether OTP is present. Also, the createAccount method mentions AccountDTO, but the DTO section actually defines BankAccountDTO, not AccountDTO. So there is a naming inconsistency you should normalize in your implementation.

Service layer requirements

You must create three service interfaces:

1.  BankAccountService
2.  DigitalBankAccountService
3.  TransactionService

You must also create their implementation classes:

1.  BankAccountServiceImpl
2.  DigitalBankAccountServiceImpl
3.  TransactionServiceImpl

These implementation classes must be marked as Spring service classes, must autowire required repositories and a logger, and must log method names. The service layer is where the business rules are expected to live.

Service method: createAccount

This method accepts account data and creates a new BankAccount record. It should invoke the repository save method and return the account number as a string. You are expected to handle exceptions. The specification explicitly permits account number auto-generation to automate the process. In other words, the caller should not have to manually provide a valid generated account number if your design handles generation internally.

Service method: listAccounts

This method takes a mobile number and returns all BankAccountDTO records linked to that mobile number. The requirement says to invoke the repository to fetch records and, if no accounts are found, throw InfyMeDigitalBankingException with the message NO_ACCOUNTS_FOUND. That means an empty list is not considered a successful business result here; it is considered an exceptional condition.

Service method: linkAccount without OTP

This method takes mobile number and account number, verifies whether one or more bank accounts exist for that mobile number, and if valid, persists a linked account record into DigitalBankAccount. If no accounts exist for the mobile number, it must throw NO_ACCOUNTS_FOUND. The main expectation is that only valid bank accounts belonging to that mobile number should be linkable.

Service method: linkAccount with OTP

This method behaves like the previous linking method but includes OTP verification. The service must validate the incoming OTP against the OTP returned by the utility. If the OTP does not match, it must throw InfyMeDigitalBankingException with the message OTP_DOESNOT_MATCH. If the mobile number does not have any bank accounts, it must throw NO_ACCOUNTS_FOUND. Only after successful validation should it persist the linked account to DigitalBankAccount.

Service method: checkBalance

This method returns the balance for a given mobile number and account number, but only if that account is linked in the digital banking app. That means the service should first verify the presence of the mobile number or mapping in the DigitalBankAccount table. If the account is not linked, it should throw NO_ACCOUNT_IS_LINKED. If linked, it should fetch the balance from the BankAccount table. The key condition here is that raw bank-account existence is not enough; the account must be linked in the digital layer.

Service method: fundTransfer

This method transfers funds from one mobile number to another. The service must verify that the sender has sufficient balance. If the requested amount exceeds the available balance, it must throw INSUFFICIENT_FUNDS. If sufficient, the amount should be debited from the sender’s account, credited to the receiver’s account, and a transaction record must be inserted into the Transaction table. The task also explicitly warns that both sender and receiver may have multiple bank accounts linked to the same mobile number, so your implementation must clearly decide from which sender account money is debited and to which receiver account money is credited. This is one of the most important business-rule areas in the project.

Service method: accountStatement

This method returns all transactions associated with a given mobile number. The repository should fetch transaction DTOs using the mobile number, and if no transactions are found, the service must throw NO_ACTIVE_TRANSACTIONS. The specification treats absence of transactions as a business exception rather than returning an empty result.

Repository layer requirements

You must create repository interfaces for AccountRepository, DigitalBankAccountRepository, and TransactionRepository. These should be Spring Data JPA repositories wherever possible. The repository layer must not contain implementation classes. You are expected to rely on method-name query derivation, @Query-based JPQL, or @NamedQuery on entity classes if needed. To put it another way, business logic belongs in services, not in hand-written repository implementations.

Entity layer requirements

You must create entity classes for BankAccountEntity, DigitalBankAccountEntity, and TransactionEntity. Each must be properly annotated as an entity and mapped to its corresponding table. The primary keys, column mappings, data types, and associations must reflect the specification. The DigitalBankAccountEntity specifically requires appropriate association mapping for accountNumber, which strongly suggests a relationship to BankAccountEntity instead of treating it as a loose scalar field only.

DTO layer requirements

You must create DTOs for BankAccountDTO and TransactionDTO. The BankAccountDTO requires validation rules on most fields. For example, account number cannot be null and should have a minimum length of 7, bank name must be between 5 and 15 characters, balance cannot be negative, account type and IFSC code cannot be null and must respect length rules, opening date must be a past date, and mobile number must be exactly 10 digits in length. TransactionDTO fields are listed but no explicit validation rules are given in the visible section, so you may add validations if needed as long as they are consistent with the business rules.

Validation-related expectation

The document expects custom validation messages to be externalized into a separate ValidationMessages.properties file. That means bean validation should not just use default messages. Instead, the application should return consistent, custom, property-driven validation messages whenever DTO input fails constraints. The global exception handler must also handle MethodArgumentNotValidException and ConstraintViolationException.

OTP utility requirement

You must create an OTPUtility class. Its responsibility is to generate OTP values for mobile numbers. To simplify the task, it always returns a hardcoded OTP of 123456. The document explicitly says you may enhance this behavior by generating different OTPs for different mobile numbers or even storing them in a database table, but that enhancement is optional. So the baseline expected behavior is a hardcoded OTP generator.

Exception handling requirements

You must create a global exception handler class named InfyMeDigitalBankingGlobalExceptionHandler. It should be a REST controller advice and should be common to both REST projects, although this document mainly describes the Accounts project. It must autowire environment values and handle application exceptions as well as validation-related exceptions such as MethodArgumentNotValidException and ConstraintViolationException. The supporting model class ErrorInformation must store errorMessage, errorCode, and errorTimeStamp.

Exception constants requirement

You must create an enum called ExceptionConstants containing keys such as SERVER_ERROR, AUTHENTICATION_FAILED, USER_NOT_FOUND, USERID_NOT_FOUND, NO_USERS_FOUND, NO_ACCOUNTS_FOUND, NO_ACCOUNT_IS_LINKED, INSUFFICIENT_FUNDS, and NO_ACTIVE_TRANSACTIONS. The note says you may remove unused constants when using them across both projects. That means some of these constants are generic and may have come from a broader system template, but you should retain or adapt only those needed by your implementation.

Application starter and property source requirement

You must create the main Spring Boot starter class named InfyMeDigitalBankingAccountApplication. It should be annotated as a Spring Boot application and configured to read values from messages.properties using property source configuration. This indicates that your application is expected to externalize business and error messages rather than hardcoding them in service classes.

application.properties expectations

The application.properties file must include basic application setup, application name, web configuration, port configuration, database properties, MySQL driver details, database URL, database username and password, table auto-generation settings, and log file naming. The document does not give exact values, but it clearly expects these categories to be present and correctly wired.

messages.properties expectations

The messages.properties file must contain the listed custom messages such as server.invalid, authentication.failed, user.not.found, user.id.not.found, no.users.found, no.account.found, no.account.is.linked, insufficient.funds, and no.active.transactions. The document also expects a separate ValidationMessages.properties file for DTO validation messages. In simple words, all reusable error and validation text should come from property files.

Logging expectations

The controller and service classes are expected to autowire or instantiate loggers and log the names of the methods being executed. This is not just a nice-to-have suggestion; it is explicitly called out in multiple sections. So your implementation should include method-level logging in the relevant layers.

CORS expectation

The AccountApi controller must include cross-origin resource-sharing support. That tells you the backend is expected to be callable from a front-end application hosted on another origin, which is common in UI-backend separation.

Business conditions you must enforce

There are several business conditions that are central to correctness:

1.  A mobile number can have multiple bank accounts.
2.  The same mobile number may be associated with accounts from different banks.
3.  Each new account linking may require separate OTP verification.
4.  Balance checking is allowed only for linked accounts.
5.  Fund transfer must fail if the sender lacks sufficient balance.
6.  Account statements should be fetched by mobile number.
7.  Missing data is usually treated as an exception rather than returning empty results.

Important implementation limitations and simplifications

The project intentionally simplifies real-world banking behavior in a few places. First, bank account details are stored in a single table instead of being fetched from actual bank systems. Second, OTP generation is hardcoded. Third, transaction mode is simplified to fund transfer only. Fourth, sample data can be adjusted if needed. These simplifications reduce integration complexity and help you focus on core backend architecture and business logic.

Potential gray areas you should handle thoughtfully

There are a few areas where the specification leaves room for design judgment:

1.  The exact structure of request bodies for account linking is not clearly standardized.
2.  The same endpoint path is used for link-with-OTP and link-without-OTP.
3.  The document alternates between AccountDTO and BankAccountDTO.
4.  The exact relationship mapping between DigitalBankAccount and BankAccount is not fully spelled out, though association mapping is requested.
5.  The exact method for generating custom string IDs like W_1001 is not prescribed.
6.  The transaction statement requirement says it is based on mobile number, while one property message mentions phone number and account number. That mismatch should be normalized in your final design.

What a strong submission should demonstrate

A strong implementation should show clean separation of layers, correct entity-to-table mapping, proper DTO validation, consistent exception handling, reusable repository methods, sound service-level business logic, and reliable database state changes during fund transfer. For example, fund transfer should ideally be implemented transactionally so that debit, credit, and transaction logging succeed or fail together. Even though the document does not explicitly mention transaction management annotations, this is an expectation that naturally follows from the business requirement.
