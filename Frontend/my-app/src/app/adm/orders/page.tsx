// app/admin/orders/page.tsx
'use client';

import { useState, useEffect } from 'react';
import { getOrders } from '@/lib/client';
import type { Order } from '@/type/orders';

// 배송 상태에 따른 스타일을 반환하는 헬퍼 함수
const getStatusBadge = (status: number) => {
    switch (status) {
        case 0:
            return <span className="px-2 py-1 text-xs font-semibold text-yellow-800 bg-yellow-200 rounded-full">배송준비</span>;
        case 1:
            return <span className="px-2 py-1 text-xs font-semibold text-blue-800 bg-blue-200 rounded-full">배송중</span>;
        case 2:
            return <span className="px-2 py-1 text-xs font-semibold text-green-800 bg-green-200 rounded-full">배송완료</span>;
        default:
            return <span className="px-2 py-1 text-xs font-semibold text-gray-800 bg-gray-200 rounded-full">{status}</span>;
    }
};

export default function OrderListPage() {
    const [orders, setOrders] = useState<Order[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    //현재 선택된 필터를 관리할 state 추가 ('all'이 기본값)
    const [filter, setFilter] = useState('all');

    useEffect(() => {
        const loadOrders = async () => {
            setIsLoading(true);
            try {
                //필터 값에 따라 API 경로를 결정
                const statusMap: { [key: string]: string | undefined } = {
                    all: undefined,
                    ready: 'delivery-ready',
                    inProgress: 'delivery-in-progress',
                    completed: 'delivery-completed',
                };
                
                const responseData = await getOrders(statusMap[filter]);
                
                //날짜순 정렬
                const sortedOrders = responseData.orders.sort((a, b) => 
                    new Date(b.orderDate).getTime() - new Date(a.orderDate).getTime()
                );
                
                setOrders(sortedOrders);
            } catch (error) {
                console.error("주문 목록을 불러오는 데 실패했습니다:", error);
            } finally {
                setIsLoading(false);
            }
        };

        loadOrders();
    }, [filter]); //filter state가 변경될 때마다 useEffect가 다시 실행

    if (isLoading) {
        return <div className="flex justify-center items-center h-screen">주문 목록 로딩 중...</div>;
    }

    return (
        <div className="bg-gray-100 min-h-screen p-8">
            <div className="max-w-4xl mx-auto">
                <h1 className="text-3xl font-bold mb-6">전체 주문 내역</h1>

                {/*필터 버튼 UI 추가 */}
                <div className="flex space-x-2 mb-6">
                    <button onClick={() => setFilter('all')} className={`px-4 py-2 rounded-lg ${filter === 'all' ? 'bg-blue-600 text-white' : 'bg-white'}`}>전체</button>
                    <button onClick={() => setFilter('ready')} className={`px-4 py-2 rounded-lg ${filter === 'ready' ? 'bg-blue-600 text-white' : 'bg-white'}`}>배송준비</button>
                    <button onClick={() => setFilter('inProgress')} className={`px-4 py-2 rounded-lg ${filter === 'inProgress' ? 'bg-blue-600 text-white' : 'bg-white'}`}>배송중</button>
                    <button onClick={() => setFilter('completed')} className={`px-4 py-2 rounded-lg ${filter === 'completed' ? 'bg-blue-600 text-white' : 'bg-white'}`}>배송완료</button>
                </div>

                <div className="space-y-4">
                    {orders.map((order) => (
                        <div key={order.orderId} className="bg-white p-6 rounded-lg shadow-md">
                            <div className="flex justify-between items-start">
                                <div>
                                    <p className="text-lg font-semibold text-gray-800">주문 번호: {order.orderId}</p>
                                    <p className="text-sm text-gray-500">{order.email}</p>
                                    <p className="text-sm text-gray-500">주문일시: {new Date(order.orderDate).toLocaleString('ko-KR')}</p>
                                </div>
                                <div className="text-right">
                                    <p className="text-lg font-bold text-blue-600">{order.totalPrice.toLocaleString()}원</p>
                                    {/*배송 상태 표시 */}
                                    <div className="mt-1">{getStatusBadge(order.deliveryStatus)}</div>
                                </div>
                            </div>
                            <div className="mt-4 pt-4 border-t">
                                <h3 className="text-md font-semibold mb-2">주문 상품</h3>
                                <ul className="space-y-1">
                                    {order.items.map((item) => (
                                        <li key={`${order.orderId}-${item.id}`} className="flex justify-between text-sm">
                                            <span>{item.name}</span>
                                            <span>수량: {item.qty}</span>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}