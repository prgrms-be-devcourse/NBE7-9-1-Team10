export type SaleItem = {
    id: number;
    name: string;
    totalQty: number;
};
export type Sale = {
    totalSale: number;
    itemSales: SaleItem[];
};