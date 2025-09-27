export type SaleItem = {
    id: number;
    itemName: string;
    totalQty: number;
};
export type Sale = {
    totalSale: number;
    itemSales: SaleItem[];
};