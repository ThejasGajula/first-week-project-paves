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
}

const choices = {
  rock: "/assets/images/rock.png",
  paper: "/assets/images/paper.png",
  scissors: "/assets/images/scissors.png",
};

export default function GameArea({ webcamRef, handPreference, animateHand, opponentMove }: GameAreaProps) {
  return (
    <div className={cn("flex gap-10", handPreference === "right" && "flex-row-reverse")}>
      <Card className="w-1/2 glassMorph z-10">
        <CardContent className="p-2 flex flex-col items-center space-y-4">
          <Webcam
            ref={webcamRef}
            screenshotFormat="image/jpeg"
            className="rounded-md"
            videoConstraints={{ facingMode: "user" }}
            mirrored
          />
        </CardContent>
      </Card>

      <Card className="w-1/2 glassMorph z-10">
        <CardContent className={cn("p-4 flex flex-col", handPreference === "left" && "rotate-y-180")}>
          <div>
            {opponentMove && (
              <Image
                src={choices[opponentMove as keyof typeof choices]}
                alt={`${opponentMove} image`}
                width={100}
                height={300}
                className={cn(
                  "origin-bottom rotate-y-180 rotate-z-270",
                  animateHand && opponentMove === "rock" && "animate-swing"
                )}
              />
            )}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
