import { useEffect, useState } from "react";

const Charts = () => {
    const [loading, setLoading] = useState(false);
    const [data, setData] = useState({d: 'test'});

    useEffect(() => {
        setLoading(true)
        fetch('/cats')
          .then((res) => res.json())
          .then((data) => {
            console.log(`data ${JSON.stringify(data)}`);
            setData({...data, d: data.data[0]})
            setLoading(false)
          })
          .catch(error => {
            setData({...data, d: 'Error occurred'})
          })
      }, [])
    return (
        <>
          The charts component {data.d}
        </>
    )
}
export default Charts;