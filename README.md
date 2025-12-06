
---

# **🚀 Advanced Validation API — All-in-One Identity & Data Quality Validator**

### **Email • Phone • IP • ZIP • Bulk Validation • API Key Auth • Rate Limiting • Swagger UI**

A production-grade API built with **Spring Boot 3**, designed for developers who need **clean, accurate, and fast validation** of digital identity inputs.

This API combines **4+ validation systems** into ONE service:

✔ Email validation (syntax, MX, disposable, SMTP status, reputation score)

✔ Phone validation (format, line type, carrier lookup, VOIP detection, risk scoring)

✔ IP validation (IPv4/IPv6, public/private, reserved)

✔ ZIP code validation (US ZIP → state detection)

✔ Bulk validation (process 100+ items per request)

✔ API Key authentication

✔ Built-in rate limiting

✔ Global exception handling

✔ Fully documented Swagger UI

---

# **🆚 Competitor Comparison — Why Choose Our API? (SaaS Table)**

| **Feature / API** | **Our API** | **AbstractAPI** | **APILayer** | **IP2Location** | **ZipCodeAPI** |
| --- | --- | --- | --- | --- | --- |
| **Email Validation** | ⭐ Syntax, MX, disposable, SMTP, scoring | ✔ | ✔ | ✖ | ✖ |
| **Phone Validation** | ⭐ Line type, carrier lookup, VOIP detection, risk scoring | ✔ (paid) | ✔ (paid) | ✖ | ✖ |
| **IP Validation** | ⭐ IPv4/6, private/public, reserved | ✔ | ✔ | Geo only | ✖ |
| **ZIP Validation** | ⭐ Valid format + state lookup | ✖ | ✖ | ✖ | ✔ |
| **Bulk Validation** | ⭐ Email + Phone + IP + ZIP in *one* call | Limited | Limited | ✖ | ✖ |
| **Single Unified Request** | ⭐ Validate everything in 1 API call | ✖ | ✖ | ✖ | ✖ |
| **API Key Security** | ✔ Strong header auth | ✔ | ✔ | ✔ | ✔ |
| **Rate Limiting** | ⭐ Built-in throttle with custom tiers | Depends | Depends | Depends | Depends |
| **Error Standardization** | ⭐ Clean, consistent JSON errors | ✖ | ✖ | ✖ | ✖ |
| **Swagger UI** | ⭐ Full interactive docs | Partial | ✖ | ✖ | ✖ |
| **Free Tier** | ⭐ 500 free monthly requests | Low | Limited | None | None |
| **Pricing** | 💰 Extremely Affordable | $$$ | $$ | $$ | $$ |
| **Developer Experience** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐ | ⭐⭐ |

---

# **✨ What Makes Our API Stand Out?**

### **1️⃣ A Unified Validator API (Unique Feature)**

Competitors offer separate APIs for each field.

**We do Email + Phone + IP + ZIP in ONE request.**

### **2️⃣ Bulk Validation for All Four Types**

Competitors only offer *bulk email* or *bulk phone* → **never combined**.

### **3️⃣ High-Quality Phone Intelligence**

- Carrier lookup
- VOIP detection
- Line type (mobile, landline, toll-free, voip)
- Risk scoring (0–100)
- Risk level (LOW / MEDIUM / HIGH / CRITICAL)

### **4️⃣ SMTP Email Verification + Scoring**

- MX + SMTP handshake
- Disposable domain detection
- Reputation scoring
- Role-based email detection
- Domain reputation (basic)

### **5️⃣ Best Developer DX**

Clean, predictable responses:

```
{
  "success": true,
  "data": { }
}
```

### **6️⃣ Beautiful Swagger UI**

With **real examples**, reusable schemas, and deep API docs.

---

# **🌍 Base URL**

```
https://your-domain.com/api/v1/validate
```

---

# **🔑 Authentication**

Every request must include:

```
X-API-KEY: YOUR_API_KEY_HERE
```

*Swagger & /health endpoints do not require an API key.*

---

# **📘 Swagger Documentation**

```
https://your-domain.com/swagger-ui/index.html
```

---

# **🩺 Health Check Endpoints**

