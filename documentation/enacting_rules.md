# Enacting Rules

Returns json data about the account creation.

- **URL:**

  /api/rules_amendments/enactRule

- **Method:**

  `POST`

- **URL Params:**

  None

- **Data Params:**

  __Required:__ ruleId

  `ruleId: [int]`

  1

  __Required:__ index

  `index: [int]`

  101

  __Required:__ description

  `description: [string]`

  `this is the rule`

  __Required:__ title

  `title: [string]`

  `player's rule`

  __Required:__ gameId

  `gameId: [int]`

  1

- __Required:__ active

  `active: [int]`

  1

- __Required:__ mutable

  `mutable: [int]`

  1

- **Auth Required:** Yes, JWT set in Authorization header.

## Response

- **Success Response:**

  **Code:** `201 CREATED`

  **Content:**

  ```json
  {
    "success": true,
    "status": 201,
    "data": "Rule Created"
  }
  ```

- **Error Response**

  **Code:** `400 BAD REQUEST`

  **Content:**

  ```json
  {
    "success": false,
    "status": 400,
    "data": "Invalid Data"
  }
  ```

**Code:** `401 UNAUTHORIZED`

**Content:**

  ```json
  {
    "success": false,
    "status": 401,
    "data": "Unauthorized"
  }
  ```

**Code:** `429 TOO MANY REQUESTS`

**Content:**

None

**Code:** `500 INTERNAL SERVER ERROR`

**Content:**

  ```json
  {
    "success": false,
    "status":500,
    "data": "Internal Server Error"
  }
  ```

## Sample Call

```json
axios({
  method: 'POST',
  url: '/api/rules_amendments/enactRule',
  responseType: 'json',
  data: {
    ruleId: 1,
    index: 101,
    description: "no fun this game",
    title: "my rule title",
    gameId: 20,
    active: 1,
    mutable: 1,
  }
})