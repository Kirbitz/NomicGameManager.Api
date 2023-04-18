# List games

Returns json data about the list of games that a user has created.

- **URL:**

  /api/game/list

- **Method:**

  `GET`

- **URL Params:**

  None

- **Data Params:**

  **Required:** Size

  `size: [unsigned int]` - Must be between 1 and 100

  50

  **Optional:** Offset - how many games to skip

  `offset: [unsigned int]`

  0

- **Auth Required:** Yes, Authorization header with a Bearer JWT token.

## Response

- **Success Response:**

  **Code:** `200 OK`

  **Content:**

  ```json
  {
    "success": true,
    "status": "OK",
    "data": [
        {
            "gameId": 1,
            "title": "The first Game",
            "createDate": "2023-04-18",
            "currentPlayer": null,
            "userId": 1
        },
        {
            "gameId": 2,
            "title": "The Second Game",
            "createDate": "2023-04-18",
            "currentPlayer": 2,
            "userId": 1
        }
    ]
  }
  ```

- **Error Response**

  **Code:** `401 UNAUTHORIZED`

  **Content:**

  ```json
  {
    "success": false,
    "status": 401,
    "data": "Unauthorized"
  }
  ```

  **Code:** `400 BAD REQUEST`

  If `size` is greater than 100, or if `size` is less than 1, the following error is returned.

  **Content:**

    ```json
    {
      "success": false,
      "status": "BAD_REQUEST",
      "data": "You must request at least 1 game and at most 100 games."
    }
    ```

  **Code:** `404 NOT FOUND`

  If `offset` exceeds the possible size of the page, the following error is returned.

  **Content:**

    ```json
    {
      "success": false,
      "status": "NOT_FOUND",
      "data": "Invalid offset"
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
  url: '/api/game/list',
  responseType: 'json'
}
```
