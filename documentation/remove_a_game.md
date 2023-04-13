# Remove A Game

Returns json data of the removal of a game.

- **URL:**

  /api/game/remove/{gameId}


- **Method:**

  `DELETE`


- **URL Params:**

  __Required:__ GameId

  `gameId: [int]`

  4321


- **Data Params:**

  None

- **Auth Required:** Yes, Authorization header with a Bearer JWT token.

## Response

- **Success Response:**

  **Code:** `202 ACCEPTED`

  **Content:**

  ```json
  {
    "success": true,
    "status": 202,
    "data": "Game Deleted"
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

  **Code:** `404 NOT FOUND`
  
  **Content:**
  
  ```json
  {
    "success": false,
    "status": 404,
    "data": "Game Not Found"
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
  method: 'DELETE',
  url: '/api/game/remove/{gameId}',
  responseType: 'json'
}
```
