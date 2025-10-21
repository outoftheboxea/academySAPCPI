# 📘 SAP CPI Integration – Supplier Data Retry & Persistence using Data Store

## 📌 Overview

This integration demonstrates a **resilient supplier creation flow** in SAP CPI, leveraging the **SAP S/4HANA OData API** (`/sap/opu/odata/sap/API_SUPPLIER_SRV/`) with **Data Store-based retry handling**.

It ensures **no data loss** by persisting failed supplier records to **SAP CPI’s Data Store**, and includes a separate **retry iFlow** to reprocess these entries periodically.

---

## 🏢 Business Use Case

A company creates new suppliers from an external application by sending requests to **SAP CPI**, which then posts the data to **SAP S/4HANA** via OData.

However, failures can occur due to:

* Temporary SAP downtime
* Authentication issues
* Validation errors

To prevent data loss and support reliable integration, a retry mechanism is implemented using **Data Store**.

---

## 🔁 Integration Flow – Phase 1: Supplier Creation

### Steps

1. **HTTP Sender Adapter** receives a new supplier creation request (JSON format)
2. **Message transformation** prepares the payload for SAP S/4HANA
3. **HTTP Receiver Adapter** posts the data to `/sap/opu/odata/sap/API_SUPPLIER_SRV/`
4. **Success**: Response is returned to the sender
5. **Failure**:

   * Message is written to **Data Store** with a unique key (`supplierId_timestamp`)
   * Error is logged
   * Alert email is sent via **Gmail Adapter**

---

## 🔁 Integration Flow – Phase 2: Retry Failed Suppliers

### Steps

1. **Timer Start Event** triggers a **scheduled iFlow**
2. **Data Store Read** operation fetches unprocessed supplier entries
3. For each record:

   * Re-attempt the POST to S/4HANA
   * On success → delete from Data Store
   * On failure → update error details (or retain for next retry)
4. **Email notification** sent for permanently failed retries (after max attempts)

---

## 🧾 Sample Input JSON (Supplier Request via HTTP)

```json
{
  "Supplier": "SUPP1005",
  "SupplierName": "Techno Supplies Ltd.",
  "Country": "DE",
  "City": "Berlin",
  "Street": "Alexanderplatz 10",
  "PostalCode": "10178"
}
```

---

## 📦 Data Store Key Format

```
SUPP1005_20251021T102300
```

> Combines supplier ID and timestamp to ensure uniqueness.

---

## 🔧 Design Considerations

| Component           | Details                                                       |
| ------------------- | ------------------------------------------------------------- |
| **Sender**          | External App / Postman (HTTP)                                 |
| **Receiver**        | SAP S/4HANA OData API (`API_SUPPLIER_SRV`)                    |
| **Adapters**        | HTTP (Sender and Receiver), Gmail                             |
| **Error Handling**  | Exception Subprocess with Data Store Write + Email Alert      |
| **Retry Mechanism** | Timer-based iFlow + Data Store Read + Conditional Retry Logic |
| **Payload Format**  | JSON input, transformed to required OData structure           |

---

## 🧩 Components & Artifacts

| Artifact              | Type                   |
| --------------------- | ---------------------- |
| SupplierCreate_iFlow  | Main processing flow   |
| SupplierRetry_iFlow   | Scheduled retry flow   |
| Gmail Notification    | Email alert on failure |
| Data Store Operations | Write, Read, Delete    |

---

## ✅ Pre-requisites

* S/4HANA OData service `/sap/opu/odata/sap/API_SUPPLIER_SRV/` active and reachable
* CPI Data Store setup with named store (e.g., `Supplier_Retry`)
* Gmail Adapter configured for alerting
* Proper credentials (OAuth or Basic Auth) for S/4HANA access
* Custom error handling logic (Groovy optional) for retries and max-attempt logic

