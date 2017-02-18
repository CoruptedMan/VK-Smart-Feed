package ru.ifmo.practice.util;

import java.util.ArrayList;

import ru.ifmo.practice.model.Note;

public interface OnDownloadFeedDataResultDelegate {
    void taskCompletionResult(ArrayList<Note> result, String pStartFrom);
}