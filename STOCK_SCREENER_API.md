# Stock Screener API Documentation

## Overview
Stock Screener API, search-service içerisinde implement edilmiş olan filtrelenebilir hisse senedi arama sistemidir.

## Endpoints

### 1. Get Filter Options
Filtreleme seçeneklerini getirir (industries, countries, sectors, exchanges, currencies ve range değerleri).

```http
GET /api/v1/search/stocks/filter-options
```

**Response:**
```json
{
  "industries": ["Technology", "Healthcare", "Finance", ...],
  "countries": ["US", "CA", "GB", ...],
  "sectors": ["Technology", "Healthcare", ...],
  "exchanges": ["NASDAQ", "NYSE", "TSX", ...],
  "currencies": ["USD", "CAD", "EUR", ...],
  "rangeOptions": {
    "marketCap": {
      "min": 1000000,
      "max": 3000000000000
    },
    "price": {
      "min": 0.01,
      "max": 500.00
    },
    "beta": {
      "min": -2.5,
      "max": 5.0
    }
  }
}
```

### 2. Filter Stocks
Belirtilen filtrelere göre hisse senetlerini arar.

```http
POST /api/v1/search/stocks/filter
Content-Type: application/json

{
  "industries": ["Technology", "Healthcare"],
  "countries": ["US", "CA"],
  "sectors": ["Technology"],
  "exchanges": ["NASDAQ", "NYSE"],
  "currencies": ["USD"],
  "minMarketCap": 1000000000,
  "maxMarketCap": 100000000000,
  "minPrice": 10.00,
  "maxPrice": 500.00,
  "minBeta": 0.5,
  "maxBeta": 2.0,
  "isEtf": false,
  "isActivelyTrading": true,
  "isFund": false,
  "isAdr": null,
  "sortBy": "market_cap",
  "sortOrder": "desc",
  "page": 0,
  "size": 20
}
```

**Request Parameters:**
- `industries` (array): Industry filtreleri
- `countries` (array): Ülke filtreleri
- `sectors` (array): Sektör filtreleri
- `exchanges` (array): Borsa filtreleri
- `currencies` (array): Para birimi filtreleri
- `minMarketCap/maxMarketCap` (number): Piyasa değeri aralığı
- `minPrice/maxPrice` (number): Fiyat aralığı
- `minBeta/maxBeta` (number): Beta aralığı
- `isEtf` (boolean): ETF filtresi
- `isActivelyTrading` (boolean): Aktif işlem görme filtresi
- `isFund` (boolean): Fon filtresi
- `isAdr` (boolean): ADR filtresi
- `sortBy` (string): Sıralama alanı (market_cap, price, company_name, symbol, changes, beta)
- `sortOrder` (string): Sıralama yönü (asc, desc)
- `page` (number): Sayfa numarası (0'dan başlar)
- `size` (number): Sayfa boyutu (max 100)

**Response:**
```json
{
  "stocks": [
    {
      "symbol": "AAPL",
      "companyName": "Apple Inc.",
      "sector": "Technology",
      "industry": "Consumer Electronics",
      "country": "US",
      "exchange": "NASDAQ Global Select",
      "exchangeShortName": "NASDAQ",
      "currency": "USD",
      "price": 175.43,
      "marketCap": 2876543210000,
      "beta": 1.25,
      "changes": 2.15,
      "changesPercentage": 1.24,
      "priceRange": "124.17 - 199.62",
      "isEtf": false,
      "isActivelyTrading": true,
      "isFund": false,
      "isAdr": false
    }
  ],
  "pagination": {
    "currentPage": 0,
    "totalPages": 150,
    "pageSize": 20,
    "totalElements": 2999,
    "hasNext": true,
    "hasPrevious": false
  },
  "filterSummary": {
    "totalMatches": 2999,
    "appliedFilters": [
      "Industries: Technology, Healthcare",
      "Countries: US, CA",
      "Min Market Cap: 1000000000",
      "Actively Trading: true"
    ]
  }
}
```

## Sort Options
- `market_cap`: Piyasa değerine göre
- `price`: Fiyata göre
- `company_name`: Şirket adına göre
- `symbol`: Sembole göre
- `changes`: Değişime göre
- `beta`: Beta değerine göre

## Performance Features
- **Caching**: Filter options cache'lenir (filter değişikliklerinde otomatik güncellenir)
- **Pagination**: Büyük veri setleri için sayfalama desteği
- **Efficient Queries**: OpenSearch ile optimize edilmiş arama
- **Database Filtering**: PostgreSQL'den hızlı filter options

## Example Usage

```bash
# Filter options'ı al
curl -X GET "http://localhost:8084/api/v1/search/stocks/filter-options"

# Technology sektöründe, market cap > 1B olan US hisselerini ara
curl -X POST "http://localhost:8084/api/v1/search/stocks/filter" \
  -H "Content-Type: application/json" \
  -d '{
    "sectors": ["Technology"],
    "countries": ["US"],
    "minMarketCap": 1000000000,
    "isActivelyTrading": true,
    "sortBy": "market_cap",
    "sortOrder": "desc",
    "page": 0,
    "size": 10
  }'
```

## Error Handling
API, standard HTTP status kodları döner:
- `200`: Başarılı
- `400`: Geçersiz request parametreleri
- `500`: Server hatası

Hata durumlarında reactive error handling ile graceful degradation sağlanır.