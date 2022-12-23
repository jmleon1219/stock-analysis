# Stock Analysis Demo Project

The main motivation for this project is to create components of a data pipeline in different languages.  If this was intended to be a real project then these components could be orchastrated in `airflow`, `dagster` etc...


 A secondary motivation is to keep all this around as reference material

Although I did intentionally use specific stocks/etfs across a range of categories; growth, value, commodity. I dont have a plan to make this into a actual stock market strategy model.


This repo is broken down into 3 separate sub-repos
* Data Ingestion (`Rust`)
* Data Cleaning (`Scala`)
* Data Analysis (`Python`)

The db layer is a sqlite3 db that is in the top level directory