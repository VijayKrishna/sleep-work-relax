library(tm)
library(wordcloud)

wc_comments <- function(day) {
  lords <- readLines(paste("/home/vijay/Dropbox/Reddit Hackaton/commentsPerHour/",day,".txt", sep = ""))
  txt <- VectorSource(lords);
  txt.corpus <- Corpus(txt);
  lords <- tm_map(txt.corpus, stripWhitespace)
  lords <- tm_map(lords, removePunctuation)
  lords <- tm_map(lords, content_transformer(tolower), lazy=TRUE)
  lords <- tm_map(lords, removeWords, stopwords("english"), lazy = TRUE)
  lords <- tm_map(lords, removeWords, c("gif","when","the","this","how","reddit","xpost","post"))
  lords <- tm_map(lords, removeWords, stopwords("SMART"), lazy = TRUE)
  
  layout(matrix(c(1, 2), nrow=2), heights=c(1, 4))
  par(mar=rep(0, 4))
  plot.new()
  text(x=0.5, y=0.5, paste("Comments at time-", day))
  wordcloud(lords, scale=c(5,0.5), max.words=200, random.order=FALSE, rot.per=0.35, use.r.layout=FALSE, colors=brewer.pal(8, "Dark2"))  
}

wc_comments("0")
wc_comments("5")
wc_comments("10")
wc_comments("15")
wc_comments("20")