// Test script to check API endpoints
const BASE_URL = 'http://localhost:9999/bw-api';

async function testAPI() {
  try {
    console.log('Testing API endpoints...');
    
    // Test health endpoint
    console.log('\n1. Testing health endpoint...');
    const healthResponse = await fetch(`${BASE_URL}/health`);
    console.log('Health status:', healthResponse.status);
    
    // Test wallets endpoint without filters
    console.log('\n2. Testing wallets endpoint (no filters)...');
    const walletsResponse = await fetch(`${BASE_URL}/wallets`);
    const walletsData = await walletsResponse.json();
    console.log('Wallets response:', JSON.stringify(walletsData, null, 2));
    
    // Test wallets endpoint with COMPANY filter
    console.log('\n3. Testing wallets endpoint (COMPANY filter)...');
    const companyWalletsResponse = await fetch(`${BASE_URL}/wallets?type=COMPANY&active=true`);
    const companyWalletsData = await companyWalletsResponse.json();
    console.log('Company wallets response:', JSON.stringify(companyWalletsData, null, 2));
    
  } catch (error) {
    console.error('Error testing API:', error);
  }
}

testAPI();
