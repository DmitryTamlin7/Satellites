import http from 'k6/http';
import { check, sleep } from 'k6';
import { htmlReport } from 'https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js';

export const options = {
    stages: [
        { duration: '15s', target: 50 },
        { duration: '40s', target: 50 },
        { duration: '10s', target: 0 },
    ],
};

const BASE_URL = 'http://localhost:8080/api/constellations';
const HEADERS = { 'Content-Type': 'application/json' };


export default function() {
    const uniqueId = `${__VU}-${__ITER}-${Date.now()}`;
    const constellationName = `Const-Group-${uniqueId}`;
    const imageSatName = `Zenit-Img-${uniqueId}`;
    const commSatName = `Scan-Comm-${uniqueId}`;

    // Создание группировки POST запрос
    let res = http.post(`${BASE_URL}?name=${constellationName}`);
    check(res, { '1. Constellation created (200)': (r) => r.status === 200 });

    // Добавление 2 спутников IMAGE и COMMUNICATION POST запрос
   const imageSatPayload = JSON.stringify({
           constellationName: constellationName,
           satelliteParam: {
               type: "IMAGE",
               name: imageSatName,
               batteryLevel: 100.0,
               resolution: 1440.0
           }
       });
       res = http.post(`${BASE_URL}/satellites`, imageSatPayload, { headers: HEADERS });
       check(res, { '2. IMAGE Satellite added (200)': (r) => r.status === 200 });

   const commSatPayload = JSON.stringify({
           constellationName: constellationName,
           satelliteParam: {
               type: "COMMUNICATION",
               name: commSatName,
               batteryLevel: 95.5,
               bandWidth: 500.0
           }
       });
       res = http.post(`${BASE_URL}/satellites`, commSatPayload, { headers: HEADERS });
       check(res, { '3. COMM Satellite added (200)': (r) => r.status === 200 });

   sleep(0.8)

   //Запуск миссии группировки POST запрос
   const missionPayload = JSON.stringify({ constellationName: constellationName });
       res = http.post(`${BASE_URL}/missions`, missionPayload, { headers: HEADERS });
       check(res, { '5. Mission executed (200)': (r) => r.status === 200 });

   sleep(0.5);

   //Статус группировки GET запрос
   res = http.get(`${BASE_URL}/${constellationName}`);
   check(res, { '4. System overview retrieved (200)': (r) => r.status === 200 });

   //Удаление спутника из группировки DELETE запрос
   res = http.del(`${BASE_URL}/${constellationName}/satellites/${imageSatName}`);
       check(res, { '6. Satellite decommissioned (200)': (r) => r.status === 200 });
   sleep(1);

}

export function handleSummary(data) {
    return {
        "report.html": htmlReport(data),
    };
}