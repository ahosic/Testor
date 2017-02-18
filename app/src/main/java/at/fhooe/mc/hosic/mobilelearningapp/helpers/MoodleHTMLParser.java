package at.fhooe.mc.hosic.mobilelearningapp.helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Provides methods to parse the Quiz HTML provided by Moodle.
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
        String question = qText.select("p").text();

        return question;
    }

    /**
     * Extracts the answers out of the HTML provided by Moodle.
     *
     * @param _attemptID  The ID of the quiz attempt
     * @param _questionNo The number of the question
     * @return
     */
    public String[] getAnswers(int _attemptID, int _questionNo) {
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
}
