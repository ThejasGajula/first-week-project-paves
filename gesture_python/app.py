from flask import Flask, request, jsonify
import cv2
from flask_cors import CORS
import numpy as np
from gesture_model import predict_gesture_from_cv2_image

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})

@app.route("/predict", methods=["POST"])
def predict():
    try:
        if "image" not in request.files:
            return jsonify({"error": "No image file provided"}), 400

        file = request.files["image"]

        # Convert image file to numpy array
        in_memory_file = file.read()
        np_arr = np.frombuffer(in_memory_file, np.uint8)
        image_np = cv2.imdecode(np_arr, cv2.IMREAD_COLOR)

        gesture = predict_gesture_from_cv2_image(image_np)
        return jsonify({"gesture": gesture})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
