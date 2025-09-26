"use client";

import {useEffect, useState} from "react";
import {ItemDto} from "@/type/items";
import {fetchApi} from "@/lib/client";
import {Sale} from "@/type/sales";


type CartItem = {
    item: ItemDto;
    count: number;
};


export default function Home() {

    const [cart, setCart] = useState<CartItem[]>([]);
    const [items, setItems] = useState<ItemDto[]>([]);
    const [totalPrice, setTotalPrice] = useState(0);
    const [sale, setSale]= useState<Sale| null>(null);


    useEffect(() => {
        fetchApi("/api/v1/items", {
            method: "GET"
        })
            .then((data) => {
                setItems(data);
            })
            .catch((err) => {
                console.error("에러 발생:", err);
            });

    }, []);

    useEffect(() => {
        fetchApi("/api/v1/orders/sales", {
            method: "GET"
        })
            .then((data) => {
                console.log(data);
                setSale(data);
            })
            .catch((err) => {

            });
    }, []);


    // cart가 바뀔 때마다 실행
    useEffect(() => {
        let result = 0;
        cart.map((c) => {
            result += c.item.price * c.count;
        })
        setTotalPrice(result);
    }, [cart]);

    function addToCart(newItem: ItemDto) {
        setCart((prev) => {
            // 기존에 있는지 확인
            const existing = prev.find((c) => c.item.itemId === newItem.itemId);

            if (existing) {
                // 있으면 count 증가
                return prev.map((c) =>
                    c.item.itemId === newItem.itemId ? {...c, count: c.count + 1} : c
                );
            } else {
                // 없으면 새로 추가
                return [...prev, {item: newItem, count: 1}];
            }
        });
    }

    function subFromCart(target: ItemDto) {
        setCart((prev) =>
            prev
                .map((c) =>
                    c.item.itemId === target.itemId ? {...c, count: c.count - 1} : c
                )
                .filter((c) => c.count > 0) // count가 0 이하면 제거
        );
    }

    function deleteFromCart(target: ItemDto) {
        setCart((prev) =>
            prev.filter((c) => c.item.itemId != target.itemId)
        );
    }

    const handleSubmit = (e: any) => {
        e.preventDefault();

        const form = e.target;

        const emailInput = form.email;
        const addressInput = form.address;

        if (emailInput.value.length === 0) {
            alert("이메일을 입력해주세요.");
            emailInput.focus();
            return;
        }

        if (addressInput.value.length === 0) {
            alert("주소를 입력해주세요.");
            addressInput.focus();
            return;
        }

        if (cart.length === 0) {
            alert("구매하실 제품을 추가해주세요.");
            return;

        }


        const orderItems = cart.map(item => ({
            id: item.item.itemId,
            qty: item.count
        }));

        const requestBody = JSON.stringify({
            email: emailInput.value,
            totalPrice: totalPrice,
            address: addressInput.value,
            items: orderItems
        });


        fetchApi(`/api/v1/orders`, {
            method: "POST",
            body: requestBody,
        }).then((data) => {
            
            alert("주문 완료");
        })
            .catch((err) => {
                alert("오류 : "+err);
            });

    };


    return (
        <div>
        <div className="flex md:flex-row justify-center rounded-2xl bg-white md:space-x-8 ">
            {/* 상품 목록 */}
            <div className="p-4 flex-1">
                <h5 className="font-bold">상품 목록</h5>
                <hr/>
                <ul className="flex flex-col">
                    {items != null && items.map((item) => (
                        <li className="border flex flex-row m-2 p-2 justify-between items-center"
                            key={item.itemId}>

                            <img className="" src={item.imageUrl} alt="" width={50}/>

                            <div className="font-semibold">{item.itemName}</div>

                            <div className="text-gray-600">{item.price}원</div>

                            <button className="bg-black text-white text-sm p-1 border rounded-sm" onClick={() => addToCart(item)}>추가</button>


                        </li>
                    ))}
                </ul>
            </div>

            {/* Summary and Form */}
            <div className="p-4 bg-gray-200 rounded-r-2xl flex-shrink-0">
                <div>
                    <h5 className="font-bold m-0 p-0">Summary</h5>
                </div>
                <hr/>
                {cart.map((c, index) => (
                    <div className="flex justify-between items-center my-2" key={index}>
                        <div className={"flex flex-row gap-3"}>
                            <h6 className="font-semibold">
                                {c.item.itemName}
                            </h6>
                            <span className="badge bg-black text-white rounded-sm text-sm p-1">{c.count}개</span>
                        </div>

                        <div>
                            <button className="p-1 px-2 border rounded-full mr-1 bg-black text-white"
                                    onClick={() => addToCart(c.item)}>+
                            </button>
                            <button className="p-1 px-2 border rounded-full mr-1 bg-black text-white"
                                    onClick={() => subFromCart(c.item)}>-
                            </button>
                            <button className="p-1 px-2 rounded-full bg-white"
                                    onClick={() => deleteFromCart(c.item)}>삭제
                            </button>
                        </div>
                    </div>
                ))}

                <form onSubmit={handleSubmit} className={"my-5"}>
                    <div className="mb-3">
                        <label htmlFor="email" className="form-label">이메일</label>
                        <input type="email" className="form-control mb-1 border rounded-md p-2 w-full bg-white"
                               id="email" name="email"/>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="address" className="form-label">주소</label>
                        <input type="text" className="form-control mb-1 border rounded-md p-2 w-full bg-white"
                               id="address" name="address"/>
                    </div>
                    <div>당일 오후 2시 이후의 주문은 다음날 배송을 시작합니다.</div>
                    <div className="flex justify-between items-center py-2 border-t mt-2">
                        <h5 className="font-bold">총금액</h5>
                        <h5 className="font-bold text-right">{totalPrice}원</h5>
                    </div>
                    <button className="btn btn-dark w-full bg-black text-white p-2 rounded-md mt-2" type="submit">결제하기
                    </button>

                </form>


            </div>

        </div>
            {/*판매 순위*/}
            <div className={"bg-gray-400 text-white flex justify-center flex-col items-center"}>
                {sale != null ? (
                    <>
                        <div>실시간 판매 순위</div>
                        {sale.itemSales.map((item, index) => (
                            <div key={item.id}>
                                {index + 1}위 {item.name}
                            </div>
                        ))}
                    </>
                ) : (
                    <div>
                        오늘의 추천 제품 : {items.length > 0 ? items[0].itemName : "로딩 중..."}
                    </div>
                )}


            </div>
        </div>

    );
}
