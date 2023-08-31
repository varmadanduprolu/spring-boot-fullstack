import axios, {Axios} from "axios";

export const getCustomer = async ()=>{
    try {
       return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customer`)
    }catch (e){
        throw e;
    }
}