package main.java.com.clearmatics.soliditycompiler.transcompile.emitter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Stack;

public class JsonEmitter implements Emitter {
    private JSONArray         _statements = new JSONArray();
    private Stack<JSONObject> _parents    = new Stack<JSONObject>();

    public JsonEmitter() {}

    @Override public String toString() {
        JSONObject root = new JSONObject();
        JSONObject sol = new JSONObject();
        root.put("solidity", sol);
        JSONObject ast = new JSONObject();
        ast.put("statements", _statements);
        ast.put("version", 1);
        sol.put("ast", ast);
        return root.toJSONString();
    }
}
