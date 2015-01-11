data <- read.csv("/home/vijay/Downloads/redditSubmissions.csv", header=TRUE)

timeGrid <- data.frame(data$reddit_id, data$localtime, data$score)

write.table(timeGrid, "localtimes.csv", sep=",")

timeGrid2 <- data.frame(data$reddit_id, data$localtime, data$score, data$title)
write.table(timeGrid2, "localtimes2.csv", sep=",")

timeSlots <- c(4828, 4953, 5208, 5525, 5764, 5875, 6131, 6519, 6729, 6706, 6952, 6928, 7223, 7179, 6552, 5836, 5171, 4681, 4017, 3818, 3612, 3825, 3910, 4365)
# barplot(timeSlots, names.arg=c(1:24))
# daySlots <- c(19213, 20004, 19253, 19780, 19447, 17571, 17039)
# barplot(daySlots, names.arg=c("S", "M", "T", "W", "T", "F", "S"))

lords <- tm_map(txt.corpus, stripWhitespace)
lords <- tm_map(corpus, content_transformer(tolower))
lords <- tm_map(lords, removeWords, stopwords("english"))
lords <- tm_map(lords, stemDocument)
# wordcloud(lords, scale=c(5,0.5), max.words=100, random.order=FALSE, rot.per=0.35, use.r.layout=FALSE, colors=brewer.pal(8, "Dark2"))
tdm <- TermDocumentMatrix(lords)
m <- as.matrix(tdm)
v <- sort(rowSums(m),decreasing=TRUE)
d <- data.frame(word = names(v),freq=v)

wordcloud(d$word,d$freq, scale=c(8,.3),min.freq=2,max.words=100, random.order=T, rot.per=.15, colors=brewer.pal(8, "Dark2"), vfont=c("sans serif","plain"))

wordcloud(lords, scale=c(5,0.5), max.words=100, random.order=FALSE, rot.per=0.35, use.r.layout=FALSE, colors=brewer.pal(8, "Dark2"))