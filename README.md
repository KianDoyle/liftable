# LIFTABLE

# API Documentation for ApiController

## Base URL
```
/api
```

## Description
The `ApiController` provides access to powerlifting data, including individual lifter records and regional rankings, in both CSV and JSON formats.

---

## Endpoints

### 1. Get Lifter Data (CSV)
**Endpoint:**
```
GET /api/csv/{name}
```
**Description:**
Fetches raw powerlifting competition data for a specified lifter in CSV format.

**Path Parameters:**
- `name` (String) – The name of the lifter whose data is requested.

**Responses:**
- `200 OK` – Returns a CSV string containing the lifter's powerlifting data.
- `400 Bad Request` – Invalid or missing lifter name.
- `500 Internal Server Error` – An error occurred while processing the request.

**Response Example:**
```csv
Name,Sex,Event,Equipment,Age,AgeClass,BirthYearClass,Division,BodyweightKg,WeightClassKg,Squat1Kg,Squat2Kg,Squat3Kg,Squat4Kg,Best3SquatKg,Bench1Kg,Bench2Kg,Bench3Kg,Bench4Kg,Best3BenchKg,Deadlift1Kg,Deadlift2Kg,Deadlift3Kg,Deadlift4Kg,Best3DeadliftKg,TotalKg,Place,Dots,Wilks,Glossbrenner,Goodlift,Tested,Country,State,Federation,ParentFederation,Date,MeetCountry,MeetState,MeetTown,MeetName,Sanctioned Kian Doyle,M,B,Raw,,,,MR-Jr,100.7,105,,,,,,167.5,175,180,,180,,,,,,180,2,110.46,109.24,104.33,82.19,Yes,England,YNE,BP,IPF,2025-02-16,UK,,Cardiff,British Classic Bench Press Championships,Yes
```
---

### 2. Get Lifter Data (JSON)
**Endpoint:**
```
GET /api/json/lifter/{name}
```
**Description:**
Retrieves structured JSON data for a specified lifter.

**Path Parameters:**
- `name` (String) – The name of the lifter whose data is requested.

**Responses:**
- `200 OK` – Returns JSON object containing the lifter's powerlifting statistics.
- `400 Bad Request` – Invalid or missing lifter name.
- `500 Internal Server Error` – An error occurred while processing the request.

**Response Example:**
```json
[
  {
    "Name": "Kian Doyle",
    "Sex": "M",
    "Event": "B",
    "Equipment": "Raw",
    "Age": "",
    "AgeClass": "",
    "BirthYearClass": "",
    "Division": "MR-Jr",
    "BodyweightKg": "100.7",
    "WeightClassKg": "105",
    "Squat1Kg": "",
    "Squat2Kg": "",
    "Squat3Kg": "",
    "Squat4Kg": "",
    "Best3SquatKg": "",
    "Bench1Kg": "167.5",
    "Bench2Kg": "175",
    "Bench3Kg": "180",
    "Bench4Kg": "",
    "Best3BenchKg": "180",
    "Deadlift1Kg": "",
    "Deadlift2Kg": "",
    "Deadlift3Kg": "",
    "Deadlift4Kg": "",
    "Best3DeadliftKg": "",
    "TotalKg": "180",
    "Place": "2",
    "Dots": "110.46",
    "Wilks": "109.24",
    "Glossbrenner": "104.33",
    "Goodlift": "82.19",
    "Tested": "Yes",
    "Country": "England",
    "State": "YNE",
    "Federation": "BP",
    "ParentFederation": "IPF",
    "Date": "2025-02-16",
    "MeetCountry": "UK",
    "MeetState": "",
    "MeetTown": "Cardiff",
    "MeetName": "British Classic Bench Press Championships",
    "Sanctioned": "Yes"
  }
]
```

---

### 3. Get Regional Rankings (JSON)
**Endpoint:**
```
GET /api/json/region/{region}
```
**Description:**
Retrieves top 10 powerlifting rankings for a specified region - denoted by lowercase Continent 2-letter-abbreviation - in JSON format from 1964-Current.

**Path Parameters:**
- `region` (String) – The continent whose rankings are requested.

**Responses:**
- `200 OK` – Returns JSON object containing ranked lifters in the specified region.
- `400 Bad Request` – Invalid or missing region name.
- `500 Internal Server Error` – An error occurred while processing the request.

**Response Example:**
```json
{
  "eu": [
    [
      0,
      1,
      "Ade Omisakin",
      "adeomisakin",
      "a.omisakin",
      null,
      "England",
      null,
      "EPF",
      "2024-09-13",
      "Malta",
      null,
      "epf/2408",
      "M",
      "Classic",
      "25~",
      "Open",
      "82.7",
      "83",
      "305.5",
      "192.5",
      "363",
      "861",
      "119.37"
    ]
  ]
}
```
