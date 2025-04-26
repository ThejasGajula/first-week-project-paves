'use client'

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { cn } from "@/lib/utils";

export default function SetupPage() {
  const router = useRouter();
  const [name, setName] = useState("");
  const [hand, setHand] = useState<string>("right");
  const [rounds, setRounds] = useState(3);


  const handleStart = () => {
    if (name.trim() === "") {
      alert("Please enter your name");
      return;
    }
    localStorage.setItem("playerName", name);
    localStorage.setItem("handPreference", hand);
    localStorage.setItem("rounds", rounds.toString());

    router.push("/play");
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center p-4">
      <div className="max-w-md w-full space-y-6">
        <h1 className="text-3xl font-bold text-center">Setup</h1>

        <div className="space-y-2">
          <Label htmlFor="name">Name</Label>
          <Input
            id="name"
            placeholder="Enter your name"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>

        <div className="space-y-2">
          <Label>Hand Preference</Label>
          <RadioGroup className="flex justify-around p-10" value={hand} onValueChange={(value) => {setHand(value as "left" | "right"); console.log(value);
          }}>
            <div className={cn("border-dark-1 border rounded px-4 py-4 cursor-pointer",hand === "left"&&"bg-primary text-white")}>
              <RadioGroupItem className="hidden" value="left" id="left" />
              <Label className="cursor-pointer" htmlFor="left">Left Handed</Label>
            </div>
            <div className={cn("border-dark-1 border rounded px-4 py-4 cursor-pointer",hand === "right" && "bg-primary text-white")}>
              <RadioGroupItem className="hidden" value="right" id="right" />
              <Label className="cursor-pointer" htmlFor="right">Right Handed</Label>
            </div>
          </RadioGroup>
        </div>

        <div className="space-y-2">
          <Label htmlFor="rounds">Number of Rounds</Label>
          <Input
            id="rounds"
            type="number"
            value={rounds}
            min={1}
            onChange={(e) => setRounds(Number(e.target.value))}
          />
        </div>

        <Button className="w-full cursor-pointer" onClick={handleStart}>
          Start Game
        </Button>
      </div>
    </div>
  );
}
