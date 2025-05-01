'use client';

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { cn } from "@/lib/utils";
import Image from "next/image";

export default function SetupPage() {
  const router = useRouter();
  const [name, setName] = useState("Rohit");
  const [hand, setHand] = useState<"left" | "right">("right");
  const [rounds, setRounds] = useState(3);

  const handleStart = async () => {
    if (name.trim() === "") {
      alert("Please enter your name");
      return;
    }

    const apiUrl = process.env.NEXT_PUBLIC_API_URL;

    try {
      const response = await fetch(`${apiUrl}/setup`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, handPreference: hand, rounds })
      });

      if (!response.ok) {
        throw new Error("Failed to setup player");
      }

      localStorage.setItem("playerName", name);
      localStorage.setItem("handPreference", hand);
      localStorage.setItem("rounds", rounds.toString());

      router.push("/play");
    } catch (error) {
      console.error("Setup error:", error);
      alert("Failed to setup player, please try again.");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4 ">

      <div className="flex-1 flex items-center justify-center">
        <Image
          src="/assets/images/text_logo.webp"
          alt="Rock Paper Scissors"
          width={500}
          height={500}
          className="object-cover"
        />
      </div>

      <div className="flex-1 flex items-center justify-center">
      <div className="max-w-lg w-full space-y-6 z-50 glassMorph p-10 ">
        <h1 className="text-3xl text-gray-200 font-bold text-center">Game Setup</h1>

        <div className="space-y-2">
          <Input
            id="name"
            placeholder="Enter your name"
            className="text-white text-2xl"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>

        <div className="space-y-2">
          <RadioGroup
            className="flex justify-around p-10"
            value={hand}
            onValueChange={(value) => setHand(value as "left" | "right")}
          >
            <div className={cn(" border rounded px-4 py-4 cursor-pointer", hand === "left" && "bg-primary text-white")}>
              <RadioGroupItem className="hidden" value="left" id="left" />
              <Label className="cursor-pointer" htmlFor="left">Left Handed</Label>
            </div>
            <div className={cn(" border rounded px-4 py-4 cursor-pointer", hand === "right" && "bg-primary text-white")}>
              <RadioGroupItem className="hidden" value="right" id="right" />
              <Label className="cursor-pointer" htmlFor="right">Right Handed</Label>
            </div>
          </RadioGroup>
        </div>

        <div className="space-y-2 flex justify-around items-center">
          <Label htmlFor="rounds">Number of Rounds :</Label>
          <Input
            id="rounds"
            type="number"
            value={rounds}
            min={1}
            onChange={(e) => setRounds(Number(e.target.value))}
            className="text-white text-2xl w-16"
          />
        </div>

        <Button className="w-full cursor-pointer" onClick={handleStart}>
          Start Game
        </Button>
      </div>
      </div>
    </div>
  );
}
