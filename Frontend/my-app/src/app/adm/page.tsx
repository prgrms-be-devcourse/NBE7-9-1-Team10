'use client';

import { useState, useEffect } from 'react';
import { ItemDto } from '@/type/items';
import { getItems,deleteItem  } from '@/lib/client';
import Link from 'next/link';

export default function AdminPage() {
    const [products, setProducts] = useState<ItemDto[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const loadItems = async () => {
            try {
                const data = await getItems();
                setProducts(data);
            } catch (error) {
                console.error(error);
            } finally {
                setIsLoading(false);
            }
        };

        loadItems();
    }, []);

    const handleDeleteItem = async (itemId: number) => {
        // 사용자에게 정말 삭제할 것인지 확인받습니다.
        if (!window.confirm(`정말로 상품(ID: ${itemId})을 삭제하시겠습니까?`)) {
            return;
        }

        try {
            await deleteItem(itemId);
            alert('상품이 성공적으로 삭제되었습니다.');
            // 성공 시 화면의 목록에서도 해당 상품을 제거하여 바로 반영합니다.
            setProducts(products.filter(product => product.itemId !== itemId));
        } catch (error) {
            console.error('삭제 실패:', error);
            alert('상품 삭제에 실패했습니다.');
        }
    };

    // 로딩 중 UI
    if (isLoading) {
        return <div className="flex justify-center items-center h-screen">로딩 중...</div>;
    }

    // 메인 UI
    return (
        <div className="bg-gray-100 min-h-screen flex items-center justify-center p-4">
            <div className="w-full max-w-2xl bg-white rounded-xl shadow-md p-8 space-y-6">
                {/* 헤더 */}
                <div className="flex justify-between items-center">
                    <h1 className="text-2xl font-bold">상품목록</h1>
                    <Link href="/adm/new">
                    <button className="bg-gray-200 text-gray-700 font-semibold py-2 px-4 rounded-lg hover:bg-gray-300">
                        상품 추가하기
                    </button>
                    </Link>
                </div>

                {/* 상품 목록 */}
                <div className="space-y-2">
                    {products.map((product) => (
                        <div key={product.itemId} className="flex items-center justify-between p-4 border rounded-lg bg-gray-50/50">
                            <div className="flex items-center space-x-4">
                            <img
                                src={product.imageUrl}
                                alt={product.itemName}
                                width={64}
                                height={64}
                                className="rounded-md object-cover"
                            />
                            </div>
                            <div>
                                <div>
                                    <p className="font-semibold">{product.itemName}</p>
                                    <p className="text-sm text-gray-500">상품 ID: {product.itemId}</p>
                                </div>
                            </div>
                            <div className="flex items-center space-x-4">
                                <span className="font-medium w-24 text-right">{product.price.toLocaleString()}원</span>
                                <div className="flex space-x-2">
                                <Link href={`/adm/update/${product.itemId}`}>
                                    <button 
                                    className="bg-gray-200 text-gray-700 text-sm font-semibold py-1 px-3 rounded-md hover:bg-gray-300">
                                        수정
                                    </button>
                                    </Link>
                                    <button 
                                    onClick={() => handleDeleteItem(product.itemId)}
                                    className="bg-blue-500 text-white text-sm font-semibold py-1 px-3 rounded-md hover:bg-blue-600">
                                        삭제
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
                
                {/* 푸터 */}
                <div className="flex justify-end pt-4">
                    <button className="bg-gray-200 text-gray-700 font-semibold py-2 px-4 rounded-lg hover:bg-gray-300">
                        주문내역
                    </button>
                </div>
            </div>
        </div>
    );
}