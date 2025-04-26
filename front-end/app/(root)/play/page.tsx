"use client";

import { useEffect, useRef, useState } from "react";
import Webcam from "react-webcam";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import Image from "next/image";

const phrases = ["Rock", "Paper", "Scissors!", "Shoot!"];

export default function PlayPage() {
  const webcamRef = useRef<Webcam>(null);
  const [playerName, setPlayerName] = useState("");
  const [handPreference, setHandPreference] = useState<"left" | "right">("right");
  const [totalRounds, setTotalRounds] = useState(3);
  const [currentRound, setCurrentRound] = useState(1);
  const [score, setScore] = useState({ wins: 0, losses: 0, draws: 0 });
  const [result, setResult] = useState<string | null>(null);
  const [gameStarted, setGameStarted] = useState(false);
  const [countdownText, setCountdownText] = useState("");
  const [showFinal, setShowFinal] = useState(false);
  const [temp,setTemp] = useState<string>("");

  // Load initial values
  useEffect(() => {
    const name = localStorage.getItem("playerName") ?? "";
    const hand = localStorage.getItem("handPreference") as "left" | "right";
    const rounds = parseInt(localStorage.getItem("rounds") ?? "3");

    setPlayerName(name);
    setHandPreference(hand === "left" ? "left" : "right");
    setTotalRounds(rounds);
  }, []);

  // Countdown logic
  const runCountdownAndCapture = async () => {
    for (let i = 0; i < phrases.length; i++) {
      setCountdownText(phrases[i]);
      await new Promise((r) => setTimeout(r, 600));
    }
    setCountdownText("");

    await captureAndEvaluate();
  };

  // Send image to backend
  const captureAndEvaluate = async () => {
    const imageSrc = webcamRef.current?.getScreenshot();
    
    if (!imageSrc) return;

    setTemp(imageSrc);

    try {
      const response = await fetch("http://localhost:5000/predict", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ image: imageSrc, hand: handPreference }),
      });

      const data = await response.json();
      setResult(data.prediction);

      // Random opponent
      const choices = ["rock", "paper", "scissors"];
      const opponent = choices[Math.floor(Math.random() * 3)];

      if (data.prediction === opponent) {
        setScore((s) => ({ ...s, draws: s.draws + 1 }));
      } else if (
        (data.prediction === "rock" && opponent === "scissors") ||
        (data.prediction === "paper" && opponent === "rock") ||
        (data.prediction === "scissors" && opponent === "paper")
      ) {
        setScore((s) => ({ ...s, wins: s.wins + 1 }));
      } else {
        setScore((s) => ({ ...s, losses: s.losses + 1 }));
      }

    } catch (error) {
      console.error("Error sending image:", error);
    }

    if (currentRound < totalRounds) {
      setTimeout(() => {
        setCurrentRound((r) => r + 1);
        runCountdownAndCapture();
      }, 1000);
    } else {
      setShowFinal(true);
    }
  };

  const handleStartGame = () => {
    setGameStarted(true);
    runCountdownAndCapture();
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center p-6 space-y-4">
      <h1 className="text-3xl font-bold">Welcome, {playerName} 👋</h1>
      <p className="text-muted-foreground">
        Playing {totalRounds} round{totalRounds > 1 ? "s" : ""} — Good luck!
      </p>

      <Card>
        <CardContent className="p-4 flex flex-col items-center space-y-4">
          <Webcam
            ref={webcamRef}
            screenshotFormat="image/jpeg"
            className="rounded-md max-w-sm"
            videoConstraints={{ facingMode: "user" }}
          />

          {gameStarted ? (
            <>
              {countdownText && <div className="text-2xl font-semibold">{countdownText}</div>}
              {result && (
                <div className="text-xl font-medium">
                  Round {currentRound} Result:{" "}
                  <span className="text-primary font-bold">{result}</span>
                </div>
              )}
            </>
          ) : (
            <Button onClick={handleStartGame}>Ready</Button>
          )}

          {showFinal && (
            <div className="pt-4 space-y-2 text-center">
              <h2 className="text-2xl font-bold">🎉 Final Score</h2>
              <p>Wins: {score.wins}</p>
              <p>Losses: {score.losses}</p>
              <p>Draws: {score.draws}</p>
            </div>
          )}
        </CardContent>
      </Card>

      <div id="temp">
          <Image src={temp} alt="dd" width={200} height={200}/>
      </div>
    </div>
  );
}
