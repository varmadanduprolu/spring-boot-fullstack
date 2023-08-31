import { Button, ButtonGroup } from '@chakra-ui/react';
import sideBar from "./shared/sideBar.jsx";
import SidebarWithHeader from "./shared/sideBar.jsx";
import {useEffect} from "react";
import {getCustomer} from "./services/Client.js";

const App = ()=> {
    useEffect( ()=>{
        getCustomer().then(res =>{
            console.log(res)
        }).catch(err =>{
            console.log(err)
        })
    },[])
    return(
        <SidebarWithHeader>
            <Button colorScheme='teal' variant='outline'>Click me</Button>
        </SidebarWithHeader>
);
}

export default App;