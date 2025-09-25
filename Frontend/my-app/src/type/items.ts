export type ItemDto = {
    itemId: number;
    itemName: string;
    price: number;
    imageUrl: string;
};
export type ItemCreateRequest = {
    
    itemName: string;
    price: number;
    imageUrl?: string;
};