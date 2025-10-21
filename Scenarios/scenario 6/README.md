# 📘 SAP CPI Integration – Supplier Creation via HTTP (Testing Use Case)

## 📌 Overview

This integration scenario demonstrates how to test and validate **supplier creation** in **SAP S/4HANA** using **SAP CPI** before going live with a production-grade solution.

It uses **Postman** (or any REST client) to simulate incoming supplier creation requests via **HTTP**, routes the data through SAP CPI, and posts it to **S/4HANA** using the OData service:
`/sap/opu/odata/sap/API_SUPPLIER_SRV/`

---

## 🏢 Business Use Case

A company is onboarding a new supplier creation process from an external system. Before integrating the full application, the development team wants to **validate connectivity, payload structure, error handling**, and **S/4HANA response behavior**.

---

## 🔁 Integration Flow

### Steps

1. **HTTP Sender Adapter** receives supplier creation request in **JSON format** from Postman
2. **Message transformation** (via Content Modifier or Groovy) adapts data to S/4HANA OData format
3. **HTTP Receiver Adapter** sends the request to `/sap/opu/odata/sap/API_SUPPLIER_SRV/`
4. **Success**: Returns response to Postman
5. **Failure**:

   * Captured in **Exception Subprocess**
   * Alert email sent via **Gmail Adapter** with supplier details and error trace

---

## 🧾 Sample Input JSON (Postman Request)

```json
{
  "Supplier": "SUPP1002",
  "SupplierName": "Global Tech Supplies",
  "Country": "US",
  "City": "New York",
  "Street": "5th Avenue 123",
  "PostalCode": "10001"
}
```

---

## 📤 Output – SAP S/4HANA Response

* On success: 201 Created with OData response
* On failure: 400 or 500 response with detailed error message from S/4HANA

---

## 📧 Exception Handling

If the S/4HANA call fails due to validation or network issues:

* Exception Subprocess catches the error
* Email alert sent via **Gmail Adapter** with:

  * Supplier ID
  * Error message
  * Timestamp

---

## 🧩 Components & Artifacts

| Component      | Technology                         |
| -------------- | ---------------------------------- |
| Request Tool   | Postman (HTTP Sender)              |
| Target API     | S/4HANA OData (`API_SUPPLIER_SRV`) |
| Adapters Used  | HTTP Sender & Receiver, Gmail      |
| Error Handling | Exception Subprocess + Email       |
| Payload Format | JSON → XML (if required by API)    |

---

## ✅ Pre-requisites

* Active S/4HANA OData API: `/sap/opu/odata/sap/API_SUPPLIER_SRV/`
* S/4HANA destination configured in SAP CPI
* Gmail adapter credentials configured
* Proper headers in Postman (e.g., `Content-Type: application/json`)
* Authorization token (Basic or OAuth) passed to CPI

