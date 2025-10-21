
# 📘 SAP CPI Integration – Job Requisition Archival with DB Logging via JDBC

## 📌 Overview

This integration demonstrates how to **extract job requisition data** from **SAP SuccessFactors** using the **OData API**, and **log the requisition metadata** into an **on-premise SQL database** via the **JDBC Adapter** for auditing and compliance.

It is designed to run on a **scheduled basis** and includes **exception handling** with email notifications and optional data persistence via **Data Store**.

---

## 🏢 Business Use Case

A multinational enterprise uses **SAP SuccessFactors** to manage job requisitions. For compliance, the HR IT team needs to **archive key requisition details** to a centralized **SQL database**.

The data is:

* Pulled from the **Job Requisition OData API** (`/odata/v2/JobRequisition`)
* Filtered based on creation date or status
* Logged into the **Job_Requisition_Audit** table
* Optionally used for downstream reporting or analytics

---

## 🔁 Integration Flow

### Steps

1. **Timer Start Event** triggers the iFlow (e.g., every 4 hours)
2. **OData Receiver Adapter** pulls job requisitions using filter/pagination
3. **Looping Process Call** or pagination handles multiple pages
4. **Splitter** iterates over each job requisition
5. **Content Modifier** formats the fields for DB insert
6. **JDBC Adapter** writes to the audit table
7. **Exception Subprocess** logs errors and sends alerts

---

## 🧾 Sample API Response (Job Requisition JSON)

```json
{
  "d": {
    "results": [
      {
        "jobReqId": "1002345",
        "jobTitle": "Senior Developer",
        "location": "Berlin",
        "department": "Engineering",
        "status": "Open",
        "createdDate": "/Date(1721155200000)/"
      }
    ]
  }
}
```

---

## 🗃️ Database Structures

### Job_Requisition_Audit (JDBC Write Table)

```sql
CREATE TABLE Job_Requisition_Audit (
  job_req_id VARCHAR(20),
  job_title VARCHAR(100),
  location VARCHAR(100),
  department VARCHAR(100),
  status VARCHAR(20),
  created_date TIMESTAMP
);
```

---

### SQL Insert Query (Used in JDBC Write)

```sql
INSERT INTO Job_Requisition_Audit 
(job_req_id, job_title, location, department, status, created_date)
VALUES (?, ?, ?, ?, ?, ?);
```

> Parameter values are extracted and formatted from each requisition entry.

---

## 📧 Exception Handling

* In case of API, DB, or transformation failure:

  * Failed message is logged to **Data Store**
  * Alert email is triggered via **Gmail Adapter** with error details and jobReqId

---

## 🧩 Components & Artifacts

| Component      | Technology                     |
| -------------- | ------------------------------ |
| Source         | SAP SuccessFactors (OData API) |
| Data Retrieval | OData Receiver Adapter         |
| DB Logging     | JDBC Adapter (Insert)          |
| Pagination     | Looping Process Call           |
| Error Handling | Exception Subprocess           |
| Alerts         | Gmail Adapter                  |
| Backup         | Data Store (Optional)          |

---

## ✅ Pre-requisites

* OData credentials for SAP SuccessFactors
* JDBC data source configured in CPI (to SQL DB)
* Target table created in SQL DB
* Gmail adapter credentials configured
* Data Store setup for fallback/error storage
