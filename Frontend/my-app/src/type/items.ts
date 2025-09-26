export type ItemDto = {
    itemId: number;
    itemName: string;
    price: number;
    imageUrl: string;
    //onSale: 활성화, noQty : 비활성화
    status: string;
};
export type ItemCreateRequest = {
    itemName: string;
    price: number;
    imageUrl?: string;
};