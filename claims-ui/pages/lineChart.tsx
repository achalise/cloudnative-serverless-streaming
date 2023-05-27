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
    const [items, setItems] = useState<any[]>([]);
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
        const groupByTime = (items: any[]) => {
            let grouped = {} as any;
            items.forEach( item => {
                if (grouped[item.name]) { grouped[item.name].push(item) } 
                else {
                    grouped[item.name] = [];
                    grouped[item.name].push(item);
                }
            });
            return grouped;
        }

        const aggregate = (items: any[]) => {
            let res = items.reduce((aggregate: any, current: any) => {
                aggregate.countB = aggregate.countB + current.countB;
                aggregate.countC = aggregate.countC + current.countC;
                aggregate.countA = aggregate.countA + current.countA;
                aggregate.name = current.name;
                return aggregate;
            }, { countA: 0, countB: 0, countC: 0, name: '' });
            return res;
        }

        const enrich = (claimCount: any) => {
            let enrichedClaimCount = {
                ...claimCount,
                countA: claimCount.claimType === 'A' ? claimCount.count : 0,
                countB: claimCount.claimType === 'B' ? claimCount.count : 0,
                countC: claimCount.claimType === 'C' ? claimCount.count : 0,
                name: claimCount.timeStamp
            }
            return enrichedClaimCount;
        }
        eventSource.onmessage = m => {
            console.log(`Received event stream`);
            console.log(m);
            let claimCount = JSON.parse(m.data);
            let enrichedClaimCount = enrich(claimCount);
            setItems(items => {
                let newItems = [...items, enrichedClaimCount]
                if (newItems.length === 3) {
                    updateChartData(newItems);
                    return [];
                } else {
                    return newItems;
                }
            })

            const updateChartData = (newItems: any) => {

                setChartData((prevState: any[]) => {
                    while(prevState.length > 10) { prevState.shift(); }
                    let res = aggregate(newItems);
                    prevState.push(res);
                    
                    let grouped = groupByTime(prevState);
                    let aggregated = [] as any;
                    Object.keys(grouped).forEach (
                        key => {
                            let temp = grouped[key];
                            let res = temp.reduce((aggregate: any, current: any) => {
                                aggregate.countB = aggregate.countB + current.countB;
                                aggregate.countC = aggregate.countC + current.countC;
                                aggregate.countA = aggregate.countA + current.countA;
                                aggregate.name = key; 
                                return aggregate;
                            }, { countA: 0, countB: 0, countC: 0, name: '' });
                            aggregated.push(res);
                        }
                    )
                    
                    return [...aggregated];
                });
            }
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
                <Line type="monotone" dataKey="countA" stroke="#8884d8" isAnimationActive={false} />
                <Line type="monotone" dataKey="countB" stroke="#999a9d" isAnimationActive={false} />
                <Line type="monotone" dataKey="countC" stroke="#82ca9d" isAnimationActive={false} />
            </LineChart>
        </>
    );

}

export default SimpleChart;