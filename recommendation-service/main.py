from fastapi import FastAPI

app = FastAPI(title="recommendation-service")

# Hard-coded rules: vegetable -> vegetables that go well with it.
# No paid API or API key is used.
RULES = {
    "tomato": ["onion", "green chilli", "coriander"],
    "potato": ["onion", "peas", "cauliflower"],
    "onion": ["tomato", "potato", "green chilli"],
    "cauliflower": ["potato", "peas", "ginger"],
    "green peas": ["potato", "carrot", "coriander"],
    "spinach": ["potato", "garlic", "onion"],
    "carrot": ["green peas", "beans", "onion"],
}

DEFAULT_SUGGESTIONS = ["potato", "onion", "tomato"]


@app.get("/recommendations/{produce_name}")
def get_recommendations(produce_name: str):
    name = produce_name.strip().lower()
    return {
        "produce": name,
        "suggestions": RULES.get(name, DEFAULT_SUGGESTIONS),
    }
