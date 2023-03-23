# Login

Returns a JWT access token that can be used to authenticate on other endpoints

- **URL:**

  /api/auth/token


- **Method:**

  `POST`


- **Header Params:**

  __Required:__  *Either*
  
  Authorization Basic Header

  `Authorization: Basic [credentials]`

  `credentials`: Base 64 encoded `username:password`

  *or*

  Authorization JWT Bearer Header

  `Authorization: Bearer [token]`

  `token`: JWT access token


- **URL Params:**

  None


- **Data Params:**

  None

- **Auth Required:** None

## Response

- **Success Response:**

  **Code:** `200 CREATED`

  **Content:**

  ```json
  {
    "isSuccess": true,
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoiTm9taWNHYW1lTWFuYWdlci5BcGkiLCJleHAiOjE2NzcyODE4NjEsImlhdCI6MTY3NzI3NDY2MX0.vewwb46Ac1q_QNp3cdJ4Ot2n8PLsIl-YzhSMYi4N97P4AhFeTNg5L4ldocxiWNm2mk1q_neekP0iBnHS44w_E95st3PKKKWqavG9cc3Xgbq8O4pHU2fvarYmChfQUAA2V-XM9a7m11nvnYhkBVXdAetk_q2r1dJbBnMZv2iApChxQ5w6WvgtCOnQDLniOm-fzTLRyf9-DgzBqDn28CWR3m-PsmYcZ2omUqzAS8z10Qbmbrej3wSd6tAAcAK0Fruw4BzpAQ8vaVbWMCmNwNiOL6Ozxm-XmjsCAjU6bbbuE2nTWdq2STsu3diZMK5onjuD-ZQnwzs3y5v-wlXMHp5A4g"
  }
  ```

- **Error Response**
  
  **Code:** `403 UNAUTHENTICATED`
  
  **Content:**

  None

  **Code:** `500 INTERNAL SERVER ERROR`

  **Content:**

  ```json
  {
    "timestamp": "2023-02-24T21:29:15.137+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "path": "/api/auth/token"
  }
  ```

## Sample Call

```javascript
{
  method: 'POST',
  url: '/api/auth/token',
  responseType: 'json',
  header: {
    Authorization: Basic VGVzdFVzZXI6cGFzc3dvcmQ=
  }
}
```

or

```javascript
{
  method: 'POST',
  url: '/api/auth/token',
  responseType: 'json',
  header: {
    Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoiTm9taWNHYW1lTWFuYWdlci5BcGkiLCJleHAiOjE2NzcyODE4NjEsImlhdCI6MTY3NzI3NDY2MX0.vewwb46Ac1q_QNp3cdJ4Ot2n8PLsIl-YzhSMYi4N97P4AhFeTNg5L4ldocxiWNm2mk1q_neekP0iBnHS44w_E95st3PKKKWqavG9cc3Xgbq8O4pHU2fvarYmChfQUAA2V-XM9a7m11nvnYhkBVXdAetk_q2r1dJbBnMZv2iApChxQ5w6WvgtCOnQDLniOm-fzTLRyf9-DgzBqDn28CWR3m-PsmYcZ2omUqzAS8z10Qbmbrej3wSd6tAAcAK0Fruw4BzpAQ8vaVbWMCmNwNiOL6Ozxm-XmjsCAjU6bbbuE2nTWdq2STsu3diZMK5onjuD-ZQnwzs3y5v-wlXMHp5A4g
  }
}
```