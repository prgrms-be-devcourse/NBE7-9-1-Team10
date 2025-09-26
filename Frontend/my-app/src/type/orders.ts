// 단일 주문 상품의 타입
export type OrderItem = {
    id: number;
    name: string;
    qty: number;
};

// 단일 주문의 타입
export type Order = {
    orderId: number;
    email: string;
    address: string;
    orderDate: string; // 날짜는 문자열로 받습니다.
    totalPrice: number;
    deliveryStatus: string
    items: OrderItem[]; // 주문 상품 배열
};

// 전체 API 응답의 타입
export type OrdersResponse = {
    orders: Order[];
};