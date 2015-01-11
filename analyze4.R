d <- read.csv("/home/vijay/Dropbox/Reddit Hackaton/Tokenize/wordFrequency.txt", header=TRUE)
# d$revised = d$average_score
# wordcloud(d$word,d$revised, scale=c(5,0.5), max.words=200, random.order=FALSE, rot.per=0.35, use.r.layout=FALSE, colors=brewer.pal(8, "Dark2"))
# 
# wordcloud(d$word,d$average_comments, scale=c(5,0.5), max.words=200, random.order=FALSE, rot.per=0.35, use.r.layout=FALSE, colors=brewer.pal(8, "Dark2"))

d$revised1 = 20 - d$average_score
# wordcloud(d$word,d$revised1, scale=c(5,0.5), max.words=200, random.order=FALSE, rot.per=0.35, use.r.layout=FALSE, colors=brewer.pal(8, "Dark2"))
d$revised2 = 2 - d$average_comments
wordcloud(d$word,d$revised2, scale=c(5,0.5), max.words=100, random.order=FALSE, rot.per=0.35, use.r.layout=FALSE, colors=brewer.pal(8, "Dark2"))