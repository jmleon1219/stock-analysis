use yahoo_finance_api as yf;
use rusqlite::Connection;

#[tokio::main]
async fn main() {
    let provider = yf::YahooConnector::new();
    let mut vec: Vec<&str> = vec!["SIL","GDX", "USO", "META", "GOOGL", "BRK-B", "PG"];
    let mut stock_info: Vec<StockQuote> = vec![];
    while let Some(y) = vec.pop(){
        println!("{}", y);
        stock_info.push(get_quotes(&provider, y).await)
    }
    
    let conn = Connection::open("../stock-quotes.db").unwrap();

    while let Some(q) = stock_info.pop(){
        let _ = conn.execute("INSERT INTO stock_quotes_landing VALUES(?1,?2,?3,?4,?5,?6,?7)",(q.ticker,q.close, q.open, q.high, q.low, q.timestamp, q.volume));
    }
}

async fn get_quotes(provider: &yf::YahooConnector, ticker:&str) -> StockQuote{
    
    let resp = provider.get_latest_quotes(ticker, "1d").await;
    let attr = resp.unwrap().last_quote().unwrap();
    println!("{}", attr.close);

    return StockQuote{ticker: ticker.to_owned(),
            close: attr.close,
            open: attr.open,
            high: attr.high,
            low: attr.low,
            timestamp: attr.timestamp,
            volume: attr.volume,
    }
}



struct StockQuote {
    ticker: String,
    close: f64,
    open: f64,
    high: f64,
    low: f64,
    timestamp: u64,
    volume: u64,
}