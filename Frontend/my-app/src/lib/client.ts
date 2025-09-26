import { ItemDto, ItemCreateRequest } from "@/type/items";
import { OrdersResponse } from "@/type/orders";
import { UserRole } from "@/type/user";

export function fetchApi(url: string, options?: RequestInit) {
    if (options?.body) {
        const headers = new Headers(options.headers || {});
        headers.set("Content-Type", "application/json");
        options.headers = headers;
    }

    return fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}${url}`, options).then(
        async (res) => {
            if (!res.ok) {
                const rsData = await res.json();
                throw new Error(rsData.msg || "요청 실패");
            }
            if (res.status === 204) {
                // 2. 내용이 없을 경우, .json()을 호출하지 않고 null이나 undefined를 반환합니다.
                return null; 
            }
            return res.json();
        }
    );
}

//일단 테스트 데이터 넣기

/*export function getItems(): ItemDto[] {
    return [
        { itemId: 1, itemName: "name1", price: 100, imageUrl: "https://tech.wonderwall.kr/_astro/nextjs.D927XOPf.png" },
        { itemId: 2, itemName: "name2", price: 200, imageUrl: "https://tech.wonderwall.kr/_astro/nextjs.D927XOPf.png" },
        { itemId: 3, itemName: "name3", price: 300, imageUrl: "https://tech.wonderwall.kr/_astro/nextjs.D927XOPf.png" }
    ];
}*/

export function getItems(): Promise<ItemDto[]> {
    return fetchApi('/api/v1/items');
}

export function getItem(itemId: number): Promise<ItemDto> {
    return fetchApi(`/api/v1/items/${itemId}`);
}
//상품 추가
export function createItem(itemData: ItemCreateRequest): Promise<ItemDto> {
    return fetchApi('/api/v1/items', {
        method: 'POST',
        body: JSON.stringify(itemData),
    });
}
//상품 수정
export function updateItem(itemId: number, itemData: ItemCreateRequest): Promise<ItemDto> {
    return fetchApi(`/api/v1/items/${itemId}`, {
        method: 'PUT',
        body: JSON.stringify(itemData),
    });
}
export function deleteItem(itemId: number):Promise<void>{
    return fetchApi(`/api/v1/items/${itemId}`, {
        method: 'DELETE',
    });
}
export function getOrders(status?: string): Promise<OrdersResponse> {
    let url = '/api/v1/orders';
    if (status && status !== 'all') {
        url = `/api/v1/orders/${status}`;
    }
    return fetchApi(url);
}
export function login(email: string): Promise<UserRole> {
    return fetchApi('/api/v1/login', {
        method: 'POST',
        body: JSON.stringify({ email }),
    });
}