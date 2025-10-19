# Stock Screener API - Frontend Developer Documentation

## 🔗 Base URL
```
http://localhost:8084/api/v1/search
```

## 📊 Stock Screener Endpoints

### 1. Get Filter Options

**Endpoint:** `GET /stocks/filter-options`

**Purpose:** Frontend'in filter dropdown'larını doldurmak için tüm mevcut filter seçeneklerini getirir.

**Request:**
```bash
GET /api/v1/search/stocks/filter-options
Content-Type: application/json
```

**Response:**
```json
{
  "industries": [
    "Auto - Manufacturers",
    "Banks - Regional",
    "Beverages - Alcoholic",
    "Chemicals",
    "Conglomerates",
    "Consumer Electronics",
    "Internet Content & Information",
    "Software - Infrastructure",
    "Specialty Retail"
  ],
  "countries": ["DE", "TR", "US"],
  "sectors": [
    "Basic Materials",
    "Communication Services",
    "Consumer Cyclical",
    "Consumer Defensive",
    "Financial Services",
    "Industrials",
    "Technology"
  ],
  "exchanges": [
    "Frankfurt Stock Exchange",
    "Istanbul Stock Exchange",
    "NASDAQ Global Select",
    "New York Stock Exchange"
  ],
  "currencies": ["EUR", "TRY", "USD"],
  "rangeOptions": {
    "marketCap": {"min": 0, "max": 0},
    "price": {"min": 0.0, "max": 0.0},
    "beta": {"min": 0.0, "max": 0.0}
  }
}
```

**Frontend Usage:**
- Page load'da çağır
- Dropdown'ları doldur
- Range slider min/max değerleri ayarla

---

### 2. Filter Stocks (Ana Endpoint)

**Endpoint:** `POST /stocks/filter`

**Purpose:** Kullanıcının seçtiği filtrelere göre hisse senetlerini getir.

#### 2.1 Tüm Hisseler (Filtresiz)

**Request:**
```json
POST /api/v1/search/stocks/filter
Content-Type: application/json

{
  "page": 0,
  "size": 10,
  "sortBy": "market_cap",
  "sortOrder": "desc"
}
```

**Response:**
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
    },
    {
      "symbol": "MSFT",
      "companyName": "Microsoft Corporation",
      "sector": "Technology",
      "industry": "{en=Software - Infrastructure, tr=Software - Infrastructure (TR)}",
      "country": "US",
      "exchange": "NASDAQ Global Select",
      "exchangeShortName": "NASDAQ",
      "currency": "USD",
      "price": 431.31,
      "marketCap": 3.2059444824E+12,
      "beta": 0.9,
      "changes": -0.8,
      "changesPercentage": -0.1900,
      "priceRange": "309.45-468.35",
      "isEtf": null,
      "isActivelyTrading": null,
      "isFund": null,
      "isAdr": null
    }
  ],
  "pagination": {
    "currentPage": 0,
    "totalPages": 7,
    "pageSize": 10,
    "totalElements": 13,
    "hasNext": true,
    "hasPrevious": false
  },
  "filterSummary": {
    "totalMatches": 13,
    "appliedFilters": []
  }
}
```

#### 2.2 US Technology Hisseleri

**Request:**
```json
POST /api/v1/search/stocks/filter
Content-Type: application/json

{
  "countries": ["US"],
  "sectors": ["Technology"],
  "sortBy": "market_cap",
  "sortOrder": "desc",
  "page": 0,
  "size": 5
}
```

**Response:**
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
    },
    {
      "symbol": "MSFT",
      "companyName": "Microsoft Corporation",
      "sector": "Technology",
      "industry": "{en=Software - Infrastructure, tr=Software - Infrastructure (TR)}",
      "country": "US",
      "exchange": "NASDAQ Global Select",
      "exchangeShortName": "NASDAQ",
      "currency": "USD",
      "price": 431.31,
      "marketCap": 3.2059444824E+12,
      "beta": 0.9,
      "changes": -0.8,
      "changesPercentage": -0.1900,
      "priceRange": "309.45-468.35",
      "isEtf": null,
      "isActivelyTrading": null,
      "isFund": null,
      "isAdr": null
    },
    {
      "symbol": "ORCL",
      "companyName": "Oracle Corporation",
      "sector": "Technology",
      "industry": "{en=Software - Infrastructure, tr=Software - Infrastructure (TR)}",
      "country": "US",
      "exchange": "New York Stock Exchange",
      "exchangeShortName": "NYSE",
      "currency": "USD",
      "price": 168.1,
      "marketCap": 4.65800057E+11,
      "beta": 1.0,
      "changes": 1.82,
      "changesPercentage": 1.0800,
      "priceRange": "99.26-173.99",
      "isEtf": null,
      "isActivelyTrading": null,
      "isFund": null,
      "isAdr": null
    }
  ],
  "pagination": {
    "currentPage": 0,
    "totalPages": 1,
    "pageSize": 5,
    "totalElements": 3,
    "hasNext": false,
    "hasPrevious": false
  },
  "filterSummary": {
    "totalMatches": 3,
    "appliedFilters": ["Countries: US", "Sectors: Technology"]
  }
}
```

