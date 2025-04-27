"use client";

import { useEffect, useRef, useState } from "react";
import Webcam from "react-webcam";
import { cn } from "@/lib/utils";
import Image from "next/image";
import GameArea from "@/components/play/GameArea";
import ScoreBoard from "@/components/play/ScoreBoard";

const phrases = ["Rock", "Paper", "Scissors!", "Shoot!"];
const choices = ["rock", "paper", "scissors"];

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
  const [temp, setTemp] = useState<string>();
  const [animateHand, setAnimateHand] = useState(false);
  const [opponent, setOpponent] = useState<string>("rock");

  useEffect(() => {
    const name = localStorage.getItem("playerName") ?? "";
    const hand = localStorage.getItem("handPreference") as "left" | "right";
    const rounds = parseInt(localStorage.getItem("rounds") ?? "3");

    setPlayerName(name);
    setHandPreference(hand === "left" ? "left" : "right");
    setTotalRounds(rounds);
  }, []);

  const runCountdownAndCapture = async (round: number) => {
    await new Promise((r) => setTimeout(r, 10));
    setAnimateHand(true);

    for (let i = 0; i < phrases.length; i++) {
      setCountdownText(phrases[i]);
      console.log(animateHand);
      
      await new Promise((r) => setTimeout(r, 500));
    }

    setCountdownText("");

    await captureAndEvaluate(round);
  };

  const captureAndEvaluate = async (round: number) => {
    const imageSrc = webcamRef.current?.getScreenshot();
    if (!imageSrc) return;
    setTemp(imageSrc);

    const randomOpponent = choices[Math.floor(Math.random() * 3)];
    setOpponent(randomOpponent);
    setAnimateHand(false);

    try {
      const apiUrl = process.env.NEXT_PUBLIC_API_URL;
      const response = await fetch(`${apiUrl}/predict`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ image: imageSrc, hand: handPreference }),
      });

      const data = await response.json();
      setResult(data.prediction);

      

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

      if (round < totalRounds) {
        setTimeout(() => {
          setCurrentRound((prev) => prev + 1);
          runCountdownAndCapture(round + 1);
        }, 1500);
      } else {
        setShowFinal(true);
      }
    } catch (error) {
      console.error("Error sending image:", error);
    }
  };

  const handleStartGame = () => {
    setGameStarted(true);
    setCurrentRound(1);
    setScore({ wins: 0, losses: 0, draws: 0 });
    setShowFinal(false);
    runCountdownAndCapture(1);
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center p-6 space-y-4">
      <h1 className="text-3xl font-bold">Welcome, {playerName} 👋</h1>
      <p className="text-muted-foreground">
        Playing {totalRounds} round{totalRounds > 1 ? "s" : ""} — Good luck!
      </p>

      <div className="w-full max-w-4xl">
        
        <GameArea
          webcamRef={webcamRef}
          handPreference={handPreference}
          animateHand={animateHand}
          opponent={opponent}
        />
        <ScoreBoard
          gameStarted={gameStarted}
          countdownText={countdownText}
          result={result}
          opponent={opponent}
          currentRound={currentRound}
          score={score}
          showFinal={showFinal}
          onStartGame={handleStartGame}
        />
      </div>

      <div id="temp">
        {temp && <Image src={temp} alt="temp" width={200} height={200} />}
      </div>
    </div>
  );
}
