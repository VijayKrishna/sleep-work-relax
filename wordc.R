library(tm)
library(wordcloud)

# lords <- readLines("/home/vijay/Dropbox/Reddit Hackaton/comments/6ttui.txt")
lords <- readLines("/home/vijay/hackdata/titles_Mo")
txt <- VectorSource(lords);
txt.corpus <- Corpus(txt);
lords <- tm_map(txt.corpus, stripWhitespace)
lords <- tm_map(lords, removePunctuation)
lords <- tm_map(lords, content_transformer(tolower), lazy=TRUE)
lords <- tm_map(lords, removeWords, stopwords("english"), lazy = TRUE)
lords <- tm_map(lords, removeWords, c("gif","when","the","this","how","reddit","xpost","post"), lazy = TRUE)
lords <- tm_map(lords, removeWords, stopwords("SMART"), lazy = TRUE)
# stopwords("SMART")
# lords <- tm_map(lords, stemDocument, , lazy = TRUE)

# tdm <- TermDocumentMatrix(lords)
# m <- as.matrix(tdm)
# v <- sort(rowSums(m),decreasing=TRUE)
# d <- data.frame(word = names(v),freq=v)
wordcloud(lords, scale=c(5,0.5), max.words=200, random.order=FALSE, rot.per=0.35, use.r.layout=FALSE, colors=brewer.pal(8, "Dark2"))