#### 2.3 Market Cap Aralığı (1B - 100B)

**Request:**
```json
POST /api/v1/search/stocks/filter
Content-Type: application/json

{
  "minMarketCap": 1000000000,
  "maxMarketCap": 100000000000,
  "sortBy": "market_cap",
  "sortOrder": "desc",
  "page": 0,
  "size": 10
}
```

#### 2.4 Fiyat Aralığı ($10 - $500)

**Request:**
```json
POST /api/v1/search/stocks/filter
Content-Type: application/json

{
  "minPrice": 10.00,
  "maxPrice": 500.00,
  "sortBy": "price",
  "sortOrder": "asc",
  "page": 0,
  "size": 15
}
```

#### 2.5 Çoklu Endüstri Filtresi

**Request:**
```json
POST /api/v1/search/stocks/filter
Content-Type: application/json

{
  "industries": ["Consumer Electronics", "Software - Infrastructure"],
  "countries": ["US"],
  "sortBy": "company_name",
  "sortOrder": "asc",
  "page": 0,
  "size": 10
}
```

#### 2.6 Beta Aralığı (Düşük Risk)

**Request:**
```json
POST /api/v1/search/stocks/filter
Content-Type: application/json

{
  "minBeta": 0.5,
  "maxBeta": 1.5,
  "countries": ["US"],
  "sortBy": "beta",
  "sortOrder": "asc",
  "page": 0,
  "size": 10
}
```

#### 2.7 Sadece ETF'ler

**Request:**
```json
POST /api/v1/search/stocks/filter
Content-Type: application/json

{
  "isEtf": true,
  "sortBy": "market_cap",
  "sortOrder": "desc",
  "page": 0,
  "size": 20
}
```

#### 2.8 Aktif İşlem Gören Hisseler

**Request:**
```json
POST /api/v1/search/stocks/filter
Content-Type: application/json

{
  "isActivelyTrading": true,
  "countries": ["US", "DE"],
  "currencies": ["USD", "EUR"],
  "sortBy": "changes",
  "sortOrder": "desc",
  "page": 0,
  "size": 10
}
```

#### 2.9 Kompleks Filtre (Tüm Parametreler)

**Request:**
```json
POST /api/v1/search/stocks/filter
Content-Type: application/json

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
  "sortOrder": "desc",
  "page": 0,
  "size": 5
}
```

---

### 3. Legacy Company Search

#### 3.1 Şirket Arama (GET)

**Endpoint:** `GET /companies?query={searchTerm}`

**Request:**
```bash
GET /api/v1/search/companies?query=apple
Content-Type: application/json
```

**Response:**
```json
{
  "stocks": [
    {
      "companyName": "Apple Inc.",
      "listings": [
        {
          "symbol": "AAPL",
          "exchange": "NASDAQ",
          "currency": "USD",
          "type": "STOCK",
          "country": "US",
          "exchange_country": "US"
        }
      ]
    }
  ],
  "analyses": []
}
```

#### 3.2 Şirket Arama (POST)

**Request:**
```json
POST /api/v1/search/companies
Content-Type: application/json

{
  "query": "microsoft"
}
```

#### 3.3 Symbol ve Exchange ile Şirket Getir

**Endpoint:** `GET /company/{symbol}/{exchange}`

**Request:**
```bash
GET /api/v1/search/company/AAPL/NASDAQ
Content-Type: application/json
```

---

## 📝 Request Parameters Detayları

### Filter Parameters
```typescript
interface StockFilterRequest {
  // Array Filters
  industries?: string[];      // ["Consumer Electronics", "Software - Infrastructure"]
  countries?: string[];       // ["US", "DE", "TR"]
  sectors?: string[];         // ["Technology", "Financial Services"]
  exchanges?: string[];       // ["NASDAQ", "NYSE"]
  currencies?: string[];      // ["USD", "EUR", "TRY"]

  // Range Filters
  minMarketCap?: number;      // 1000000000 (1B)
  maxMarketCap?: number;      // 100000000000 (100B)
  minPrice?: number;          // 10.00
  maxPrice?: number;          // 500.00
  minBeta?: number;           // 0.5
  maxBeta?: number;           // 1.5

  // Boolean Filters
  isEtf?: boolean;           // true/false
  isActivelyTrading?: boolean; // true/false
  isFund?: boolean;          // true/false
  isAdr?: boolean;           // true/false

  // Sorting & Pagination
  sortBy?: string;           // "market_cap", "price", "company_name", "symbol", "changes", "beta"
  sortOrder?: string;        // "asc", "desc"
  page?: number;            // 0, 1, 2, ... (0-based)
  size?: number;            // 1-100 (default: 20)
}
```

### Sort Options
- `market_cap` - Piyasa değerine göre
- `price` - Fiyata göre
- `company_name` - Şirket adına göre
- `symbol` - Sembole göre
- `changes` - Günlük değişime göre
- `beta` - Beta değerine göre

---

## 📱 Frontend Implementation Örnekleri

