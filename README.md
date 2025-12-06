# **🚀 Advanced Validation API**

### **Email • Phone • IP • ZIP • Bulk Validation • API Key Auth • Rate Limiting**

A production-grade validation API built with **Spring Boot**, featuring:

✔ Email validation (syntax, MX, disposable, score)

✔ Phone validation (country, formatting, line type)

✔ IP validation (IPv4/IPv6, private/public)

✔ ZIP code validation (US ZIP metadata)

✔ Bulk validation

✔ API Key Authentication

✔ Global Exception Handling

✔ Built-in Rate Limiting

✔ Swagger UI documentation

---

# **🆚 Competitor Comparison — Why Choose Our API?**

This is a polished SaaS-style comparison that RapidAPI customers LOVE.

| **Feature / API** | **Our API** | **AbstractAPI** | **APILayer / NumVerify** | **IP2Location** | **ZipCodeAPI** |
| --- | --- | --- | --- | --- | --- |
| **Email Validation** | ✔ Syntax, MX, disposable, scoring | ✔ | ✔ | ✖ | ✖ |
| **Phone Validation** | ✔ Country auto-detect, line type, formats | ✔ (paid) | ✔ (paid) | ✖ | ✖ |
| **IP Validation** | ✔ IPv4/IPv6, private/public, reserved ranges | ✔ | ✔ | ✔ Geo but not validation | ✖ |
| **ZIP Code Validation** | ✔ State lookup + format validation | ✖ | ✖ | ✖ | ✔ (ZIP only) |
| **Bulk Validation** | ✔ Yes, Email + Phone + IP + ZIP together | Limited | Limited | ✖ | ✖ |
| **Unified API Request** | ✔ All 4 validations in 1 call | ✖ | ✖ | ✖ | ✖ |
| **API Key Security** | ✔ Strong header-based API Key | ✔ | ✔ | ✔ | ✔ |
| **Rate Limiting Included** | ✔ Yes, customizable tiers | Depends | Depends | Depends | Depends |
| **Error Normalization** | ✔ Consistent structured responses | ✖ | ✖ | ✖ | ✖ |
| **Swagger UI** | ✔ Full interactive docs | Partial | ✖ | ✖ | ✖ |
| **Free Tier** | ✔ 500 free monthly requests | Very low | Low | none | none |
| **Pricing** | 💰 Extremely Affordable | $$$ | $$ | $$ | $$ |
| **Ease of Use** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐ | ⭐⭐ |

---

# **⭐ Why Our API Looks Strong?**

### **🧩 1. A single unified validator**

No other API validates **Email + Phone + IP + ZIP** in one request.

### **🚀 2. Bulk validation across all fields**

Competitors give bulk email OR bulk phone — NOT all fields together.

### **👨‍💻 3. Developer-friendly response model**

Your output is **clean, normalized, predictable**:

```
{
  "success": true,
  "data": { ... }
}
```

Most APIs return inconsistent formats.

### **💸 4. Best free tier on RapidAPI**

500 free requests → more than most well-known vendors.

### **🛡 5. Strong security & rate limiting built-in**

Even top APIs lack structured rate-limit handling responses.

### **⭐ 6. Swagger UI available**

Most paid APIs don’t give interactive test UI.

### **👑 7. You provide ZIP + IP + Phone + Email in one product**

This becomes an **all-in-one identity quality API**, which is rare and sells well.

---

## **📌 Base URL**

```
https://your-domain.com/api/v1/validate
```

---

# **🔑 Authentication (API Key Required)**

Every request must include:

```
X-API-KEY: YOUR_API_KEY_HERE
```

Swagger & /health endpoints do not require a key.

---

# **📘 Swagger Documentation**

Open the full API docs:

```
https://your-domain.com/swagger-ui/index.html
```

---

# **🩺 Health Check Endpoints**

```
GET /health
GET /health/status
```

---

---

# **📬 1. Full Validation (Email + Phone + IP + ZIP)**

### **POST /api/v1/validate**

### **📝 Request**

```
{
  "email": "test@example.com",
  "phone": "+14155552671",
  "country": "US",
  "ip": "8.8.8.8",
  "zipcode": "94016"
}
```

