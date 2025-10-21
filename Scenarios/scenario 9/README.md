
# 📘 SAP CPI Integration – Product Master Sync from SQL DB to AMQP Queue

## 📌 Overview

This integration showcases a **data extraction and distribution pattern** using **JDBC read** from an on-premise SQL database and **message publishing** to an external **AMQP-based message broker** (e.g., RabbitMQ, Azure Service Bus).

This setup enables **real-time propagation** of product master data to multiple downstream systems in a decoupled and scalable way.

---

## 🏢 Business Use Case

A retail company maintains **product master data** in an internal SQL database. This data must be synced periodically with:

* E-commerce portals
* Mobile apps
* POS systems

To enable **event-driven architecture**, the system should:

1. Periodically **read new/updated product records** from the database
2. **Format the records as JSON messages**
3. **Publish** them to an **AMQP queue/exchange** for downstream consumers

---

## 🔁 Integration Flow

### Steps

1. **Timer Start Event** triggers the iFlow every X minutes
2. **JDBC Adapter (Select)** reads new/changed products from `Product_Master`
3. **Splitter** processes each product individually
4. **Content Modifier** converts product structure to JSON format
5. **AMQP Adapter** publishes each product message to the queue
6. **Exception Subprocess** logs errors and alerts via Gmail

---

## 🧾 Sample Output JSON (Per Product Message)

```json
{
  "ProductID": "P001",
  "Name": "Smart TV 55 inch",
  "Category": "Electronics",
  "Price": 749.99,
  "Currency": "USD",
  "Stock": 120,
  "Status": "ACTIVE"
}
```

---

## 🗃️ Database Structures

### Product_Master (Used in JDBC Read)

```sql
CREATE TABLE Product_Master (
  ProductID VARCHAR(20) PRIMARY KEY,
  Name VARCHAR(100),
  Category VARCHAR(50),
  Price DECIMAL(10,2),
  Currency VARCHAR(5),
  Stock INT,
  Status VARCHAR(20),
  Sync_Status VARCHAR(10), -- e.g., 'NEW', 'SYNCED'
  Last_Updated TIMESTAMP
);

-- Sample data
INSERT INTO Product_Master (ProductID, Name, Category, Price, Currency, Stock, Status, Sync_Status, Last_Updated)
VALUES ('P001', 'Smart TV 55 inch', 'Electronics', 749.99, 'USD', 120, 'ACTIVE', 'NEW', CURRENT_TIMESTAMP);
```

---

### SQL Select Query (JDBC Read)

```sql
SELECT * FROM Product_Master WHERE Sync_Status = 'NEW';
```

---

## ✉️ Message Broker (AMQP)

* **Exchange/Queue Name**: `product.sync.queue`
* **Message Format**: JSON
* **Routing Key**: Optional (depending on AMQP setup)

---

## 📧 Exception Handling

* Any failure during AMQP publishing triggers an **Exception Subprocess**
* Failed product record is:

  * Stored in **Data Store** for retry
  * An **email alert** is sent to the integration team via **Gmail Adapter**, including ProductID and error details

---

## 🧩 Components & Artifacts

| Component          | Technology              |
| ------------------ | ----------------------- |
| Source             | On-premise SQL DB       |
| DB Read            | JDBC Adapter (SELECT)   |
| Record Split       | General Splitter        |
| Format Conversion  | Content Modifier → JSON |
| Message Publishing | AMQP Adapter (Producer) |
| Error Handling     | Data Store + Gmail      |

---

## ✅ Pre-requisites

* JDBC Data Source for on-prem SQL DB configured in CPI
* AMQP connection and credentials configured
* Data Store defined in CPI tenant
* Gmail Adapter set up for alerting

