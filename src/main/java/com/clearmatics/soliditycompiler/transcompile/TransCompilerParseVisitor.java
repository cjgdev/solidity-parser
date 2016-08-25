package main.java.com.clearmatics.soliditycompiler.transcompile;

import grammar.solidityParser;
import grammar.solidityBaseVisitor;
import main.java.com.clearmatics.soliditycompiler.transcompile.emitter.Emitter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class TransCompilerParseVisitor<T> extends solidityBaseVisitor<T> {
    private Emitter _emitter;

    public TransCompilerParseVisitor(Emitter emitter) {
        _emitter = emitter;
    }

  	@Override public T visitSolidity(solidityParser.SolidityContext ctx) {
      return visitChildren(ctx);
    }

  	@Override public T visitContract(solidityParser.ContractContext ctx) {
      return visitChildren(ctx);
    }

  	@Override public T visitSt(solidityParser.StContext ctx) {
      return visitChildren(ctx);
    }

  	@Override public T visitTypedef(solidityParser.TypedefContext ctx) {
      return visitChildren(ctx);
    }

  	@Override public T visitType(solidityParser.TypeContext ctx) {
      return visitChildren(ctx);
    }

  	@Override public T visitStatedef(solidityParser.StatedefContext ctx) {
      return visitChildren(ctx);
    }

  	@Override public T visitMethod(solidityParser.MethodContext ctx) {
      return visitChildren(ctx);
    }

  	@Override public T visitQualifier(solidityParser.QualifierContext ctx) {
      return visitChildren(ctx);
    }

  	@Override public T visitStatement(solidityParser.StatementContext ctx) {
      return visitChildren(ctx);
    }

  	@Override public T visitExpression(solidityParser.ExpressionContext ctx) {
      return visitChildren(ctx);
    }

  	@Override public T visitLhs_expression(solidityParser.Lhs_expressionContext ctx) {
      return visitChildren(ctx);
    }

  	@Override public T visitLiteral(solidityParser.LiteralContext ctx) {
      return visitChildren(ctx);
    }

  	@Override public T visitBinop(solidityParser.BinopContext ctx) {
      return visitChildren(ctx);
    }

  	@Override public T visitUnop(solidityParser.UnopContext ctx) {
      return visitChildren(ctx);
    }
}
