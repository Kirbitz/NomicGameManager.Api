# Enacting Rules

Returns json data about the amendment creation.

- **URL:**

  /api/rules_amendments/enactAmendment

- **Method:**

  `POST`

- **URL Params:**

  None

- **Data Params:**

  __Required:__ ruleId

  `ruleId: [int]`

  1

  __Required:__ id

  `id: [int]`

  21

  __Required:__ index

  `index: [int]`

  101

  __Required:__ title

  `title: [string]`

  `player's amendment`

  __Optional:__ description

  `description: [string]`

  `this is the amendment`

- **Auth Required:** Yes, Authorization header with a Bearer JWT token.

## Response

- **Success Response:**

  **Code:** `201 CREATED`

  **Content:**

  ```json
  {
    "success": true,
    "status": 201,
    "data": "Amendment Created"
  }
  ```

- **Error Response**

  **Code:** `400 BAD REQUEST`

  **Content:**

  ```json
  {
    "success": false,
    "status": 400,
    "data": "Invalid Data"
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
    "status":500,
    "data": "Internal Server Error"
  }
  ```

## Sample Call

```json
axios({
  method: 'POST',
  url: '/api/rules_amendments/enactRule',
  responseType: 'json',
  data: {
    ruleId: 1,
    id: 205,
    index: 101,
    title: "my rule title",
    description: "no fun this game"
  }
})