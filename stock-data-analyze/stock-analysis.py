import pandas as pd
import seaborn as sns
import sqlite3
import seaborn.objects as so

con = sqlite3.connect("../stock-quotes.db")
df = pd.read_sql_query("select * from stock_quotes", con)


df["txn_date"] = pd.to_datetime(df.txn_date, format='%Y-%m-%d')


#sns.relplot(data=df_lp, x='txn_date', y='close', hue="ticker_symbol", kind='line', sort=True, height=4, aspect=2)

#sns.lineplot(data=df,x='txn_date',ddy='close')


print(df.pivot(index='txn_date', columns='ticker_symbol', values='close'))


sns.pairplot(data=df)


so.Plot(data=df, x=df.txn_date, y='close') \
.facet('ticker_symbol', wrap=2) \
.layout(size=(10,7)) \
.add(so.Line(marker='o')).show()


so.Plot(data=df, x=df.txn_date, y='close',color='ticker_symbol') \
.layout(size=(10,7)) \
.add(so.Line(marker='o')).show()



sns.FacetGrid(data=df[['ticker_symbol', 'txn_date', 'close']], col='ticker_symbol' )



