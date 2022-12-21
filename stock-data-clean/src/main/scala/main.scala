package com.jacobleon.stocks

import java.sql.DriverManager
import scala.util.{Try, Success, Failure}
import java.time.{Instant, ZoneOffset, LocalDateTime, LocalDate}

case class StockQuote(ticker:String, close:Float, open:Float, high:Float, low:Float, txn_timestamp:Int, txn_date:Option[LocalDate]=None, volume:Int)
object CleanData {

    def main(args: Array[String]): Unit = { 
        val dm= DriverManager.getConnection("jdbc:sqlite:../stock-quotes.db")
        var statement = dm.createStatement()
        val res = Try(statement.executeQuery("select * from stock_quotes_landing"))

        val p = res match {
            case Success(rs) => {
                new Iterator[StockQuote]{
                    def hasNext = rs.next()
                    def next() = StockQuote(ticker = rs.getString("ticker_symbol"),
                                            close = rs.getFloat("close"),
                                            open = rs.getFloat("open"),
                                            high = rs.getFloat("high"),
                                            low = rs.getFloat("low"),
                                            txn_timestamp = rs.getInt("timestamp"),
                                            volume = rs.getInt("volume")
                                            )
                }.toStream
            }
            case Failure(ex) => {
                                 println{"Query Failed"}
                                 throw ex
                                }
        }
        val cleaned = p.map(x=> x.copy(ticker= x.ticker.toUpperCase(), txn_date= Some(LocalDateTime.ofInstant(Instant.ofEpochSecond(x.txn_timestamp), ZoneOffset.UTC).toLocalDate())))
        val latestRec = cleaned.groupBy(r => (r.txn_date, r.ticker)).values.map(_.maxBy(_.txn_date)) // Retrieve latest record per ticker per day
        val insertData = (sq :StockQuote) => statement.executeUpdate(s"INSERT INTO stock_quotes VALUES('${sq.ticker}', ${sq.close}, ${sq.open}, ${sq.high}, ${sq.low}, ${sq.txn_timestamp}, '${sq.txn_date.getOrElse("").toString()}', ${sq.volume})")
        // TODO work on upsert
        statement.executeUpdate("delete from stock_quotes")
        latestRec.foreach(insertData)
    }
}
