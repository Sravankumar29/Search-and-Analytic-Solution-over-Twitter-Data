import json
from pprint import pprint
from textblob import TextBlob
import sys

def sentim(tweet):
        analysis = TextBlob(tweet)
        pprint(analysis.sentiment.polarity)

sentim(sys.argv[1])