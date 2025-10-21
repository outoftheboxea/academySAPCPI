# 📘 SAP CPI Integration – Regional Sales Order File Creation

## 📌 Overview

This integration demonstrates how to handle **region-specific sales order processing** in SAP CPI. Sales orders from various regions are received and enriched using product metadata via an **OData API**, then routed and saved as files in region-specific folders.

It features **ODATA lookup**, **pagination**, **looping process calls**, **file size management**, and **dynamic file creation** using the **File Receiver Adapter**.

---

## 🏢 Business Use Case

A global e-commerce company operates in multiple regions: **US, EU, and APAC**. When sales orders are placed:

* Product metadata must be enriched using SAP's Product Master API
* Orders must be grouped per region
* Maximum of **20 product entries per file** must be maintained to control file size
* Final files must be dropped into **region-specific folders** on the File Server

---

## 🔁 Integration Flow

### Steps

1. **HTTP/File Sender Adapter** receives sales order payload (batch)
2. **Splitter** breaks down orders or items
3. **Looping Process Call + OData Lookup** fetches product metadata from `/sap/opu/odata/sap/API_PRODUCT_SRV/`
4. **Content Modifier** adds enriched data and region attributes
5. **Router** directs message to region-specific branch
6. **Gather and File Splitter** groups max 20 products per file
7. **File Receiver Adapter** drops files into folders: `/orders/EU/`, `/orders/US/`, `/orders/APAC/`

---

## 🧾 Sample Input JSON (Sales Order Payload)

```json
{
  "OrderID": "ORD1001",
  "Region": "EU",
  "Items": [
    { "ProductID": "P001", "Quantity": 3 },
    { "ProductID": "P002", "Quantity": 1 }
  ]
}
```

---

## 🔍 Product Metadata Enrichment (ODATA Lookup)

* API Used: `/sap/opu/odata/sap/API_PRODUCT_SRV/`
* Sample Fields Fetched:

  * `ProductDescription`
  * `UoM`
  * `ProductType`
  * `TaxCode`

---

## 📂 Output File Format (Per Region, Max 20 Products)

```xml
<SalesOrders>
  <Order>
    <OrderID>ORD1001</OrderID>
    <Region>EU</Region>
    <Product>
      <ProductID>P001</ProductID>
      <Description>Wireless Mouse</Description>
      <Quantity>3</Quantity>
      <UoM>EA</UoM>
      <TaxCode>EU19</TaxCode>
    </Product>
    ...
  </Order>
</SalesOrders>
```

* File is saved as: `/orders/EU/ORD1001_001.xml`

---

## 📧 Exception Handling

* Any OData lookup or file write failure is handled via:

  * **Exception Subprocess**
  * **Data Store** entry for retry (optional)
  * **Gmail Adapter** sends alert with OrderID and error details

---

## 🧩 Components & Artifacts

| Component       | Technology                       |
| --------------- | -------------------------------- |
| Source          | E-commerce frontend or order API |
| Metadata Lookup | OData API (`API_PRODUCT_SRV`)    |
| DB/API Read     | Looping Process Call             |
| Grouping        | Gather + General Splitter        |
| File Output     | File Receiver Adapter            |
| Folder Routing  | Router (based on Region)         |
| Error Handling  | Exception Subprocess + Gmail     |

---

## ✅ Pre-requisites

* API `/sap/opu/odata/sap/API_PRODUCT_SRV/` active and reachable
* File receiver connection configured with region-wise folder structure
* Gmail Adapter set for notifications
* Message size and file splitting logic (max 20 items per file) implemented using properties

