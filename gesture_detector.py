import cv2
import mediapipe as mp
import math

mp_hands = mp.solutions.hands
hands = mp_hands.Hands(max_num_hands=1, min_detection_confidence=0.7)
mp_draw = mp.solutions.drawing_utils

def distance(point1, point2):
    return math.hypot(point1.x - point2.x, point1.y - point2.y)

def classify_gesture(landmarks):
    wrist = landmarks[0]

    # Index finger
    dist_tip_index = distance(landmarks[8], wrist)
    dist_mid_index = distance(landmarks[6], wrist)

    # Middle finger
    dist_tip_middle = distance(landmarks[12], wrist)
    dist_mid_middle = distance(landmarks[10], wrist)

    # Ring finger
    dist_tip_ring = distance(landmarks[16], wrist)
    dist_mid_ring = distance(landmarks[14], wrist)

    # Pinky finger
    dist_tip_pinky = distance(landmarks[20], wrist)
    dist_mid_pinky = distance(landmarks[18], wrist)

    # Thumb (slightly different)
    dist_tip_thumb = distance(landmarks[4], landmarks[17])
    dist_mid_thumb = distance(landmarks[2], landmarks[17])

    fingers = [
        dist_tip_thumb > dist_mid_thumb,      # Thumb
        dist_tip_index > dist_mid_index,      # Index
        dist_tip_middle > dist_mid_middle,    # Middle
        dist_tip_ring > dist_mid_ring,        # Ring
        dist_tip_pinky > dist_mid_pinky       # Pinky
    ]

    # return fingers

    if fingers == [False, False, False, False, False]:
        return "rock"
    elif fingers[1] and fingers[2] and not fingers[3] and not fingers[4]:
        return "scissors"
    elif all(fingers):
        return "paper"
    else:
        return "unknown"


if __name__ == "__main__":
    cap = cv2.VideoCapture(0)

    while True:
        success, frame = cap.read()
        if not success:
            break

        frame = cv2.flip(frame, 1)
        rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        result = hands.process(rgb)

        gesture = "No hand"
        if result.multi_hand_landmarks:
            for hand_landmarks in result.multi_hand_landmarks:
                mp_draw.draw_landmarks(frame, hand_landmarks, mp_hands.HAND_CONNECTIONS)
                gesture = classify_gesture(hand_landmarks.landmark)

        cv2.putText(frame, f"Gesture: {gesture}", (10, 50), cv2.FONT_HERSHEY_SIMPLEX,
                    1, (0, 255, 0), 2)

        cv2.imshow("RPS with Distances", frame)
        if cv2.waitKey(1) & 0xFF == ord("q"):
            break

    cap.release()
    cv2.destroyAllWindows()
