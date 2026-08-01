from flask import Flask, request, jsonify
from flask_cors import CORS
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.naive_bayes import MultinomialNB
import numpy as np

app = Flask(__name__)
CORS(app)
# Training data — merchant name → category
training_data = [
    ("swiggy", "Food"),
    ("zomato", "Food"),
    ("mcdonalds", "Food"),
    ("dominos", "Food"),
    ("kfc", "Food"),
    ("pizza hut", "Food"),
    ("canteen", "Food"),
    ("restaurant", "Food"),
    ("cafe", "Food"),
    ("biryani", "Food"),
    ("ola", "Transport"),
    ("uber", "Transport"),
    ("auto", "Transport"),
    ("rapido", "Transport"),
    ("bus", "Transport"),
    ("metro", "Transport"),
    ("cab", "Transport"),
    ("amazon", "Shopping"),
    ("flipkart", "Shopping"),
    ("myntra", "Shopping"),
    ("meesho", "Shopping"),
    ("ajio", "Shopping"),
    ("nykaa", "Shopping"),
    ("airtel", "Recharge"),
    ("jio", "Recharge"),
    ("vi", "Recharge"),
    ("bsnl", "Recharge"),
    ("recharge", "Recharge"),
    ("dmart", "Groceries"),
    ("bigbasket", "Groceries"),
    ("blinkit", "Groceries"),
    ("zepto", "Groceries"),
    ("grofers", "Groceries"),
    ("supermarket", "Groceries"),
    ("netflix", "Entertainment"),
    ("spotify", "Entertainment"),
    ("hotstar", "Entertainment"),
    ("prime", "Entertainment"),
    ("youtube", "Entertainment"),
    ("bookmyshow", "Entertainment"),
    ("movie", "Entertainment"),
    ("college", "Education"),
    ("university", "Education"),
    ("course", "Education"),
    ("udemy", "Education"),
    ("books", "Education"),
]

# Train the model
merchants = [item[0] for item in training_data]
categories = [item[1] for item in training_data]

vectorizer = TfidfVectorizer()
X = vectorizer.fit_transform(merchants)
model = MultinomialNB()
model.fit(X, categories)

# Guilt score logic
def calculate_guilt(merchant, category, amount):
    guilt = 3  # default low guilt
    merchant_lower = merchant.lower()

    if category == "Food":
        if any(x in merchant_lower for x in ["swiggy", "zomato"]):
            guilt = 7
        else:
            guilt = 4

    elif category == "Shopping":
        if amount > 1000:
            guilt = 9
        elif amount > 500:
            guilt = 7
        else:
            guilt = 5

    elif category == "Entertainment":
        guilt = 5

    elif category == "Transport":
        guilt = 2

    elif category == "Groceries":
        guilt = 1

    elif category == "Recharge":
        guilt = 2

    elif category == "Education":
        guilt = 1

    guilt_messages = {
        1: "Essential spend — no guilt at all!",
        2: "Totally justified — carry on!",
        3: "Reasonable spend!",
        4: "Okay but could cook at home?",
        5: "Think before you spend next time!",
        6: "Getting a bit much...",
        7: "You didn't need this Sam!",
        8: "Seriously reconsidering your choices?",
        9: "Bro really?? 💀",
        10: "Financial red flag 🚩"
    }

    return guilt, guilt_messages.get(guilt, "Spend wisely!")


@app.route('/predict/category', methods=['POST'])
def predict_category():
    data = request.json
    merchant = data.get('merchant', '').lower()
    X_test = vectorizer.transform([merchant])
    category = model.predict(X_test)[0]
    return jsonify({"category": category})


@app.route('/predict/guilt', methods=['POST'])
def predict_guilt():
    data = request.json
    merchant = data.get('merchant', '')
    amount = data.get('amount', 0)
    category = data.get('category', '')

    if not category:
        X_test = vectorizer.transform([merchant.lower()])
        category = model.predict(X_test)[0]

    score, message = calculate_guilt(merchant, category, amount)
    return jsonify({
        "guiltScore": score,
        "message": message,
        "category": category
    })


@app.route('/predict/brokedate', methods=['POST'])
def predict_broke_date():
    data = request.json
    daily_spending = data.get('dailyAverage', 0)
    remaining = data.get('remaining', 0)
    days_left = data.get('daysLeft', 30)

    if daily_spending <= 0:
        return jsonify({
            "predictedBrokeDay": days_left,
            "message": "You're spending wisely — money will last the month!"
        })

    days_until_broke = remaining / daily_spending

    if days_until_broke >= days_left:
        return jsonify({
            "predictedBrokeDay": days_left,
            "message": "You're on track — money will last the month!"
        })
    else:
        broke_on_day = int(30 - days_left + days_until_broke)
        return jsonify({
            "predictedBrokeDay": broke_on_day,
            "message": f"Warning! You'll run out of money on Day {broke_on_day} of this month!"
        })


@app.route('/health', methods=['GET'])
def health():
    return jsonify({"status": "ML Model is running!"})


if __name__ == '__main__':
    app.run(port=5000, debug=True)