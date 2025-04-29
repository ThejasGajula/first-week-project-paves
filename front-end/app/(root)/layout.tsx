import React, { ReactNode } from 'react'

export default function RootLayout({children} : {children:ReactNode}) {
  return (
    <div className='bg-[url(/assets/images/background.png)] bg-repeat'>
        {children}
    </div>
  )
}
