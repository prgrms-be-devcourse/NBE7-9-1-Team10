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
export function getItems(): ItemDto[]  {

    return [{
            itemId: 1,
            itemName: "name1",
            price: 100,
            imageUrl: "https://www.google.com/url?sa=i&url=https%3A%2F%2Fblog.given-log.com%2Fpost%2Fdev%2F20241015-react-useState&psig=AOvVaw1_0Spntces3qGBsDH_U5Ml&ust=1758762393208000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCPi56tGa8I8DFQAAAAAdAAAAABAE"
        },{
            itemId: 2,
            itemName: "name2",
            price: 200,
            imageUrl: "https://www.google.com/url?sa=i&url=https%3A%2F%2Fblog.given-log.com%2Fpost%2Fdev%2F20241015-react-useState&psig=AOvVaw1_0Spntces3qGBsDH_U5Ml&ust=1758762393208000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCPi56tGa8I8DFQAAAAAdAAAAABAE"
        }];


}

//상품 추가
export function createItem(itemData: ItemCreateRequest): Promise<ItemDto> {
    return fetchApi('/items', {
        method: 'POST',
        body: JSON.stringify(itemData),
    });
}