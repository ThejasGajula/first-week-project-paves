"use client";

import { cn } from "@/lib/utils";
import { Card, CardContent } from "@/components/ui/card";
import Image from "next/image";
import Webcam from "react-webcam";

interface GameAreaProps {
  webcamRef: React.RefObject<Webcam | null>;
  handPreference: "left" | "right";
  animateHand: boolean;
  opponent: string;
}

const choices = {
  rock: "/assets/images/rock.png",
  paper: "/assets/images/paper.png",
  scissors: "/assets/images/scissors.png",
};

export default function GameArea({ webcamRef, handPreference, animateHand, opponent }: GameAreaProps) {
  return (
    <div className={cn("flex gap-10", handPreference === "right" && "flex-row-reverse")}>
      <Card className="w-1/2">
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

      <Card className="w-1/2">
        <CardContent className={cn("p-4 flex flex-col", handPreference === "left" && "rotate-y-180")}>
          <div>
            {opponent && (
              <Image
                src={choices[opponent as keyof typeof choices]}
                alt={`${opponent} image`}
                width={100}
                height={300}
                className={cn(
                  "origin-bottom rotate-y-180 rotate-z-270",
                  animateHand && opponent === "rock" && "animate-swing"
                )}
              />
            )}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
