"use client";

import { useEffect, useRef, useState } from "react";
import Webcam from "react-webcam";
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
  const [showResult, setShowResult] = useState(false);
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

  async function callBackendRestartGame() {
    // Call your backend API to restart the game
    await fetch(`${process.env.NEXT_PUBLIC_API_URL}/restart`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then(
        (response) => {
          if (!response.ok) {
            throw new Error("Failed to restart game");
          }
        })
      .catch((error) => {
        console.error("Error restarting game:", error);
      });
    
  
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
          playerMove: gesture,
          opponentMove: randomOpponentMove,
        }),
      });

      const data = await response.json();
      setResult(data.result); // 'win', 'loss', or 'draw'
      setShowResult(true);
      setScore(data.score);   // updated { wins, losses, draws }

      if (data.roundNumber >= totalRounds) {
        setCurrentRound(data.roundNumber)
        setShowFinal(true);
      } else {
        setTimeout(() => {
          setCurrentRound(data.roundNumber);
          setShowResult(false);
          runCountdownAndCapture();
        }, 1500);
      }
    } catch (error) {
      console.error("Error during round submission:", error);
    }
  };

  const handleStartGame = () => {
    console.log("Starting game...");  
    callBackendRestartGame();
    setGameStarted(true);
    setCurrentRound(1);
    setScore({ wins: 0, losses: 0, draws: 0 });
    setShowFinal(false);
    setShowResult(false);
    runCountdownAndCapture();
  };



  return (
    <div className="min-h-screen flex flex-col items-center justify-center p-6 space-y-4 z-50">
      <div className="flex items-center space-x-2 mb-4 text-3xl text-white font-bold">
        <Image
          src="/assets/images/logo.webp"
          alt="Rock Paper Scissors"
          width={60}
          height={60}
          className="object-cover"
        />
        <h1>Rock paper Scissors</h1>
      </div>
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
          score={score}
          showResult={showResult}
        />
        <ScoreBoard
          gameStarted={gameStarted}
          countdownText={countdownText}
          result={result}
          opponentMove={opponentMove}
          currentRound={currentRound}
          score={score}
          showFinal={showFinal}
          setShowFinal={setShowFinal}
          handleRestart={handleStartGame}
          handleStart={handleStartGame}
          playerMove={playerMove}
          setShowResult={setShowResult}
          showResult={showResult}
        />
      </div>

    </div>
  );
}


