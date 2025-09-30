# ☕ Grids & Circles
> Spring Boot와 Next.js 기반의 카페 주문 및 상품 관리 시스템

## 📖 프로젝트 소개
작은 로컬 카페 **`Grids & Circles`** 입니다. 고객들은 온라인 웹사이트를 통해 커피 원두 패키지를 주문합니다. 우리는 매일 전날 오후 2시부터 당일 오후 2시까지의 주문을 취합하여 배송하는 온라인 주문 플랫폼입니다.


## 🛠️ 기술 스택
<img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white"/>
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/> 
  <br/>
  <img src="https://img.shields.io/badge/Next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white"/>  <img src="https://img.shields.io/badge/Tailwind CSS-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white"/>  <br/>
 <img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white"/> <img src="https://img.shields.io/badge/Mockito-C5D9C8?style=for-the-badge"/>
## 👥 팀원 소개

<div align="center">

| <img src="https://github.com/do04080.png" width="120px;" alt=""/> | <img src="https://github.com/rogrhrh.png" width="120px;" alt=""/> | <img src="https://github.com/kheeyoung.png" width="120px;" alt=""/> | <img src="https://github.com/DEV-Cheeze.png" width="120px;" alt=""/> | <img src="https://github.com/minibr.png" width="120px;" alt=""/> |
| :----------------------------------------------------: | :----------------------------------------------------: | :----------------------------------------------------: | :----------------------------------------------------: | :----------------------------------------------------: |
|   [도석환](https://github.com/do04080)   |   [안병선](https://github.com/rogrhrh)   |   [김희영](https://github.com/kheeyoung)   |   [이창중](https://github.com/DEV-Cheeze)   |   [박민형](https://github.com/minibr)   |

</div>
  
## 🎯 핵심 기능 (MVP)
- **고객**: 상품 조회, 장바구니 관리, 주문 생성, 이메일 기반 주문 내역 조회
- **관리자**: 상품 CRUD, 일별 주문 통합 조회, 고객별 주문 검색, 배송 상태 관리
- **주문 시스템**: 14시 기준 주문 취합, 배송 상태 추적(준비→배송중→완료), 주문 당시 가격 고정
## 📂프로젝트 구조
```
backend/
├── src/main/java/com/example/cafe
│   ├── domain
│   │   ├── item
│   │   │   ├── controller
│   │   │   ├── dto
│   │   │   ├── entity
│   │   │   ├── repository
│   │   │   └── service
│   │   └── order
│   │       ├── controller
│   │       ├── dto
│   │       ├── entity
│   │       ├── repository
│   │       └── service
│   └── global
│       └── config
└── src/main/resources

frontend/
├── app
│   ├── page.tsx
│   ├── orders
│   └── search
└── components
    └── ui
```
## 🗄️ ERD

<div align="center">
  <img width="700" alt="ERD 다이어그램" src="https://github.com/user-attachments/assets/0a6f547d-b6b0-460e-a42a-c4134f456c40" />
</div>

## 🤝 협업 방식

### 🔀 Branch 전략
> Issue 생성 → Branch 생성 → 개발 → Commit → Push → PR → Code Review → Merge
- 이슈 페이지 생성후 관련 브랜치 생성 후 작업
- 기능 단위로 브랜치 생성, 기능 완료 후 Pull Request
- 병합 전 반드시 팀원 리뷰 & 테스트
- 브랜치명 예시
  
| 유형 | 설명 |
|------|------|
| feat/add-product | 새로운 기능 추가 |
| feat/add-login-api | 새로운 기능(로그인) 추가 |
| fix/delete-user | 버그/수정사항 관련 |


### 📝 Commit Convention
- 형식 → 커밋 유형: 간단한 설명
- 커밋 유형은 영어 대문자로 작성
  
| 유형 | 설명 | 예시 |
|------|------|------|
| `feat` | 새로운 기능 추가 | `feat: 상품 목록 조회 API 구현` |
| `fix` | 버그 & 수정사항 관련 | `fix: 에러 수정` |
| `test` | 테스트 코드, 리팩토링 테스트 코드 추가 | `test: 테스트 코드 추가 및 수정` |
| `docs` | 문서 수정 | `docs: 문서 수정` |
| `refactor` | 코드 리팩토링 | `refactor: ItemService 로직 개선` |

### 🔍 Code Review 규칙
- 최소 1명 이상의 approve 필요
- 테스트 통과 필수
- 코드 컨벤션 준수 확인

