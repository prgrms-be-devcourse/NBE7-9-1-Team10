export type LoginResponse = {
    email: string;
    role: 'ADMIN' | 'USER'; // 역할은 특정 문자열만 오도록 제한
};