```
GET /health
GET /health/status
```

---

# **📬 1. Full Validation (Email + Phone + IP + ZIP)**

### **POST /api/v1/validate**

### **Request:**

```
{
  "email": "test@example.com",
  "phone": "+14155552671",
  "country": "US",
  "ip": "8.8.8.8",
  "zipcode": "94016"
}
```

### **Response:**

```
{
  "success": true,
  "data": {
    "email": { "syntaxValid": true, "hasMxRecord": true, "smtpStatus": "valid" },
    "phone": { "valid": true, "country": "US" },
    "ip": { "valid": true, "type": "public" },
    "zipcode": { "valid": true, "state": "California" }
  }
}
```

---

# **📧 2. Quick Email Validation**

### **GET**

### **/api/v1/validate/email?email=test@gmail.com**

### **Response:**

```
{
  "success": true,
  "data": {
    "syntaxValid": true,
    "hasMxRecord": true,
    "smtpStatus": "valid",
    "disposable": false,
    "roleBased": false,
    "domain": "gmail.com",
    "reputation": "excellent",
    "score": 98
  }
}
```

---

# **📱 3. Quick Phone Validation**

### **GET**

### **/api/v1/validate/phone?phone=+14155552671&country=US**

### **Response:**

```
{
  "success": true,
  "data": {
    "valid": true,
    "country": "US",
    "lineType": "mobile",
    "carrier": "Verizon Wireless",
    "voip": false,
    "internationalFormat": "+1 415-555-2671",
    "nationalFormat": "(415) 555-2671",
    "riskScore": 12,
    "riskLevel": "LOW"
  }
}
```

---

# **🌐 4. Quick IP Validation**

### **GET**

### **/api/v1/validate/ip?ip=8.8.8.8**

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

# **🏷 5. Quick ZIP Code Validation**

### **GET**

### **/api/v1/validate/zipcode?zipcode=94016**

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

# **📚 6. Bulk Validation (UPDATED)**

### **POST**

### **/api/v1/validate/bulk**

### **Request:**

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

### **Response:**

```
{
  "success": true,
  "data": {
    "emails": [
      {
        "index": 0,
        "input": "valid@gmail.com",
        "result": {
          "syntaxValid": true,
          "hasMxRecord": true,
          "smtpStatus": "valid",
          "disposable": false,
          "domain": "gmail.com",
          "reputation": "excellent",
          "score": 98
        }
      }
    ],
    "phones": [
      {
        "index": 0,
        "input": "+14155552671",
        "country": "US",
        "result": {
          "valid": true,
          "lineType": "mobile",
          "carrier": "Verizon Wireless",
          "voip": false,
          "internationalFormat": "+1 415-555-2671",
          "riskScore": 12,
          "riskLevel": "LOW"
        }
      }
    ]
  }
}
```

---

# **🚨 7. Error Responses**

### **Missing API Key**

```
{
  "success": false,
  "error": "Missing API Key. Please provide X-API-KEY header."
}
```

### **Invalid API Key**

```
{
  "success": false,
  "error": "Invalid API Key."
}
```

### **Rate Limit Exceeded**

```
{
  "success": false,
  "error": "Rate limit exceeded. Try again later."
}
```

---

# **⚙️ Tech Stack**

- Java 17
- Spring Boot 3
- Spring Web
- Lombok
- DNSJava
- LibPhoneNumber
- Jakarta Validation

---

# **📊 Rate Limiting**

| **Limit** | **Value** |
| --- | --- |
| Requests/sec | 20 |
| Requests/min | 1200 |

---

# **🛡 Security**

✔ API Key validation

✔ Sanitized exception handling

✔ Throttling middleware

✔ Validation on all inputs

---

# **📦 Run Locally**

```
mvn spring-boot:run
```

### **Build JAR:**

```
mvn clean package
```

---

# **🚀 Deployment Ready**

✔ Railway

✔ Render

✔ RapidAPI

✔ AWS / GCP / Azure

---

# **📄 License**

MIT License — free for commercial use.

---

# **💬 Support**

For help integrating this API, open an issue or connect via RapidAPI.