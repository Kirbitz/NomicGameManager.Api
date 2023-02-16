# Create New Account

Returns json data about the game creation.

- **URL:**

  /api/create_game

- **Method:**

  `POST`

- **URL Params:**

  None

- **Data Params:**

  **Required:** Title

  `title: [string]`

  `New Nomic Game`

  **Required:** Create Date

  `createDate: [date]`

  `2023-02-09`

  **Required:** User ID - ID from user to get the user

  `userId: [int]`

  2

- **Auth Required:** Yes, JWT set in Authorization header.

## Response

- **Success Response:**

  **Code:** `201 CREATED`

  **Content:**

  ```json
  {
    "success": {
      "status": 201,
      "message": "Created new game"
    }
  }
  ```

- **Error Response**

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

```javascript
axios({
  method: "POST",
  url: "/api/create_game",
  responseType: "json",
  data: {
    title: "New Nomic Game",
    createDate: 2023 - 02 - 09,
    userId: 2,
  },
});
```