import { BackgroundGradientAnimation } from '@/components/ui/background-gradient-animation'
import React, { ReactNode } from 'react'

export default function RootLayout({children} : {children:ReactNode}) {
  return (
    <BackgroundGradientAnimation  gradientBackgroundStart="#4e8562" blendingValue="color-burn">
      {children}
    </BackgroundGradientAnimation>
  )
}
