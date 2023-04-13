# Get Rules and Amendments Data

Returns json data of rules and amendments connected to a specific game.

- **URL:**

  /api/rules_amendments/collect/{gameId}


- **Method:**

  `GET`


- **URL Params:**

  __Required:__ GameId
  
  `gameId: [int]`

  4321


- **Data Params:**

  None

- **Auth Required:** Yes, Authorization header with a Bearer JWT token.

## Response

- **Success Response:**

  **Code:** `200 CREATED`

  **Content:**

  ```json
  {
    "success": true,
    "status": 200,
    "data": [
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
  url: '/api/rules_amendments/collect/{gameId}',
  responseType: 'json'
}
```
