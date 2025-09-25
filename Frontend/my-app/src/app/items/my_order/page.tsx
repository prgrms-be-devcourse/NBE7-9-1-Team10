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
        const emailInput = form.email.value;


        fetchApi(`/api/v1/orders/user?email=${emailInput}`, {method: "GET",})
            .then((data) => {
                // 데이터 변환
                const processedOrders = data.orders.map((order: any) => ({
                    ...order,
                    orderDate: new Date(order.orderDate) // 문자열을 Date 객체로 변환
                }));

                setOrders(processedOrders);

                if (processedOrders.length === 0) {
                    alert("검색결과가 없습니다.");
                }
            })
            .catch((err) => {
                console.error("에러 발생:", err);
            });



    }

    function checkTime(orderDate: Date): boolean {
        const yesterday2PM = new Date();
        yesterday2PM.setDate(yesterday2PM.getDate() - 1);
        yesterday2PM.setHours(14, 0, 0, 0); // 어제 오후 2시 0분 0초

        // orderDate가 어제 오후 2시보다 이후인지 비교
        return orderDate > yesterday2PM;
    }


    return (
        <div className="flex flex-col justify-center items-start rounded-2xl bg-white md:space-x-8">
            {/* 주문 목록 */}
            <div className="p-4">
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
                        <li key={order.orderid} className="border flex flex-col m-2 p-2 gap-2" >
                            {/* 주문 정보 */}

                            {!checkTime(order.orderDate) && (
                                <div>배송 완료</div>
                            )}

                            {checkTime(order.orderDate) && (
                                <div>배송 예정</div>
                            )}


                            <div className=" gap-15 items-center">
                                {/* 주문 날짜 */}
                                <div>
                                    <div>
                                        Order Date:
                                        {order.orderDate.getMonth() + 1}월
                                        {order.orderDate.getDate()}일
                                        {order.orderDate.getHours()}시
                                        {order.orderDate.getMinutes()}분
                                    </div>

                                </div>
                                <div>
                                    배송 주소 : {order.address}
                                </div>
                                <div>
                                    총 가격 : {order.totalPrice}
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
