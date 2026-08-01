# 🎓 Student Wallet AI

An AI-powered personal finance management platform designed specifically for students. The application helps users track expenses, manage budgets, and receive AI-driven spending insights and predictions.

---

## 🚀 Features

- 🔐 User Authentication (JWT + Google OAuth)
- 💰 Expense & Income Tracking
- 📊 Budget Management
- 🤖 AI-powered Expense Prediction
- 📈 Dashboard with Financial Overview
- 📧 Gmail Integration
- 🌐 RESTful APIs
- 🔒 Secure Authentication & Authorization

---

## 🏗️ Project Structure

```
student-wallet-ai/
│
├── frontend/          # React.js frontend
│
├── backend/           # Spring Boot backend
│   ├── src/
│   ├── pom.xml
│   └── ...
│
├── ml-model/          # Python ML service
│   └── app.py
│
└── README.md
```

---

## 🛠️ Tech Stack

### Frontend
- React.js
- HTML
- CSS
- JavaScript

### Backend
- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Maven

### Database
- PostgreSQL

### Machine Learning
- Python
- Flask/FastAPI (depending on your implementation)

### APIs & Services
- Google OAuth
- Gmail API

### Tools
- Git
- GitHub
- VS Code
- IntelliJ IDEA
- Postman

---

## 📂 Modules

### 👤 Authentication
- User Registration
- User Login
- JWT Authentication
- Google Sign-In

### 💳 Wallet
- Add Transactions
- Update Transactions
- Delete Transactions
- Transaction History

### 📊 Budget
- Set Monthly Budget
- Budget Tracking
- Remaining Balance

### 🤖 AI Prediction
- Expense Prediction
- Spending Analysis
- Smart Financial Insights

---

## ⚙️ Installation

### 1. Clone the Repository

```bash
git clone https://github.com/samhitmacherla/student-wallet-ai.git
cd student-wallet-ai
```

---

## Backend Setup

```bash
cd backend
```

Configure your own:

- `application.properties`
- Google OAuth credentials (`credentials.json`)

Run:

```bash
mvn spring-boot:run
```

Backend runs on:

```
http://localhost:8080
```

---

## Frontend Setup

```bash
cd frontend
npm install
npm start
```

Frontend runs on:

```
http://localhost:3000
```

---

## ML Service

```bash
cd ml-model
pip install -r requirements.txt
python app.py
```

---

## Environment Variables

The following files are **not included** in this repository:

```
backend/src/main/resources/application.properties

backend/src/main/resources/credentials.json

backend/tokens/
```

Create them locally before running the project.

---

## Screenshots

> Add screenshots here once the project UI is complete.

Example:

```
screenshots/

login.png

dashboard.png

transactions.png

predictions.png
```

---

## Future Enhancements

- 📱 Mobile Application
- 📊 Interactive Charts
- 💹 Investment Suggestions
- 🧾 Receipt Scanner
- 🔔 Budget Notifications
- ☁️ Cloud Deployment
- 📈 Advanced AI Recommendations

---

## Contributors

- **Macherla Samhit**
- Team Members

---

## License

This project is developed for educational and learning purposes.

```

---

# ⭐ I also recommend one more improvement

Right now your repository is:

```
student-wallet-ai/
├── frontend/
├── backend/
└── ml-model/
```

I'd add these folders too:

```text
student-wallet-ai/
│
├── frontend/
├── backend/
├── ml-model/
│
├── docs/
│   ├── architecture.png
│   ├── api.md
│   └── screenshots/
│
├── .gitignore
├── LICENSE
└── README.md
```

This looks much more professional and is the structure commonly seen in high-quality open-source and portfolio projects.
