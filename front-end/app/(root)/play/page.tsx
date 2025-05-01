"use client";

import { useEffect, useRef, useState } from "react";
import Webcam from "react-webcam";
import { cn } from "@/lib/utils";
import Image from "next/image";
import ScoreBoard from "@/components/play/ScoreBoard";
import GameArea from "@/components/play/GameArea";

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
  const [opponentMove, setOpponentMove] = useState<"rock"|"paper"|"scissors">("rock");
  const [playerMove, setPlayerMove] = useState<"rock"|"paper"|"scissors">("rock");

  useEffect(() => {
    const name = localStorage.getItem("playerName") ?? "";
    const hand = localStorage.getItem("handPreference") as "left" | "right";
    const rounds = parseInt(localStorage.getItem("rounds") ?? "3");

    setPlayerName(name);
    setHandPreference(hand === "left" ? "left" : "right");
    setTotalRounds(rounds);
  }, []);

  async function sendFrameToFlask(base64Image: string): Promise<string> {
    try {
      // Convert base64 image (data:image/jpeg;base64,...) to Blob
      const res = await fetch(base64Image);
      const blob = await res.blob();
  
      // Package as FormData
      const formData = new FormData();
      formData.append("image", blob, "frame.jpg");
  
      // Send to Flask endpoint
      const response = await fetch("http://localhost:5000/predict", {
        method: "POST",
        body: formData,
      });
  
      const data = await response.json();
      return data.gesture || "unknown";
    } catch (err) {
      console.error("Error sending image to Flask:", err);
      return "error";
    }
  }
  

  const runCountdownAndCapture = async () => {
    await new Promise((r) => setTimeout(r, 10));
    setOpponentMove("rock")
    setAnimateHand(true);

    for (let i = 0; i < phrases.length; i++) {
      setCountdownText(phrases[i]);
      await new Promise((r) => setTimeout(r, 500));
    }

    setCountdownText("");
    await captureAndSubmit();
  };

  const captureAndSubmit = async () => {
    const imageSrc = webcamRef.current?.getScreenshot();
    if (!imageSrc) return;
    setTemp(imageSrc);

    const gesture = await sendFrameToFlask(imageSrc) as "rock" | "paper" | "scissors" ;
    setPlayerMove(gesture);

    const randomOpponentMove = choices[Math.floor(Math.random() * choices.length)] as "rock" | "paper" |"scissors";
    setOpponentMove(randomOpponentMove);
    setAnimateHand(false);

    try {
      const apiUrl = process.env.NEXT_PUBLIC_API_URL;

      const response = await fetch(`${apiUrl}/submit`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          playerMove: playerMove,
          opponentMove: randomOpponentMove,
        }),
      });

      const data = await response.json();
      setResult(data.result); // 'win', 'loss', or 'draw'
      setScore(data.score);   // updated { wins, losses, draws }

      if (data.roundNumber >= totalRounds) {
        setCurrentRound(data.roundNumber)
        setShowFinal(true);
      } else {
        setTimeout(() => {
          setCurrentRound(data.roundNumber);
          runCountdownAndCapture();
        }, 1500);
      }
    } catch (error) {
      console.error("Error during round submission:", error);
    }
  };

  const handleStartGame = () => {
    setGameStarted(true);
    setCurrentRound(1);
    setScore({ wins: 0, losses: 0, draws: 0 });
    setShowFinal(false);
    runCountdownAndCapture();
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center p-6 space-y-4">
      <h1 className="text-3xl font-bold">Rock paper scissors</h1>
      <p className="text-muted-foreground">
        Playing {totalRounds} round{totalRounds > 1 ? "s" : ""} — Good luck!
      </p>

      <div className="w-full max-w-4xl">
        <GameArea
          webcamRef={webcamRef}
          handPreference={handPreference}
          animateHand={animateHand}
          opponentMove={opponentMove}
          playerMove={playerMove}
        />
        <ScoreBoard
          gameStarted={gameStarted}
          countdownText={countdownText}
          result={result}
          opponentMove={opponentMove}
          currentRound={currentRound}
          score={score}
          showFinal={showFinal}
          onStartGame={handleStartGame}
          playerMove={playerMove}
        />
      </div>

      <div id="temp">
        {temp && <Image src={temp} alt="Captured move" width={200} height={200} />}
      </div>
    </div>
  );
}
