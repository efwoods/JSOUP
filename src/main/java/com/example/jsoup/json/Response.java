package com.example.jsoup.json;

import java.util.List;

public class Response {
    private String title;
    private List<Headlines> headLines;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<Headlines> getHeadLines() {
        return headLines;
    }
    public void setHeadLines(List<Headlines> headLines) {
        this.headLines = headLines;
    }
}
