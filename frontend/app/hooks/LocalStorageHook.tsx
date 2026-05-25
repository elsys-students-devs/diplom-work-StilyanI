"use client";

import { useState } from "react";

export function useLocalStorage(key: string, initialValue: null) {
    const [value, setValue] = useState<string | null>(() => {
        if (globalThis.window === undefined) return initialValue;

        const stored = localStorage.getItem(key);
        return stored && stored !== "null" ? stored : initialValue;
    });

    const setLocalStorageValue = (newValue: string | null) => {
        localStorage.setItem(key, newValue!);

        setValue(newValue);
    };

    return [value, setLocalStorageValue] as const;
}