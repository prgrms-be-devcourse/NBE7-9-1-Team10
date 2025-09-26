"use client";

import {useEffect, useState} from "react";
import {fetchApi} from "@/lib/client";

import {Order} from "@/type/orders";




export default function Home() {

    const [orders, setOrders] = useState<Order[]>([]);

    function handleSubmit(e: any) {
        e.preventDefault();

        const form = e.target;
        const emailInput = form.email.value;


        fetchApi(`/api/v1/orders/user?email=${emailInput}`, {method: "GET",})
            .then((data) => {
                console.log(data);

                setOrders(data.orders);

                if (data.orders.length === 0) {
                    alert("검색결과가 없습니다.");
                }
            })
            .catch((err) => {
                console.error("에러 발생:", err);
            });



    }

    function cutDate(date: string){
        const dates = date.split(".")[0].replace("T"," ");

        return dates;
    }

    function getState(num: string){
       if(num=="0") return "배송준비"
        if(num=="1") return "배송중"

        return "배송완료";
    }

    return (
        <div className="flex flex-col justify-center rounded-2xl bg-white md:space-x-8">
            {/* 주문 목록 */}
            <div className="max-w-4xl mx-auto p-10">
                <h5 className="font-bold">주문 내역 조회</h5>
                <hr/>
                {/* 검색창 */}
                <div className={"m-8"}>
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <input type="email" className="form-control border rounded-md p-2 w-full bg-white"
                                   id="email" name="email"/>
                        </div>
                        <button className="btn btn-dark w-full bg-black text-white p-2 rounded-md" type="submit">검색
                        </button>
                    </form>
                </div>

                <ul className="flex flex-col">
                    {orders != null && orders.map((order) => (
                        <li key={order.orderId} className="border flex flex-col m-2 p-2 gap-2" >
                            {/* 주문 정보 */}

                            <div>{getState(order.deliveryStatus)}</div>


                            <div className="items-center">
                                {/* 주문 날짜 */}
                                <div>
                                    <div>
                                        Order Date: {
                                        cutDate(order.orderDate)
                                    }
                                    </div>

                                </div>
                                <div>
                                    배송 주소 : {order.address}
                                </div>
                                <div>
                                    총 가격 : {order.totalPrice}원
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
