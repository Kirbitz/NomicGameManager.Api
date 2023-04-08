# Repeal Rules

Changes the active flag of a specified amendment

- **URL:**

  /api/rules_amendments/repeal_amendment/{amendId}


- **Method:**

  `GET`


- **URL Params:**

  __Required:__ AmendId

  `amendmId: [int]`

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
    "success": true,
    "status": 200,
    "data": "Amendment Repealed"
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
  url: '/api/rules_amendments/repeal_amendment/{amendmentId}',
  responseType: 'json'
}
```
