'use client';

import { createContext, useContext, useState, ReactNode } from 'react';
import { UserRole as User } from "@/type/user";



// Context에 담길 값들의 타입 정의
type AuthContextType = {
    user: User | null;
    login: (userData: User) => void;
    logout: () => void;
};

// 1. Context 생성
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// 2. AuthProvider 컴포넌트: 앱 전체에 사용자 정보를 제공하는 역할
export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<User | null>(null);

    const login = (userData: User) => {
        setUser(userData);
    };

    const logout = () => {
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth는 반드시 AuthProvider 안에서 사용되어야 합니다.');
    }
    return context;
}
