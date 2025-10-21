# 📘 SAP CPI Integration – HR Timesheets Processing and Validation

## 📌 Overview

This integration handles **employee timesheet submissions** from a web application. Timesheet records are **validated**, **converted**, and **stored in the HR system** in **CSV format** using region-specific folders.

It demonstrates the use of **HTTP Adapter**, **Splitter**, **Filter**, **Router**, **Format Conversion**, and **File Adapter**, suitable for automating and validating HR operations.

---

## 🏢 Business Use Case

Employees submit daily timesheets via a **custom web application**. Each timesheet record must be:

* Validated (e.g., working hours ≤ 12)
* Converted into a **CSV format**
* Stored in an HR system using a **File Receiver Adapter**
* Invalid records should be **logged separately** for compliance review

---

## 🔁 Integration Flow

### Steps

1. **HTTP Sender Adapter** receives batch JSON data
2. **JSON to XML Converter** prepares structure for processing
3. **XML Splitter** breaks down entries into individual records
4. **Filter** validates each record (`Hours` ≤ 12)
5. **Content Modifier** adds metadata (e.g., company name, processed timestamp)
6. **Router** separates valid and invalid records
7. **XML to CSV Converter** formats data
8. **File Receiver Adapter** writes valid CSVs to `/hr/valid/`, invalid ones to `/hr/invalid/`

---

## 🧾 Sample Input JSON (from Web App)

```json
{
  "Timesheets": [
    { "EmployeeID": "E123", "Date": "2025-07-16", "Hours": 9 },
    { "EmployeeID": "E456", "Date": "2025-07-16", "Hours": 14 }
  ]
}
```

---

## 🔁 Converted XML (after JSON to XML)

```xml
<Timesheets>
  <Entry>
    <EmployeeID>E123</EmployeeID>
    <Date>2025-07-16</Date>
    <Hours>9</Hours>
  </Entry>
  <Entry>
    <EmployeeID>E456</EmployeeID>
    <Date>2025-07-16</Date>
    <Hours>14</Hours>
  </Entry>
</Timesheets>
```

---

## 🧹 Filter Logic

* Only include records with:
  `Hours <= 12`

---

## ✏️ Metadata Enrichment via Content Modifier

* `CompanyName = ABC Corp`
* `ProcessedDate = ${date:now}`
* `Status = Valid/Invalid` (optional)

---

## 🔄 CSV Output Example (Valid Record)

```csv
E123,2025-07-16,9
```

---

## 📂 File Output Paths

* Valid Timesheets: `/hr/valid/E123_20250716.csv`
* Invalid Timesheets: `/hr/invalid/E456_20250716.csv`

---

## 📧 Exception Handling

* Any transformation or file-write error is:

  * Logged to **Data Store**
  * Alert sent via **Gmail Adapter** with employee ID and error details

---

## 🧩 Components & Artifacts

| Component         | Technology                    |
| ----------------- | ----------------------------- |
| Source            | Web App (HTTP Sender Adapter) |
| Validation        | Filter (Working hours check)  |
| Format Conversion | JSON to XML → XML to CSV      |
| Routing           | Router (Valid vs Invalid)     |
| Output            | File Receiver Adapter         |
| Enrichment        | Content Modifier              |
| Optional Alerts   | Gmail + Exception Subprocess  |

---

## ✅ Pre-requisites

* Web app configured to call SAP CPI endpoint
* Folder structure in File Receiver: `/hr/valid/`, `/hr/invalid/`
* Gmail Adapter set up for alerts
* Ongoing maintenance of employee ID formatting and data quality rules

