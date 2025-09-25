export type OrderDto = {

    orderid : number,
    totalPrice : number,
    address : string,
    orderDate: Date,
    items : OrderItems[]

};

export type OrderItems = {
    id: number;
    name: string;
    qty: number;
};
