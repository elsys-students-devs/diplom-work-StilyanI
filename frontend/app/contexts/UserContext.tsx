"use client";

import React, {createContext, useState, ReactNode, useEffect, useMemo} from "react";
import {useLocalStorage} from "@/app/hooks/LocalStorageHook";
import {getUser} from "@/app/services/AuthService";

export type User = {
    id: string;
    username: string;
};

type UserContextType = {
    user: User | null;
    setUser: (user: User | null) => void;
};

export const UserContext = createContext<UserContextType>({
    user: null,
    setUser: () => {},
});

export const UserProvider = ({ children }: { children: ReactNode }) => {
    const [id] = useLocalStorage("userId", null);
    const [user, setUser] = useState<User | null>(null);

    useEffect(() => {
        const init = async () => {
            if (!id) {
                setUser(null);
                return;
            }

            try {
                const res = await getUser(id);
                setUser(res.data);
            } catch (error) {
                console.error(error);
            }
        }

        init();
    }, [id]);

    const userMemo = useMemo(
        () => ({ user, setUser }),
        [user]
    );

    return (
        <UserContext.Provider value={userMemo}>
            {children}
        </UserContext.Provider>
    );
};
