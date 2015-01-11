library(tm)
library(wordcloud)

timedData <- read.csv("/home/vijay/hackdata/timebasedData2.csv", header=TRUE)

day_wordcloud <- function(day) {
  lords <- readLines(paste("/home/vijay/hackdata/titles_",day, sep = ""))
  txt <- VectorSource(lords);
  txt.corpus <- Corpus(txt);
  lords <- tm_map(txt.corpus, stripWhitespace)
  lords <- tm_map(lords, removePunctuation)
  lords <- tm_map(lords, content_transformer(tolower), lazy=TRUE)
  lords <- tm_map(lords, removeWords, stopwords("english"), lazy = TRUE)
  lords <- tm_map(lords, removeWords, c("gif","when","the","this","how","reddit","xpost","post"))
  lords <- tm_map(lords, removeWords, stopwords("SMART"), lazy = TRUE)

  wordcloud(lords, scale=c(5,0.5), max.words=200, random.order=FALSE, rot.per=0.35, use.r.layout=FALSE, colors=brewer.pal(8, "Dark2"))  
}


day_titles <- function (day) {
#   DAY = timedData$day == day
  DAY = timedData$hour == day
  sundayposts <- timedData[DAY,]
  write.table(sundayposts$title, paste("titles_",day, sep = ""), sep=",", row.names = FALSE, col.names = FALSE)
  day_wordcloud(day)
}




# day_titles("Su")
# day_titles("Mo")
# day_titles("Tu")
# day_titles("We")
# day_titles("Th")
# day_titles("Fr")
# day_titles("Sa")

day_titles("0")
day_titles("1")
day_titles("2")
day_titles("3")
day_titles("4")
day_titles("5")
day_titles("6")
day_titles("7")
day_titles("8")
day_titles("9")
day_titles("10")
day_titles("11")
day_titles("12")
day_titles("13")
day_titles("14")
day_titles("15")
day_titles("16")
day_titles("17")
day_titles("18")
day_titles("19")
day_titles("20")
day_titles("21")
day_titles("22")
day_titles("23")

