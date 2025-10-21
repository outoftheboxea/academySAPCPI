# 📘 SAP CPI Integration – Customer Feedback Archival

## 📌 Overview

This integration flow handles the **archival of daily customer feedback data** collected from a web application. The feedback is sent in **batch XML format** and needs to be:

1. **Split** into individual feedback entries
2. **Filtered** for valid comments
3. **Enriched** with metadata
4. **Converted to JSON**
5. **Stored as individual files** for downstream analytics or sentiment analysis

---

## 🏢 Business Use Case

The company collects **customer feedback** daily from a survey system. The data is sent as a single XML file and needs to be processed such that:

* Only valid entries (non-empty comments) are retained
* Each feedback is enriched with metadata (e.g., company name, processing timestamp)
* Entries are converted to JSON format for BI tools
* Each valid feedback is saved as a **separate file** in an archive directory

---

## 🔁 Integration Flow

### Steps

1. **File Sender Adapter** picks up the batch feedback XML file
2. **XML Splitter** breaks it into individual `<Entry>` elements
3. **Filter** retains only entries with non-empty `<Comment>`
4. **Content Modifier** adds metadata like company name, timestamp
5. **XML to JSON Converter** converts valid entries to JSON
6. **File Receiver Adapter** stores each JSON as a file

---

## 🧾 Sample Input XML (Batch Feedback File)

```xml
<Feedbacks>
  <Entry>
    <CustomerID>C001</CustomerID>
    <Rating>5</Rating>
    <Comment>Excellent service</Comment>
  </Entry>
  <Entry>
    <CustomerID>C002</CustomerID>
    <Rating>3</Rating>
    <Comment></Comment>
  </Entry>
  <Entry>
    <CustomerID>C003</CustomerID>
    <Rating>4</Rating>
    <Comment>Very satisfied</Comment>
  </Entry>
</Feedbacks>
```

---

## 🔁 After Split + Filter (Valid Entries Only)

```xml
<Entry>
  <CustomerID>C001</CustomerID>
  <Rating>5</Rating>
  <Comment>Excellent service</Comment>
</Entry>

<Entry>
  <CustomerID>C003</CustomerID>
  <Rating>4</Rating>
  <Comment>Very satisfied</Comment>
</Entry>
```

---

## ✏️ Metadata Enrichment via Content Modifier

* Header/Property Examples:

  * `CompanyName = XYZ Ltd`
  * `ProcessedDate = ${date:now:yyyy-MM-dd'T'HH:mm:ss}`

---

## 🔄 JSON Output Example

```json
{
  "Entry": {
    "CustomerID": "C001",
    "Rating": 5,
    "Comment": "Excellent service"
  }
}
```

---

## 📂 Output File Path (via File Receiver)

* File location: `/feedback_archive/`
* File naming convention: `C001_20251021T101500.json`

---

## 📧 Exception Handling (Optional)

* Catch transformation or file-write errors using **Exception Subprocess**
* Log to **Data Store** and send alert via **Gmail Adapter**

---

## 🧩 Components & Artifacts

| Component          | Technology                 |
| ------------------ | -------------------------- |
| Source             | Survey XML batch (File)    |
| File Handling      | File Sender & Receiver     |
| Message Processing | XML Splitter, Filter       |
| Enrichment         | Content Modifier           |
| Format Conversion  | XML to JSON Converter      |
| Optional Alerts    | Gmail Adapter + Data Store |

---

## ✅ Pre-requisites

* Survey system configured to drop XML feedback files
* Target directory created on File Receiver (e.g., SFTP)
* File write permissions granted
* (Optional) Gmail Adapter configured for failure alerts

