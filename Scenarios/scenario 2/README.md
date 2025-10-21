# 📘 SAP CPI Integration – Product Shipment Orders Processing

## 📌 Overview

This integration handles **bulk shipment orders** by splitting them into individual orders, validating quantities, routing them based on region, and converting them into structured formats for warehouse systems.

The scenario demonstrates the use of **Splitter**, **Filter**, **Router**, and **Converter** palette steps in SAP CPI and helps streamline logistics automation.

---

## 🏢 Business Use Case

A logistics system sends a **daily shipment file** containing multiple product orders for different regional warehouses (EU, US). Each order includes the product ID, quantity, and region.

Processing needs to:

* Validate orders (quantity > 0)
* Route based on region
* Convert format from XML to JSON
* Store orders as separate files in **region-specific folders**

---

## 🔁 Integration Flow

### Steps

1. **File Sender Adapter** picks up batch XML file
2. **XML Splitter** splits each `<Order>` from the batch
3. **Filter** removes invalid orders (e.g., quantity = 0)
4. **Content Modifier** enriches each message with metadata (e.g., processed timestamp)
5. **Router** directs orders to regional branches (EU/US)
6. **XML to JSON Converter** transforms each valid order
7. **File Receiver Adapter** writes to `/shipments/EU/` or `/shipments/US/`

---

## 🧾 Sample Input XML (Batch Shipment File)

```xml
<Shipments>
  <Order>
    <ID>ORD001</ID>
    <Region>EU</Region>
    <Quantity>10</Quantity>
  </Order>
  <Order>
    <ID>ORD002</ID>
    <Region>US</Region>
    <Quantity>0</Quantity>
  </Order>
  <Order>
    <ID>ORD003</ID>
    <Region>EU</Region>
    <Quantity>5</Quantity>
  </Order>
</Shipments>
```

---

## 🔍 Processing Example

* **Filtered out**: `ORD002` (Quantity = 0)
* **Processed and routed**: `ORD001` → EU folder, `ORD003` → EU folder

---

## 🔄 JSON Output Example (ORD001)

```json
{
  "Order": {
    "ID": "ORD001",
    "Region": "EU",
    "Quantity": 10
  }
}
```

---

## 📂 File Output (File Receiver Adapter)

* Output path:
  `/shipments/EU/ORD001.json`
  `/shipments/EU/ORD003.json`

* Output format: JSON

* File naming: Based on order ID and region

---

## 📧 Exception Handling (Optional)

* **Catch step errors** in file write or transformation
* Log to **Data Store**
* Notify logistics support team via **Gmail Adapter**

---

## 🧩 Components & Artifacts

| Component       | Technology                   |
| --------------- | ---------------------------- |
| Source          | File system with batch XML   |
| Validation      | Filter (Quantity > 0)        |
| Routing         | Router (Region: EU/US)       |
| Transformation  | XML to JSON Converter        |
| Output          | File Receiver Adapter        |
| Metadata        | Content Modifier             |
| Optional Alerts | Exception Subprocess + Gmail |

---

## ✅ Pre-requisites

* File system access for batch input and regional output folders
* Folder structure created: `/shipments/EU/`, `/shipments/US/`
* File Receiver Adapter configured with dynamic path handling
* Gmail Adapter configured (optional)
