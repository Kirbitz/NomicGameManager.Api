# Get Rules and Amendments Data

Returns json data about the account creation.

- **URL:**

  /api/collect_rules_amendments/:gameId


- **Method:**

  `GET`


- **URL Params:**

  __Required:__ GameId
  
  `gameId: [int]`

  4321


- **Data Params:**

  None

- **Auth Required:** Yes, TBD

## Response

- **Success Response:**

  **Code:** `200 CREATED`

  **Content:**

  ```json
  {
    "success": {
      "status": 200,
      "rules": [
        {
          "ruleId": 1234,
          "index": 35,
          "title": "My Awesome Rule",
          "description": "No one can win the game",
          "amendments": [
            {
              "amendId": 2345,
              "index": 2,
              "description": "A player may only win if they blow up the DB"
            },
            {
              "...": "As many as found"
            }
          ]
        },
        {
          "...": "As many as found"
        }
      ]
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
  url: '/api/collect_rules_amendments/:gameId',
  responseType: 'json',
  data: {
    gameId: 1234
  }
}
```