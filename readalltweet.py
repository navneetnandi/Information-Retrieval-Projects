from tweepy import Stream
from tweepy import OAuthHandler
from tweepy.streaming import StreamListener


#consumer key, consumer secret, access token, access secret. These values will be unique to each user. They have been intentionally redacted 
ckey="********************"
csecret="*******************"
atoken="*****************"
asecret="**********************"

class MyListener(StreamListener):
 
    def on_data(self, data):
        try:
            with open('iphone.json', 'a') as f:
                f.write(data)
                return True
        except BaseException as e:
            print("Error on_data:" % str(e))
        return True
 
    def on_error(self, status):
        print(status)
        return True
 
auth = OAuthHandler(ckey, csecret)
auth.set_access_token(atoken, asecret)

twitterStream = Stream(auth, MyListener())
twitterStream.filter(track=["#iPhone7", "#Apple", "#iPhone", "#iOS"])