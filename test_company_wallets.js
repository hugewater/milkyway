const fetch = require('node-fetch');

const testCompanyWallets = async () => {
    const baseUrl = "http://localhost:9999/bw-api";
    const url = `${baseUrl}/wallets?type=COMPANY&active=true`;

    console.log(`Testing API endpoint: ${url}`);

    try {
        const response = await fetch(url);
        console.log(`Response status: ${response.status}`);
        console.log(`Response headers:`, response.headers.raw());
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        console.log("API Response:", JSON.stringify(data, null, 2));
    } catch (error) {
        console.error("Error fetching company wallets:", error);
    }
};

testCompanyWallets();
