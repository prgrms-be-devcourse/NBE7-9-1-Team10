'use client';

import { useState, useEffect } from 'react';
import Image from 'next/image';

// 상품 데이터의 타입을 정의합니다.
type Product = {
  id: number;
  imageUrl: string;
  name: string;
  description: string;
  price: number;
};

/*
const mockProducts: Product[] = [
  {
    id: 1,
    imageUrl: '/images/coffee-bean.png',
    name: '커피콩',
    description: 'Columbia Nariño',
    price: 5000,
  },
  {
    id: 2,
    imageUrl: '/images/coffee-bean.png',
    name: '커피콩',
    description: 'Columbia Nariño',
    price: 5000,
  },
  {
    id: 3,
    imageUrl: '/images/coffee-bean.png',
    name: '커피콩',
    description: 'Columbia Nariño',
    price: 5000,
  },
];*/ //예시 데이터

export default function AdminPage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  /* 예시 데이터
  useEffect(() => {
    setProducts(mockProducts);
  }, []);*/
  useEffect(() => {
    // API 호출을 위한 비동기 함수 선언
    const fetchProducts = async () => {
      try {
        // API 엔드포인트로 GET 요청을 보낸다
        const response = await fetch('/api/v1/adm/items');

        // 요청이 실패했을 경우 에러
        if (!response.ok) {
          throw new Error('데이터를 불러오는 데 실패했습니다.');
        }
        // 응답받은 JSON 데이터를 파싱
        const data = await response.json(); 
        setProducts(data);
      } catch (error) {
        console.error(error); 
      } finally {
        setIsLoading(false);
      }
    };

    fetchProducts();
  }, []); 

  // 로딩중
  if (isLoading) {
    return <div className="flex justify-center items-center h-screen">로딩 중...</div>;
  }

  return (
    <div className="bg-gray-100 min-h-screen flex items-center justify-center p-4">
      <div className="w-full max-w-2xl bg-white rounded-xl shadow-md p-8 space-y-6">
        {/* 상단 헤더 */}
        <div className="flex justify-between items-center">
          <h1 className="text-2xl font-bold">상품목록</h1>
          <button className="bg-gray-200 text-gray-700 font-semibold py-2 px-4 rounded-lg hover:bg-gray-300">
            상품 추가하기
          </button>
        </div>

        <div className="space-y-2">
          {products.map((product) => (
            <div key={product.id} className="flex items-center justify-between p-4 border rounded-lg bg-gray-50/50">
              <div className="flex items-center space-x-4">
                <Image
                  src={product.imageUrl}
                  alt={product.name}
                  width={64}
                  height={64}
                  className="rounded-md object-cover"
                />
                <div>
                  <p className="font-semibold">{product.name}</p>
                  <p className="text-sm text-gray-500">{product.description}</p>
                </div>
              </div>
              <div className="flex items-center space-x-4">
                <span className="font-medium w-24 text-right">{product.price.toLocaleString()}원</span>
                <div className="flex space-x-2">
                  <button className="bg-gray-200 text-gray-700 text-sm font-semibold py-1 px-3 rounded-md hover:bg-gray-300">
                    수정
                  </button>
                  <button className="bg-blue-500 text-white text-sm font-semibold py-1 px-3 rounded-md hover:bg-blue-600">
                    삭제
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
        {/* --- 여기까지 --- */}

        {/* 하단 버튼 */}
        <div className="flex justify-end pt-4">
          <button className="bg-gray-200 text-gray-700 font-semibold py-2 px-4 rounded-lg hover:bg-gray-300">
            주문내역
          </button>
        </div>
      </div>
    </div>
  );
}