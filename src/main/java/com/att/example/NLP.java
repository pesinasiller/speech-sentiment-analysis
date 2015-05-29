package com.att.example;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.sentiment.*;

import java.util.Properties;
public class NLP {
    static StanfordCoreNLP pipeline;

    public static void init() {
    	
    	
   
    	    Properties props = new Properties();
    	    // we could do the following to change the language to spanish:
    	    //props.setProperty("tokenize.language", "es");
    	    //props.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/spanish/spanish-distsim.tagger");
    	    //props.setProperty("parse.model", "edu/stanford/nlp/models/srparser/spanishSR.ser.gz");
    	    
    	    props.setProperty("annotators", "tokenize,ssplit,pos,parse,sentiment"); 

    	    pipeline = new StanfordCoreNLP(props);
    
    }
    

    
    public static String findSentiment(String text) {
        String[] ratings = {"Very Negative","Negative", "Neutral", "Positive", "Very Positive"};
        float mainSentiment = 0;
        if (text != null && text.length() > 0) {
         
            int sentences=0;
         
            Annotation annotation = pipeline.process(text);
            
            
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                    mainSentiment += sentiment;
                    sentences++;
          
               

            }
            
            mainSentiment=mainSentiment/sentences;
        }
        return ratings[(int)mainSentiment];
    }
}