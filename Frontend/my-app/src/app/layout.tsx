'use client'; 

import Link from 'next/link';
import { AuthProvider, useAuth } from '@/context/AuthContext'; // useAuth도 import
import type { Metadata } from 'next';
import './globals.css';

// 헤더의 내용을 별도 함수로 분리하거나 Layout 안에 직접 작성할 수 있습니다.
function Header() {
    const { user, logout } = useAuth();

    return (
        <nav className="bg-white shadow-md fixed top-0 left-0 right-0 z-50">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between items-center h-16">
                    <Link href="/" className="text-2xl font-bold text-gray-800">
                        Grids & Circle
                    </Link>
                    <div>
                        {user ? (
                            <div className="flex items-center gap-4">
                                <span className="text-gray-700">{user.email}님</span>
                                <Link href="/">
                                <button
                                    onClick={logout}
                                    className="px-4 py-2 bg-red-500 text-white font-semibold rounded-lg hover:bg-red-400 text-sm"
                                >
                                    로그아웃
                                </button>
                                </Link>
                            </div>
                        ) : (
                            <Link href="/">
                                <button className="px-4 py-2 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-500 text-sm">
                                    로그인
                                </button>
                            </Link>
                        )}
                    </div>
                </div>
            </div>
        </nav>
    );
}


export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body>
        <AuthProvider>
          <Header />
          <main className="pt-16">
            {children}
          </main>
        </AuthProvider>
      </body>
    </html>
  );
}