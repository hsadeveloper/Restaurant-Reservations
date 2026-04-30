# Restaurant-Reservations

![Alt text describing image](UML_1.png)

---
# Make A Reservation (Order)

**`POST`** `http://localhost:7083/api/reservations`

---

### Sample Request

```json
{
  "date": "2026-04-19",
  "time": "13:00",
  "partySize": 4,
  "customerId": "c-126"
}
```

---

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| **Request Body** | | | |
| `date` | `string` | Yes | Reservation date in `YYYY-MM-DD` format. e.g. `"2026-04-19"` |
| `time` | `string` | Yes | Time in `HH:MM` 24-hour format. Must be at least 30 minutes in the future. e.g. `"13:00"` |
| `partySize` | `integer` | Yes | Number of guests. Must be a positive integer. Maximum allowed party size per table is `6`. e.g. `4` |
| `customerId` | `string` | Yes | ID of the customer making the reservation. e.g. `"c-126"` |
| **Response — 201 Created** | | | |
| `id` | `string` | Response | Unique identifier for the reservation. e.g. `"2"` |
| `status` | `string` | Response | Reservation status. Always `PENDING` on creation. |
| `expiresAt` | `string` | Response | ISO 8601 expiry datetime. e.g. `"2026-04-26T11:15:00"` |
| `_links.confirm.href` | `string` | Response | URL to confirm the reservation. e.g. `"/api/reservations/2/confirm"` |
| `_links.confirm.method` | `string` | Response | HTTP method for the confirm URL. Always `"POST"` |
| **Errors** | | | |
| `400` | `Bad Request` | Error | Duplicate reservation detected for this customer, date, and time. |
| `400` | `Bad Request` | Error | Maximum allowed party size per table is `6`. |

---

### Sample Response

```json
{
  "id": "2",
  "status": "PENDING",
  "expiresAt": "2026-04-26T11:15:00",
  "_links": {
    "confirm": {
      "href": "/api/reservations/2/confirm",
      "method": "POST"
    }
  }
}
```
