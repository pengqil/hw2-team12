package edu.cmu.lti.oaqa.openqa.test.team12.keyterm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.FileUtils;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.ConfidenceChunker;
import com.aliasi.util.AbstractExternalizable;

import edu.cmu.lti.oaqa.cse.basephase.keyterm.AbstractKeytermExtractor;
import edu.cmu.lti.oaqa.framework.data.Keyterm;

import edu.upenn.cis.taggers.LoadModelException;
import edu.upenn.cis.taggers.Tagger;

public class PengqilBioNERKeyTermExtractor extends AbstractKeytermExtractor {
    String modelFilePath;
    String tokenFilePath;
    File tokenFile;
    GeneTagger tagger = null;
  @Override
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    modelFilePath = getClass().getClassLoader().getResource(".").getPath() + (String) aContext.getConfigParameterValue("geneModel");
    System.out.println(modelFilePath);
    tokenFilePath = getClass().getClassLoader().getResource(".").getPath() + (String) aContext.getConfigParameterValue("tokenModel");    

  }
  
  @Override
  protected List<Keyterm> getKeyterms(String question) {
    int index = question.indexOf("|");
    String gTag = question.substring(index+1);
    List<Keyterm> keyterms = new ArrayList<Keyterm>();
    
    try {
      //use the geneModel1 to tag the documents
      tagger = new GeneTagger(modelFilePath);
    } catch (LoadModelException e2) {
      // TODO Auto-generated catch block
      e2.printStackTrace();
    }
    
    try { 

      //tag the sentence
     ArrayList geneWord = (ArrayList) tagger.tAG(gTag,tokenFilePath);
     System.out.println(geneWord.size());
     //System.out.println(result);  
     for(int i=0;i<geneWord.size();i++){
       String gene = geneWord.get(i).toString();
       gene = gene.replace("O", "") ;
       gene = gene.replace("\t", "");
       gene = gene.replace("],[", " ");
       gene = gene.replace("]", "");
       gene = gene.replace("[", "");
       String[] token = gene.split("-");
       gene = "";
       for(String s:token){
         gene = gene + s.trim() + "-";
       }
       gene = gene.substring(0, gene.length()-1);  
       keyterms.add(new Keyterm(gene));
       System.out.println(gene);
     }
      
   } catch (IOException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
   }
    return keyterms;
  }
}
