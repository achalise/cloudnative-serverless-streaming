import { useEffect, useState } from "react";
import { CartesianGrid, Legend, Line, LineChart, Tooltip, XAxis, YAxis } from "recharts";

const data = [
    {
        name: 'Page A',
        uv: 4000,
        pv: 2400,
        amt: 2400,
    },
    {
        name: 'Page B',
        uv: 3000,
        pv: 1398,
        amt: 2210,
    },
    {
        name: 'Page C',
        uv: 2000,
        pv: 9800,
        amt: 2290,
    },
    {
        name: 'Page D',
        uv: 2780,
        pv: 3908,
        amt: 2000,
    },
    {
        name: 'Page E',
        uv: 1890,
        pv: 4800,
        amt: 2181,
    },
    {
        name: 'Page F',
        uv: 2390,
        pv: 3800,
        amt: 2500,
    },
    {
        name: 'Page G',
        uv: 3490,
        pv: 4300,
        amt: 2100,
    },
];
const generateData = () => {
    let d = data.shift();
    data.push(d!!);
    return data;

    //return data.map(v => ({v, sort: Math.random()})).sort((a, b) => a.sort - b.sort).map(v => v.v);
}

const SimpleChart = () => {

    const [chartData, setChartData] = useState<any[]>([]);
    const [count, setCount] = useState(0);

    // useEffect(() => {
    //     const interval = setInterval(() => {
    //         setChartData([...generateData()]);
    //     }, 500)
    //     return () => {
    //         clearInterval(interval);
    //     }
    // }, []);

    useEffect(() => {
        const eventSource = new EventSource(`http://localhost:8081/retrieveClaimCount`);
        eventSource.onmessage = m => {
            console.log(`Received event stream`);
            console.log(m);
            let claimCount = {...JSON.parse(m.data), name: `Name_${count}`};
            setCount(prev => prev + 1);
            setChartData((prevState) => {return [...prevState, claimCount]}); 
            console.log(`the count ${count}`);
        }
        return () => {
            eventSource.close();
        }
    }, [])

    return (
        <>
        <p>{count}</p>
        <LineChart
            width={500}
            height={300}
            data={chartData}
            margin={{
                top: 5,
                right: 30,
                left: 20,
                bottom: 5,
            }}
        >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Line type="monotone" dataKey="pv" stroke="#8884d8" activeDot={{ r: 8 }} isAnimationActive={false}/>
            <Line type="monotone" dataKey="uv" stroke="#82ca9d" isAnimationActive={false}/>
            <Line type="monotone" dataKey="count" stroke="#82ca9d" isAnimationActive={false}/>
        </LineChart>
        </>
    );

}

export default SimpleChart;