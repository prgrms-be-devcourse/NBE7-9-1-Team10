"use client";

import { useRouter } from "next/navigation";
import { createItem } from "@/lib/client";

export default function NewItemPage() {
  const router = useRouter();

  const handleSubmit = (e: any) => {
    
    e.preventDefault();
    const form = e.target;
    const itemName = form.itemName;
    const price = form.price;
    const imageUrl = form.imageUrl;
    const itemData = { itemName: itemName.value, price: Number(price.value), imageUrl: imageUrl.value };
    
    if (itemName.value.length === 0) {
        alert("이름을 입력해주세요.");
        itemName.focus();
      }
  
      if (price.value.length === 0) {
        alert("가격을 입력해주세요.");
        price.focus();
      }
  
      if (price.value.length < 2) {
        alert("가격은 10원 이상 입력해주세요.");
        price.focus();
        return;
      }

    createItem(itemData).then(() => {
      router.push('/adm');
    })
    .catch((error) => {
        console.error(error);
        alert(error.message || "상품 추가에 실패했습니다.");
      });
}

return (
    <div className="bg-gray-100 min-h-screen flex items-center justify-center p-4">
      <div className="w-full max-w-lg bg-white rounded-xl shadow-md p-8">
        <h1 className="text-2xl font-bold mb-6">새 상품 추가</h1>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label
              htmlFor="itemName"
              className="block text-sm font-medium text-gray-700"
            >
              상품명
            </label>
            <input
              type="text"
              id="itemName"
              name="itemName" // name 속성으로 input을 식별합니다.
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              required
            />
          </div>
          <div>
            <label
              htmlFor="price"
              className="block text-sm font-medium text-gray-700"
            >
              가격
            </label>
            <input
              type="number"
              id="price"
              name="price"
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              required
            />
          </div>
          <div>
            <label
              htmlFor="imageUrl"
              className="block text-sm font-medium text-gray-700"
            >
              이미지 URL
            </label>
            <input
              type="text"
              id="imageUrl"
              name="imageUrl"
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
            />
          </div>
          <div className="flex justify-end pt-4">
            <button
              type="submit"
              className="bg-blue-500 text-white font-semibold py-2 px-4 rounded-lg hover:bg-blue-600 disabled:bg-gray-400"
            >
              저장하기
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}