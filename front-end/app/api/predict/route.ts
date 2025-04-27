// app/api/predict/route.ts
import { NextResponse } from "next/server";

export async function POST(req: Request) {
  const body = await req.json();
  const { image, hand } = body;

  console.log("Received image and hand:", { hand });

  const choices = ["rock", "paper", "scissors"];
  const randomPrediction = choices[Math.floor(Math.random() * choices.length)];

  // Simulate slight delay (optional)
  await new Promise((resolve) => setTimeout(resolve, 500));

  return NextResponse.json({ prediction: randomPrediction });
}
