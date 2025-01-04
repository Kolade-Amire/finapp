// app/layout.tsx
import { ReactNode } from 'react';

export const metadata = {
    title: 'FinApp',
    description: 'A Next.js App',
};

const RootLayout = ({ children }: { children: ReactNode }) => {
    return (
        <html lang="en">
        <body>{children}</body>
        </html>
    );
};

export default RootLayout;
