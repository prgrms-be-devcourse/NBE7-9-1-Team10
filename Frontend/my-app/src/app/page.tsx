import Link from 'next/link';

export default function HomePage() {
  return (
    // 전체 화면을 차지하고, 컨텐츠를 수직/수평 중앙에 정렬합니다.
    <main className="flex min-h-screen flex-col items-center justify-center p-24">
      <div className="text-center">
        <h1 className="text-4xl font-bold mb-8">
        Grids & Circle
        </h1>
        
        {/* 버튼들을 담는 컨테이너 */}
        <div className="flex gap-4">
          
          {/* Link 컴포넌트로 클라이언트 사이드 네비게이션을 구현합니다. */}
          <Link href="/adm">
            <button className="px-6 py-3 bg-gray-800 text-white font-semibold rounded-lg shadow-md hover:bg-gray-700 transition-colors">
              관리자 페이지로 이동
            </button>
          </Link>

          {/* 사용자 페이지 경로를 '/items'로 가정했습니다. 필요에 맞게 수정하세요. */}
          <Link href="/items">
             <button className="px-6 py-3 bg-blue-600 text-white font-semibold rounded-lg shadow-md hover:bg-blue-500 transition-colors">
              사용자 페이지로 이동
            </button>
          </Link>
          
        </div>
      </div>
    </main>
  );
}