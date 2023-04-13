# Create New Account

Returns json data about the game creation.

- **URL:**

  /api/game/create

- **Method:**

  `POST`

- **URL Params:**

  None

- **Data Params:**

  **Required:** Title

  `title: [string]`

  `New Nomic Game`

  **Required:** User ID - ID from user to get the user

  `userId: [int]`

  2

- **Auth Required:** Yes, Authorization header with a Bearer JWT token.

## Response

- **Success Response:**

  **Code:** `201 CREATED`

  **Content:**

  ```json
  {
    "success": true,
    "status": 201,
    "data": "Game Created"
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
axios({
  method: "POST",
  url: "/api/game/create",
  responseType: "json",
  data: {
    title: "New Nomic Game",
    userId: 2,
  },
});
```
