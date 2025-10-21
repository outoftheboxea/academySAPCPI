
# 📘 SAP CPI Integration – Secure Customer Registration via AMQP with DB Enrichment and Logging

## 📌 Overview

This integration scenario demonstrates how to process **encrypted customer registration events** received via **AMQP**, enrich them using a **JDBC database lookup**, and store the final results in a **SQL logging table** for compliance and analytics.

The flow also handles **decryption**, **database read/write**, and **exception processing** with alerting.

---

## 🏢 Business Use Case

A digital enterprise receives **customer registration data** from its websites and mobile apps in **encrypted form** via **AMQP queues**. Each message includes:

* Encrypted customer name and email
* A region code (e.g., EU, US, APAC)

The CPI integration must:

1. **Decrypt** the sensitive fields
2. **Lookup the full region name** from an on-premise SQL database
3. **Insert** the enriched customer data into a logging table
4. **Handle exceptions** by logging failed records and sending email notifications

---

## 🔁 Integration Flow

### Steps

1. **AMQP Sender Adapter** receives encrypted JSON messages
2. **Groovy Script** decrypts customer name and email
3. **JDBC Lookup** retrieves region name using `regionCode`
4. **JDBC Insert** writes enriched record to `Customer_Registration_Log`
5. **Exception Subprocess**:

   * On failure: write to Data Store and send email via **Gmail Adapter**

---

## 🧾 Sample Input (Encrypted JSON)

```json
{
  "customerId": "CUST12345",
  "encryptedName": "U2FsdGVkX1+zO2FsdlTgmw==",
  "encryptedEmail": "U2FsdGVkX18znbkFvE+NdA==",
  "regionCode": "EU"
}
```

---

## 🔓 Sample Output (Decrypted & Enriched JSON)

```json
{
  "customerId": "CUST12345",
  "customerName": "Anna Becker",
  "customerEmail": "anna.becker@example.com",
  "regionCode": "EU",
  "regionName": "Europe",
  "registrationDate": "2025-07-16T14:20:00"
}
```

---

## 🗃️ Database Structures

### Region_Master (Used in JDBC Read)

```sql
CREATE TABLE Region_Master (
  region_code VARCHAR(10) PRIMARY KEY,
  region_name VARCHAR(100)
);

INSERT INTO Region_Master (region_code, region_name) VALUES
('EU', 'Europe'),
('US', 'United States'),
('APAC', 'Asia Pacific');
```

---

### Customer_Registration_Log (Used in JDBC Write)

```sql
CREATE TABLE Customer_Registration_Log (
  customer_id VARCHAR(20),
  customer_name VARCHAR(100),
  customer_email VARCHAR(100),
  region_code VARCHAR(10),
  region_name VARCHAR(100),
  registration_date TIMESTAMP
);
```

---

### JDBC Insert Query

```sql
INSERT INTO Customer_Registration_Log 
(customer_id, customer_name, customer_email, region_code, region_name, registration_date)
VALUES (?, ?, ?, ?, ?, ?);
```

---

## 🔒 Decryption (Groovy)

Use AES or GPG decryption in a Groovy script. Ensure the decryption key is stored securely using **Secure Parameter** in CPI.

> You may use Java’s `javax.crypto` for AES or BouncyCastle for GPG support.

---

## 📧 Exception Handling

* Use **Exception Subprocess** to catch any errors.
* Store the failed message in **Data Store** for reprocessing.
* Send an alert to the IT/Support team using the **Gmail Adapter**, including:

  * Customer ID
  * Error stack trace

---

## 🧩 Components & Artifacts

| Component        | Technology                       |
| ---------------- | -------------------------------- |
| Source           | AMQP Queue                       |
| Decryption       | Groovy Script                    |
| DB Read          | JDBC (Region_Master)             |
| DB Write         | JDBC (Customer_Registration_Log) |
| Exception Alerts | Gmail Adapter                    |
| Retry Support    | CPI Data Store                   |

---

## ✅ Pre-requisites

* JDBC connection to SQL DB configured in CPI
* Secure parameter for decryption key
* AMQP adapter setup
* Gmail credentials set for email alerts

---