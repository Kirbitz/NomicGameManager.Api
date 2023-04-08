# Repeal Rules

Changes the mutable flag of a specified rule

- **URL:**

  /api/rules_amendments/transmute_rule/{ruleId}


- **Method:**

  `POST`


- **URL Params:**

  __Required:__ RuleId

  `ruleId: [int]`

  4321


- **Data Params:**

  __Required:__ mutableInput

  `mutableInput: [bool]`

  false

- **Auth Required:** Yes, TBD

## Response

- **Success Response:**

  **Code:** `200 SUCCESS`

  **Content:**

  ```json
  {
    "success": true,
    "status": 200,
    "data": "Rule Transmuted"
  }
  ```

- **Error Response**

  **Code:** `400 BAD REQUEST`

  **Content:**

  ```json
  {
    "success": false,
    "status": 400,
    "data": "Bad Request"
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
  **Code:** `404 Not Found`

  **Content:**

  ```json
  {
    "success": false,
    "status": 404,
    "data": "The entity with id 'ID' was not found on the database."
  }
  ```

  **Code:** `500 INTERNAL SERVER ERROR`

  **Content:**

  ```json
  {
    "success": false,
    "status": 500,
    "data": "Internal Server Error"
  }
  ```

## Sample Call

```javascript
{
  method: 'GET',
  url: '/api/rules_amendments/transmute_rule/{ruleId}',
  responseType: 'json',
  data: {
    mutableInput: false
  }
}
```
