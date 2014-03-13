package edu.macalester.comp124.wordcounter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Interesting word finder.
 * To use this class, first call countWords(), then call findDistinctive.
 * <p/>
 * Your tasks are:
 * 1. Complete the countWordsInOneFile method (you only need to add three lines of code).
 * 2. Complete the findInterestingWordsOneWay method (this should be done in 3 stages).
 *
 * @author shilad
 */
public class DistinctiveWordFinder
{
    // Word counts from each file.
    private AllWordsCounter primaryCounts;
    private AllWordsCounter secondaryCounts;

    /**
     * Constructs a new word counter.
     */
    public DistinctiveWordFinder()
    {
        primaryCounts = new AllWordsCounter();
        secondaryCounts = new AllWordsCounter();
    }

    /**
     * Counts the words in two files.
     *
     * @param primaryFile
     * @param secondaryFile
     * @throws java.io.IOException
     */
    public void countWords(File primaryFile, File secondaryFile) throws IOException
    {
        countWordsInOneFile(primaryFile, primaryCounts);
        countWordsInOneFile(secondaryFile, secondaryCounts);
    }

    /**
     * Counts the words in a single file.  The counts should be tallied
     * in the passed-in counts object.
     *
     * @param file1
     * @param counter
     * @throws java.io.IOException
     */
    private void countWordsInOneFile(File file1, AllWordsCounter counter) throws IOException
    {
        BufferedReader r = new BufferedReader(new FileReader(file1));
        while (true)
        {
            String line = r.readLine();
            if (line == null)
               break;
            for (String w : splitLine(line))
                counter.count(w);
        }
    }

    /**
     * Find and display words in the primary word counter that are interesting.
     * Use System.out.println() to print out information about the interesting
     * words.
     */
    private void findDistinctive()
    {
        String words[] = primaryCounts.getAllWords();

        // This array will be used to sort the word scores;
        WordScore scores[] = new WordScore[words.length];

        for (int i = 0; i < words.length; i++)
        {
            String word = words[i];

            // the primaryCounts and secondaryCounts instance variables.
            int primary = primaryCounts.getCount(word);
            int secondary = secondaryCounts.getCount(word);

            // Calculate the interestingness score and prepare it in the output queue
            double score = getDistinctiveScore(primary, secondary);
           // System.out.println(word + " : " + score);
            scores[i] = new WordScore(word, score);
        }

        // Display the most interesting scores.
        Arrays.sort(scores);
        for (int i = 0; i < 10; i++)
        {
            String word = scores[i].getWord();
            int c1 = primaryCounts.getCount(word);
            int c2 = secondaryCounts.getCount(word);
            System.out.println("word: " + word + ", primary=" + c1 + ", secondary=" + c2);
        }
    }

    public static final int K = 1;

    /**
     * Returns a "score" indicating how interesting a word is.
     *
     * @param primaryCount   The count for a specific word for the primary candidate (i.e. Biden)
     * @param secondaryCount The count for a specific word for the secondary candidate (i.e. Palin)
     * @return
     */
    private double getDistinctiveScore(int primaryCount, int secondaryCount)
    {
        double score1 = (double) primaryCount / (primaryCount + secondaryCount + K);
        double score2 = (double) secondaryCount / (primaryCount + secondaryCount + K);
        //score1 = primaryCount - secondaryCount;
    //    if(score1 > score2)
  //          return score2;
        return score1;
    }

    /**
     * Splits a line into words.
     *
     * @param line
     * @return An array containing the words.
     */
    private String[] splitLine(String line)
    {
        return line.split("[^a-zA-Z0-9']+");
    }

    public static final File f3 = new File("C:\\Users\\needa_000\\IdeaProjects\\word-counter\\dat\\palin.txt");
    public static final File f4 = new File("C:\\Users\\needa_000\\IdeaProjects\\word-counter\\dat\\biden.txt");
    public static void main(String args[]) throws IOException
    {
        File f1 = new File("/dat/palin.txt");
        File f2 = new File("/dat/biden.txt");
        System.out.println(f1.getAbsolutePath());

        DistinctiveWordFinder finder = new DistinctiveWordFinder();
        finder.countWords(f3, f4);
        finder.findDistinctive();
    }

    /**
     * Helper class used for sorting word scores.
     */
    private static class WordScore implements Comparable<WordScore>
    {
        private String word;
        private Double score;

        private WordScore(String word, Double score)
        {
            this.word = word;
            this.score = score;
        }

        private String getWord()
        {
            return word;
        }

        private Double getScore()
        {
            return score;
        }

        @Override
        public int compareTo(WordScore wordScore)
        {
            return -1 * score.compareTo(wordScore.getScore());
        }
    }
}
