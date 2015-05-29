package com.att.example;


// Import the relevant code kit parts
import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.att.api.speech.model.NBest;
import com.att.api.speech.model.NLUHypothesis;
import com.att.api.speech.model.OutComposite;
import com.att.api.speech.model.SpeechResponse;
import com.att.example.SpeechService;
import com.att.api.oauth.OAuthService;
import com.att.api.oauth.OAuthToken;

public class App {

    private static void setProxySettings() {
        // set any proxy settings
        //RESTConfig.setDefaultProxy("proxy.host", 8080);
    }

    private static String formatList(String preface, List<?> lstring) {
        StringBuilder sbuild = new StringBuilder(preface);

        Iterator<?> itr = lstring.iterator();
        if (itr.hasNext()) {
            sbuild.append("[ " + itr.next());
            while (itr.hasNext()) {
                sbuild.append(", " + itr.next());
            }
        }
        sbuild.append(" ]");

        return sbuild.toString();
    }

    public static void main(String[] args) {
        try {
        	

    		// initializes nlp
        	NLP.init();

        	
            setProxySettings();

            // Use the app settings from developer.att.com for the following
            // values. Make sure Speech is enabled for the app key/secret.

            final String fqdn = "https://api.att.com";

            // Enter the value from 'App Key' field
            final String clientId = "YOUR-CLIENT-ID";

            // Enter the value from 'Secret' field
            final String clientSecret = "YOUR-SECRET";

            // Create service for requesting an OAuth token
            OAuthService osrvc = new OAuthService(fqdn, clientId, clientSecret);

            // Get OAuth token using the Speech scope
            OAuthToken token = osrvc.getToken("SPEECH");

            // Create service for interacting with the Speech api
            SpeechService speechSrvc = new SpeechService(fqdn, token);

            // Set this to a single channel audio file
            final File AUDIO_FILE = new File("src/test/resources/positive.wav");

            // Make the request to convert the audio file
            final SpeechResponse response = speechSrvc.speechToText(AUDIO_FILE);

            System.out.println("Converted Speech with status response:"+ response.getStatus());
            System.out .println("Response ID:" + response.getResponseId() + "\n");

            System.out.println("---------------");
            for (NBest nbest : response.getNbests()){

                System.out.println("\tLanguage Id: " + nbest.getLanguageId());

                System.out.println(formatList("\tWords: ", 
                            nbest.getWords())
                        );

                NLUHypothesis nlu = nbest.getNluHypothesis();
                if (nlu != null) {
                    System.out.println("\tNluHypothesis:");
                    List<OutComposite> composites = nlu.getOutComposite();
                    for (OutComposite comp : composites) {
                        System.out.println("\t\tOut: " + comp.getOut());
                        System.out.println("\t\tGrammar: " 
                                + comp.getGrammar());
                    }
                }
                

  	           System.out.println(nbest.getResultText());
  		       System.out.println("Sentimient: "+ NLP.findSentiment(nbest.getResultText()));
  			        
  		    
            }
          
         

        } catch (Exception re) {
            // handle exceptions here
            re.printStackTrace();
        } finally {
            // perform any clean up here
        }
    }
}
