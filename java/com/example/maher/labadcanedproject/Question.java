package com.example.maher.labadcanedproject;

/**
 * Created by Maher on 12/27/2016.
 */
public class Question {
    int quizId;
    int qNum;
    String question;
    String ans1;
    String ans2;
    String ans3;
    int correctAns;

    public Question(
            int quizId,
            int qNum,
            String questin,
            String ans1,
            String ans2,
            String ans3,
            int correctAns) {

        super();
        this.quizId = quizId;
        this.qNum = qNum;
        this.question = questin;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.correctAns = correctAns;
    }

    public int getQuizId() {
        return quizId;
    }


    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getqNum() {
        return qNum;
    }

    public void setqNum(int qNum) {
        this.qNum = qNum;
    }

    public String getQuestion() {
        return question;
    }

    public int getQuestionNumber() {
        return qNum;
    }


    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAns1() {
        return ans1;
    }

    public void setAns1(String ans1) {
        this.ans1 = ans1;
    }

    public String getAns2() {
        return ans2;
    }

    public void setAns2(String ans2) {
        this.ans2 = ans2;
    }

    public String getAns3() {
        return ans3;
    }

    public void setAns3(String ans3) {
        this.ans3 = ans3;
    }

    public int getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(int correctAns) {
        this.correctAns = correctAns;
    }


    @Override
    public String toString() {
        return "Question{" +
                "quizId=" + quizId +
                ", qNum=" + qNum +
                ", question='" + question + '\'' +
                ", ans1='" + ans1 + '\'' +
                ", ans2='" + ans2 + '\'' +
                ", ans3='" + ans3 + '\'' +
                ", correctAns=" + correctAns +
                '}';
    }
}
