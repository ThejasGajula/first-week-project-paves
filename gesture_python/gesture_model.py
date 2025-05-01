import cv2
import math
import numpy as np
import mediapipe as mp

mp_hands = mp.solutions.hands
hands = mp_hands.Hands(static_image_mode=True, max_num_hands=1, min_detection_confidence=0.1)

def distance(point1, point2):
    return math.hypot(point1.x - point1.x, point1.y - point2.y)

def classify_gesture(landmarks):
    wrist = landmarks[0]

    dist_tip_index = distance(landmarks[8], wrist)
    dist_mid_index = distance(landmarks[6], wrist)

    dist_tip_middle = distance(landmarks[12], wrist)
    dist_mid_middle = distance(landmarks[10], wrist)

    dist_tip_ring = distance(landmarks[16], wrist)
    dist_mid_ring = distance(landmarks[14], wrist)

    dist_tip_pinky = distance(landmarks[20], wrist)
    dist_mid_pinky = distance(landmarks[18], wrist)

    dist_tip_thumb = distance(landmarks[4], landmarks[17])
    dist_mid_thumb = distance(landmarks[2], landmarks[17])

    fingers = [
        dist_tip_thumb > dist_mid_thumb,
        dist_tip_index > dist_mid_index,
        dist_tip_middle > dist_mid_middle,
        dist_tip_ring > dist_mid_ring,
        dist_tip_pinky > dist_mid_pinky
    ]

    if fingers[1:] == [False, False, False, False]:
        return "rock"
    elif fingers[1] and fingers[2] and not fingers[3] and not fingers[4]:
        return "scissors"
    elif fingers[1] and fingers[2] and fingers[3] and fingers[4]:
        return "paper"
    else:
        return "unknown"

def predict_gesture_from_cv2_image(image_np):
    try:
        rgb = cv2.cvtColor(image_np, cv2.COLOR_BGR2RGB)
        result = hands.process(rgb)

        if result.multi_hand_landmarks:
            for hand_landmarks in result.multi_hand_landmarks:
                return classify_gesture(hand_landmarks.landmark)
        return "No hand"
    except Exception as e:
        print("Prediction Error:", e)
        return "error"
