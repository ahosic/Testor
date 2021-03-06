package at.fhooe.mc.hosic.mobilelearningapp.helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;

/**
 * Provides methods to parse the QuizDTO HTML provided by Moodle.
 *
 * @author Almin Hosic
 * @version 1.0
 */
public class MoodleHTMLParser {

    private String mHTML;

    public MoodleHTMLParser(String _html) {
        mHTML = _html;
    }

    /**
     * Extracts the question out of the HTML provided by Moodle.
     *
     * @return The question text of the HTML.
     */
    public String getQuestion() {
        Document doc = Jsoup.parse(mHTML);
        Element qText = doc.select(".qtext").first();
        String question = qText.text();

        return question;
    }

    /**
     * Extracts the answers out of the HTML provided by Moodle.
     *
     * @param _attemptID  The ID of the quiz attempt
     * @param _questionNo The number of the question
     * @return
     */
    public String[] getMultiChoiceAnswers(int _attemptID, int _questionNo) {
        Document doc = Jsoup.parse(mHTML);

        StringBuilder builder = new StringBuilder();
        builder.append("q");
        builder.append(_attemptID + 1);
        builder.append(":");
        builder.append(_questionNo);
        builder.append("_answer");

        String answerGroup = builder.toString();
        String[] answers = new String[4];

        for (int i = 0; i < 4; i++) {
            Element answerNode = doc.select(".answer").select("label[for=" + answerGroup + i + "]").first();
            String answerText = answerNode.text();
            answers[i] = answerText;
        }

        return answers;
    }

    /**
     * Parses Answers for question type 'Matching'.
     *
     * @return An array of two LinkedLists containing objects of type String.
     * First list contains the questions and second the available options.
     */
    public LinkedList<String>[] getMatchingAnswers() {
        Document doc = Jsoup.parse(mHTML);

        LinkedList<String> questions = new LinkedList<String>();
        LinkedList<String> options = new LinkedList<String>();

        Element answers = doc.select(".answer").first();

        // Getting questions
        Elements texts = answers.select(".text");
        for (Element e : texts) {
            questions.add(e.text());
        }

        // Getting options
        Elements optElements = answers.select(".select").first().select("option");
        for (Element e : optElements) {
            options.add(e.text());
        }

        return new LinkedList[]{questions, options};
    }
}
