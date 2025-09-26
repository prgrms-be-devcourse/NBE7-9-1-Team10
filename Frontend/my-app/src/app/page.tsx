"use client"

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { login } from '@/lib/client';

export default function HomePage() {

  const router = useRouter();
    const [email, setEmail] = useState('');
    const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!email) {
        alert('이메일을 입력해주세요.');
        return;
    }
    setIsLoading(true);

    try {
        const response = await login(email);
        // 백엔드에서 받은 role에 따라
        if (response.role === 'ADMIN') {
            router.push('/admin'); // 관리자 페이지로 이동
        } else {
            router.push('/items'); // 사용자 페이지로 이동
        }
    } catch (error) {
        console.error(error);
        alert('로그인에 실패했습니다. 이메일을 확인해주세요.');
    } finally {
        setIsLoading(false);
    }
};

  return (
        <main className="flex min-h-screen flex-col items-center justify-center p-24">
            <div className="w-full max-w-sm text-center">
                <h1 className="text-4xl font-bold mb-8">
                    Grids & Circle
                </h1>
                
                <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="이메일을 입력하세요"
                        className="px-4 py-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <button 
                        type="submit" 
                        disabled={isLoading}
                        className="px-6 py-3 bg-blue-600 text-white font-semibold rounded-lg shadow-md hover:bg-blue-500 transition-colors disabled:bg-gray-400"
                    >
                        {isLoading ? '로그인 중...' : '로그인 / 시작하기'}
                    </button>
                </form>
            </div>
        </main>
    );
}