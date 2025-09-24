"use client";

import {useEffect, useState} from "react";
import {ItemDto} from "@/type/items";
import {fetchApi, getItems} from "@/lib/items";

type CartItem = {
    item: ItemDto;
    count: number;
};



export default function Home() {
    const [cart, setCart] = useState<CartItem[]>([]);
    const [items, setItems] = useState<ItemDto[]>([]);

    useEffect(() => {
        fetchApi("/api/items")
            .then((res) => res.json())
            .then((data) => setItems(data));

        setItems(getItems());
    }, []);


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
            prev.filter((c) => c.item.itemId==target.itemId) // count가 0 이하면 제거
        );
    }


    if (items == null) {
        return (
            <>
                items null
            </>
        );
    }
    return (
        <div>
            <div className={"bg-white"}>
                <h3>상품 목록</h3>
                {
                    items.map((a, i) => {
                        return (
                            <div className={"border-1"} key={items[i].itemId}>
                                <img src={items[i].imageUrl} alt={items[i].status}></img>
                                <h4>{items[i].itemName}</h4>
                                <h4>{items[i].price}원</h4>
                                <button onClick={()=>{addToCart(items[i]);}}>추가</button>

                            </div>
                        )
                    })
                }
            </div>
            <div>
                <h3> Summary </h3>
                {
                    cart.map((a, i) => {
                        return (
                            <div key={i}>
                                <h4>{a.item.itemName}</h4>
                                <h4>{a.count}</h4>
                                <button onClick={()=>{subFromCart(a.item)}}>제거</button>

                            </div>
                        )
                    })
                }
            </div>
        </div>
    );
}
