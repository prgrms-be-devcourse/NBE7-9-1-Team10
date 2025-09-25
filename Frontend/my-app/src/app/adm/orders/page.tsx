'use client';

import { useState, useEffect } from 'react';
import { getOrders } from '@/lib/client';
import type { Order } from '@/type/orders';

export default function OrderListPage() {
    const [orders, setOrders] = useState<Order[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const loadOrders = async () => {
            try {
                const responseData = await getOrders();
                setOrders(responseData.orders);
            } catch (error) {
                console.error("주문 목록을 불러오는 데 실패했습니다:", error);
            } finally {
                setIsLoading(false);
            }
        };

        loadOrders();
    }, []);

    if (isLoading) {
        return <div className="flex justify-center items-center h-screen">주문 목록 로딩 중...</div>;
    }

    return (
        <div className="bg-gray-100 min-h-screen p-8">
            <div className="max-w-4xl mx-auto">
                <h1 className="text-3xl font-bold mb-6">전체 주문 내역</h1>
                <div className="space-y-4">
                    {orders.map((order) => (
                        <div key={order.orderId} className="bg-white p-6 rounded-lg shadow-md">
                            <div className="flex justify-between items-start">
                                <div>
                                    <p className="text-lg font-semibold text-gray-800">주문 번호: {order.orderId}</p>
                                    <p className="text-sm text-gray-500">{order.email}</p>
                                    <p className="text-sm text-gray-500">{order.address}</p>
                                </div>
                                <div className="text-right">
                                    <p className="text-lg font-bold text-blue-600">{order.totalPrice.toLocaleString()}원</p>
                                    <p className="text-xs text-gray-400">{order.orderDate}</p>
                                </div>
                            </div>
                            <div className="mt-4 pt-4 border-t">
                                <h3 className="text-md font-semibold mb-2">주문 상품</h3>
                                <ul className="space-y-1">
                                    {order.items.map((item) => (
                                        <li key={item.id} className="flex justify-between text-sm">
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