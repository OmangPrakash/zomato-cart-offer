# Zomato Cart Offer Test Automation

A comprehensive test suite for Zomato's cart offer functionality, validating different offer types (FLATX and FLATPERCENT) across various customer segments.

## 📋 Project Overview

This project implements automated tests for a cart offer system that applies discounts based on:
- Customer segments (p1, p2, p3)
- Offer types (FLATX - flat amount off, FLATPERCENT - percentage off)
- Restaurant-specific offers

## 🧪 Test Coverage

**Total Test Cases:** 15 identified  
**Implemented & Passing:** 9 tests  
**Future Scope:** 6 additional test scenarios

### Implemented Tests

1. ✅ **Basic Functionality** - FLATX offer creation
2. ✅ **Offer Creation** - FLATX for single segment
3. ✅ **Offer Creation** - Multiple segments
4. ✅ **FLATX Application** - Cart equals offer value
5. ✅ **FLATPERCENT Application** - 20% discount
6. ✅ **FLATPERCENT Application** - 50% discount
7. ✅ **Segment Validation** - User segment mismatch
8. ✅ **No Offer Scenario** - Restaurant without offers
9. ✅ **Edge Cases** - Large cart values

### Test Results

```
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
✅ All tests passing
```

## 🛠️ Technology Stack

- **Java 21**
- **Spring Boot 2.7.18**
- **JUnit 5**
- **Maven**
- **MockServer** (for user segment API mocking)
- **Jackson** (JSON processing)

## 📁 Project Structure

```
sample-cart-offer/
├── src/
│   ├── main/
│   │   └── java/com/springboot/
│   │       ├── controller/
│   │       │   ├── AutowiredController.java
│   │       │   ├── OfferRequest.java
│   │       │   ├── ApplyOfferRequest.java
│   │       │   └── ApplyOfferResponse.java
│   │       └── CartOfferApplication.java
│   └── test/
│       └── java/com/springboot/
│           ├── CartOfferTests.java
│           └── CartOfferApplicationTests.java
├── mockserver/
│   ├── docker-compose.yml
│   └── initializerJson.json
├── pom.xml
└── README.md
```

## 🚀 Setup Instructions

### Prerequisites

- Java 21 or higher
- Maven 3.6+
- Docker Desktop (for MockServer)

### Installation & Running

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/zomato-cart-offer-tests.git
   cd zomato-cart-offer-tests
   ```

2. **Start MockServer** (Terminal 1)
   ```bash
   cd mockserver
   docker-compose up
   ```

3. **Build and start the application** (Terminal 2)
   ```bash
   ./mvnw clean package -DskipTests
   java -jar target/simple-springboot-app-0.0.1-SNAPSHOT.jar
   ```

4. **Run tests** (Terminal 3)
   ```bash
   ./mvnw test
   ```

## 📊 API Endpoints

### Create Offer
```
POST /api/v1/offer
Content-Type: application/json

{
  "restaurant_id": 1,
  "offer_type": "FLATX",
  "offer_value": 50,
  "customer_segment": ["p1"]
}
```

### Apply Offer to Cart
```
POST /api/v1/cart/apply_offer
Content-Type: application/json

{
  "cart_value": 200,
  "user_id": 1,
  "restaurant_id": 1
}
```

### Mock User Segment API
```
GET /api/v1/user_segment?user_id=1

Response:
{
  "segment": "p1"
}
```

## 🧩 Test Scenarios

### Offer Types

**FLATX (Flat Amount Off)**
- If offer_value = 50 and cart = 200
- Final cart value = 150

**FLATPERCENT (Percentage Off)**
- If offer_value = 20 and cart = 500
- Final cart value = 400 (20% off)

### Customer Segments

The system supports three customer segments:
- **p1** - Premium customers
- **p2** - Standard customers  
- **p3** - New customers

Offers are applied only if the user's segment matches the offer's target segments.

## 📈 Future Enhancements

Identified test cases for future implementation:
- Invalid offer type handling
- FLATX discount greater than cart value
- 100% discount scenarios
- Multiple offers for same restaurant
- Missing required fields validation
- Zero cart value edge cases

## 🤝 Contributing

This is a test automation project for assignment purposes. Feedback and suggestions are welcome!

## 📄 License

This project is created for educational and assignment purposes.

## 📧 Contact

For any questions regarding this implementation, please reach out via GitHub issues.

---

**Assignment Status:** ✅ Completed  
**Test Success Rate:** 100% (9/9 passing)  
**Last Updated:** December 2024
