# Get Rules and Amendments Data

Returns json data of the removal of a game.

- **URL:**

  /api/games/remove/{gameId}


- **Method:**

  `DELETE`


- **URL Params:**

  __Required:__ GameId

  `gameId: [int]`

  4321


- **Data Params:**

  None

- **Auth Required:** Yes, TBD

## Response

- **Success Response:**

  **Code:** `202 ACCEPTED`

  **Content:**

  ```json
  {
    "success": {
      "status": 202,
      "message": "Game Deleted"
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

    **Code:** `404 NOT FOUND`
    
    **Content:**
    
      ```json
      {
        "error": {
          "status": 404,
          "message": "Game Not Found"
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
  method: 'DELETE',
  url: '/api/games/remove/{gameId}',
  responseType: 'json',
  data: {
    gameId: 1234
  }
}
```
