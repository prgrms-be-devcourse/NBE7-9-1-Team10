export type ItemDto = {
    itemId: number;
    itemName: string;
    price: number;
    imageUrl: string;
};
export type ItemCreateRequest = {
    name: string;
    price: number;
    imageUrl?: string;
};