# Stock Screener API - Postman Collection Kullanım Kılavuzu

## 📁 Dosyalar

- `StockScreenerAPI.postman_collection.json` - Ana API collection
- `StockScreener.postman_environment.json` - Environment değişkenleri
- Bu dosya - Kullanım kılavuzu

## 🚀 Kurulum

### 1. Postman'e Import Etme

1. Postman'i açın
2. **Import** butonuna tıklayın
3. **Upload Files** seçin
4. Bu dosyaları seçin:
   - `StockScreenerAPI.postman_collection.json`
   - `StockScreener.postman_environment.json`

### 2. Environment Ayarlama

1. Sağ üst köşedeki environment dropdown'dan **"Stock Screener Environment"** seçin
2. Environment değişkenlerini kontrol edin:
   - `baseUrl`: `http://localhost:8084` (local development)

## 📊 API Endpoints

### 🔧 Stock Screener Endpoints

#### 1. **Get Filter Options**
- **Method:** GET
- **URL:** `/api/v1/search/stocks/filter-options`
- **Açıklama:** Tüm filtreleme seçeneklerini getirir
- **Dönen Veriler:**
  - Industries, countries, sectors, exchanges, currencies
  - Market cap, price, beta range'leri

#### 2. **Filter Stocks** (Multiple Variations)

**a) All Results (Filtresiz)**
```json
{
  "page": 0,
  "size": 10,
  "sortBy": "market_cap",
  "sortOrder": "desc"
}
```

**b) US Technology Stocks**
```json
{
  "countries": ["US"],
  "sectors": ["Technology"],
  "sortBy": "market_cap",
  "sortOrder": "desc",
  "page": 0,
  "size": 5
}
```

**c) Market Cap Range (1B - 100B)**
```json
{
  "minMarketCap": 1000000000,
  "maxMarketCap": 100000000000,
  "sortBy": "market_cap",
  "sortOrder": "desc"
}
```

**d) Price Range ($10 - $500)**
```json
{
  "minPrice": 10.00,
  "maxPrice": 500.00,
  "sortBy": "price",
  "sortOrder": "asc"
}
```

**e) Beta Range (0.5 - 1.5)**
```json
{
  "minBeta": 0.5,
  "maxBeta": 1.5,
  "sortBy": "beta",
  "sortOrder": "asc"
}
```

**f) ETF Only**
```json
{
  "isEtf": true,
  "sortBy": "market_cap",
  "sortOrder": "desc"
}
```

**g) Complex Filter (Tüm parametreler)**
```json
{
  "industries": ["Software - Infrastructure", "Consumer Electronics"],
  "countries": ["US"],
  "sectors": ["Technology"],
  "exchanges": ["NASDAQ", "NYSE"],
  "currencies": ["USD"],
  "minMarketCap": 5000000000,
  "maxMarketCap": 500000000000,
  "minPrice": 50.00,
  "maxPrice": 300.00,
  "minBeta": 0.8,
  "maxBeta": 1.5,
  "isEtf": false,
  "isActivelyTrading": true,
  "isFund": false,
  "sortBy": "market_cap",
  "sortOrder": "desc"
}
```

### 🔍 Legacy Search Endpoints

#### 1. **Search Companies**
- **GET:** `/api/v1/search/companies?query=apple`
- **POST:** `/api/v1/search/companies` (body: `{"query": "microsoft"}`)

#### 2. **Get Company by Symbol**
- **GET:** `/api/v1/search/company/{symbol}/{exchange}`
- **Örnek:** `/api/v1/search/company/AAPL/NASDAQ`

## ⚙️ Request Parameters

### Sorting Options
- `sortBy`: `market_cap`, `price`, `company_name`, `symbol`, `changes`, `beta`
- `sortOrder`: `asc`, `desc`

### Filter Fields
- **String Arrays:** `industries`, `countries`, `sectors`, `exchanges`, `currencies`
- **Number Ranges:** `minMarketCap`, `maxMarketCap`, `minPrice`, `maxPrice`, `minBeta`, `maxBeta`
- **Booleans:** `isEtf`, `isActivelyTrading`, `isFund`, `isAdr`
- **Pagination:** `page` (0-based), `size` (max 100)

## 📋 Test Scripts

Collection'da otomatik test script'leri bulunur:

```javascript
// Her request için çalışan testler
pm.test('Status code is 200', function () {
    pm.response.to.have.status(200);
});

pm.test('Response time is less than 2000ms', function () {
    pm.expect(pm.response.responseTime).to.be.below(2000);
});

pm.test('Content-Type is application/json', function () {
    pm.expect(pm.response.headers.get('Content-Type')).to.include('application/json');
});
```

## 🔄 Response Format

### Stock Filter Response
```json
{
  "stocks": [
    {
      "symbol": "AAPL",
      "companyName": "Apple Inc.",
      "sector": "Technology",
      "industry": "{en=Consumer Electronics, tr=Consumer Electronics (TR)}",
      "country": "US",
      "exchange": "NASDAQ Global Select",
      "exchangeShortName": "NASDAQ",
      "currency": "USD",
      "price": 227.52,
      "marketCap": 3.459236832E+12,
      "beta": 1.24,
      "changes": 1.15,
      "changesPercentage": 0.5100,
      "priceRange": "164.08-237.23",
      "isEtf": null,
      "isActivelyTrading": null,
      "isFund": null,
      "isAdr": null
    }
  ],
  "pagination": {
    "currentPage": 0,
    "totalPages": 7,
    "pageSize": 2,
    "totalElements": 13,
    "hasNext": true,
    "hasPrevious": false
  },
  "filterSummary": {
    "totalMatches": 13,
    "appliedFilters": ["Countries: US", "Sectors: Technology"]
  }
}
```

## 💡 Kullanım İpuçları

1. **İlk olarak** "Get Filter Options" çalıştırarak mevcut değerleri görün
2. **Test Environment'ı** local development için ayarlanmış
3. **Complex Filter** örneği tüm parametreleri gösterir
4. **Pagination** büyük veri setleri için kullanın (max 100 items per page)
5. **Sorting** multiple field'larda mümkün
6. **Cache** filter options endpoint'inde aktif

## 🔧 Troubleshooting

### Service Çalışmıyor mu?
```bash
# Search service'i başlatın
cd search-service
./gradlew bootRun
```

### Environment Değişkenleri
- Local: `http://localhost:8084`
- Production: `https://api.investxpert.com` (henüz aktif değil)

### Common Issues
- **Port 8084** kullanımda mı? Service logs kontrol edin
- **Empty results?** Filter kriterlerini gevşetin
- **500 Error?** Service logs'unda detayları görün