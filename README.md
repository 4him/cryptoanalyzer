# Documentation
## Installation
Clone both projects from repo.
Run command below
``` bash 
mvn clean package
```
After all is compiled successful run next command
``` bash
docker-compose up --build
```
You should have both services running

# Description of end-points behavior
The CryptoStatsController is a REST controller providing an API for analyzing cryptocurrency statistics. It allows users to retrieve data about cryptocurrencies sorted by various parameters, as well as data on normalized ranges and specific statistics.

## 1. List of All Cryptocurrencies Sorted by Normalized Range
**Endpoint:**
`GET /crypto/stats/normalized`

**Description:** Returns a list of all cryptocurrencies sorted by their normalized range (from highest to lowest). The normalized range is calculated as the difference between the maximum and minimum value, divided by the minimum value.

**Sample Response:**
```json
[
     {
        "currency_code": "ETH",
        "statistics": {
            "normalized_range": 0.63838
        }
    },
    {
        "currency_code": "XRP",
        "statistics": {
            "normalized_range": 0.51786
        }
    },

]
```


## 2. Statistics for a Specific Cryptocurrency (Oldest, Newest, Min, Max Prices)
**Endpoint:**
` GET /crypto/stats/{symbol}`

**Path Parameters:**
symbol (string): The cryptocurrency symbol (e.g., BTC, ETH).

**Description:**
Returns detailed information about the cryptocurrency, including:
The oldest price.
The newest price.
The minimum price.
The maximum price.

**Sample Response:**
```json
{
    "currency_code": "ETH",
    "statistics": {
        "min_price": 2336.52,
        "max_price": 3828.11,
        "oldest_date_time": "01-01-2022 10:00:00",
        "newest_date_time": "31-01-2022 22:00:00"
    }
}
```



## 3. Cryptocurrency with the Highest Normalized Range for a Given Day ##
**Endpoint:**
`GET /crypto/stats/highest?day=15&month=01&year=2022`

**Query Parameters:**
day (int): The day.
month (int): The month.
year (int): The year.

**Description:**
 Returns the cryptocurrency with the highest normalized range for the specified day.

**Sample Response:**
```json
{
    "currency_code": "LTC",
    "statistics": {
        "normalized_range": 0.02143
    }
}
```

