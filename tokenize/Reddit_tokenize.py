import nltk
import math
import fileinput
import re

lijst = dict()
def do_tokens(title):
  tokens = [t for t in nltk.word_tokenize(title)]
  return tokens
  
# with open("ScoreText_small.txt", "r") as f
  # print(f)
  # data = f.read()

f=open('ScoreText_small.txt','r')

next(f) #skip first line
for line in f:
  a = line.strip(' \t\n\r').split(';')
  score = int(a[0])
  number_of_comments = int(a[1])
  title = a[2]
  print(a)
  tokens = do_tokens(title)
  print(tokens)


