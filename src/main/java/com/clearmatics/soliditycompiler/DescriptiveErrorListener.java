package main.java.com.clearmatics.soliditycompiler;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DescriptiveErrorListener extends BaseErrorListener {
    public static DescriptiveErrorListener INSTANCE = new DescriptiveErrorListener();
    private static JSONArray errors = new JSONArray();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg, RecognitionException e)
    {
        JSONObject error = new JSONObject();
        error.put("filename", recognizer.getInputStream().getSourceName());
        error.put("line", line);
        error.put("charpos", charPositionInLine);
        error.put("message", msg);
        errors.add(error);
    }

    public static JSONArray getErrors() {
        return errors;
    }
}