### **✅ Response**

```
{
  "success": true,
  "data": {
    "email": { "syntaxValid": true, "hasMxRecord": true },
    "phone": { "valid": true, "country": "US" },
    "ip": { "valid": true, "type": "public" },
    "zipcode": { "valid": true, "state": "California" }
  }
}
```

---

# **📧 2. Quick Email Validation**

### **GET /api/v1/validate/email?email=your@email.com**

### **✅ Response**

```
{
  "success": true,
  "data": {
    "syntaxValid": true,
    "hasMxRecord": true,
    "disposable": false
  }
}
```

---

# **📱 3. Quick Phone Validation**

### **GET /api/v1/validate/phone?phone=9876543210&country=IN**

### **✅ Response**

```
{
  "success": true,
  "data": {
    "valid": true,
    "country": "IN",
    "lineType": "mobile"
  }
}
```

---

# **🌐 4. Quick IP Validation**

### **GET /api/v1/validate/ip?ip=8.8.8.8**

### **✅ Response**

```
{
  "success": true,
  "data": {
    "valid": true,
    "version": "IPv4",
    "isPrivate": false
  }
}
```

---

# **📦 5. Quick ZIP Code Validation**

### **GET /api/v1/validate/zipcode?zipcode=94016**

### **✅ Response**

```
{
  "success": true,
  "data": {
    "valid": true,
    "state": "California"
  }
}
```

---

# **📚 6. Bulk Validation**

### **POST /api/v1/validate/bulk**

### **📝 Request**

```
{
  "emails": ["valid@gmail.com", "wrong-email"],
  "phones": [
    { "number": "+14155552671", "country": "US" },
    { "number": "12345", "country": "US" }
  ],
  "ips": ["8.8.8.8", "300.300.1.1"],
  "zipcodes": ["94016", "ABCDE"]
}
```

### **✅ Response**

```
{
  "success": true,
  "data": {
    "emails": [
      { "index": 0, "input": "valid@gmail.com", "result": { "syntaxValid": true } },
      { "index": 1, "input": "wrong-email", "result": { "syntaxValid": false } }
    ],
    "phones": [
      { "index": 0, "input": "+14155552671", "result": { "valid": true } },
      { "index": 1, "input": "12345", "result": { "valid": false } }
    ],
    "ips": [
      { "index": 0, "input": "8.8.8.8", "result": { "valid": true } },
      { "index": 1, "input": "300.300.1.1", "result": { "valid": false } }
    ],
    "zipcodes": [
      { "index": 0, "input": "94016", "result": { "valid": true } },
      { "index": 1, "input": "ABCDE", "result": { "valid": false } }
    ]
  }
}
```

---

# **🚨 7. Error Responses**

### **❌ Missing API Key**

```
{
  "success": false,
  "error": "Missing API Key. Please provide X-API-KEY header."
}
```

### **❌ Invalid API Key**

```
{
  "success": false,
  "error": "Invalid API Key."
}
```

### **❌ Rate Limit Exceeded**

```
{
  "success": false,
  "error": "Rate limit exceeded. Try again later."
}
```

---

# **⚙️ Tech Stack**

- **Java 17**
- **Spring Boot 3**
- **Swagger / OpenAPI 3**
- **Lombok**
- **DNSJava**
- **LibPhoneNumber**
- **Jakarta Validation**

---

# **📊 Rate Limiting (Free Tier)**

| **Limit** | **Value** |
| --- |-----------|
| Requests per second | **20**    |
| Requests per minute | **1200**  |

---

# **🛡 Security**

✔ API Key Authentication

✔ Custom Exceptions

✔ Sanitized Error Messages

✔ Throttling (Rate Limiting)

---

# **📦 Project Setup**

### **Run locally:**

```
mvn spring-boot:run
```

### **Build jar:**

```
mvn clean package
```

---

# **📄 License**

MIT License — free for commercial use.

---

# **💬 Support**

If you need help integrating this API, open an issue or contact via RapidAPI.

---

# **🚀 Ready for Deployment**

This API is ready for:

✔ RapidAPI

✔ Railway

✔ Render

✔ AWS / GCP / Azure