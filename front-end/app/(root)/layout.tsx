import React, { ReactNode } from 'react'

export default function RootLayout({children} : {children:ReactNode}) {
  return (
   <div className=
                  'bg-gradient-to-br from-purple-500 via-pink-500 to-yellow-500 min-h-screen'>
        {children}
    </div>
  )
}
