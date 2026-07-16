# Sales Tracker — API Testing & UI Customization Guide

> Base URL for all API calls: `http://localhost:8080`

---

## Table of Contents

1. [Prerequisites](#1-prerequisites)
2. [Sales Endpoints](#2-sales-endpoints)
   - [GET all sales](#21-get-all-sales)
   - [GET sale by ID](#22-get-sale-by-id)
   - [POST create a sale](#23-post-create-a-sale)
   - [PUT update a sale](#24-put-update-a-sale)
   - [DELETE a sale](#25-delete-a-sale)
3. [User Type Endpoints](#3-user-type-endpoints)
   - [GET all user types](#31-get-all-user-types)
   - [GET user type by ID](#32-get-user-type-by-id)
4. [UI Color Customization Examples](#4-ui-color-customization-examples)

---

## 1. Prerequisites

- Spring Boot backend running on port `8080`
- For **curl**: any terminal (Git Bash, WSL, macOS/Linux terminal)
- For **Postman**: download free at https://www.postman.com/downloads/
- Set this header on every POST/PUT request:
  ```
  Content-Type: application/json
  ```

---

## 2. Sales Endpoints

### 2.1 GET all sales

Fetches every sale record in the database.

**curl**
```bash
curl http://localhost:8080/api/sales
```

**Postman**
| Field  | Value                            |
|--------|----------------------------------|
| Method | GET                              |
| URL    | `http://localhost:8080/api/sales` |
| Body   | none                             |

**Expected response**
```json
[
  {
    "id": 1,
    "customerName": "Alice Johnson",
    "product": "Widget Pro",
    "amount": 1500.00,
    "saleDate": "2024-01-15",
    "status": "Closed",
    "userType": {
      "id": 1,
      "typeName": "Inside Sales",
      "phoneNumber": "555-100-0001"
    }
  },
  {
    "id": 2,
    "customerName": "Bob Smith",
    "product": "Gadget Plus",
    "amount": 3200.50,
    "saleDate": "2024-02-20",
    "status": "Open",
    "userType": {
      "id": 2,
      "typeName": "Field Sales",
      "phoneNumber": "555-100-0002"
    }
  }
]
```

---

### 2.2 GET sale by ID

Fetches a single sale by its `id`.

**curl**
```bash
curl http://localhost:8080/api/sales/1
```

**Postman**
| Field  | Value                              |
|--------|------------------------------------|
| Method | GET                                |
| URL    | `http://localhost:8080/api/sales/1` |
| Body   | none                               |

**Expected response** — same shape as one object from the list above.

**If not found:** HTTP `404 Not Found` with an empty body.

---

### 2.3 POST create a sale

Creates a new sale record.

#### Example A — with a user type assigned

**curl**
```bash
curl -X POST http://localhost:8080/api/sales \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Dan Rivera",
    "product": "Turbo Widget",
    "amount": 4750.00,
    "saleDate": "2024-07-16",
    "status": "Open",
    "userType": { "id": 2 }
  }'
```

**JSON body**
```json
{
  "customerName": "Dan Rivera",
  "product": "Turbo Widget",
  "amount": 4750.00,
  "saleDate": "2024-07-16",
  "status": "Open",
  "userType": { "id": 2 }
}
```

> `"userType": { "id": 2 }` links this sale to "Field Sales". You only need the `id`.

#### Example B — without a user type

```json
{
  "customerName": "Sara Lee",
  "product": "Basic Kit",
  "amount": 199.99,
  "saleDate": "2024-07-16",
  "status": "Pending"
}
```

**Postman**
| Field   | Value                             |
|---------|-----------------------------------|
| Method  | POST                              |
| URL     | `http://localhost:8080/api/sales` |
| Headers | `Content-Type: application/json`  |
| Body    | raw → JSON → paste body above     |

**Expected response** — the newly created sale object with its generated `id`:
```json
{
  "id": 4,
  "customerName": "Dan Rivera",
  "product": "Turbo Widget",
  "amount": 4750.00,
  "saleDate": "2024-07-16",
  "status": "Open",
  "userType": {
    "id": 2,
    "typeName": "Field Sales",
    "phoneNumber": "555-100-0002"
  }
}
```

---

### 2.4 PUT update a sale

Updates an existing sale. You must include **all fields** (not just the ones changing).

#### Example — change status and reassign user type

**curl**
```bash
curl -X PUT http://localhost:8080/api/sales/1 \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Alice Johnson",
    "product": "Widget Pro",
    "amount": 1500.00,
    "saleDate": "2024-01-15",
    "status": "Closed",
    "userType": { "id": 3 }
  }'
```

**JSON body**
```json
{
  "customerName": "Alice Johnson",
  "product": "Widget Pro",
  "amount": 1500.00,
  "saleDate": "2024-01-15",
  "status": "Closed",
  "userType": { "id": 3 }
}
```

#### Example — remove the user type (set to none)

```json
{
  "customerName": "Alice Johnson",
  "product": "Widget Pro",
  "amount": 1500.00,
  "saleDate": "2024-01-15",
  "status": "Closed"
}
```

**Postman**
| Field   | Value                               |
|---------|-------------------------------------|
| Method  | PUT                                 |
| URL     | `http://localhost:8080/api/sales/1` |
| Headers | `Content-Type: application/json`    |
| Body    | raw → JSON → paste body above       |

**Expected response** — the full updated sale object.

**If not found:** HTTP `404 Not Found`.

---

### 2.5 DELETE a sale

Deletes the sale with the given `id`.

**curl**
```bash
curl -X DELETE http://localhost:8080/api/sales/1
```

**Postman**
| Field  | Value                               |
|--------|-------------------------------------|
| Method | DELETE                              |
| URL    | `http://localhost:8080/api/sales/1` |
| Body   | none                                |

**Expected response:** HTTP `204 No Content` (empty body — this is correct, it means success).

---

## 3. User Type Endpoints

### 3.1 GET all user types

Use this to populate a dropdown or verify what reference data exists.

**curl**
```bash
curl http://localhost:8080/api/user-types
```

**Postman**
| Field  | Value                                  |
|--------|----------------------------------------|
| Method | GET                                    |
| URL    | `http://localhost:8080/api/user-types` |
| Body   | none                                   |

**Expected response**
```json
[
  { "id": 1, "typeName": "Inside Sales",    "phoneNumber": "555-100-0001" },
  { "id": 2, "typeName": "Field Sales",     "phoneNumber": "555-100-0002" },
  { "id": 3, "typeName": "Account Manager", "phoneNumber": "555-100-0003" }
]
```

---

### 3.2 GET user type by ID

**curl**
```bash
curl http://localhost:8080/api/user-types/2
```

**Postman**
| Field  | Value                                    |
|--------|------------------------------------------|
| Method | GET                                      |
| URL    | `http://localhost:8080/api/user-types/2` |
| Body   | none                                     |

**Expected response**
```json
{ "id": 2, "typeName": "Field Sales", "phoneNumber": "555-100-0002" }
```

---

## 4. UI Color Customization Examples

All styles live in two places:
- **Global styles** → `frontend/src/styles.css` (affects the whole app)
- **Inline styles** → inside each component's `template` string

---

### 4.1 Change the table header background color

**File:** `frontend/src/styles.css`

```css
/* Current — blue */
th {
  background: #1976d2;
  color: white;
}

/* Change to dark green */
th {
  background: #2e7d32;
  color: white;
}

/* Change to dark grey */
th {
  background: #424242;
  color: white;
}
```

---

### 4.2 Color-code the Status cell (Open / Pending / Closed)

This is the most useful change — it makes statuses instantly readable.

**File:** `frontend/src/app/sale-list/sale-list.component.ts`

Find this line in the template:
```html
<td>{{ sale.status }}</td>
```

Replace it with:
```html
<td>
  <span [ngStyle]="{
    'background': sale.status === 'Open' ? '#e8f5e9' :
                  sale.status === 'Pending' ? '#fff3e0' : '#ffebee',
    'color':      sale.status === 'Open' ? '#2e7d32' :
                  sale.status === 'Pending' ? '#e65100' : '#c62828',
    'padding': '3px 10px',
    'border-radius': '12px',
    'font-weight': '600',
    'font-size': '13px'
  }">
    {{ sale.status }}
  </span>
</td>
```

Result:
- `Open` → green badge
- `Pending` → orange badge
- `Closed` → red badge

---

### 4.3 Change button colors

**File:** `frontend/src/styles.css`

```css
/* Primary button — currently blue #1976d2 */
button.primary {
  background: #6a1b9a;   /* purple */
  color: white;
}

/* Danger button — currently red #d32f2f */
button.danger {
  background: #e65100;   /* deep orange */
  color: white;
}

/* Secondary / Cancel button — currently grey */
button.secondary {
  background: #37474f;   /* dark blue-grey */
  color: white;
}
```

---

### 4.4 Change the form card background and page background

**File:** `frontend/src/styles.css` — for the page background:
```css
body {
  background: #e8eaf6;  /* light indigo instead of grey */
}
```

**File:** `frontend/src/app/sale-form/sale-form.component.ts` — find the inline style on `<form>`:

```html
<!-- Current -->
<form ... style="...background:white;...">

<!-- Change form card to a warm off-white -->
<form ... style="...background:#fffde7;...">

<!-- Or light blue -->
<form ... style="...background:#e3f2fd;...">
```

---

### Quick color reference

| Color name     | Hex       | Good for           |
|----------------|-----------|--------------------|
| Blue (current) | `#1976d2` | Headers, primary   |
| Green          | `#2e7d32` | Success, Open      |
| Orange         | `#e65100` | Warning, Pending   |
| Red            | `#c62828` | Danger, Closed     |
| Purple         | `#6a1b9a` | Accent             |
| Dark grey      | `#424242` | Neutral headers    |
| Light indigo   | `#e8eaf6` | Page background    |
