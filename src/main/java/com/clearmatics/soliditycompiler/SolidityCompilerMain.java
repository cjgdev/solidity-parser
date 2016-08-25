package main.java.com.clearmatics.soliditycompiler;

import grammar.solidityLexer;
import grammar.solidityParser;
import main.java.com.clearmatics.soliditycompiler.cli.CLIOptions;
import main.java.com.clearmatics.soliditycompiler.transcompile.emitter.JsonEmitter;
import main.java.com.clearmatics.soliditycompiler.transcompile.emitter.Emitter;
import main.java.com.clearmatics.soliditycompiler.transcompile.TransCompilerParseVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;


public class SolidityCompilerMain {
    private static solidityLexer lexer;
    private static solidityParser parser;
    private static Emitter emitter;
    private static CLIOptions options;

    public static void main(String args[]) {
        processCommandLine(args);
    }

    private static void processCommandLine(String[] args) {
        options = new CLIOptions();
        boolean parsed = options.processOptions(args);

        if (processUsage(parsed)) return;

        String inputFileName = getInputFileName();
        if (inputFileName == null) return;

        if (!createLexer(inputFileName)) return;

        String target = getTarget();
        if (!createEmitter(target)) return;

        createParser();
        executeParser();
    }

    private static void createParser() {
        parser = new solidityParser(new CommonTokenStream(lexer));
        parser.setErrorHandler(new BailErrorStrategy());
        parser.removeErrorListeners();
        parser.addErrorListener(DescriptiveErrorListener.INSTANCE);
    }

    private static boolean createLexer(String inputFileName) {
        try {
            lexer = new solidityLexer(new ANTLRFileStream(inputFileName, "UTF8"));
            lexer.removeErrorListeners();
            lexer.addErrorListener(DescriptiveErrorListener.INSTANCE);
        } catch (IOException e) {
            System.err.println("Unable to load file " + inputFileName);
            return false;
        }
        return true;
    }

    private static boolean createEmitter(String target) {
        if (target.equals("json")) {
            emitter = new JsonEmitter();
        } else {
            return false;
        }
        return true;
    }

    private static void processDebugFlag(ParseTree t) {
        if (options.flagDebug()) {
            System.out.println(t.toStringTree(parser));
        }
    }

    private static void executeParser() {
        try {
            parser.setBuildParseTree(true);
            ParseTree t = parser.statement();
            processDebugFlag(t);

            TransCompilerParseVisitor tc = new TransCompilerParseVisitor(emitter);
            tc.visit(t);

            if (parser.getNumberOfSyntaxErrors() == 0) {
                System.out.println(emitter.toString());
            } else {
                JSONObject obj = new JSONObject();
                obj.put("errors", DescriptiveErrorListener.getErrors());
                System.err.println(obj.toJSONString());
                System.exit(1);
            }
        } catch (ParseCancellationException pce) {
            if (pce.getCause() instanceof RecognitionException) {
                RecognitionException re = (RecognitionException) pce.getCause();
                ParserRuleContext context = (ParserRuleContext) re.getCtx();

                String msg = "mismatched input " + re.getRecognizer().getTokenErrorDisplay(re.getOffendingToken())
                        + " expecting " + re.getExpectedTokens().toString(re.getRecognizer().getVocabulary());

                JSONObject error = new JSONObject();
                error.put("filename", re.getInputStream().getSourceName());
                error.put("line", re.getOffendingToken().getLine());
                error.put("charpos", re.getOffendingToken().getCharPositionInLine());
                error.put("message", msg);

                IntervalSet expected = re.getExpectedTokens();
                Vocabulary voc = re.getRecognizer().getVocabulary();

                JSONArray json_expected = new JSONArray();
                for (Interval interval : expected.getIntervals()) {
                    for (int i = interval.a; i <= interval.b; ++i) {
                        json_expected.add(i == -1 ? "EOF" : (i == -2 ? "EPSILON" : voc.getDisplayName(i)));
                    }
                }
                error.put("expected", json_expected);

                JSONArray errors = new JSONArray();
                errors.add(error);

                JSONObject obj = new JSONObject();
                obj.put("errors", errors);

                System.err.println(obj.toJSONString());
                System.exit(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getInputFileName() {
        String inputFileName = options.flagInputFileName();
        if (inputFileName == null) {
            options.printUsage();
            return null;
        }
        return inputFileName;
    }

    private static String getTarget() {
        String target = options.flagTarget();
        if (target == null) {
            options.printUsage();
            return null;
        }
        return target;
    }

    private static boolean processUsage(boolean parsed) {
        if (!parsed || options.flagHelp()) {
            options.printUsage();
            return true;
        }
        return false;
    }
}
