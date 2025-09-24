# Grids & Circles
## 팀원
- 도석환
- 김희영
- 안병선
- 이창중
- 박민형
## 프로젝트 소개
카페 주문/상품 관리 서비스
### **프로젝트의 핵심 요구사항(MVP):**

- **CRUD 구현**
    - `POST` → Create
    - `GET` → Read
    - `PUT` → Update
    - `DELETE` → Delete
- **데이터베이스 연동**
    - SQL 또는 ORM(JPA) 기반 설계
    - 상품, 주문, 주문상품, 재고, 고객 테이블 설계
## 시나리오
<details>
<summary>요약</summary>

- **상품 목록 확인**
    - 나는 쇼핑을 원하는 사용자로서, 상품 목록을 확인할 수 있다.
        
        → 그래야 원하는 상품을 찾아 장바구니에 담을 수 있다.
        
- **상품 추가**
    - 나는 쇼핑을 원하는 사용자로서, 상품의 추가 버튼을 눌러 장바구니에 상품을 담을 수 있다.
        
        → 그래야 결제 시 필요한 상품들을 모아둘 수 있다.
        
- **상품 제거**
    - 나는 장바구니를 관리하는 사용자로서, 불필요해진 상품을 장바구니에서 제거할 수 있다.
        
        → 그래야 불필요한 결제가 발생하지 않는다.
        
- **상품 수량 조절**
    - 나는 장바구니를 관리하는 사용자로서, 장바구니에서 상품 수량을 +/- 버튼으로 조절할 수 있다.
        
        → 그래야 필요한 만큼만 주문할 수 있다.
        
- **결제 진행**
    - 나는 쇼핑을 완료한 사용자로서, 장바구니의 결제 버튼을 눌러 주문을 확정할 수 있다.
        
        → 그래야 원하는 상품을 실제로 구매할 수 있다.
        

### 관리자(Admin)

1. 상품을 등록한다
2. 상품을 관리한다
3. 상품을 수정한다
4. 상품을 품절(삭제)한다
5. 들어온 주문들을 본다

---

- **상품 등록**
    - 나는 관리자(Admin)로서, 새로운 상품을 등록할 수 있다.
        
        → 그래야 고객들이 선택할 수 있는 상품을 제공할 수 있다.
        
- **상품 관리**
    - 나는 관리자(Admin)로서, 상품 목록을 관리(조회, 검색 등)할 수 있다.
        
        → 그래야 운영 효율성을 유지할 수 있다.
        
- **상품 수정**
    - 나는 관리자(Admin)로서, 기존 상품의 정보를 수정할 수 있다.
        
        → 그래야 잘못된 정보나 변경된 조건을 반영할 수 있다.
        
- **상품 품절(삭제)**
    - 나는 관리자(Admin)로서, 판매 불가 상품을 품절 처리하거나 삭제할 수 있다.
        
        → 그래야 사용자들이 구매 불가능한 상품을 주문하지 않게 된다.
        
- **주문 확인**
    - 나는 관리자(Admin)로서, 들어온 주문 내역을 확인할 수 있다.
        
        → 그래야 주문을 준비하고 고객에게 배송할 수 있다.
      </details>
## 기술 스택
<img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white"/>
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/>
  <br/>
  <img src="https://img.shields.io/badge/Next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white"/>
  <img src="https://img.shields.io/badge/Tailwind CSS-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white"/>

## 프로젝트 구조

## ERD 설계
<img width="1079" height="565" alt="ERD" src="https://github.com/user-attachments/assets/9330ce99-8516-4adf-9fd5-e380b8b86138" />


## 브랜치 규칙
- 이슈 페이지 생성후 관련 브랜치 생성 후 작업
- 기능 단위로 브랜치 생성, 기능 완료 후 Pull Request
- 병합 전 반드시 팀원 리뷰 & 테스트
- 브랜치명 예시
  
| 유형 | 설명 |
|------|------|
| feat/add-product | 새로운 기능 추가 |
| feat/add-login-api | 새로운 기능(로그인) 추가 |
| fix/delete-user | 버그/수정사항 관련 |


## 커밋 컨벤션
### 커밋 유형
- 형식 → 커밋 유형: 간단한 설명
- 커밋 유형은 영어 대문자로 작성
  
| 유형 | 설명 | 예시 |
|------|------|------|
| `feat` | 새로운 기능 추가 | `feat: 상품 목록 조회 API 구현` |
| `fix` | 버그 & 수정사항 관련 | `fix: 에러 수정` |
| `test` | 테스트 코드, 리팩토링 테스트 코드 추가 | `test: 테스트 코드 추가 및 수정` |
| `docs` | 문서 수정 | `docs: 문서 수정` |

