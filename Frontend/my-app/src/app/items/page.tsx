"use client";

import {useEffect, useState} from "react";
import {ItemDto} from "@/type/items";
import {fetchApi, getItems} from "@/lib/client";


type CartItem = {
    item: ItemDto;
    count: number;
};



export default function Home() {

    const [cart, setCart] = useState<CartItem[]>([]);
    const [items, setItems] = useState<ItemDto[]>([]);
    const [totalPrice, setTotalPrice] = useState(0);

    useEffect(() => {
        fetchApi("/api/items")
            .then((res) => res.json())
            .then((data) => setItems(data));

        setItems(getItems());
    }, []);

    // cart가 바뀔 때마다 실행
    useEffect(() => {
        let result = 0;
        cart.map((c)=>{
            result+=c.item.price*c.count;
        })
        setTotalPrice(result);
    }, [cart]); // <- cart가 dependency

    function addToCart(newItem: ItemDto) {
        setCart((prev) => {
            // 기존에 있는지 확인
            const existing = prev.find((c) => c.item.itemId === newItem.itemId);

            if (existing) {
                // 있으면 count 증가
                return prev.map((c) =>
                    c.item.itemId === newItem.itemId ? { ...c, count: c.count + 1 } : c
                );
            } else {
                // 없으면 새로 추가
                return [...prev, { item: newItem, count: 1 }];
            }
        });
    }

    function subFromCart(target: ItemDto) {
        setCart((prev) =>
            prev
                .map((c) =>
                    c.item.itemId === target.itemId ? { ...c, count: c.count - 1 } : c
                )
                .filter((c) => c.count > 0) // count가 0 이하면 제거
        );
    }
    function deleteFromCart(target: ItemDto) {
        setCart((prev) =>
            prev.filter((c) => c.item.itemId!=target.itemId)
        );
    }

    const handleSubmit = (e: any) => {
        e.preventDefault();

        const form = e.target;

        const emailInput = form.email;
        const addressInput = form.address;
        const postcodeInput = form.postcode;

        if (emailInput.value.length === 0) {
            alert("이메일을 입력해주세요.");
            emailInput.focus();
        }

        if (addressInput.value.length === 0) {
            alert("주소를 입력해주세요.");
            addressInput.focus();
        }

        if (postcodeInput.value.length === 0) {
            alert("우편번호를 입력해주세요.");
            postcodeInput.focus();
        }



    };


    return (
        <div className="card">
            <div className="row">
                <div className="col-md-8 mt-4 d-flex flex-column align-items-start p-3 pt-0">
                    <h5 className="flex-grow-0"><b>상품 목록</b></h5>
                    <ul className="list-group products">
                        {items.map((item)=>{
                                return (<li className="list-group-item d-flex mt-3" key={item.itemId}>
                                    <div className="col-2">
                                        <img className="img-fluid" src={item.imageUrl} alt="" width={100}/>
                                    </div>
                                    <div className="col">
                                        <div className="row text-muted">{item.itemName}</div>
                                    </div>
                                    <div className="col text-center price">{item.price}</div>
                                    <div className="col text-end action">
                                        <button className="btn btn-small btn-outline-dark" onClick={()=>{
                                            addToCart(item);
                                        }}>추가</button>
                                    </div>
                                </li>);
                            })
                        }


                    </ul>
                </div>
                <div className="col-md-4 summary p-4">
                    <div>
                        <h5 className="m-0 p-0"><b>Summary</b></h5>
                    </div>
                    <hr/>
                    {cart.map((c, index)=>{
                        return (<div className="row" key={index}>
                            <h6 className="p-0">{c.item.itemName} <span className="badge bg-dark text-">{c.count}</span></h6>
                            <button onClick={()=>{addToCart(c.item);}}>+</button>
                            <button onClick={()=>{subFromCart(c.item);}}>-</button>
                            <button onClick={()=>{deleteFromCart(c.item);}}>삭제</button>
                        </div>)
                    })}


                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label htmlFor="email" className="form-label" >이메일</label>
                            <input type="email" className="form-control mb-1" id="email" name="email"/>
                        </div>
                        <div className="mb-3">
                            <label htmlFor="address" className="form-label">주소</label>
                            <input type="text" className="form-control mb-1" id="address" name="address"/>
                        </div>
                        <div className="mb-3">
                            <label htmlFor="postcode" className="form-label">우편번호</label>
                            <input type="text" className="form-control" id="postcode" name="postcode"/>
                        </div>
                        <div>당일 오후 2시 이후의 주문은 다음날 배송을 시작합니다.</div>

                    <div className="row pt-2 pb-2 border-top">
                        <h5 className="col">총금액</h5>
                        <h5 className="col text-end">{totalPrice}원</h5>
                    </div>
                    <button className="btn btn-dark col-12"  type="submit">결제하기</button>
                    </form>
                </div>
            </div>
        </div>
    );
}
