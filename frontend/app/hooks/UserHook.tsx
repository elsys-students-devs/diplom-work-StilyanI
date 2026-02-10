import {useContext} from "react";
import {UserContext} from "@/app/contexts/UserContext";

export const useUser = () => {
    return useContext(UserContext);
};