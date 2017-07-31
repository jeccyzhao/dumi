package com.nokia.oss.sdm.tools.dumi.spellchecker.engine;

import com.nokia.oss.sdm.tools.dumi.spellchecker.SpellCheckerManager;
import com.nokia.oss.sdm.tools.dumi.spellchecker.engine.SuggestionProvider;
import com.nokia.oss.sdm.tools.dumi.spellchecker.util.NameUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by x36zhao on 2017/7/31.
 */
public class BaseSuggestionProvider implements SuggestionProvider
{
    private final SpellCheckerManager manager;

    public BaseSuggestionProvider (SpellCheckerManager manager)
    {
        this.manager = manager;
    }

    @Override
    public List<String> getSuggestions (String text)
    {
        String[] words = NameUtil.nameToWords(text);
        int index = 0;
        List[] res = new List[words.length];
        int i = 0;
        for (String word : words)
        {
            int start = text.indexOf(word, index);
            int end = start + word.length();
            if (manager.hasProblem(word))
            {
                List<String> variants = manager.getRawSuggestions(word);
                res[i++] = variants;
            }

            index = end;
        }

        String[] all;
        int[] counter = new int[i];
        int size = 1;
        for (int j = 0; j < i; j++)
        {
            size *= res[j].size();
        }
        all = new String[size];
        for (int k = 0; k < size; k++)
        {
            for (int j = 0; j < i; j++)
            {
                if (all[k] == null)
                {
                    all[k] = "";
                }
                all[k] += res[j].get(counter[j]);
                counter[j]++;
                if (counter[j] >= res[j].size())
                {
                    counter[j] = 0;
                }
            }
        }

        return Arrays.asList(all);
    }
}
