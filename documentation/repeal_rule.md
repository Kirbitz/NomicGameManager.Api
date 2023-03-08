# Repeal Rules

Changes the active flag of a specified rule and any amendments that depend on it

- **URL:**

  /api/rules_amendments/repeal_rule/{ruleId}


- **Method:**

  `GET`


- **URL Params:**

  __Required:__ RuleId

  `ruleId: [int]`

  4321


- **Data Params:**

  None

- **Auth Required:** Yes, TBD

## Response

- **Success Response:**

  **Code:** `200 SUCCESS`

  **Content:**

  ```json
  {
        "success": "True",
        "message": "Updated Successfully",
        "ruleId": "Whichever Id was Submitted"
  }
  ```

- **Error Response**

  **Code:** `400 BAD REQUEST`

  **Content:**

  ```json
  {
    "error": {
      "status": 400,
      "message": "Bad Request"
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
  **Code:** `404 Not Found`

  **Content:**

  ```json
  {
    "error": {
      "status": 404,
      "message": "The entity with id 'ID' was not found on the database."
    }
  }
  ```

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

```javascript
{
  method: 'GET',
  url: '/api/rules_amendments/repeal_rule/{ruleId}',
  responseType: 'json',
  data: {
    gameId: 1234
  }
}
```
