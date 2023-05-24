import appConstants from "@/constants";

const submitFile = async (data: any): Promise<{ParsedResults: string[]}> => {
    const imageString = await convertToBase64String(data);
    console.log(imageString);
    const result = await submit(imageString);

    return result;
}

export default submitFile;

const convertToBase64String = async (file: File) => {
    return new Promise((resolve, reject) => {
        const fr = new FileReader();
        fr.onloadend = () => resolve(fr.result);
        fr.onerror = (err) => reject(err);
        fr.readAsDataURL(file);
      });
}

const submit = async (base64String: any): Promise<{ParsedResults: string[]}> => {
    const fd = new FormData();
    fd.append("base64Image", base64String);
    return fetch(appConstants.ocrApiUrl, {
        method: 'POST',
        body: fd,
        headers: {
            'apiKey': appConstants.ocrApiKey
        }
    }).then(res => res.json())
}
