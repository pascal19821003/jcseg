package org.lionsoul.jcseg.tokenizer.core;

import java.util.LinkedList;
import java.util.List;

import org.lionsoul.jcseg.tokenizer.Word;

/**
 * abstract segmentor kit class 
 * 
 * @author  chenxin<chenxin619315@gmail.com>
*/
public class SegKit
{
    /**
     * quick interface to do the synonyms word append
     * You got check if the specified word has any synonyms first
     * 
     * @param	config
     * @param   wordPool
     * @param   wd
    */
    public final static void appendSynonyms(JcsegTaskConfig config, LinkedList<IWord> wordPool, IWord wd)
    {
        final List<IWord> synList = wd.getSyn().getList();
        synchronized (synList) {
            for ( int j = 0; j < synList.size(); j++ ) {
            	final IWord curWord = synList.get(j);
                if ( curWord.getValue()
                        .equals(wd.getValue()) ) {
                    continue;
                }
                
                final IWord synWord = synList.get(j).clone();
                synWord.setPosition(wd.getPosition());
                synWord.setLength(wd.getLength());	/* Force the length equals to the root word's */
                wordPool.add(synWord);
                
                // check and append its Pinyin
                if ( config.APPEND_CJK_PINYIN 
                        && config.LOAD_CJK_PINYIN && synWord.getPinyin() != null ) {
                	appendPinyin(config, wordPool, synWord);
                }
            }
        }
    }
    
    /**
     * Quick interface to do the pinyin word append.
     * You got to check if the specified word has the pinyin attribute
     * 
     * @param	config
     * @param	wordPool
     * @param	wd
    */
    public final static void appendPinyin(JcsegTaskConfig config, LinkedList<IWord> wordPool, IWord wd)
    {
    	/* 
    	 * For search, you know this is a complex topic for pinyin process 
    	 * You may add your logic for processing the Pinyin for search here.
    	 * By default we just merge them and take it as single word item. 
    	*/
    	final String pinyin = wd.getPinyin().replaceAll("\\s+", "");
        final IWord pyWord = new Word(pinyin, IWord.T_CJK_PINYIN);
        pyWord.setPosition(wd.getPosition());
        pyWord.setLength(wd.getLength());
        pyWord.setEntity(wd.getEntity());
        pyWord.setPartSpeech(wd.getPartSpeech());
        wordPool.add(pyWord);
    }
    
}
