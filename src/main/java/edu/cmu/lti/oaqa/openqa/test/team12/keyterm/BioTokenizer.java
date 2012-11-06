package edu.cmu.lti.oaqa.openqa.test.team12.keyterm;
import opennlp.tools.tokenize.TokenizerME;
import java.io.*;
import opennlp.maxent.*;
import opennlp.maxent.io.*;
import java.util.zip.GZIPInputStream;

/**
 * A class to handle tokenization of biological text
 * @author Ryan McDonald <a href="mailto:ryantm@cis.upenn.edu">ryantm@cis.upenn.edu</a>
 * @author Kevin Lerman <a href="mailto:klerman@seas.upenn.edu">Kevin Lerman</a>
 * */
public class BioTokenizer {
  private TokenizerME tokenizer;
  public BioTokenizer(String in){    
    tokenizer = new TokenizerME(getModel(in));
  }
  
  /**
   * Returns a tokenized version of the passed String
   * @param in The String to tokenize
   * @return The tokenized String
   * */
  public String tokenize(String in){
    String toReturn="";
    String[] toks = tokenizer.tokenize(in);
    for(int x=0;x<toks.length;x++){
      toReturn+=toks[x]+' ';
    }
    return toReturn.substring(0,toReturn.length()-1);
  }
  
  
  private static MaxentModel getModel(String name) {
    try {
      return new BinaryGISModelReader(
                                      new DataInputStream(
                                                          new GZIPInputStream(new FileInputStream(name))))
        .getModel();
    } catch (IOException E) {
      E.printStackTrace();
      return null;
    }
  }
  
}
