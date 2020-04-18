package com.example.sudohelp;

public class AcceptedWriterProfile {
    private String exam;
    private String writer;
    private String date;

    public AcceptedWriterProfile() {
    }

    public AcceptedWriterProfile(String exam, String writer, String date) {
        this.exam = exam;
        this.writer = writer;
        this.date = date;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
