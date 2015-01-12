import nltk
import math
import fileinput
import re

lijst = dict()
smileys = re.compile('[:;]-?[()PD/\\\\]')
def do_tokens(tweet):
  tokens = [t for t in nltk.word_tokenize(tweet) if len(t) > 2]
  tokens.extend(smileys.findall(tweet))
  return tokens

total = {-1: 0, 0: 0, +1: 0}
with open("Training_data.txt", "r") as f:
  next(f) #skip firstline
  for line in f:
    a = line.strip().split("\t")
    tweet = a[0]
    score = int(a[1])
	# if score == 0 : continue # (haal neutrale eruit)
    tokens = do_tokens(tweet)
    for t in tokens:
      
      total[score] += 1
      if not t in lijst:
        lijst[t] = {-1: 0, 0: 0, +1: 0}
      lijst[t][score] += 1

for i in lijst:
  score = lijst[i]
  score[-1] *= 1.0 / total[-1]
  score[ 0] *= 1.0 / total[ 0]
  score[+1] *= 1.0 / total[+1]
  score['sum'] = score[-1] + score[ 0] + score[+1]
  # w = 1 # elk woord gewicht van hoe vaak het voorkomt.
  w = math.sqrt(score['sum']) # iets ertussen in
  # w = score['sum'] # elk woord zelfde gewicht
  score[-1] /= w
  score[ 0] /= w
  score[+1] /= w
  score['weight'] = 1.0 / w
  score['perc'] = score[+1] - score[-1]

def getal(a):
  return int(a*1000000)

for line in fileinput.input():
  tokens = do_tokens(line.strip())
  rating = 0
  weight = 0
  rec = dict()
  for t in tokens:
    if t in lijst:
      rec[t] = getal(lijst[t]['perc'])
      rating += lijst[t]['perc']
      weight += lijst[t]['weight']
  if weight > 0:
    print getal(rating/weight), ":", line, "(",rec,")"
  else:
    print "unknown :", line, "(",rec,")"
