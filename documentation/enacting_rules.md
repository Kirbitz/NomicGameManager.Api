# Enacting Rules

Returns json data about the account creation.

- **URL:**

  /api/enacting_rules

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
    "success": {
      "status": 201,
      "message": "Rule Enacted"
    }
  }
  ```

- **Error Response**

  **Code:** `400 BAD REQUEST`

  **Content:**

  ```json
  {
    "error": {
      "status": 400,
      "message": "Bad Request - Non-Unique Fields",
      "fields": ["field1", "field2"]
    }
  }
  ```


**Code:** `401 UNAUTHORIZED`




**Content:**

  ```json
  {
    "error": {
      "status": 401,
      "message": "Unauthorized"
    }
  }
  ```

**Code:** `429 TOO MANY REQUESTS`

**Content:**

None

**Code:** `500 INTERNAL SERVER ERROR`

**Content:**

  ```json
  {
    "error": {
      "status": 500,
      "message": "Internal Server Error"
    }
  }
  ```

## Sample Call

```json
axios({
  method: 'POST',
  url: '/api/enacting_rules',
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