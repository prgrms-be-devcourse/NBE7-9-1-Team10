'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';

export default function AdminLayout({
    children,
}: {
    children: React.ReactNode;
}) {
    const pathname = usePathname();

    return (
        <div className="min-h-screen bg-gray-100">
            {/* 관리자 하위 네비게이션 */}
            <div className="bg-white shadow-sm border-b">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex space-x-8">
                        <Link
                            href="/adm"
                            className={`py-4 px-1 border-b-2 font-medium text-sm ${
                                pathname === '/adm' || pathname.startsWith('/adm/update') || pathname.startsWith('/adm/new')
                                    ? 'border-blue-500 text-blue-600'
                                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                            }`}
                        >
                            상품관리
                        </Link>
                        <Link
                            href="/adm/orders"
                            className={`py-4 px-1 border-b-2 font-medium text-sm ${
                                pathname === '/adm/orders'
                                    ? 'border-blue-500 text-blue-600'
                                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                            }`}
                        >
                            주문관리
                        </Link>
                    </div>
                </div>
            </div>
            
            {/* 메인 컨텐츠 */}
            <main>
                {children}
            </main>
        </div>
    );
}
