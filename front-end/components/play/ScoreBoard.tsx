"use client";

import { Button } from "@/components/ui/button";

interface ScoreBoardProps {
  gameStarted: boolean;
  countdownText: string;
  result: string | null;
  opponent: string;
  currentRound: number;
  score: { wins: number; losses: number; draws: number };
  showFinal: boolean;
  onStartGame: () => void;
}

export default function ScoreBoard({
  gameStarted,
  countdownText,
  result,
  opponent,
  currentRound,
  score,
  showFinal,
  onStartGame,
}: ScoreBoardProps) {
  return (
    <div className="w-full flex mt-10 flex-col items-center">
      {gameStarted ? (
        <>
          {countdownText && <div className="text-2xl font-semibold">{countdownText}</div>}
          {result && (
            <div className="text-xl font-medium">
              Round {currentRound} Result: {opponent} —{" "}
              <span className="text-primary font-bold">{result}</span>
            </div>
          )}
        </>
      ) : (
        <Button onClick={onStartGame}>Ready</Button>
      )}

      {showFinal && (
        <div className="pt-4 space-y-2 text-center">
          <h2 className="text-2xl font-bold">🎉 Final Score</h2>
          <p>Wins: {score.wins}</p>
          <p>Losses: {score.losses}</p>
          <p>Draws: {score.draws}</p>
        </div>
      )}
    </div>
  );
}
