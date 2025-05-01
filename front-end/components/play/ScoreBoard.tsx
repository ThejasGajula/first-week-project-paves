"use client";

import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogTitle, DialogDescription } from "@/components/ui/dialog";
import { DialogHeader } from "../ui/dialog";

interface ScoreBoardProps {
  gameStarted: boolean;
  countdownText: string;
  result: string | null;
  opponentMove: string;
  currentRound: number;
  score: { wins: number; losses: number; draws: number };
  showFinal: boolean;
  handleStart: () => void;
  handleRestart: () => void;
  setShowFinal: (show: boolean) => void;
  playerMove: string;
  showResult: boolean;
  setShowResult: (show: boolean) => void;
}

export default function ScoreBoard({
  gameStarted,
  countdownText,
  result,
  showResult,
  setShowResult,
  opponentMove,
  currentRound,
  score,
  showFinal,
  playerMove,
  handleStart,
  handleRestart,
  setShowFinal,
}: ScoreBoardProps) {
  return (
    <div className="w-full flex mt-10 flex-col items-center">
      {gameStarted ? (
        <>
          {countdownText && <div className="text-2xl font-semibold">{countdownText}</div>}
          {showResult && result && (
            <div className="text-xl font-medium">
              Round {currentRound} Result:
              <span className="text-primary font-bold">{result}</span>
            </div>
          )}
        </>
      ) : (
        <Button onClick={handleStart} className="z-100 cursor-pointer">Ready</Button>
      )}


      {showFinal && (
        <Dialog open={showFinal} onOpenChange={setShowFinal} >
          <DialogContent className="max-w-sm mx-auto glassMorph-dark">
            <DialogHeader>
              <DialogTitle className="text-center text-3xl underline">Final Score</DialogTitle>
              <DialogDescription className="text-black text-center space-y-1 pt-2">
                <p>Wins: {score.wins}</p>
                <p>Losses: {score.losses}</p>
                <p>Draws: {score.draws}</p>
                {score.wins > score.losses ? (
                  <p className="text-green-500 font-bold text-2xl">You Win! 🎉
                  </p>
                ) : score.wins < score.losses ? (
                  <p className="text-red-500 font-bold text-2xl">You Lose!
                  </p>
                ) : (
                  <p className="text-yellow-500 font-bold text-2xl">It's a Draw!</p>
                )}
                <p className="text-gray-500">Thanks for playing! Click below to play again.</p>

              </DialogDescription>
              <div className="flex justify-center mt-4">
                <Button onClick={handleRestart} className="w-1/2 text-black glassMorph-dark">
                  Play Again
                </Button>
              </div>
            </DialogHeader>
          </DialogContent>
        </Dialog>
      )}

    </div>
  );
}
