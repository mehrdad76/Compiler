package Main;

import Main.Lexer.Lexer;
import Main.Lexer.State;
import Main.PriorityRegexDictionary.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer();

        lexer.analyse("code.c");

    }
}
