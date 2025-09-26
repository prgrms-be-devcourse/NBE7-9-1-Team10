'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';

export default function ItemsLayout({
    children,
}: {
    children: React.ReactNode;
}) {
    const pathname = usePathname();

    return (
        <div className="min-h-screen bg-gray-100">
            {/* 사용자 하위 네비게이션 - 고정 위치 */}
            <div className="bg-white shadow-sm border-b fixed top-16 left-0 right-0 z-40">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex space-x-8">
                        <Link
                            href="/items"
                            className={`py-4 px-1 border-b-2 font-medium text-sm ${
                                pathname === '/items'
                                    ? 'border-blue-500 text-blue-600'
                                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                            }`}
                        >
                            주문하기
                        </Link>
                        <Link
                            href="/items/my_order"
                            className={`py-4 px-1 border-b-2 font-medium text-sm ${
                                pathname === '/items/my_order'
                                    ? 'border-blue-500 text-blue-600'
                                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                            }`}
                        >
                            내 주문 내역
                        </Link>
                    </div>
                </div>
            </div>
            
            {/* 메인 컨텐츠 - 위쪽 여유공간 확보 */}
            <div className="pt-16 p-4">
                <div className="w-full max-w-6xl mx-auto">
                    {children}
                </div>
            </div>
        </div>
    );
}