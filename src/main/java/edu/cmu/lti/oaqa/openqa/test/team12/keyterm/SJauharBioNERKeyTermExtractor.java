package edu.cmu.lti.oaqa.openqa.test.team12.keyterm;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.resource.ResourceInitializationException;

import edu.cmu.lti.oaqa.cse.basephase.keyterm.AbstractKeytermExtractor;
import edu.cmu.lti.oaqa.framework.data.Keyterm;

import com.aliasi.chunk.*;
import com.aliasi.util.AbstractExternalizable;

public class SJauharBioNERKeyTermExtractor extends AbstractKeytermExtractor {
  
  private ConfidenceChunker chunker;
  
  private double confidenceThreshold = 0.65;
  
  @Override
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    String modelFilePath = getClass().getClassLoader().getResource(".").getPath() + (String) aContext.getConfigParameterValue("GeneChunkerModelFile");
    File modelFile = new File(modelFilePath);
    try {
      chunker = (ConfidenceChunker) AbstractExternalizable.readObject(modelFile);
    } catch (Exception e) {
      throw new ResourceInitializationException(e);
    }
  }
  
  @Override
  protected List<Keyterm> getKeyterms(String question) {
    
    char[] annotationtext = question.toCharArray();
    Iterator<Chunk> it = chunker.nBestChunks(annotationtext, 0, annotationtext.length, 100);
    
    List<Keyterm> keyterms = new ArrayList<Keyterm>();
    while (it.hasNext()) {
      Chunk currentchunk = (Chunk) it.next();
      double conf = Math.pow(2.0, currentchunk.score());
      
      if (conf >= confidenceThreshold) {
        keyterms.add(new Keyterm(question.substring(currentchunk.start(), currentchunk.end())));
      }
    }
    return keyterms;
  }
  
}