"use client"

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { login as loginApi } from '@/lib/client'; // API 함수 이름과 겹치므로 별칭 사용
import { useAuth } from '@/context/AuthContext'; // useAuth 훅 import

export default function HomePage() {
    const router = useRouter();
    const { login } = useAuth(); // Context의 login 함수를 가져옵니다.
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
            const response = await loginApi(email);
            
            // 1. API 성공 응답으로 전역 상태(Context)를 업데이트합니다.
            login({ email: response.email, role: response.role });

            // 2. 역할에 따라 페이지를 이동시킵니다.
            if (response.role === 'admin') {
                router.push('/adm');
            } else {
                router.push('/items');
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
