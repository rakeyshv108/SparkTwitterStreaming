import org.apache.commons.cli.{Options, ParseException, PosixParser}
import twitter4j.auth.OAuthAuthorization
import twitter4j.conf.ConfigurationBuilder

object Utils {
  val CONSUMER_KEY = "consumerKey"
  val CONSUMER_SECRET = "consumerSecret"
  val ACCESS_TOKEN = "accessToken"
  val ACCESS_TOKEN_SECRET = "accessTokenSecret"

  val numFeatures = 1000
  
  val THE_OPTIONS = {
    val options = new Options()
    //options.addOption(CONSUMER_KEY, true, "s0GIvvTC0EXAMYBKU0ylVKv4T")
    //options.addOption(CONSUMER_SECRET, true, "nmwyIBT8UoQPxfLmsKMH4kspecRA4FKSlGeSIh2PEew6O7aSQF")
    //options.addOption(ACCESS_TOKEN, true, "141897544-XcPh6BuboPrDOcMxpESxNRkA7RNByShasii2ytu6")
    //options.addOption(ACCESS_TOKEN_SECRET, true, "HltO1YGQt7yo0GgADygFq4fUGrXkrnynFQwacUXXXEQgV")
    options.addOption(CONSUMER_KEY, true, "Twitter OAuth Consumer Key")
    options.addOption(CONSUMER_SECRET, true, "Twitter OAuth Consumer Secret")
    options.addOption(ACCESS_TOKEN, true, "Twitter OAuth Access Token")
    options.addOption(ACCESS_TOKEN_SECRET, true, "Twitter OAuth Access Token Secret")
    options
  }

  def parseCommandLineWithTwitterCredentials(args: Array[String]) = {
    val parser = new PosixParser
    try {
      
      val cl = parser.parse(THE_OPTIONS, args)
      System.setProperty("twitter4j.oauth.consumerKey", cl.getOptionValue(CONSUMER_KEY))
      System.setProperty("twitter4j.oauth.consumerSecret", cl.getOptionValue(CONSUMER_SECRET))
      System.setProperty("twitter4j.oauth.accessToken", cl.getOptionValue(ACCESS_TOKEN))
      System.setProperty("twitter4j.oauth.accessTokenSecret", cl.getOptionValue(ACCESS_TOKEN_SECRET))

      //System.setProperty("twitter4j.oauth.consumerKey", "s0GIvvTC0EXAMYBKU0ylVKv4T")
      //System.setProperty("twitter4j.oauth.consumerSecret", "nmwyIBT8UoQPxfLmsKMH4kspecRA4FKSlGeSIh2PEew6O7aSQF")
      //System.setProperty("twitter4j.oauth.accessToken", "141897544-XcPh6BuboPrDOcMxpESxNRkA7RNByShasii2ytu6")
      //System.setProperty("twitter4j.oauth.accessTokenSecret", "HltO1YGQt7yo0GgADygFq4fUGrXkrnynFQwacUXXXEQgV")
      cl.getArgList.toArray
    } catch {
      case e: ParseException =>
        System.err.println("Parsing failed.  Reason: " + e.getMessage)
        System.exit(1)
    }
  }

  def getAuth = {
    Some(new OAuthAuthorization(new ConfigurationBuilder().build()))
  }

  object IntParam {
    def unapply(str: String): Option[Int] = {
      try {
        Some(str.toInt)
      } catch {
        case e: NumberFormatException => None
      }
    }
  }
}