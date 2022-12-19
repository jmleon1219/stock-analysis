import java.sql.DriverManager;
import scala.util.{Try, Success, Failure}

object cleanData {

    def main(args: Array[String]) = { 
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
                                            timestamp = rs.getInt("timestamp"),
                                            volume = rs.getInt("volume")
                                            )
                }.toStream
            }
           // case Failure(ex) => Iterator[StockQuote]
        }
        val c =p.map(x=> x.copy(ticker= x.ticker.toUpperCase()))
        //add timestamp to groupby after formatting it for date
        val o = c.groupBy(_.ticker).values.map(_.maxBy(_.timestamp))
        c.groupMapReduce(_.ticker)(_.timestamp.toInt)(_ max _)
    }
}

case class StockQuote(ticker:String, close:Float, open:Float, high:Float, low:Float, timestamp:Int, volume:Int)