### React/TypeScript Example

```typescript
// API Service
class StockScreenerAPI {
  private baseURL = 'http://localhost:8084/api/v1/search';

  async getFilterOptions(): Promise<FilterOptionsResponse> {
    const response = await fetch(`${this.baseURL}/stocks/filter-options`);
    return response.json();
  }

  async filterStocks(filters: StockFilterRequest): Promise<StockScreenerResponse> {
    const response = await fetch(`${this.baseURL}/stocks/filter`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(filters)
    });
    return response.json();
  }
}

// Component Usage
const StockScreener: React.FC = () => {
  const [filters, setFilters] = useState<StockFilterRequest>({
    page: 0,
    size: 20,
    sortBy: 'market_cap',
    sortOrder: 'desc'
  });

  const [stocks, setStocks] = useState<StockItem[]>([]);
  const [pagination, setPagination] = useState<PaginationInfo | null>(null);

  const handleFilter = async () => {
    const api = new StockScreenerAPI();
    const response = await api.filterStocks(filters);
    setStocks(response.stocks);
    setPagination(response.pagination);
  };

  // Component render...
};
```

### Vue.js Example

```javascript
// Composable
export function useStockScreener() {
  const stocks = ref([]);
  const pagination = ref(null);
  const loading = ref(false);

  const filterStocks = async (filters) => {
    loading.value = true;
    try {
      const response = await fetch('/api/v1/search/stocks/filter', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(filters)
      });
      const data = await response.json();
      stocks.value = data.stocks;
      pagination.value = data.pagination;
    } finally {
      loading.value = false;
    }
  };

  return { stocks, pagination, loading, filterStocks };
}
```

---

## 🎨 UI Component Önerileri

### Filter Panel
```jsx
<FilterPanel>
  <MultiSelect
    label="Industries"
    options={filterOptions.industries}
    value={filters.industries}
    onChange={(value) => setFilters({...filters, industries: value})}
  />

  <MultiSelect
    label="Countries"
    options={filterOptions.countries}
    value={filters.countries}
    onChange={(value) => setFilters({...filters, countries: value})}
  />

  <RangeSlider
    label="Market Cap"
    min={filterOptions.rangeOptions.marketCap.min}
    max={filterOptions.rangeOptions.marketCap.max}
    value={[filters.minMarketCap, filters.maxMarketCap]}
    onChange={([min, max]) => setFilters({...filters, minMarketCap: min, maxMarketCap: max})}
    formatter={(value) => formatMarketCap(value)}
  />

  <RangeSlider
    label="Price Range"
    min={filterOptions.rangeOptions.price.min}
    max={filterOptions.rangeOptions.price.max}
    value={[filters.minPrice, filters.maxPrice]}
    onChange={([min, max]) => setFilters({...filters, minPrice: min, maxPrice: max})}
    formatter={(value) => `$${value}`}
  />
</FilterPanel>
```

### Results Table
```jsx
<Table>
  <TableHeader>
    <SortableColumn field="symbol">Symbol</SortableColumn>
    <SortableColumn field="company_name">Company</SortableColumn>
    <SortableColumn field="price">Price</SortableColumn>
    <SortableColumn field="market_cap">Market Cap</SortableColumn>
    <SortableColumn field="changes">Change</SortableColumn>
    <SortableColumn field="beta">Beta</SortableColumn>
  </TableHeader>
  <TableBody>
    {stocks.map(stock => (
      <TableRow key={stock.symbol}>
        <TableCell>{stock.symbol}</TableCell>
        <TableCell>{stock.companyName}</TableCell>
        <TableCell>${stock.price}</TableCell>
        <TableCell>{formatMarketCap(stock.marketCap)}</TableCell>
        <TableCell className={stock.changes >= 0 ? 'positive' : 'negative'}>
          {stock.changes} ({stock.changesPercentage}%)
        </TableCell>
        <TableCell>{stock.beta}</TableCell>
      </TableRow>
    ))}
  </TableBody>
</Table>
```

---

## 🚀 Performance Tips

1. **Caching:** Filter options endpoint cache'lenir, sadece sayfa yüklenirken çağır
2. **Debouncing:** Kullanıcı filter değiştirirken debounce kullan (300ms)
3. **Pagination:** Büyük veri setleri için sayfalama kullan (max 100 items)
4. **Loading States:** API çağrıları sırasında loading indicator göster
5. **Error Handling:** Network hataları için retry mechanism

---

## ❌ Error Responses

```json
// 500 Internal Server Error
{
  "timestamp": "2025-10-14T14:44:30.756+00:00",
  "path": "/api/v1/search/stocks/filter",
  "status": 500,
  "error": "Internal Server Error",
  "requestId": "eadfa4ae-5"
}

// 400 Bad Request (Invalid parameters)
{
  "timestamp": "2025-10-14T14:44:30.756+00:00",
  "path": "/api/v1/search/stocks/filter",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid sort field: invalid_field"
}
```

Bu dokümantasyon ile frontend developer tüm endpoint'leri, request/response formatlarını ve implementation örneklerini görebilir! 🚀