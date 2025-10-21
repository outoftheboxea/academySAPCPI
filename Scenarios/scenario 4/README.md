# 📘 SAP CPI Integration – Sales Order Processing with Conditional Enrichment and Dynamic Routing

## 📌 Overview

This integration scenario enables **smart sales order processing** by dynamically enriching and routing orders based on **region**, **order value**, and **customer segment**. Orders are received from an external application, enriched with dynamic metadata, and routed to regional **SAP S/4HANA** systems.

The scenario uses the **Content Modifier** for complex header/property enrichment and **Router** for region-based dynamic flow branching.

---

## 🏢 Business Use Case

A global e-commerce company needs to send **sales orders** from their web application to regional **SAP S/4HANA systems** (US, EU, APAC). However:

* Orders must include region-specific data like **tax codes**, **pricing tier**, and **partner ID**
* Orders must be **routed** based on the region
* A **tracking URL** and **correlation ID** must be dynamically generated
* Order priority is assigned based on **order amount**

---

## 🔁 Integration Flow

### Steps

1. **HTTP Sender Adapter** receives order in JSON format
2. **JSON to XML Converter** standardizes the message structure
3. **Content Modifier** enriches:

   * `Region`, `OrderTier`, `TrackingURL`, `CorrelationID`, `TaxCode`
4. **Router** determines the target branch based on region
5. **HTTP/SOAP Receiver Adapter** sends order to regional S/4HANA instance
6. **Exception Subprocess** handles errors with Data Store backup and email alerts

---

## 🧾 Sample Input JSON (Sales Order Request)

```json
{
  "Order": {
    "OrderID": "SO789",
    "CustomerID": "CUST202",
    "Region": "EU",
    "Items": [
      { "ProductID": "P1001", "Quantity": 3, "UnitPrice": 150 },
      { "ProductID": "P2002", "Quantity": 1, "UnitPrice": 300 }
    ],
    "Currency": "EUR",
    "OrderDate": "2025-07-16"
  }
}
```

---

## ✏️ Enriched Data via Content Modifier

* **Headers**:

  * `Region = EU`
  * `OrderTier = Premium` (if total > 500)
  * `TrackingURL = https://track.company.com/SO789`
  * `CorrelationID = SO789-20250716`
* **Properties**:

  * `PartnerID = PARTNER_EU`
  * `TaxCode = EU19`
  * `ProcessedDate = 2025-07-16T10:45:00`

---

## 🔀 Routing Logic (Router)

| Region | Target System           | Adapter   |
| ------ | ----------------------- | --------- |
| EU     | SAP S/4HANA EU System   | HTTP/SOAP |
| US     | SAP S/4HANA US System   | HTTP/SOAP |
| APAC   | SAP S/4HANA APAC System | HTTP/SOAP |

---

## 🧾 Final XML Payload Example (for EU region)

```xml
<SalesOrder>
  <OrderID>SO789</OrderID>
  <PartnerID>PARTNER_EU</PartnerID>
  <Region>EU</Region>
  <OrderTier>Premium</OrderTier>
  <TrackingURL>https://track.company.com/SO789</TrackingURL>
  <TaxCode>EU19</TaxCode>
  <Items>
    <Item>
      <ProductID>P1001</ProductID>
      <Quantity>3</Quantity>
      <UnitPrice>150</UnitPrice>
    </Item>
    <Item>
      <ProductID>P2002</ProductID>
      <Quantity>1</Quantity>
      <UnitPrice>300</UnitPrice>
    </Item>
  </Items>
  <OrderDate>2025-07-16</OrderDate>
</SalesOrder>
```

---

## 📧 Exception Handling

* Captures API failures or mapping errors
* Logs message to **Data Store** for retry
* Sends **alert email** via Gmail with:

  * Order ID
  * Error description
  * Region and Correlation ID

---

## 🧩 Components & Artifacts

| Component    | Technology                      |
| ------------ | ------------------------------- |
| Source       | E-commerce order application    |
| Receiver     | Regional SAP S/4HANA systems    |
| Adapters     | HTTP Sender/Receiver, Gmail     |
| Enrichment   | Content Modifier                |
| Routing      | Router (based on Region header) |
| Backup/Error | Data Store + Gmail              |

---

## ✅ Pre-requisites

* All regional endpoints configured and reachable
* Order total calculator logic for tier classification (optional Groovy)
* Gmail Adapter credentials configured
* Secure parameter storage for dynamic content (if needed)

