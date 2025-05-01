"use client";

import { cn } from "@/lib/utils";
import { Card, CardContent } from "@/components/ui/card";
import Image from "next/image";
import Webcam from "react-webcam";

interface GameAreaProps {
  webcamRef: React.RefObject<Webcam | null>;
  handPreference: "left" | "right";
  animateHand: boolean;
  opponentMove: string;
  playerMove: string;
  score: { wins: number; losses: number; draws: number };
  showResult: boolean;
}

const choices = {
  rock: "/assets/images/rock.png",
  paper: "/assets/images/paper.png",
  scissors: "/assets/images/scissors.png",
};

export default function GameArea({ webcamRef, handPreference, animateHand, opponentMove ,score,showResult ,playerMove}: GameAreaProps) {
  return (
    <div className={cn("flex gap-10", handPreference === "right" && "flex-row-reverse")}>
      <Card className="w-1/2 glassMorph z-10">
        <CardContent className=" flex flex-col items-center space-y-4 relative">
          <Webcam
            ref={webcamRef}
            screenshotFormat="image/jpeg"
            className="rounded-md"
            videoConstraints={{ facingMode: "user" }}
            mirrored
          />
          {score.wins > 0 && (
            <div className="absolute top-2 left-2 bg-green-500 text-white rounded px-2 py-1 text-sm">
              Wins: {score.wins}
              </div>
          )}
          {showResult && (
            <p className="px-2 bottom-2 absolute  left-1/2 translate-[-50%] glassMorph-dark" >
              {playerMove}
            </p>
          )}
          
        </CardContent>
      </Card>

      <Card className="w-1/2 glassMorph z-10">
        <CardContent className={cn("p-4 flex flex-col relative  h-full", handPreference === "left" && "rotate-y-180")}>
          <div>
            {opponentMove && (
              <Image
                src={choices[opponentMove as keyof typeof choices]}
                alt={`${opponentMove} image`}
                width={100}
                height={300}
                className={cn(
                  "origin-bottom rotate-y-180 rotate-z-270 ",
                  animateHand && opponentMove === "rock" && "animate-swing"
                )}
              />
            )}
          </div>
          {score.wins > 0 && (
            <div className="absolute top-2 right-2 bg-red-500 text-white rounded px-2 py-1 text-sm">
              Wins: {score.losses}
              </div>
          )}
           {showResult && (
            <p className="px-2 bottom-2 absolute  left-1/2 translate-[-50%] glassMorph-dark" >
              {opponentMove}
            </p>
          )}
          
        </CardContent>
       
      </Card>
    </div>
  );
}
