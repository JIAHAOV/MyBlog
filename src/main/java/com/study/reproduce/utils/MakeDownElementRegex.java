package com.study.reproduce.utils;

public interface MakeDownElementRegex {
    String HTML_ELEMENT_REGEX = "<[^>]+>";
    String CODE_ELEMENT_REGEX = "```[\\s\\S]*?```";
    String IMAGE_ELEMENT_REGEX = "!\\[image-[\\s\\S]*?\\)";
}
