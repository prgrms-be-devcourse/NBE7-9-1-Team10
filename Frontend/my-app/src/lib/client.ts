import { ItemDto, ItemCreateRequest } from "@/type/items";

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
            return res.json();
        }
    );
}

//일단 테스트 데이터 넣기

export function getItems(): ItemDto[] {
    return [
        { itemId: 1, itemName: "name1", price: 100, imageUrl: "https://tech.wonderwall.kr/_astro/nextjs.D927XOPf.png" },
        { itemId: 2, itemName: "name2", price: 200, imageUrl: "https://tech.wonderwall.kr/_astro/nextjs.D927XOPf.png" },
        { itemId: 3, itemName: "name3", price: 300, imageUrl: "https://tech.wonderwall.kr/_astro/nextjs.D927XOPf.png" }
    ];
}

//상품 추가
export function createItem(itemData: ItemCreateRequest): Promise<ItemDto> {
    return fetchApi('/items', {
        method: 'POST',
        body: JSON.stringify(itemData),
    });
}

