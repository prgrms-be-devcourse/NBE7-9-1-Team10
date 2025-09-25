"use client";

import {useEffect, useState} from "react";
import {fetchApi, getOrder} from "@/lib/client";
import {useParams} from "next/navigation";
import {OrderDto} from "@/type/orders";
import {ItemDto} from "@/type/items";



export default function Home() {

    const [orders, setOrders] = useState<OrderDto[]>([]);

    function handleSubmit(e: any) {
        e.preventDefault();

        const form = e.target;
        const emailInput = form.email;


        fetchApi(`/api/v1/orders/user?email=${encodeURIComponent(emailInput)}`, {
            method: "GET",
        })
            .then((data) => {
                console.log("받은 데이터:", data);
                setOrders(data.orders);
            })
            .catch((err) => {
                console.error("에러 발생:", err);
            });

    }

    return (
        <div className="flex flex-col md:flex-row justify-center items-start rounded-2xl bg-white md:space-x-8">
            {/* 주문 목록 */}
            <div className="p-4">
                <h5 className="font-bold">주문 내역 조회</h5>
                <hr/>
                {/* 검색창 */}
                <div className={"m-8"}>
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <input type="email" className="form-control mb-1 border rounded-md p-2 w-full bg-white"
                                   id="email" name="email"/>
                        </div>
                        <button className="btn btn-dark w-full bg-black text-white p-2 rounded-md" type="submit">검색
                        </button>
                    </form>
                </div>
                <ul className="flex flex-col">
                    {orders != null && orders.map((order) => (
                        <li className="border flex flex-col m-2 p-2 gap-2" key={order.orderid}>
                            {/* 주문 정보 */}

                            <div className="flex flex-row gap-15 items-center">
                                {/* 주문 날짜 */}
                                <div>
                                    Order Date:
                                    {order.orderDate.getMonth()}
                                    월
                                    {order.orderDate.getDay()}
                                    일
                                    {order.orderDate.getHours()}
                                    시
                                    {order.orderDate.getMinutes()}
                                    분
                                </div>


                            {/* 주문 아이템 리스트 */}
                            {order.items != null && order.items.length > 0 && (
                                <ul className="flex flex-col ml-4 mt-2 gap-1">
                                    {order.items.map((item) => (
                                        <li key={item.id} className="border p-2 flex justify-between items-center">

                                            <div>{item.name}</div>
                                            <div>{item.qty}개</div>

                                        </li>
                                    ))}
                                </ul>
                            )}
                            </div>


                        </li>
                    ))}
                </ul>

            </div>


        </div>

    );
}
